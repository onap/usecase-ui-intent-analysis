/*
 * ============LICENSE_START=======================================================
 * ONAP
 * ================================================================================
 *  Copyright (C) 2022 Huawei Canada Limited.
 *  Copyright (C) 2022 CTC, Inc.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.usecaseui.intentanalysis.adapters.dmaap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.vavr.collection.List;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.api.MessageRouterSubscriber;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.model.MessageRouterSubscribeRequest;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.model.MessageRouterSubscribeResponse;
import org.onap.usecaseui.intentanalysis.util.DmaapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * This is a Dmaap message-router topic monitor.
 * It takes advantage of AT&T's dmaap-client's long-polling implementation, this monitor constantly fetch/refetch target msg topic.
 * So that new msg can be notified almost immediately. This is the major different from previous implementation.
 */
public class MRTopicMonitor implements Runnable {

    private final String configFileName;

    private volatile boolean running = false;

    private static Logger logger = LoggerFactory.getLogger(MRTopicMonitor.class);

    private static int DEFAULT_TIMEOUT_MS_FETCH = 15000;

    private MRConsumerWrapper consumerWrapper;

    private NotificationCallback callback;

    /**
     * Constructor
     *
     * @param configFileName name of topic subscriber file
     * @param callback callbackfunction for received message
     */
    public MRTopicMonitor(String configFileName, NotificationCallback callback) {
        this.configFileName = configFileName;
        this.callback = callback;
    }

    /**
     * Start the monitoring thread
     */
    public void start() {
        logger.info("Starting Dmaap Bus Monitor");
        try {
            File configFile = Resources.getResourceAsFile("dmaapConfig/" + configFileName);
            String configBody = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(configBody).getAsJsonObject();
            consumerWrapper = buildConsumerWrapper(jsonObject);
            running = true;
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Main loop that keep fetching and processing
     */
    @Override
    public void run() {
        while (running) {
            try {
                List<JsonElement> dmaapMsgs = consumerWrapper.fetch();
                for (JsonElement msg : dmaapMsgs) {
                    logger.debug("Event {} Received message: {}" + "\r\n and processing start", consumerWrapper.getTopicName(), msg);
                    process(msg.toString());
                }
            } catch (IOException | RuntimeException e) {
                logger.error("fetchMessage encountered error: {}", e);
            }
        }
        logger.info("{}: exiting thread", this);
    }

    /**
     * Stop the monitor
     */
    public void stop() {
        logger.info("{}: exiting", this);
        running = false;
        this.consumerWrapper.close();
        this.consumerWrapper = null;
    }

    private void process(String msg) {
        try {
            callback.activateCallBack(msg);
        } catch (Exception e) {
            logger.error("process message encountered error: {}", e);
        }
    }

    private List<JsonElement> fetch() throws IOException {
        return this.consumerWrapper.fetch();
    }

    private MRConsumerWrapper buildConsumerWrapper(@NonNull JsonObject topicParamsJson)
        throws IllegalArgumentException {
        MRTopicParams topicParams = MRTopicParams.builder().buildFromConfigJson(topicParamsJson).build();
        return new MRConsumerWrapper(topicParams);
    }

    /**
     * Wrapper class of DmaapClient (package org.onap.dmaap.mr.client)
     * A polling fashion dmaap  consumer, whose #fetch() sleep a certain time when connection fails, otherwise keep retryiny.
     * It supports both https and http protocols.
     */
    private class MRConsumerWrapper {
        /**
         * Name of the "protocol" property.
         */
        protected static final String PROTOCOL_PROP = "Protocol";

        /**
         * Fetch timeout.
         */
        protected int fetchTimeout;

        /**
         * Time to sleep on a fetch failure.
         */
        @Getter
        private final int sleepTime;

        /**
         * Topic Name to Subscribe
         */
        @Getter
        private String topicName;

        /**
         * Counted down when {@link #close()} is invoked.
         */
        private final CountDownLatch closeCondition = new CountDownLatch(1);

        protected MessageRouterSubscriber subscriber;

        protected MessageRouterSubscribeRequest request;

        /**
         * Constructs the object.
         *
         * @param MRTopicParams parameters for the bus topic
         */
        protected MRConsumerWrapper(MRTopicParams MRTopicParams) {
            this.topicName = MRTopicParams.getTopicName();
            this.fetchTimeout = MRTopicParams.getFetchTimeout();

            if (this.fetchTimeout <= 0) {
                this.sleepTime = DEFAULT_TIMEOUT_MS_FETCH;
            } else {
                // don't sleep too long, even if fetch timeout is large
                this.sleepTime = Math.min(this.fetchTimeout, DEFAULT_TIMEOUT_MS_FETCH);
            }

            if (MRTopicParams.isTopicInvalid()) {
                throw new IllegalArgumentException("No topic for DMaaP");
            }

            if (MRTopicParams.isServersInvalid()) {
                throw new IllegalArgumentException("Must provide at least one host for HTTP AAF");
            }

            try {
                this.subscriber = DmaapUtil.buildSubscriber();
                this.request = DmaapUtil.buildSubscriberRequest(topicName + "-Subscriber", MRTopicParams.getTopic(),
                    MRTopicParams.getConsumerGroup(), MRTopicParams.getConsumerInstance());

            } catch (Exception e) {
                throw new IllegalArgumentException("Illegal MrConsumer parameters");
            }

        }

        /**
         * Try fetch new message. But backoff for some sleepTime when connection fails.
         *
         * @return
         * @throws IOException
         */
        public List<JsonElement> fetch() throws IOException {
            Mono<MessageRouterSubscribeResponse> responses = this.subscriber.get(this.request);
            MessageRouterSubscribeResponse resp = responses.block();
            List<JsonElement> list = resp.items();
            return list;

        }

        /**
         * Causes the thread to sleep; invoked after fetch() fails.  If the consumer is closed,
         * or the thread is interrupted, then this will return immediately.
         */
        protected void sleepAfterFetchFailure() {
            try {
                logger.info("{}: backoff for {}ms", this, sleepTime);
                if (this.closeCondition.await(this.sleepTime, TimeUnit.MILLISECONDS)) {
                    logger.info("{}: closed while handling fetch error", this);
                }

            } catch (InterruptedException e) {
                logger.warn("{}: interrupted while handling fetch error", this, e);
                Thread.currentThread().interrupt();
            }
        }

        /**
         * Close the dmaap client and this thread
         */
        public void close() {
            this.closeCondition.countDown();
        }
    }
}

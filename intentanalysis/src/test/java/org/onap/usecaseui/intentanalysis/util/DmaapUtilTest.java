package org.onap.usecaseui.intentanalysis.util;

import java.io.IOException;
import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.api.MessageRouterPublisher;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.api.MessageRouterSubscriber;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.model.MessageRouterPublishRequest;
import org.onap.dcaegen2.services.sdk.rest.services.dmaap.client.model.MessageRouterSubscribeRequest;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class DmaapUtilTest {

    private static final String testMRURl = "http://mrhost:30607/events/unauthenticated.CCVPN_CL_DCAE_EVENT";

    private static final String testConsumerGroup = "testGroup";

    private static final String testConsumerId = "testUd";

    private static final String testSubscriberName = "testName";

    private static final String testPublisherName = "testName";

    private static final String testTopicUrl = "http://mrhost:30607/topics/create";

    @Test
    public void testBuildSubscriberSuccess() {
        MessageRouterSubscriber subscriber = DmaapUtil.buildSubscriber();
        Assert.assertNotNull(subscriber);
    }

    @Test
    public void testBuildSubscriberRequestSuccess() {
        MessageRouterSubscribeRequest request = DmaapUtil.buildSubscriberRequest(testSubscriberName, testMRURl,
            testConsumerGroup, testConsumerId);
        Assert.assertNotNull(request);
    }

    @Test
     public void testBuildPublisherSuccess(){
         MessageRouterPublisher publisher = DmaapUtil.buildPublisher();
         Assert.assertNotNull(publisher);
     }

    @Test
     public void testBuildPublisherRequestSuccess(){
         MessageRouterPublishRequest publishRequest = DmaapUtil.buildPublisherRequest(testPublisherName,testTopicUrl);
         Assert.assertNotNull(publishRequest);
     }
}

/*
 * Copyright (C) 2023 CMCC, Inc. and others. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.usecaseui.intentanalysis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.ENCODING_UTF8;

@Slf4j
public class HttpUtil {
    private static final String LOG_FORMATTER = "[ {} ] {} ";
    private static final String CLIENT_PRTOCOL_EXCEPTION = "Client protocol exception";
    private static final String IO_EXCEPTION = "IO Exception occured";
    private static final String EXCEPTION = "Exception occured";
    private static final String FAILED = "failed";

    public static String sendPostRequestByJson(String url, Map<String, String> headerMap, String requestBodyJson) {
        log.info(LOG_FORMATTER ,url , "  API POST calling is starting......");
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // set request url and header for API calling
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(defaultRequestConfig);
            setHeader(httpPost, headerMap);
            // set request body for API calling
            httpPost.setEntity(setBodyByJson(requestBodyJson));

            // execute API calling and return response
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (HttpStatus.SC_OK == statusCode) {
                    return EntityUtils.toString(response.getEntity(), ENCODING_UTF8);
                } else {
                    log.error("The response code is {}", statusCode);
                    return FAILED;
                }
            }
        } catch (ClientProtocolException cpe) {
            log.error(CLIENT_PRTOCOL_EXCEPTION, cpe);
        } catch (IOException ioe) {
            log.error(IO_EXCEPTION, ioe);
        } catch (Exception e) {
            log.error(EXCEPTION, e);
        }
        log.error(LOG_FORMATTER, url, " API POST calling has finished!");
        return FAILED;
    }

    private static void setHeader(HttpRequestBase request, Map<String, String> headerMap) {
        if (headerMap != null) {
            Set<String> keySet = headerMap.keySet();
            for (String key : keySet) {
                request.addHeader(key, headerMap.get(key));
            }
        }
    }

    private static StringEntity setBodyByJson(String requestBodyJson) {
        StringEntity se = new StringEntity(requestBodyJson, ContentType.APPLICATION_JSON);
        se.setContentEncoding(ENCODING_UTF8);
        return se;
    }
}

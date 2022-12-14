/*
 * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
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

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class RestfulServices {

    private static final Logger logger = LoggerFactory.getLogger(RestfulServices.class);

    private static final String DEFAULT_MSB_IAG_ADDRESS = "msb-iag.onap:443";

    private static final String ENV_MSB_IAG_SERVICE_HOST = "MSB_IAG_SERVICE_HOST";

    private static final String ENV_MSB_IAG_SERVICE_PORT = "MSB_IAG_SERVICE_PORT";

    public static <T> T create(String baseUrl, Class<T> clazz) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create()).build();
        return retrofit.create(clazz);
    }

    public static <T> T create(Class<T> clazz, final String userName, final String passWord) {
        //Set the interface response time

        OkHttpClient okHttpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
                @Nullable
                @Override
                public Request authenticate(@Nullable Route route, Response response) throws IOException {
                    String authStr = Credentials.basic(userName, passWord);
                    return response.request().newBuilder().addHeader("Authorization", authStr).build();
                }
            }).connectTimeout(300, TimeUnit.SECONDS).readTimeout(300, TimeUnit.SECONDS)
            .sslSocketFactory(getSSLSocketFactory(), new CustomTrustManager()).hostnameVerifier(getHostnameVerifier())
            .build();

        String msbUrl = getMSBIAGAddress();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://" + msbUrl + "/").client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create()).build();

        return retrofit.create(clazz);
    }

    public static String getMSBIAGAddress() {
        String iagAddress = System.getenv(ENV_MSB_IAG_SERVICE_HOST);
        String iagPort = System.getenv(ENV_MSB_IAG_SERVICE_PORT);
        if (iagAddress == null || iagPort == null) {
            return DEFAULT_MSB_IAG_ADDRESS;
        }
        return iagAddress + ":" + iagPort;
    }

    public static RequestBody extractBody(HttpServletRequest request) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
            logger.info("The request body content is: " + sb.toString());
            return RequestBody.create(MediaType.parse("application/json"), sb.toString());
        } catch (Exception e) {
            logger.info("RestfulServices occur exection,this content is: " + e.getMessage());
            return RequestBody.create(MediaType.parse("application/json"), sb.toString());
        } finally {
            if (null != br) {
                br.close();
            }
        }
    }

    public static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[] {new CustomTrustManager()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}


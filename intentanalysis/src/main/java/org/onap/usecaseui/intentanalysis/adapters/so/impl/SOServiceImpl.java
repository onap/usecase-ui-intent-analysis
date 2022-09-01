/*
 * Copyright 2022 Huawei Technologies Co., Ltd.
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

package org.onap.usecaseui.intentanalysis.adapters.so.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.onap.usecaseui.intentanalysis.adapters.aai.apicall.AAIAPICall;
import org.onap.usecaseui.intentanalysis.adapters.aai.apicall.AAIAuthConfig;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAPICall;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAuthConfig;
import org.onap.usecaseui.intentanalysis.adapters.so.SOService;
import org.onap.usecaseui.intentanalysis.adapters.so.apicall.SOAPICall;
import org.onap.usecaseui.intentanalysis.adapters.so.apicall.SOAuthConfig;
import org.onap.usecaseui.intentanalysis.bean.models.CCVPNInstance;
import org.onap.usecaseui.intentanalysis.util.RestfulServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SOServiceImpl implements SOService {

    private static final Logger logger = LoggerFactory.getLogger(SOServiceImpl.class);


    private SOAPICall soapiCall;

    private AAIAPICall aaiapiCall;

    @Autowired
    SOAuthConfig soAuthConfig;

    @Autowired
    AAIAuthConfig aaiAuthConfig;

    public SOAPICall getSoApiCall() {
        if (null == soapiCall) {
            this.soapiCall = RestfulServices.create(SOAPICall.class, soAuthConfig.getUserName(),
                    soAuthConfig.getPassword());
        }
        return this.soapiCall;
    }

    public AAIAPICall getAaiApiCall() {
        if (null == aaiapiCall) {
            this.aaiapiCall = RestfulServices.create(AAIAPICall.class, aaiAuthConfig.getUserName(),
                    aaiAuthConfig.getPassword());
        }
        return this.aaiapiCall;
    }

    @Override
    public int createCCVPNInstance(CCVPNInstance ccvpnInstance) {
        try{
            if (null == ccvpnInstance){
                logger.error("CCVPN instance is null!");
                return 0;
            }
            String jobId = createIntentInstanceToSO(ccvpnInstance);
            if (null == jobId){
                logger.error("CCVPN instance creation error：jobId is null");
                return 0;
            }
            ccvpnInstance.setJobId(jobId);
            ccvpnInstance.setResourceInstanceId("cll-"+ccvpnInstance.getInstanceId());

            long startTime = System.currentTimeMillis();
            long maxWaitingPeriod = 30 * 1000; // wait for 30s
            while (getCreateStatus(ccvpnInstance) != 1){
                if((System.currentTimeMillis() - startTime) > maxWaitingPeriod) {
                    logger.error("CCVPN instance creation error: failed to send to so within time frame");
                    return 0;
                }
            }
            // creation status equals to 1
            return 1;
        } catch (Exception e) {
            logger.error("Details:" + e.getMessage());
            return 0;
        }
    }

    @Override
    public void deleteIntentInstance(String serviceInstanceId) {
        try {
            deleteInstanceToSO(serviceInstanceId);
        }catch (Exception e) {
            logger.error("delete instance to SO error :" + e);
        }
    }

    public String createIntentInstanceToSO(CCVPNInstance ccvpnInstance) throws IOException {
        Map<String, Object> params = paramsSetUp(ccvpnInstance);
        params.put("additionalProperties",additionalPropertiesSetUp(ccvpnInstance));
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json"), JSON.toJSONString(params));
        Response<JSONObject> response = getSoApiCall().createIntentInstance(requestBody).execute();
        if (response.isSuccessful()) {
            return response.body().getString("jobId");
        }
        return null;
    }

    private void deleteInstanceToSO(String serviceInstanceId) throws IOException {
        JSONObject params = new JSONObject();
        params.put("serviceInstanceID", serviceInstanceId);
        params.put("globalSubscriberId", "IBNCustomer");
        params.put("subscriptionServiceType", "IBN");
        params.put("serviceType", "CLL");
        JSONObject additionalProperties = new JSONObject();
        additionalProperties.put("enableSdnc", "true");
        params.put("additionalProperties", additionalProperties);
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json"), JSON.toJSONString(params));
        getSoApiCall().deleteIntentInstance(requestBody).execute();
    }

    private int getCreateStatus(CCVPNInstance ccvpnInstance) throws IOException {
        if (ccvpnInstance == null || ccvpnInstance.getResourceInstanceId() == null) {
            return -1;
        }
        Response<JSONObject> response = getAaiApiCall().getInstanceInfo(ccvpnInstance.getResourceInstanceId()).execute();
        logger.debug(response.toString());
        if (response.isSuccessful()) {
            String status = response.body().getString("orchestration-status");
            if ("created".equals(status)) {
                return 1;
            }
        }
        logger.error("getIntentInstance Create Statue Error:" + response.toString());
        return -1;
    }

    /**
     * parameter set up for ccpvpn instance creation
      * @param ccvpnInstance
     * @return
     */
    private Map<String, Object> paramsSetUp(CCVPNInstance ccvpnInstance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", ccvpnInstance.getName());
        params.put("modelInvariantUuid", "6790ab0e-034f-11eb-adc1-0242ac120002");
        params.put("modelUuid", "6790ab0e-034f-11eb-adc1-0242ac120002");
        params.put("globalSubscriberId", "IBNCustomer");
        params.put("subscriptionServiceType", "IBN");
        params.put("serviceType", "CLL");
        return params;
    }

    /**
     * additional properties set up for ccvpn instance creation
     * @param ccvpnInstance
     * @return
     */
    private Map<String, Object> additionalPropertiesSetUp(CCVPNInstance ccvpnInstance) {
        Map<String, Object> additionalProperties = new HashMap<>();
        additionalProperties.put("enableSdnc", "true");
        additionalProperties.put("serviceInstanceID", "cll-" + ccvpnInstance.getInstanceId());

        // create connection link
        Map<String, Object> connectionLink = new HashMap<>();
        connectionLink.put("name", "");
        connectionLink.put("transportEndpointA", ccvpnInstance.getAccessPointOneName());
        connectionLink.put("transportEndpointB", ccvpnInstance.getCloudPointName());

        // create sla
        Map<String, Object> sla = new HashMap<>();
        sla.put("latency", "2");
        sla.put("maxBandwidth", ccvpnInstance.getAccessPointOneBandWidth());

        // configuration for sla and connection link
        if (ccvpnInstance.getProtectStatus() == 1) {
            sla.put("protectionType", ccvpnInstance.getProtectionType());
            connectionLink.put("transportEndpointBProtection", ccvpnInstance.getProtectionCloudPointName());
        }

        List<Map<String, Object>> connectionLinks = new ArrayList<>();
        connectionLinks.add(connectionLink);

        Map<String, Object> transportNetwork = new HashMap<>();
        transportNetwork.put("id", "");
        transportNetwork.put("sla", sla);
        transportNetwork.put("connectionLinks", connectionLinks);

        List<Map<String, Object>> transportNetworks = new ArrayList<>();
        transportNetworks.add(transportNetwork);
        additionalProperties.put("transportNetworks", transportNetworks);
        return additionalProperties;
    }
}

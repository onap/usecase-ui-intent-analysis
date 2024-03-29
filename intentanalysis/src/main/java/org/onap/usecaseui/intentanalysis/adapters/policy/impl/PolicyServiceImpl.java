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

package org.onap.usecaseui.intentanalysis.adapters.policy.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.onap.usecaseui.intentanalysis.adapters.policy.PolicyService;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAPICall;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAuthConfig;
import org.onap.usecaseui.intentanalysis.util.RestfulServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

@Service
public class PolicyServiceImpl implements PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);

    private PolicyAPICall policyAPICall;

    private static StringBuilder policyVersion = new StringBuilder("0.0.0") ;

    @Autowired
    PolicyAuthConfig policyAuthConfig;

    public PolicyAPICall getPolicyAPICall() {
        if (null == policyAPICall) {
            this.policyAPICall = RestfulServices.create(PolicyAPICall.class, policyAuthConfig.getUserName(),
                    policyAuthConfig.getPassword());
        }
        return this.policyAPICall;
    }

    public void setPolicyAPICall(PolicyAPICall policyAPICall) {
        this.policyAPICall = policyAPICall;
    }

    @Override
    public boolean createAndDeployModifyCLLPolicy() {
        try {
            //Create policy
            File policyFile = Resources.getResourceAsFile("intentPolicy/modifycll.json");
            String policyBody = FileUtils.readFileToString(policyFile, StandardCharsets.UTF_8);
            logger.info(String.format("Create policy, request body: %s", policyBody));
            RequestBody policyReq = RequestBody.create(MediaType.parse("application/json"), policyBody.toString());
            Response<ResponseBody> policyResponse = getPolicyAPICall().createPolicy(ModifyCLLPolicyConstants.policyType,
                    ModifyCLLPolicyConstants.policyTypeVersion, policyReq).execute();
            logger.info(String.format("Create policy result, code: %d body: %s", policyResponse.code(),
                    getResponseBodyStr(policyResponse)));
            if (!policyResponse.isSuccessful()) {
                logger.error("Create modify cll policy failed.");
                return false;
            }

            //Deploy policy
            File deployPolicyFile = Resources.getResourceAsFile("intentPolicy/deploy_modifycll.json");
            String deployPolicyBody = FileUtils.readFileToString(deployPolicyFile, StandardCharsets.UTF_8);
            logger.info(String.format("Deploy policy, request body: %s", deployPolicyBody));
            RequestBody deployPolicyReq = RequestBody.create(MediaType.parse("application/json"),
                    deployPolicyBody.toString());
            Response<ResponseBody> deployPolicyResponse = getPolicyAPICall().deployPolicy(deployPolicyReq).execute();
            logger.info(String.format("Deploy policy result, code: %d body: %s", deployPolicyResponse.code(),
                    getResponseBodyStr(deployPolicyResponse)));
            if (!deployPolicyResponse.isSuccessful()) {
                logger.error("Deploy modify cll policy failed.");
                return false;
            }

        } catch (IOException e) {
            logger.error("Exception in create and deploy modify cll policy.", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean undeployAndRemoveModifyCLLPolicy() {
        return undeployAndRemovePolicyIfExist(ModifyCLLPolicyConstants.policyType,
                ModifyCLLPolicyConstants.policyTypeVersion, ModifyCLLPolicyConstants.policyName,
                ModifyCLLPolicyConstants.policyVersion);
    }

    @Override
    public boolean updateIntentConfigPolicy(String cllId, String originalBW, String closedLoopStatus) {
        //the policy engine does not support update now. so we need to remove and recreate the policy now.
        logger.info(String.format(
                "Start to update the intent configuration policy, cllId: %s, originalBW： %s, closedLooopStatus：%b", cllId,
                originalBW, closedLoopStatus));
        //remove the configuration policy first
        boolean res = undeployAndRemovePolicyIfExist(IntentConfigPolicyConstants.policyType,
                IntentConfigPolicyConstants.policyTypeVersion, IntentConfigPolicyConstants.policyName,
                IntentConfigPolicyConstants.policyVersion);
        if (!res) {
            logger.warn("Undeploy and remove the intent configuration policy failed.");
        }
        res = createAndDeployIntentConfigPolicy(cllId, originalBW, closedLoopStatus);
        if (!res) {
            logger.error("Create and deploy the intent configuration policy failed.");
        }
        logger.info(String.format("update intent configuration finished, result: %b", res));
        return res;
    }

    /**
     * Create and deploy the configuration policy
     *
     * @param cllId
     * @param originalBW
     * @param closedLoopStatus
     * @return
     */
    public boolean createAndDeployIntentConfigPolicy(String cllId, String originalBW, String closedLoopStatus) {
        try {
            //Create policy type
            File policyTypeFile = Resources.getResourceAsFile("intentPolicy/intent_configs_policy_type.json");
            String policyTypeBody = FileUtils.readFileToString(policyTypeFile, StandardCharsets.UTF_8);
            logger.info(String.format("Create policy type, request body: %s", policyTypeBody));
            RequestBody policyTypeReq = RequestBody.create(MediaType.parse("application/json"),
                    policyTypeBody.toString());
            Response<ResponseBody> response = getPolicyAPICall().createPolicyType(policyTypeReq).execute();
            logger.info(String.format("Create policy type result, code: %d body: %s", response.code(),
                    getResponseBodyStr(response)));
            if (!response.isSuccessful()) {
                logger.error("Create intent configuration policy type failed.");
                return false;
            }
            //Create policy
            File policyFile = Resources.getResourceAsFile("intentPolicy/intent_configs_policy.json");
            String policyBodyTemplate = FileUtils.readFileToString(policyFile, StandardCharsets.UTF_8);
            String policyBody = policyBodyTemplate.replace("${CLL_ID}", cllId)
                    .replace("${CLOSED_LOOP_STATUS}", closedLoopStatus)
                    .replace("${ORIGINAL_BW}", originalBW);
            String policyJsonBody = updatePolicyVersion(policyBody);
            logger.info(String.format("Create policy, request body: %s", policyJsonBody));
            RequestBody policyReq = RequestBody.create(MediaType.parse("application/json"), policyJsonBody);
            Response<ResponseBody> policyResponse = getPolicyAPICall().createPolicy(
                            IntentConfigPolicyConstants.policyType, policyVersion.toString(), policyReq)
                    .execute();
            logger.info(String.format("Create policy result, code: %d body: %s", policyResponse.code(),
                    getResponseBodyStr(policyResponse)));
            if (!policyResponse.isSuccessful()) {
                logger.error("Create intent configuration policy failed.");
                return false;
            }

            //Deploy policy
            File deployPolicyFile = Resources.getResourceAsFile("intentPolicy/deploy_intent_configs.json");
            String deployPolicyBody = FileUtils.readFileToString(deployPolicyFile, StandardCharsets.UTF_8);
            logger.info(String.format("Deploy policy, request body: %s", deployPolicyBody));
            RequestBody deployPolicyReq = RequestBody.create(MediaType.parse("application/json"),
                    deployPolicyBody.toString());
            Response<ResponseBody> deployPolicyResponse = getPolicyAPICall().deployPolicy(deployPolicyReq).execute();
            logger.info(String.format("Deploy policy result, code: %d body: %s", deployPolicyResponse.code(),
                    getResponseBodyStr(deployPolicyResponse)));
            if (!deployPolicyResponse.isSuccessful()) {
                logger.error("Deploy intent configuration policy failed.");
                return false;
            }

        } catch (IOException e) {
            logger.error("Exception in create and deploy intent config policy.", e);
            return false;
        }
        return true;
    }

    /**
     * undeploy and remove the configuration policy
     *
     * @return
     */
    private boolean undeployAndRemovePolicyIfExist(String policyType, String policyTypeVersion, String policyName,
                                                   String policyVersion) {
        try {
            //check if the policy exists
            Response<ResponseBody> response = getPolicyAPICall().getPolicy(policyType, policyTypeVersion, policyName,
                    policyVersion).execute();
            logger.info(String.format("The policy query result, code: %d body: %s", response.code(),
                    getResponseBodyStr(response)));
            // remove the policy if exists.
            if (response.isSuccessful()) {
                logger.info("The policy exists, start to undeploy.");
                Response<ResponseBody> undeployResponse = getPolicyAPICall().undeployPolicy(policyName).execute();
                logger.info(String.format("Undeploy policy result. code: %d body: %s", undeployResponse.code(),
                        getResponseBodyStr(undeployResponse)));
                logger.info("Start to remove the policy.");
                Response<ResponseBody> removeResponse = getPolicyAPICall().removePolicy(policyName, policyVersion)
                        .execute();
                logger.info(String.format("Remove policy result. code: %d body: %s", removeResponse.code(),
                        getResponseBodyStr(removeResponse)));
                return true;
            }
            return true;

        } catch (IOException e) {
            logger.error("Exception in undeploy and remove policy", e);
            return false;
        }

    }

    private String getResponseBodyStr(Response<ResponseBody> response) throws IOException {
        if (response.body() != null) {
            return response.body().string();
        }
        return null;
    }

    private String updatePolicyVersion(String policyBody) {
        JSONObject policyJsonBody = JSONObject.parseObject(policyBody);
        if (policyVersion.charAt(0) == '0') {
            policyVersion.setCharAt(0, (char)(policyVersion.charAt(0) + 1));
        }
        else {
            policyVersion.setCharAt(4, (char)(policyVersion.charAt(4) + 1));
        }
        JSONObject topologyTemplateObject = policyJsonBody.getJSONObject("topology_template");
        JSONArray policiesArray = topologyTemplateObject.getJSONArray("policies");
        JSONObject configObject = policiesArray.getJSONObject(0);
        JSONObject versionObject = configObject.getJSONObject("onap.dcae.slicems.config");
        versionObject.put("version", policyVersion.toString());
        configObject.put("onap.dcae.slicems.config", versionObject);
        policiesArray.set(0, configObject);
        topologyTemplateObject.put("policies", policiesArray);
        policyJsonBody.put("topology_template", topologyTemplateObject);
        return policyJsonBody.toString();
    }
}

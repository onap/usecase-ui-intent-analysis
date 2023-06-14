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
package org.onap.usecaseui.intentanalysis.clldeliveryIntentmgt.clldeliverymodule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.adapters.so.SOService;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.bean.enums.*;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.FulfillmentInfoService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.onap.usecaseui.intentanalysis.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class CLLDeliveryActuationModule extends ActuationModule {
    public final static String NLP_HOST = "http://uui-nlp";
    public final static String NLP_ONLINE_URL_BASE = NLP_HOST + ":33011";
    public final static String PREDICT_URL = NLP_ONLINE_URL_BASE + "/api/online/predict";

    @Autowired
    private SOService soService;

    @Autowired
    private ExpectationObjectService expectationObjectService;

    @Autowired
    private ExpectationService expectationService;

    @Autowired
    private FulfillmentInfoService fulfillmentInfoService;

    @Autowired
    private IntentService intentService;
    @Autowired
    private ContextService contextService;

    @Override
    public void toNextIntentHandler(IntentGoalBean intentGoalBean, IntentManagementFunction IntentHandler) {

    }

    @Override
    public void directOperation(IntentGoalBean intentGoalBean) {
        Intent intent = intentGoalBean.getIntent();
        if (StringUtils.equalsIgnoreCase("create", intentGoalBean.getIntentGoalType().name())) {
            Map<String, Object> params = new HashMap<>();
            Map<String, String> accessPointOne = new HashMap<>();
            List<ExpectationTarget> targetList = intent.getIntentExpectations().get(0).getExpectationTargets();
            for (ExpectationTarget target : targetList) {
                String conditionName = target.getTargetConditions().get(0).getConditionName();
                String conditionValue = target.getTargetConditions().get(0).getConditionValue();
                String value = getPredictValue(conditionName, conditionValue);
                if (StringUtils.containsIgnoreCase(conditionName, "source")) {
                    accessPointOne.put("name", value);
                } else if (StringUtils.containsIgnoreCase(conditionName, "destination")) {
                    params.put("cloudPointName", value);
                } else if (StringUtils.containsIgnoreCase(conditionName, "bandwidth")) {
                    accessPointOne.put("bandwidth", value);
                }
            }
            params.put("accessPointOne", accessPointOne);
            params.put("instanceId", getInstanceId());
            params.put("name", "cll-" + params.get("instanceId"));
            params.put("protect", false);
            ResultHeader resultHeader = soService.createIntentInstance(params);

            // Get the expectationId of the first exception from intent which should be CLL_VPN DELIVERY
            String expectationId = intent.getIntentExpectations().get(0).getExpectationId();

            // Get the fulfillmentInfo of the first exception which need to be updated with resultHeader returned
            FulfillmentInfo fulfillmentInfo = new FulfillmentInfo();
            Expectation intentExpectation = expectationService.getIntentExpectation(expectationId);
            if (intentExpectation != null) {
                FulfillmentInfo expectationFulfillmentInfo = intentExpectation.getExpectationFulfillmentInfo();
                if (expectationFulfillmentInfo != null) {
                    fulfillmentInfo = expectationFulfillmentInfo;
                }
            }

            // Update fulfillmentInfo and write back to DB
            // Originally set to be NOT_FULFILLED, and will change after requesting the SO operation status
            fulfillmentInfo.setFulfillmentStatus(FulfillmentStatus.NOT_FULFILLED);
            fulfillmentInfo.setNotFulfilledReason(resultHeader.getResult_message());

            // If SO request accepted, means intent acknowledged, otherwise, means failed
            if (resultHeader.getResult_code() == 1) {
                fulfillmentInfo.setNotFulfilledState(NotFulfilledState.ACKNOWLEDGED);
            } else {
                fulfillmentInfo.setNotFulfilledState(NotFulfilledState.FULFILMENTFAILED);
            }

            fulfillmentInfoService.updateFulfillmentInfo(fulfillmentInfo, expectationId);

            ExpectationObject expectationObject = expectationObjectService.getExpectationObject(expectationId);
            expectationObject.setObjectInstance((String) params.get("name"));
            intent.getIntentExpectations().get(0).getExpectationObject().setObjectInstance((String) params.get("name"));
            expectationObjectService.updateExpectationObject(expectationObject, expectationId);
        } else if (StringUtils.equalsIgnoreCase("delete", intentGoalBean.getIntentGoalType().name())) {
            String instanceId = intent.getIntentExpectations().get(0).getExpectationObject().getObjectInstance();
            soService.deleteIntentInstance(instanceId);
        } else {
            String instanceId = intent.getIntentExpectations().get(0).getExpectationObject().getObjectInstance();
            soService.deleteIntentInstance(instanceId);
            intentService.deleteIntent(intent.getIntentId());
        }
    }

    @Override
    public void interactWithIntentHandle() {

    }

    @Override
    public void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler) {
        this.directOperation(intentGoalBean);
    }

    private String getPredictValue(String name, String value) {
        String text = "expectationName is cloud leased line Delivery Expectation, " +
                "firstName is " + name + ",firstValue is " + value;
        String[] questions = {"expectationName", "Name", "Value"};
        String bodyStr = "{\"title\": \"predict\", \"text\": \"" + text
                + "\", \"questions\":" + new JSONArray().toJSONString(Arrays.asList(questions)) + "}";
        HashMap<String, String> headers = new HashMap<>();
        String result = HttpUtil.sendPostRequestByJson(PREDICT_URL, headers, bodyStr);
        if("failed".equals(result)){
            return value;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getString("Value");
    }
	
	public String getInstanceId() {
        int random = (int) (Math.random() * 9 + 1);

        String randomString = String.valueOf(random);
        int hashCode = UUID.randomUUID().toString().hashCode();
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        return randomString + String.format("%015d", hashCode);
    }

    public void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean){
        Intent subIntent = intentGoalBean.getIntent();
        if (StringUtils.containsIgnoreCase(subIntent.getIntentName(),"delivery")) {
            List<Expectation> deliveryIntentExpectationList = intentGoalBean.getIntent().getIntentExpectations();
            List<Expectation> originIntentExpectationList = originIntent.getIntentExpectations();
            ExpectationObject deliveryExpectationObject = deliveryIntentExpectationList.get(0).getExpectationObject();
            String objectInstance = deliveryExpectationObject.getObjectInstance();

            for (Expectation originExpectation : originIntentExpectationList) {
                ExpectationObject originExpectationObject = originExpectation.getExpectationObject();
                originExpectationObject.setObjectInstance(objectInstance);
            }
        }
        log.info("cllDeliveryActuationModule begin to update originIntent subIntentInfo");
    }
}

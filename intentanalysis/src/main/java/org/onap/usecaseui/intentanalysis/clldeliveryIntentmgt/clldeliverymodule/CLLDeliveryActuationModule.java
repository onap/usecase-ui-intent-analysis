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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.adapters.so.SOService;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class CLLDeliveryActuationModule extends ActuationModule {

    @Autowired
    private SOService soService;

    @Autowired
    private ExpectationObjectService expectationObjectService;

    @Autowired
    private IntentService intentService;

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
                if (StringUtils.containsIgnoreCase(conditionName, "source")) {
                    accessPointOne.put("name", conditionValue);
                } else if (StringUtils.containsIgnoreCase(conditionName, "destination")) {
                    params.put("cloudPointName", conditionValue);
                } else if (StringUtils.containsIgnoreCase(conditionName, "bandwidth")) {
                    accessPointOne.put("bandwidth", conditionValue);
                }
            }
            params.put("accessPointOne", accessPointOne);
            params.put("instanceId", getInstanceId());
            params.put("name", "cll-" + params.get("instanceId"));
            params.put("protect", false);
            soService.createIntentInstance(params);
            String expectationId = intent.getIntentExpectations().get(0).getExpectationId();
            ExpectationObject expectationObject = expectationObjectService.getExpectationObject(expectationId);
            expectationObject.setObjectInstance((String) params.get("name"));
            expectationObjectService.updateExpectationObject(expectationObject, expectationId);
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
                expectationObjectService.updateExpectationObject(originExpectationObject,originExpectation.getExpectationId());
            }
        }


    }
}

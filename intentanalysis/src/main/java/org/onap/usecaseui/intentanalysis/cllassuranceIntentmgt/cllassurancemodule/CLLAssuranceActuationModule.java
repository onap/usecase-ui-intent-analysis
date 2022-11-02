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
package org.onap.usecaseui.intentanalysis.cllassuranceIntentmgt.cllassurancemodule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.adapters.policy.PolicyService;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class CLLAssuranceActuationModule extends ActuationModule {
    @Autowired
    private IntentService intentService;

    @Autowired
    private PolicyService policyService;
    @Autowired
    private ContextService contextService;

    @Override
    public void toNextIntentHandler(IntentGoalBean intentGoalBean, IntentManagementFunction IntentHandler) {

    }

    @Override
    public void directOperation(IntentGoalBean intentGoalBean) {
        Intent intent = intentGoalBean.getIntent();
        String cllId = getCLLId(intent);
        String bandwidth = getBandwidth(cllId);
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        if (StringUtils.equalsIgnoreCase("create", intentGoalType.name())) {
            policyService.updateIntentConfigPolicy(cllId, bandwidth, "true");
        } else if (StringUtils.equalsIgnoreCase("update", intentGoalType.name())) {
            policyService.updateIntentConfigPolicy(cllId, bandwidth, "false");
        } else if (StringUtils.equalsIgnoreCase("delete", intentGoalType.name())) {
            policyService.updateIntentConfigPolicy(cllId, bandwidth, "false");
        }
    }

    @Override
    public void interactWithIntentHandle() {

    }

    @Override
    public void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler) {
        directOperation(intentGoalBean);
    }

    @Override
    public void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean){
        log.info("cllDeliveryActuationModule begin to update originIntent subIntentInfo");
        contextService.updateContextList(originIntent.getIntentContexts(), originIntent.getIntentId());
    }

    private String getBandwidth(String cllId) {
        List<Intent> deliveryIntentList = intentService.getIntentByName("CLL Delivery Intent");
        for (Intent deliveryIntent : deliveryIntentList) {
            List<Expectation> deliveryExpectationList = deliveryIntent.getIntentExpectations();
            for (Expectation deliveryExpectation : deliveryExpectationList) {
                if (StringUtils.equalsIgnoreCase(cllId, deliveryExpectation.getExpectationObject().getObjectInstance())) {
                    List<ExpectationTarget> deliveryTargetList = deliveryExpectation.getExpectationTargets();
                    for (ExpectationTarget deliveryTarget : deliveryTargetList) {
                        if (StringUtils.equalsIgnoreCase("bandwidth", deliveryTarget.getTargetName())) {
                            List<Condition> deliveryConditionList = deliveryTarget.getTargetConditions();
                            for (Condition deliveryCondition : deliveryConditionList) {
                                if (StringUtils.equalsIgnoreCase("condition of the cll service bandwidth", deliveryCondition.getConditionName())) {
                                    return deliveryCondition.getConditionValue();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getCLLId(Intent intent) {
        List<Expectation> expectationList = intent.getIntentExpectations();
        for (Expectation expectation : expectationList) {
            if (StringUtils.equalsIgnoreCase("assurance", expectation.getExpectationType().name())) {
                return expectation.getExpectationObject().getObjectInstance();
            }
        }
        return null;
    }
}

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
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CLLAssuranceActuationModule extends ActuationModule {
    @Autowired
    private IntentService intentService;

    @Autowired
    private PolicyService policyService;

    @Override
    public void directOperation(IntentGoalBean intentGoalBean) {
        Intent intent = intentGoalBean.getIntent();
        List<String> cllIds = getCLLId(intent);
        if (CollectionUtils.isEmpty(cllIds)) {
            log.info("get cllId is empty");
            return;
        }
        cllIds.forEach(cllId -> {
            String bandwidth = getBandwidth(cllId);
            IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
            if (StringUtils.equalsIgnoreCase("create", intentGoalType.name())) {
                policyService.updateIntentConfigPolicy(cllId, bandwidth, "true");
            } else if (StringUtils.equalsIgnoreCase("update", intentGoalType.name())) {
                policyService.updateIntentConfigPolicy(cllId, bandwidth, "false");
            } else if (StringUtils.equalsIgnoreCase("delete", intentGoalType.name())) {
                policyService.updateIntentConfigPolicy(cllId, bandwidth, "false");
            }
        });
    }

    @Override
    public void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler) {
        directOperation(intentGoalBean);
    }

    @Override
    public void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean){
    }

    private String getBandwidth(String cllId) {
        List<Intent> deliveryIntentList = intentService.getIntentByName("CLL Delivery Intent");
        if (CollectionUtils.isEmpty(deliveryIntentList)) {
            log.info("get CLL Delivery Intent from the database is empty");
            return null;
        }
        for (Intent deliveryIntent : deliveryIntentList) {
            List<Expectation> deliveryExpectationList = deliveryIntent.getIntentExpectations().stream()
                    .filter(expectation -> ExpectationType.DELIVERY.equals(expectation.getExpectationType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(deliveryExpectationList)) {
                log.info("expectation is empty,intentId is {}", deliveryIntent.getIntentId());
                continue;
            }
            for (Expectation deliveryExpectation : deliveryExpectationList) {
                ExpectationObject expectationObject = deliveryExpectation.getExpectationObject();
                if (expectationObject == null) {
                    log.info("expectationObject is empty,expectationId is {}", deliveryExpectation.getExpectationId());
                    continue;
                }
                List<String> objectInstances = expectationObject.getObjectInstance();
                String object = "";
                for (String objectInstance : objectInstances) {
                    if (StringUtils.equalsIgnoreCase(cllId, objectInstance)) {
                        object = objectInstance;
                        break;
                    }
                }
                if (StringUtils.isEmpty(object)) {
                    log.info("no objectInstance is equal cllId {}", cllId);
                    continue;
                }
                List<ExpectationTarget> deliveryTargetList = deliveryExpectation.getExpectationTargets();
                List<ExpectationTarget> bandwidth = deliveryTargetList.stream()
                        .filter(target -> StringUtils.equalsIgnoreCase("bandwidth", target.getTargetName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(bandwidth)) {
                    log.info("bandwidth target is empty,expectation is {}", deliveryExpectation.getExpectationId());
                    continue;
                }
                List<Condition> deliveryConditionList = bandwidth.get(0).getTargetConditions();
                List<Condition> collect = deliveryConditionList.stream()
                        .filter(condition -> StringUtils.equalsIgnoreCase("condition of the cll service bandwidth", condition.getConditionName()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect)) {
                    log.info("condition of the cll service bandwidth is empty,targetId is {}", bandwidth.get(0).getTargetId());
                    continue;
                }
                return collect.get(0).getConditionValue();
            }
        }
        return null;
    }

    private List<String> getCLLId(Intent intent) {
        List<Expectation> expectationList = intent.getIntentExpectations();
        for (Expectation expectation : expectationList) {
            if (StringUtils.equalsIgnoreCase("assurance", expectation.getExpectationType().name())) {
                return expectation.getExpectationObject().getObjectInstance();
            }
        }
        return null;
    }
}

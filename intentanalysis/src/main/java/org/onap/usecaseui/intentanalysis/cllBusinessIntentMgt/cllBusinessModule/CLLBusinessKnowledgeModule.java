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
package org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule;

import org.apache.commons.collections.CollectionUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.DetectionGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.DetectionGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CLLBusinessKnowledgeModule implements KnowledgeModule {
    private static Logger LOGGER = LoggerFactory.getLogger(CLLBusinessKnowledgeModule.class);

    @Autowired
    IntentService intentService;

    @Override
    public Intent intentCognition(Intent intent) {
        List<String> intendIdList = intentResolution(intent);
        getSystemStatus();
        determineDetectionGoal(intent, intendIdList);
        return null;
    }

    /**
     * find similar intents in DB
     *
     * @param intent
     */

    public List<String> intentResolution(Intent intent) {
        //dc contain original intent
        String intentName = intent.getIntentName();
        List<Intent> intentList = intentService.getIntentList();
        List<Intent> sameNameIntentList = intentList.stream().filter(x -> x.getIntentName()
                .contains(intentName)).collect(Collectors.toList());
        List<String> intentIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sameNameIntentList)) {
            List<Expectation> expectationList = intent.getIntentExpectations();
            for (Intent dbIntent : sameNameIntentList) {
                String intentId = dbIntent.getIntentId();
                int count = 0;
                for (Expectation expectation : expectationList) {//original expectations
                    //Determine if there is the same ObjectType
                    List<Expectation> sameObjTypeList = dbIntent.getIntentExpectations().stream()
                            .filter(x -> x.getExpectationObject().getObjectType().equals(expectation.getExpectationObject().getObjectType()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(sameObjTypeList)) {
                        //Determine the targetName of the Expectation which hava same ObjectType
                        List<String> targetNameList = expectation.getExpectationTargets()
                                .stream().map(ExpectationTarget::getTargetName).collect(Collectors.toList());
                        for (Expectation dbExpectation : sameObjTypeList) {
                            List<String> dbTargetNameList = dbExpectation.getExpectationTargets()
                                    .stream().map(ExpectationTarget::getTargetName).collect(Collectors.toList());
                            //todo name compare need ai
                            if (dbTargetNameList.containsAll(targetNameList)) {
                                count++;
                                break;
                            }
                        }
                    }
                    if (count == expectationList.size()) {
                        intentIdList.add(intentId);
                        break;
                    }
                }
            }
        }
        return intentIdList;
    }

    void intentReportResolution() {
    }

    /**
     * query the implementation of intent requirements in the system
     */
    void getSystemStatus() {
    }


    void interactWithIntentOwner() {
    }

    /**
     * Determine add, delete, modify according to theobject,target and context of the expectation
     */
    DetectionGoalBean determineDetectionGoal(Intent intent, List<String> intentIdList) {
        int size = intentIdList.size();
        if (size == 0) {
            return new DetectionGoalBean(intent, DetectionGoalType.ADD);
        } else {
            return new DetectionGoalBean(intent, DetectionGoalType.UPDATE);
        }
    }

}

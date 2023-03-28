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

import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentDetectionService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CLLBusinessKnowledgeModule extends KnowledgeModule {
    private static Logger LOGGER = LoggerFactory.getLogger(CLLBusinessKnowledgeModule.class);

    @Autowired
    IntentService intentService;
    @Autowired
    IntentDetectionService intentDetectionService;

    @Override
    public IntentGoalBean intentCognition(Intent intent) {
        List<String> intendIdList = intentResolution(intent);
        getSystemStatus();
        return determineDetectionGoal(intent, intendIdList);
    }

    @Override
    public boolean recieveCreateIntent() {
        return true;
    }

    @Override
    public boolean recieveUpdateIntent() {
        return true;
    }

    @Override
    public boolean recieveDeleteIntent() {
        return true;
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
    public IntentGoalBean determineDetectionGoal(Intent intent, List<String> intentIdList) {
        int size = intentIdList.size();
        if (size == 0) {
            return new IntentGoalBean(intent, IntentGoalType.CREATE);
        } else {
            return new IntentGoalBean(intent, IntentGoalType.UPDATE);
        }
    }

}

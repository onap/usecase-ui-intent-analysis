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
package org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule;

import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentDetectionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FormatIntentInputKnowledgeModule implements KnowledgeModule {
    @Autowired
    IntentDetectionService intentDetectionService;

    @Override
    public IntentGoalBean intentCognition(Intent intent) {
        List<String> intendIdList = intentDetectionService.intentResolution(intent);
        getSystemStatus();
        return determineDetectionGoal(intent, intendIdList);
    }

    @Override
    public boolean recieveCreateIntent() {
        return false;
    }

    @Override
    public boolean recieveUpdateIntent() {
        return false;
    }

    @Override
    public boolean recieveDeleteIntent() {
        return false;
    }

    public void getSystemStatus() {
    }

    public IntentGoalBean determineDetectionGoal(Intent intent, List<String> intentIdList) {
        int size = intentIdList.size();
        if (size == 0) {
            return new IntentGoalBean(intent, IntentGoalType.CREATE);
        } else {
            return new IntentGoalBean(intent, IntentGoalType.UPDATE);
        }
    }
}

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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService;

import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class IntentProcessService {
    @Autowired
    IntentDetectionService intentDetectionService;
    @Autowired
    IntentInvestigationService intentInvestigationService;
    @Autowired
    IntentDefinitionService intentDefinitionService;
    @Autowired
    IntentDistributionService intentDistributionService;
    @Autowired
    IntentOperationService intentOperationService;
    @Autowired
    IntentService intentService;
    private IntentManagementFunction intentOwner;
    private IntentManagementFunction intentHandler;


    public void setIntentRole(IntentManagementFunction intentOwner, IntentManagementFunction intentHandler) {
        if (intentOwner != null) {
            this.intentOwner = intentOwner;
        }
        if (intentHandler != null) {
            this.intentHandler = intentHandler;
        }
    }

    public void intentProcess(Intent intent) {
        intentDetectionService.setIntentRole(intentOwner, intentHandler);
        IntentGoalBean intentGoalBean = intentDetectionService.detectionProcess(intent);

        //investigation process
        intentInvestigationService.setIntentRole(intentOwner, intentHandler);
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap =
                intentInvestigationService.investigationProcess(intentGoalBean);

        Iterator<Map.Entry<IntentGoalBean, IntentManagementFunction>> iterator = intentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<IntentGoalBean, IntentManagementFunction> next = iterator.next();
            //definition process  save subintent
            intentDefinitionService.setIntentRole(intentOwner, intentHandler);
            intentDefinitionService.definitionPorcess(next);

            //distribution process
            intentDistributionService.setIntentRole(intentOwner, intentHandler);
            intentDistributionService.distributionProcess(next);

            intentOperationService.setIntentRole(intentOwner, next.getValue());
            intentOperationService.operationProcess(next.getKey().getIntent());
        }
    }

}

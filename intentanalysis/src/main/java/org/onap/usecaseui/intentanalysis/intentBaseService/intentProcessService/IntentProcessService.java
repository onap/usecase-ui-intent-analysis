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
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private IntentManagementFunction intentOwner;
    private IntentManagementFunction intentHandler;


    public void setIntentRole(IntentManagementFunction intentOwner, IntentManagementFunction intentHandler){
        if (intentOwner!= null){
            this.intentOwner = intentOwner;
        }
        if (intentHandler!= null){
            this.intentHandler= intentHandler;
        }
    }
    public void intentProcess(Intent intent) {
        intentDetectionService.setIntentRole(intentOwner,intentHandler);
        Intent detectIntent = intentDetectionService.detectionProcess(intent);

        //investigation process
        intentInvestigationService.setIntentRole(intentOwner,intentHandler);
        List<IntentManagementFunction> intentManagementFunctions =
                intentInvestigationService.investigationProcess(detectIntent);//List<handler>?

        for (IntentManagementFunction intentHandler : intentManagementFunctions) {
            //definition process
            intentDefinitionService.setIntentRole(intentOwner,intentHandler);
            intentDefinitionService.definitionPorcess(detectIntent);

            //distribution process
            intentDistributionService.setIntentRole(intentOwner,intentHandler);
            intentDistributionService.distributionProcess(intentHandler);

            //operation process
            intentOperationService.setIntentRole(intentOwner,intentHandler);
            intentOperationService.operationProcess(detectIntent);
        }
    }


}

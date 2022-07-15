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
package org.onap.usecaseui.intentanalysis.intentProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntentProcessService {
    @Autowired
    IntentDetectionService intentDetectionServiceImpl;
    @Autowired
    IntentInvestigationService intentInvestigationService;
    @Autowired
    IntentDefinitionService intentDefinitionService;
    @Autowired
    IntentDistributionService intentDistributionService;
    @Autowired
    IntentOperationService intentOperationService;

    private Function intentOwner;
    private Function intentHandler;


    public void setIntentRole(Function intentOwner,Function intentHandler){
        if (intentOwner!= null){
            this.intentOwner = intentOwner;
        }
        if (intentHandler!= null){
            this.intentHandler= intentHandler;
        }
    }
    public void intentProcess() {
        intentDetectionServiceImpl.setIntentRole(intentOwner,intentHandler);
        intentDetectionServiceImpl.detectionProcess();

        //investigation process
        intentInvestigationService.setIntentRole(intentOwner,intentHandler);
        intentInvestigationService.investigationProcess();//List<handler>?

        //definition process
        intentDefinitionService.setIntentRole(intentOwner,intentHandler);
        intentDefinitionService.definitionPorcess();

        //distribution process
        intentDistributionService.setIntentRole(intentOwner,intentHandler);
        intentDistributionService.distributionProcess();

        //operation process
        intentOperationService.setIntentRole(intentOwner,intentHandler);
        intentOperationService.operationProcess();
    }


}

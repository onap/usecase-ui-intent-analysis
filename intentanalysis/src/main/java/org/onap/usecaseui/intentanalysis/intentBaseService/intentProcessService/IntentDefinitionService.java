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


import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
public class IntentDefinitionService {

    private IntentManagementFunction intentHandler;
    private IntentManagementFunction intentOwner;

    @Autowired
    public IntentContextService intentContextService;
    @Autowired
    public IntentService intentService;

    public void setIntentRole(IntentManagementFunction intentOwner, IntentManagementFunction intentHandler) {
        if (intentOwner != null) {
            this.intentOwner = intentOwner;
        }
        if (intentHandler != null) {
            this.intentHandler = intentHandler;
        }
    }

    public IntentGoalBean definitionPorcess(Intent originIntent, Map.Entry<IntentGoalBean, IntentManagementFunction> entry) {
        DecisionModule intentDecisionModule = intentOwner.getDecisionModule();
        ActuationModule intentActuationModule = intentOwner.getActuationModule();

        IntentGoalBean newIntentGoalBean = entry.getKey();
        if (newIntentGoalBean.getIntentGoalType() == IntentGoalType.CREATE){
            Intent newIdIntent = intentDecisionModule.intentDefinition(originIntent, entry.getKey().getIntent());
            intentContextService.updateIntentOwnerHandlerContext(newIdIntent, intentOwner, intentHandler);
            intentContextService.updateParentIntentContext(originIntent, newIdIntent);
            intentContextService.updateChindIntentContext(originIntent, newIdIntent);
            log.debug(newIdIntent.toString());
            intentActuationModule.saveIntentToDb(newIdIntent);
            return new IntentGoalBean(newIdIntent,IntentGoalType.CREATE);
        }

        if (newIntentGoalBean.getIntentGoalType() == IntentGoalType.UPDATE){
            intentActuationModule.updateIntentToDb(newIntentGoalBean.getIntent());
        }
        if (newIntentGoalBean.getIntentGoalType() == IntentGoalType.DELETE){
            intentActuationModule.deleteIntentToDb(entry.getKey().getIntent());
        }
        return newIntentGoalBean;
    }
}

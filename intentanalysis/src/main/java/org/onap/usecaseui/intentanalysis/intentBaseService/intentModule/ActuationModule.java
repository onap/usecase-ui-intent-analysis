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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentModule;

import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.IntentInstance;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.IntentInstanceService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class ActuationModule {
    @Autowired
    IntentInterfaceService intentInterfaceService;
    @Autowired
    IntentService intentService;

    @Autowired
    IntentInstanceService intentInstanceService;

    //send to the next level intent handler
    public abstract void toNextIntentHandler(IntentGoalBean intentGoalBean, IntentManagementFunction IntentHandler);

    //Direct operation
    public abstract void directOperation(IntentGoalBean intentGoalBean);

    public abstract void interactWithIntentHandle();

    //Save intent information to the intent instance database
    public void saveIntentToDb(Intent intent) {
        intentService.createIntent(intent);
    }

    //Update intent information to the intent instance database
    public void updateIntentToDb(Intent intent) {
        intentService.updateIntent(intent);
    }

    //Delete intent information to the intent instance database
    public void deleteIntentToDb(Intent intent) {
        intentService.deleteIntent(intent.getIntentId());
    }

    public void saveIntentInstanceToDb(IntentInstance intentInstance){
        intentInstanceService.createIntentInstance(intentInstance);
    }

    public boolean distrubuteIntentToHandler(Map.Entry<IntentGoalBean, IntentManagementFunction> entry) {
        IntentGoalType intentGoalType = entry.getKey().getIntentGoalType();
        return false;
    }

    //determine if the intent is to be processed directly or sent to the next-level processor
    public abstract void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler);

    public abstract void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean);
}

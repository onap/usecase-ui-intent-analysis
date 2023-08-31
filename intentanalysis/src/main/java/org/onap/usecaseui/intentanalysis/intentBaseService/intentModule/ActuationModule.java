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

import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ActuationModule {
    @Autowired
    IntentInterfaceService intentInterfaceService;
    @Autowired
    IntentService intentService;

    //Direct operation
    public abstract void directOperation(IntentGoalBean intentGoalBean);

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

    //determine if the intent is to be processed directly or sent to the next-level processor
    public abstract void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler);

    public abstract void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean);
}

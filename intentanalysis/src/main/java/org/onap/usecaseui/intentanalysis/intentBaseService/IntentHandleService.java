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
package org.onap.usecaseui.intentanalysis.intentBaseService;

import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IntentHandleService {
    @Autowired
    private IntentProcessService processService;

    /**
     * Process the original intent and find the corresponding IntentManagementFunction
     * @param intent
     */
    public void handleOriginalIntent(Intent intent){
        IntentManagementFunction intentOwner = selectIntentManagementFunction(intent);
        handleIntent(intent,intentOwner);
    }

    public void handleIntent(Intent intent, IntentManagementFunction intentOwner) {
        processService.setIntentRole(intentOwner, null);
        processService.intentProcess(intent);
    }

    public IntentManagementFunction selectIntentManagementFunction(Intent intent) {
        //select the IntentManagementFunctionRegInfo Based on the IMFRegistry information.
        //Only internalFunction support.
        //and based on the IntentManagementFunctionRegInfo, get the right IntentManagementFunction bean.
        //if  no  IntentManagementFunction selected, that means this intent is not supported by this system.
        return null;
    }

    public IntentManagementFunctionRegInfo selectIntentManagementFunctionRegInfo(Intent intent) {
        //select the IntentManagementFunctionRegInfo Based on the IMFRegistry information.
        //Both internalFunction and externalFunction support.
        //This is used to get he IntentManagementFunction for a subIntent decomposition.
        return null;
    }
}

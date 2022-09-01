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


import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IntentDistributionService {
    private IntentManagementFunction intentHandler;
    private IntentManagementFunction intentOwner;

    @Autowired
    IntentInterfaceService intentInterfaceService;

    public void setIntentRole(IntentManagementFunction intentOwner, IntentManagementFunction intentHandler) {
        if (intentOwner != null) {
            this.intentOwner = intentOwner;
        }
        if (intentHandler != null) {
            this.intentHandler = intentHandler;
        }
    }

    public boolean distributionProcess(Map<IntentGoalBean, IntentManagementFunction> intentMap) {

        intentOwner.getActuationModule().distrubuteIntentToHandler(intentMap);
        return false;
    }

    public boolean distrubuteIntentToHandler(Map<IntentGoalBean, IntentManagementFunction> intentMap) {

        for (Map.Entry<IntentGoalBean, IntentManagementFunction> entry : intentMap.entrySet()) {
            IntentGoalType intentGoalType = entry.getKey().getIntentGoalType();
            if (StringUtils.equalsIgnoreCase("create", intentGoalType.name())) {
                return intentInterfaceService.createInterface(entry.getKey().getIntent(), entry.getValue());
            } else if (StringUtils.equalsIgnoreCase("update", intentGoalType.name())) {
                return intentInterfaceService.updateInterface(entry.getKey().getIntent(), entry.getValue());
            } else if (StringUtils.equalsIgnoreCase("delete", intentGoalType.name())) {
                return intentInterfaceService.deleteInterface(entry.getKey().getIntent(), entry.getValue());
            }
        }
        return false;
    }
}

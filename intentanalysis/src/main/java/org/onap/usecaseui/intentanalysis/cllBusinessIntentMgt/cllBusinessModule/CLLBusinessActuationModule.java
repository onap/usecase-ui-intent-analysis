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


import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CLLBusinessActuationModule extends ActuationModule {
    @Autowired
    IntentProcessService processService;
    @Autowired
    IntentService intentService;
    @Autowired
    IntentInterfaceService intentInterfaceService;


    @Override
    public void sendToIntentHandler(IntentManagementFunction IntentHandler) {
    }

    @Override
    public void sendToNonIntentHandler() {

    }

    @Override
    public void interactWithIntentHandle() {

    }

    @Override
    public void saveIntentToDb(List<Map<IntentGoalBean, IntentManagementFunction>> intentMapList) {
        List<IntentGoalBean> subIntentGoalLit = new ArrayList<>();
        for (Map<IntentGoalBean, IntentManagementFunction> map : intentMapList) {
            subIntentGoalLit.addAll(map.keySet());
        }
        List<Intent> subIntentList = subIntentGoalLit.stream().map(IntentGoalBean::getIntent)
                .collect(Collectors.toList());
        for (Intent subIntent : subIntentList) {
            intentService.createIntent(subIntent);
        }

    }
}

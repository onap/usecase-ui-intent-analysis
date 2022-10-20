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


import org.apache.commons.collections.CollectionUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CLLBusinessActuationModule extends ActuationModule {
    @Autowired
    IntentProcessService processService;
    @Autowired
    IntentService intentService;
    @Autowired
    IntentInterfaceService intentInterfaceService;


    @Override
    public void toNextIntentHandler(IntentGoalBean intentGoalBean, IntentManagementFunction IntentHandler) {
        processService.setIntentRole(IntentHandler, null);
        processService.intentProcess(intentGoalBean);
    }

    @Override
    public void directOperation(IntentGoalBean intentGoalBean) {

    }

    @Override
    public void interactWithIntentHandle() {

    }

    @Override
    public void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler) {
        toNextIntentHandler(intentGoalBean, intentHandler);
    }
	
    @Override
    public void saveIntentToDb(Intent intent){  //ownerid   parent intent id
        List<Context> intentContexts = intent.getIntentContexts();
        if (CollectionUtils.isEmpty(intentContexts)) {
            intentContexts = new ArrayList<>();
        }
        //ownerId  intentId=""  show relatioship beteween  intent
        Context ownerIdContext = new Context();
        ownerIdContext.setContextId(CommonUtil.getUUid());
        ownerIdContext.setContextName("ownerId");
        List<Condition> idConditionList = new ArrayList<>();
        Condition idCondition = new Condition();
        idCondition.setConditionValue(intent.getIntentId());
        idCondition.setOperator(OperatorType.EQUALTO);
        idCondition.setConditionName("intentId");
        idCondition.setConditionId(CommonUtil.getUUid());

        idConditionList.add(idCondition);
        ownerIdContext.setContextConditions(idConditionList);

        intentContexts.add(ownerIdContext);
        intent.setIntentContexts(intentContexts);
        intentService.createIntent(intent);
    }

    @Override
    public void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean){

    }
}

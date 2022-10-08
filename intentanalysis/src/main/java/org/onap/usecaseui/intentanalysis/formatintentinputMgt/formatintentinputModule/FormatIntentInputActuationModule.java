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
package org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class FormatIntentInputActuationModule extends ActuationModule {
    @Autowired
    IntentProcessService processService;
    @Autowired
    IntentService intentService;
    @Override
    public void toNextIntentHandler(Intent intent, IntentManagementFunction IntentHandler) {
        log.info("do nothing");
    }

    @Override
    public void directOperation() {
    }

    @Override
    public void interactWithIntentHandle() {
    }

    @Override
    public void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler) {
    }
    @Override
    public void saveIntentToDb(Intent intent){
        List<Context> intentContexts = intent.getIntentContexts();
        if (CollectionUtils.isEmpty(intentContexts)) {
            intentContexts = new ArrayList<>();
        }
        Context ownerInfoCon = new Context();
        ownerInfoCon.setContextId(CommonUtil.getUUid());
        ownerInfoCon.setContextName("ownerInfo");
        List<Condition> conditionList = new ArrayList<>();
        Condition condition = new Condition();
        condition.setConditionId(CommonUtil.getUUid());
        condition.setConditionName("ownerName");
        condition.setOperator(OperatorType.EQUALTO);
        condition.setConditionValue(FormatIntentInputManagementFunction.class.getSimpleName());
        conditionList.add(condition);
        ownerInfoCon.setContextConditions(conditionList);
        intentContexts.add(ownerInfoCon);
        //ownerId  intentId=parent intent id
        Context ownerIdContext = new Context();
        ownerIdContext.setContextId(CommonUtil.getUUid());
        ownerIdContext.setContextName("ownerId");
        List<Condition> idConditionList = new ArrayList<>();
        Condition idCondition = new Condition();
        idCondition.setConditionId(CommonUtil.getUUid());
        idCondition.setConditionName("intentId");
        idCondition.setOperator(OperatorType.EQUALTO);
        idCondition.setConditionValue(intent.getIntentId());
        idConditionList.add(idCondition);
        ownerIdContext.setContextConditions(idConditionList);
        intentContexts.add(ownerIdContext);

        intent.setIntentContexts(intentContexts);
        intentService.createIntent(intent);
    }

}

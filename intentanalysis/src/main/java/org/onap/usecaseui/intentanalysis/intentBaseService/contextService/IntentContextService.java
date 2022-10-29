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
package org.onap.usecaseui.intentanalysis.intentBaseService.contextService;

import org.apache.commons.collections.CollectionUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.CLLBusinessIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntentContextService {

    @Autowired
    private IntentService intentService;

    @Autowired
    ApplicationContext applicationContext;

    public void updateChindIntentContext(Intent originIntent, Intent intent){
        List<Context> contextList = intent.getIntentContexts();
        if (CollectionUtils.isEmpty(contextList)) {
            contextList = new ArrayList<>();
        }
        Condition condition1 = new Condition();
        condition1.setConditionId(CommonUtil.getUUid());
        condition1.setConditionName(originIntent.getIntentName() + "id");
        condition1.setOperator(OperatorType.EQUALTO);
        condition1.setConditionValue(originIntent.getIntentId());

        Context context = new Context();
        context.setContextName("parentIntent info");
        context.setContextId(CommonUtil.getUUid());
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        context.setContextConditions(conditionList);
        contextList.add(context);
        intent.setIntentContexts(contextList);

    }

    public void updateParentIntentContext(Intent originIntent, Intent intent){
        List<Context> contextList = originIntent.getIntentContexts();
        if (CollectionUtils.isEmpty(contextList)) {
            contextList = new ArrayList<>();
        }

        Condition condition1 = new Condition();
        condition1.setConditionId(CommonUtil.getUUid());
        condition1.setConditionName(intent.getIntentName() + "id");
        condition1.setOperator(OperatorType.EQUALTO);
        condition1.setConditionValue(intent.getIntentId());

        boolean isSubIntentInfoExist = false;
        for (Context context : contextList) {
            if (context.getContextName().contains("subIntent info")){
                List<Condition> conditionList = context.getContextConditions();
                if (CollectionUtils.isEmpty(conditionList)) {
                    conditionList = new ArrayList<>();
                }
                conditionList.add(condition1);
                context.setContextConditions(conditionList);
                isSubIntentInfoExist = true;
            }
        }

        if (isSubIntentInfoExist != true){
            Context context = new Context();
            context.setContextName("subIntent info");
            context.setContextId(CommonUtil.getUUid());
            List<Condition> conditionList = new ArrayList<>();
            conditionList.add(condition1);
            context.setContextConditions(conditionList);
            contextList.add(context);
            originIntent.setIntentContexts(contextList);
        }
    }

    public void updateIntentOwnerHandlerContext(Intent intent, IntentManagementFunction intentOwner, IntentManagementFunction intentHandler){
        List<Context> contextList = intent.getIntentContexts();
        if (CollectionUtils.isEmpty(contextList)){
            contextList = new ArrayList<>();
        }

        Condition condition1 = new Condition();
        condition1.setConditionId(CommonUtil.getUUid());
        condition1.setConditionName("owner class name");
        condition1.setOperator(OperatorType.EQUALTO);
        condition1.setConditionValue(intentOwner.getClass().getName());

        Context context = new Context();
        context.setContextName("owner info");
        context.setContextId(CommonUtil.getUUid());
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(condition1);
        context.setContextConditions(conditionList);
        contextList.add(context);

        Condition condition2 = new Condition();
        condition2.setConditionId(CommonUtil.getUUid());
        condition2.setConditionName("handler class name");
        condition2.setOperator(OperatorType.EQUALTO);
        condition2.setConditionValue(intentHandler.getClass().getName());

        Context context2 = new Context();
        context2.setContextName("handler info");
        context2.setContextId(CommonUtil.getUUid());
        List<Condition> conditionList2 = new ArrayList<>();
        conditionList2.add(condition2);
        context2.setContextConditions(conditionList2);
        contextList.add(context2);

        intent.setIntentContexts(contextList);
    }

    public List<Intent> getSubIntentInfoFromContext(Intent originIntent){
        List<Intent> subIntentList = new ArrayList<>();
        //form db
      //  List<Context> contextList = originIntent.getIntentContexts();
        Intent dbIntent = intentService.getIntent(originIntent.getIntentId());
        List<Context> contextList = dbIntent.getIntentContexts();
        for (Context context : contextList) {
            if (context.getContextName().contains("subIntent info")){
                List<Condition> conditionList = context.getContextConditions();
                for (Condition condition : conditionList) {
                    String subIntentId = condition.getConditionValue();
                    Intent subInent = intentService.getIntent(subIntentId);
                    subIntentList.add(subInent);
                }
            }
        }
        return subIntentList;
    }

    public IntentManagementFunction getHandlerInfo(Intent intent){
        List<Context> contextList = intent.getIntentContexts();
        IntentManagementFunction handler = new IntentManagementFunction();

        for (Context context : contextList) {
            if (context.getContextName().contains("handler info")) {
                List<Condition> conditionList = context.getContextConditions();
                String handlerClassName = conditionList.get(0).getConditionValue();
                handler = (IntentManagementFunction) applicationContext
                    .getBean(CLLBusinessIntentManagementFunction.class.getSimpleName());
            }
        }
        return handler;
    }

    public void deleteSubIntentContext(Intent intent, String deleteIntentId){
        List<Context> contextList = intent.getIntentContexts();
        for (Context context : contextList) {
            if (context.getContextName().contains("subIntent info")) {
                List<Condition> conditionList = context.getContextConditions();
                for (Condition condition : conditionList) {
                    if (condition.getConditionValue() == deleteIntentId){
                        conditionList.remove(condition);
                    }
                }
            }
        }
    }

}

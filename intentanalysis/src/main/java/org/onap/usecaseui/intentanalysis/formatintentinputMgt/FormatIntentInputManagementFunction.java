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
package org.onap.usecaseui.intentanalysis.formatintentinputMgt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGenerateType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Data
@Component("formatIntentInputManagementFunction")
public class FormatIntentInputManagementFunction extends IntentManagementFunction {
    @Autowired
    public IntentContextService intentContextService;

    @Resource(name = "formatIntentInputKnowledgeModule")
    public void setKnowledgeModule(KnowledgeModule knowledgeModule) {
        this.knowledgeModule = knowledgeModule;
    }

    @Resource(name = "formatIntentInputActuationModule")
    public void setActuationModule(ActuationModule actuationModule) {

        this.actuationModule = actuationModule;
    }

    @Resource(name = "formatIntentInputDecisionModule")
    public void setDecisionModule(DecisionModule decisionModule) {

        this.decisionModule = decisionModule;
    }

    @Autowired
    IntentInterfaceService intentInterfaceService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    IntentService intentService;

    @Override
    public void receiveIntentAsOwner(IntentGoalBean intentGoalBean) {

        IntentGoalBean originIntentGoalBean = detection(intentGoalBean);
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> linkedMap = investigation(originIntentGoalBean);
        implementIntent(intentGoalBean.getIntent(), linkedMap);
    }

    @Override
    public void receiveIntentAsHandler(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler) {
    }

    public IntentGoalBean detection(IntentGoalBean intentGoalBean) {
        Intent originIntent = intentGoalBean.getIntent();
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        if (intentGoalType == IntentGoalType.CREATE) {
            return knowledgeModule.intentCognition(originIntent);
        } else if (intentGoalType == IntentGoalType.UPDATE) {
            return new IntentGoalBean(intentGoalBean.getIntent(), IntentGoalType.UPDATE);
        } else {
            return new IntentGoalBean(intentGoalBean.getIntent(), IntentGoalType.DELETE);
        }
    }

    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigation(IntentGoalBean intentGoalBean) {
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        if (intentGoalType == IntentGoalType.CREATE) {
            return decisionModule.investigationCreateProcess(intentGoalBean);
        } else if (intentGoalType == IntentGoalType.UPDATE) {
            return decisionModule.investigationUpdateProcess(intentGoalBean);
        } else {
            return decisionModule.investigationDeleteProcess(intentGoalBean);
        }
    }

    public boolean implementIntent(Intent originIntent, LinkedHashMap<IntentGoalBean, IntentManagementFunction> linkedIntentMap) {
        Iterator<Map.Entry<IntentGoalBean, IntentManagementFunction>> iterator = linkedIntentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<IntentGoalBean, IntentManagementFunction> next = iterator.next();
            IntentGoalBean newIntentGoalBean = next.getKey();
            IntentGoalType intentGoalType = newIntentGoalBean.getIntentGoalType();
            if (intentGoalType == IntentGoalType.CREATE) {
                Intent newIdIntent = decisionModule.intentObjectDefine(originIntent, next.getKey().getIntent());
                intentContextService.updateIntentOwnerHandlerContext(newIdIntent, this, next.getValue());
                intentContextService.updateParentIntentContext(originIntent, newIdIntent);
                intentContextService.updateChindIntentContext(originIntent, newIdIntent);
                //intent-Distribution-create
                boolean isAcceptCreate = intentInterfaceService.createInterface(originIntent,
                        new IntentGoalBean(newIdIntent, IntentGoalType.CREATE), next.getValue());
                originIntent.setIntentGenerateType(IntentGenerateType.USERINPUT);
                //save user input intent
                intentService.createIntent(originIntent);
                return isAcceptCreate;
            } else if (intentGoalType == IntentGoalType.UPDATE) {
                log.info("formatIntentInputIMF UPDATE");
                //update cllBusinessIntent's expectation
                Intent subIntent = newIntentGoalBean.getIntent();
                updateIntentInfo(originIntent, subIntent);
                // intent-Distribution and operate  |update cllBusiness intent
                boolean isAcceptUpdate = intentInterfaceService.updateInterface(originIntent,
                        new IntentGoalBean(subIntent, IntentGoalType.UPDATE), next.getValue());
                //update userInput intent
                intentService.updateIntent(originIntent);
            } else {
                // intent-Distribution-delete
                boolean isAcceptDelete = intentInterfaceService.deleteInterface(originIntent, next.getKey(), next.getValue());
            }
        }
        return true;
    }

    public void updateIntentInfo(Intent originIntent, Intent intent) {

        List<Expectation> originIntentExpectationList = originIntent.getIntentExpectations();
        List<Expectation> intentExpectationList = intent.getIntentExpectations();
        int newIntentExpectationNum = originIntentExpectationList.size();
        int oldIntentExpectationNum = intentExpectationList.size();

        List<Expectation> changeList = new ArrayList<>();
        if (newIntentExpectationNum != oldIntentExpectationNum) {
            if (newIntentExpectationNum < oldIntentExpectationNum) {

                for (Expectation oldExpectation : intentExpectationList) {//search
                    boolean bFindExpectation = false;
                    for (Expectation newExpectation : originIntentExpectationList) {//param
                        if (oldExpectation.getExpectationName().equals(newExpectation.getExpectationName())) {
                            bFindExpectation = true;
                            break;
                        }
                    }
                    if (bFindExpectation) {
                        changeList.add(oldExpectation);
                    }
                }
            }
        }
        intent.setIntentExpectations(changeList);
    }
}

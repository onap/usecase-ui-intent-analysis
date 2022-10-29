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

import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.CLLBusinessIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
@Component
public class FormatIntentInputDecisionModule extends DecisionModule {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    public IntentContextService intentContextService;

    @Autowired
    public IntentService intentService;

    @Override
    public void determineUltimateGoal() {
    }

    @Override
    public IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean) {
        // if intentName contain cll  return
        if (intentGoalBean.getIntent().getIntentName().toLowerCase(Locale.ROOT).contains("cll")) {
            return (IntentManagementFunction) applicationContext.getBean(CLLBusinessIntentManagementFunction.class.getSimpleName());
        }
        return null;
    }


    @Override
    public void decideSuitableAction() {
    }

    @Override
    public void interactWithTemplateDb() {
    }

    @Override
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationCreateProcess(IntentGoalBean intentGoalBean) {
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        boolean needDecompostion = needDecompostion(intentGoalBean);
        if (needDecompostion) {
            intentDecomposition(intentGoalBean);
        } else {
            intentMap.put(intentGoalBean, exploreIntentHandlers(intentGoalBean));
        }
        return intentMap;
    }

    public boolean needDecompostion(IntentGoalBean intentGoalBean) {
        //expectationName just contain cll and slicing need decompost
        List<Expectation> intentExpectations = intentGoalBean.getIntent().getIntentExpectations();
        List<String> expectationNameList = intentExpectations.stream().map(Expectation::getExpectationName)
                .distinct().collect(Collectors.toList());
        if (expectationNameList.size() > 1) {
            List<String> cllList = expectationNameList.stream().filter(x -> StringUtils.containsIgnoreCase(x, "cll")).collect(Collectors.toList());
            List<String> slicingList = expectationNameList.stream().filter(x -> StringUtils.containsIgnoreCase(x, "slicing")).collect(Collectors.toList());
            if (cllList.size() > 0 && slicingList.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public List<IntentGoalBean> intentDecomposition(IntentGoalBean intentGoalBean) {
        //todo
        return null;
    }

    @Override
    //format is
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationUpdateProcess(IntentGoalBean intentGoalBean) {
        //get format-cll intent
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        List<Intent> subIntentList = intentContextService.getSubIntentInfoFromContext(intentGoalBean.getIntent());
        for (Intent intent : subIntentList) {
            IntentManagementFunction intentHandlerInfo = intentContextService.getHandlerInfo(intent);
            UpdateIntentInfo(intentGoalBean.getIntent(), intent);
            IntentGoalBean subIntentGoalBean = new IntentGoalBean(intent, IntentGoalType.UPDATE);
            intentMap.put(subIntentGoalBean, intentHandlerInfo);
        }
        return intentMap;
    }

    public void UpdateIntentInfo(Intent originIntent, Intent intent){

        List<Expectation> originIntentExpectationList = originIntent.getIntentExpectations();
        List<Expectation> intentExpectationList = intent.getIntentExpectations();
        int newIntentExpectationNum = originIntentExpectationList.size();
        int oldIntentExpectationNum = intentExpectationList.size();

        if (newIntentExpectationNum != oldIntentExpectationNum){
            if (newIntentExpectationNum < oldIntentExpectationNum){
                boolean bFindExpectation = false;
                for (Expectation oldExpectation : intentExpectationList) {
                    for (Expectation newExpectation : originIntentExpectationList) {
                        if (oldExpectation.getExpectationName().equals(newExpectation.getExpectationName())){
                            bFindExpectation = true;
                        }
                    }
                    if (bFindExpectation == false){
                        intentExpectationList.remove(oldExpectation);
                    }
                }
            }
        }
        intent.setIntentExpectations(intentExpectationList);
    }

    @Override
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationDeleteProcess(IntentGoalBean intentGoalBean) {
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        List<Intent> subIntentList = intentContextService.getSubIntentInfoFromContext(intentGoalBean.getIntent());
        for (Intent intent : subIntentList) {
            IntentManagementFunction intentHandlerInfo = intentContextService.getHandlerInfo(intent);
            IntentGoalBean subIntentGoalBean = new IntentGoalBean(intent, IntentGoalType.DELETE);
            intentMap.put(subIntentGoalBean, intentHandlerInfo);
        }
        return intentMap;
    }

}

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


import lombok.extern.log4j.Log4j2;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.service.ImfRegInfoService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
public class CLLBusinessDecisionModule extends DecisionModule {
    @Autowired
    private ImfRegInfoService imfRegInfoService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void determineUltimateGoal() {
    }

    @Override
    public IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean) {
        //  db  filter imf  supportArea;
        //SupportInterface> supportInterfaces;
        IntentManagementFunctionRegInfo imfRegInfo = imfRegInfoService.getImfRegInfoList(intentGoalBean);
        return (IntentManagementFunction) applicationContext.getBean(imfRegInfo.getHandleName());
    }

    @Override
    public void intentDefinition() {
    }

    @Override
    public void decideSuitableAction() {
    }


    public boolean needDecompostion(IntentGoalBean intentGoalBean) {
        //different expectationType need decompostion  ExpectationType>1 or objtype>1
        if (intentGoalBean.getIntentGoalType().equals(IntentGoalType.CREATE)) {
            List<Expectation> intentExpectations = intentGoalBean.getIntent().getIntentExpectations();
            List<ExpectationType> expectationTypeList = intentExpectations.stream()
                    .map(Expectation::getExpectationType).distinct().collect(Collectors.toList());
            if (expectationTypeList.size() > 1) {
                return true;
            } else {
                List<ObjectType> objectTypeList = intentExpectations.stream().map(x ->
                        x.getExpectationObject().getObjectType()).collect(Collectors.toList());
                if (objectTypeList.size() > 1) {
                    return  true;
                }
            }
        }
        return false;
    }

    public List<IntentGoalBean> intentDecomposition(IntentGoalBean intentGoalBean) {
        //ExpectationType   expectation.ExpectationObject.objtype
        Map<ExpectationType, List<Expectation>> expectationTypeListMap = intentGoalBean.getIntent().getIntentExpectations()
                .stream().collect(Collectors.groupingBy(x -> x.getExpectationType()));
        List<IntentGoalBean> subIntentGoalList = new ArrayList<>();
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        for (Map.Entry<ExpectationType, List<Expectation>> entry : expectationTypeListMap.entrySet()) {

            Map<ObjectType, List<Expectation>> objTypeMap = entry.getValue().stream()
                    .collect(Collectors.groupingBy(x -> x.getExpectationObject().getObjectType()));
            for (Map.Entry<ObjectType, List<Expectation>> objEntry : objTypeMap.entrySet()) {
                IntentGoalBean subIntentGoalBean = new IntentGoalBean();
                Intent subIntent = new Intent();
                subIntent.setIntentId(CommonUtil.getUUid());
                subIntent.setIntentName(objEntry.getValue().get(0).getExpectationName().replace("Expectation", "Intent"));
                subIntent.setIntentExpectations(objEntry.getValue());
                //TODO      intentFulfilmentInfo intentContexts
                subIntentGoalBean.setIntentGoalType(intentGoalType);
                subIntentGoalBean.setIntent(subIntent);
                subIntentGoalList.add(subIntentGoalBean);
            }
        }
        return subIntentGoalList;
    }

    public List<IntentGoalBean> intentOrchestration(List<IntentGoalBean> subIntentGoalList) {
        List<IntentGoalBean> sortList = new ArrayList<>();
        List<IntentGoalBean> deliveryGoalList = subIntentGoalList.stream().filter(x -> x.getIntent().getIntentName()
                .equalsIgnoreCase("delivery")).collect(Collectors.toList());
        List<IntentGoalBean> assuranceGoalList = subIntentGoalList.stream().filter(x -> x.getIntent().getIntentName()
                .equalsIgnoreCase("assurance")).collect(Collectors.toList());
        List<IntentGoalBean> otherGoalList = subIntentGoalList.stream().filter(x -> !x.getIntent().getIntentName()
                .equalsIgnoreCase("assurance") && !x.getIntent().getIntentName()
                .equalsIgnoreCase("delivery")).collect(Collectors.toList());
        sortList.addAll(deliveryGoalList);
        sortList.addAll(assuranceGoalList);
        sortList.addAll(otherGoalList);
        return sortList;
    }

    @Override
    public void interactWithTemplateDb() {
    }

    @Override
    public List<Map<IntentGoalBean, IntentManagementFunction>> findHandler(IntentGoalBean intentGoalBean) {
        boolean needDecompostion = needDecompostion(intentGoalBean);
        List<Map<IntentGoalBean, IntentManagementFunction>> intentMapList = new ArrayList<>();
        if (needDecompostion) {
            List<IntentGoalBean> subIntentGoalList = intentDecomposition(intentGoalBean);
            List<IntentGoalBean> sortList = intentOrchestration(subIntentGoalList);
            for (IntentGoalBean subIntentGoal : sortList) {
                Map<IntentGoalBean, IntentManagementFunction> map = new HashMap<>();
                IntentManagementFunction imf = exploreIntentHandlers(subIntentGoal);
                map.put(subIntentGoal, imf);
                intentMapList.add(map);
            }
        } else {
            Map<IntentGoalBean, IntentManagementFunction> map = new HashMap<>();
            map.put(intentGoalBean, exploreIntentHandlers(intentGoalBean));
            intentMapList.add(map);
        }
        return intentMapList;
    }

}

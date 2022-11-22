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
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGenerateType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.service.ImfRegInfoService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    @Autowired
    IntentService intentService;

    @Autowired
    IntentContextService intentContextService;

    @Override
    public void determineUltimateGoal() {
    }

    @Override
    public IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean) {
        //  db  filter imf  supportArea;
        //SupportInterface> supportInterfaces;
        IntentManagementFunctionRegInfo imfRegInfo = imfRegInfoService.getImfRegInfo(intentGoalBean);
        return (IntentManagementFunction) applicationContext.getBean(imfRegInfo.getHandleName());
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
                        x.getExpectationObject().getObjectType()).distinct().collect(Collectors.toList());
                if (objectTypeList.size() > 1) {
                    return true;
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
                subIntent.setIntentName(objEntry.getValue().get(0).getExpectationName().replace("Expectation", "Intent"));
                subIntent.setIntentExpectations(objEntry.getValue());
                subIntent.setIntentGenerateType(IntentGenerateType.SYSTEMGENARATE);
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
        List<IntentGoalBean> deliveryGoalList = subIntentGoalList.stream().filter(x ->
                StringUtils.containsIgnoreCase(x.getIntent().getIntentName(), "delivery")).collect(Collectors.toList());
        List<IntentGoalBean> assuranceGoalList = subIntentGoalList.stream().filter(x ->
                StringUtils.containsIgnoreCase(x.getIntent().getIntentName(), "assurance")).collect(Collectors.toList());
        List<IntentGoalBean> otherGoalList = subIntentGoalList.stream().filter(x ->
                !StringUtils.containsIgnoreCase(x.getIntent().getIntentName(), "delivery")
                        && !StringUtils.containsIgnoreCase(x.getIntent().getIntentName(), "assurance")).collect(Collectors.toList());
        sortList.addAll(deliveryGoalList);
        sortList.addAll(assuranceGoalList);
        sortList.addAll(otherGoalList);
        return sortList;
    }

    @Override
    public void interactWithTemplateDb() {
    }

    @Override
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationCreateProcess(IntentGoalBean intentGoalBean) {
        log.info("CLLBusinessIntentManagementFunction investigation create process start");
        boolean needDecompostion = needDecompostion(intentGoalBean);
        log.debug("CLLBusinessIntentManagementFunction need decompose :" + needDecompostion);
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        if (needDecompostion) {
            List<IntentGoalBean> subIntentGoalList = intentDecomposition(intentGoalBean);
            List<IntentGoalBean> sortList = intentOrchestration(subIntentGoalList);
            for (IntentGoalBean subIntentGoal : sortList) {
                IntentManagementFunction imf = exploreIntentHandlers(subIntentGoal);
                intentMap.put(subIntentGoal, imf);
            }
        } else {
            intentMap.put(intentGoalBean, exploreIntentHandlers(intentGoalBean));
        }
        log.info("CLLBusinessIntentManagementFunction investigation create process finished");
        log.debug("CLLBusinessIntentManagementFunction decomposed subIntent list name :"
                + StringUtils.join(intentMap.keySet().stream().map(IntentGoalBean::getIntent)
                .map(Intent::getIntentName).collect(Collectors.toList()), ","));
        return intentMap;
    }


    @Override
    //format is
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationUpdateProcess(IntentGoalBean intentGoalBean) {
        log.info("CLLBusinessIntentManagementFunction investigation update process start");
        //get cll-delivery cll-assurance intent
        Intent originIntent = intentGoalBean.getIntent();
        List<Expectation> originIntentExpectationList = originIntent.getIntentExpectations();

        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        List<Intent> subIntentList = intentContextService.getSubIntentInfoFromContext(intentGoalBean.getIntent());
        for (Intent intent : subIntentList) {
            IntentManagementFunction intentHandlerInfo = intentContextService.getHandlerInfo(intent);
            boolean bFindIntent = false;
            for (Expectation originExpectation : originIntentExpectationList) {
                if (intent.getIntentName().replace("Intent", "")
                        .equals(originExpectation.getExpectationName().replace("Expectation", ""))) {
                    bFindIntent = true;
                    break;
                }
            }

            if (false == bFindIntent) {
                intentContextService.deleteSubIntentContext(originIntent, intent.getIntentId());
                IntentGoalBean subIntentGoalBean = new IntentGoalBean(intent, IntentGoalType.DELETE);
                intentMap.put(subIntentGoalBean, intentHandlerInfo);
            } else {
                IntentGoalBean subIntentGoalBean = new IntentGoalBean(intent, IntentGoalType.UPDATE);
                intentMap.put(subIntentGoalBean, intentHandlerInfo);
            }
        }
        log.info("CLLBusinessIntentManagementFunction investigation update process finished");
        log.debug("CLLBusinessIntentManagementFunction investigation update process intent list name "
                + StringUtils.join(intentMap.keySet().stream().map(IntentGoalBean::getIntent)
                .map(Intent::getIntentName).collect(Collectors.toList()), ","));
        return intentMap;
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

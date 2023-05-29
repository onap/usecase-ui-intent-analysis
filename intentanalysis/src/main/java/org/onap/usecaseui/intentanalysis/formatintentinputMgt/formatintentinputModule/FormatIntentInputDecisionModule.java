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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.CLLBusinessIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.IntentInputException;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.service.IntentReportService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FormatIntentInputDecisionModule extends DecisionModule {
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    public IntentContextService intentContextService;

    @Autowired
    public IntentService intentService;

    @Autowired
    private IntentReportService intentReportService;

    @Resource(name = "intentReportExecutor")
    private ScheduledThreadPoolExecutor executor;

    @Override
    public void determineUltimateGoal() {
    }

    @Override
    public IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean) {
        // if intentName contain cll  return
        if (intentGoalBean.getIntent().getIntentName().toLowerCase(Locale.ROOT).contains("cll")) {
            return (IntentManagementFunction) applicationContext.getBean(CLLBusinessIntentManagementFunction.class.getSimpleName());
        } else {
            String msg = String.format("intentName is: %s can't find corresponding IntentManagementFunction,please check Intent Name", intentGoalBean.getIntent().getIntentName());
            log.error(msg);
            throw new IntentInputException(msg, ResponseConsts.RET_FIND_CORRESPONDING_FAIL);
        }
    }


    @Override
    public void decideSuitableAction() {
    }

    @Override
    public void interactWithTemplateDb() {
    }

    @Override
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationCreateProcess(IntentGoalBean intentGoalBean) {
        log.info("FormatIntentInputMgt investigation create process start");
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();

        IntentGoalBean newIntentGoalBean = removeReportExpectation(intentGoalBean);

        boolean needDecompostion = needDecompostion(newIntentGoalBean);
        log.debug("FormatIntentInputMgt need decompose :" + needDecompostion);
        if (needDecompostion) {
            intentDecomposition(newIntentGoalBean);
        } else {
            intentMap.put(newIntentGoalBean, exploreIntentHandlers(newIntentGoalBean));
        }
        log.info("FormatIntentInputMgt investigation create process finished");
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
        log.info("FormatIntentInputMgt investigation update process start");
        //get format-cll intent
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        // update format-cll intentContext
        Intent intent1 = intentService.getIntent(intentGoalBean.getIntent().getIntentId());
        intentGoalBean.getIntent().setIntentContexts(intent1.getIntentContexts());
        List<Intent> subIntentList = intentContextService.getSubIntentInfoFromContext(intentGoalBean.getIntent());
        for (Intent intent : subIntentList) {
            IntentManagementFunction intentHandlerInfo = intentContextService.getHandlerInfo(intent);
            IntentGoalBean subIntentGoalBean = new IntentGoalBean(intent, IntentGoalType.UPDATE);
            intentMap.put(subIntentGoalBean, intentHandlerInfo);
        }
        log.info("FormatIntentInputMgt investigation update process finished");
        return intentMap;
    }

    public void UpdateIntentInfo(Intent originIntent, Intent intent) {

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

    private IntentGoalBean removeReportExpectation(IntentGoalBean intentGoalBean) {
        Intent intent = intentGoalBean.getIntent();
        List<Expectation> intentExpectations = intent.getIntentExpectations();
        List<Expectation> report = intentExpectations.stream()
                .filter(expectation -> ExpectationType.REPORT.equals(expectation.getExpectationType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(report)) {
            log.info("No expectation of type report is entered");
            return intentGoalBean;
        }
        generationIntentReport(report.get(0), intent.getIntentId());
        Intent newIntent = new Intent();
        BeanUtils.copyProperties(intent, newIntent);
        List<Expectation> notReport = intentExpectations.stream()
                .filter(expectation -> !ExpectationType.REPORT.equals(expectation.getExpectationType()))
                .collect(Collectors.toList());
        newIntent.setIntentExpectations(notReport);
        return new IntentGoalBean(newIntent, intentGoalBean.getIntentGoalType());
    }

    private void generationIntentReport(Expectation expectation, String intentId) {
        List<ExpectationTarget> expectationTargets = expectation.getExpectationTargets();
        if (CollectionUtils.isEmpty(expectationTargets)) {
            log.error("The expectation target is empty,expectationId is {}", expectation.getExpectationId());
            return;
        }
        ExpectationTarget expectationTarget = expectationTargets.get(0);
        List<Condition> targetConditions = expectationTarget.getTargetConditions();
        if (CollectionUtils.isEmpty(targetConditions)) {
            log.error("The target condition is empty,expectationId is {}", expectation.getExpectationId());
            return;
        }
        Condition condition = targetConditions.get(0);
        try {
            int conditionValue = Integer.parseInt(condition.getConditionValue());
            log.info("Start executing scheduled intent report generation task ");
            executor.scheduleAtFixedRate(() -> intentReportService.saveIntentReportByIntentId(intentId), 2, conditionValue, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("The exception is {}", e.getMessage());
        }
    }
}

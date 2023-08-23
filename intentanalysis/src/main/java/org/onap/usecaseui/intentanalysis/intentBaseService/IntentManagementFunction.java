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
package org.onap.usecaseui.intentanalysis.intentBaseService;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentInfo;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.service.FulfillmentInfoService;
import org.onap.usecaseui.intentanalysis.service.IntentReportService;
import org.onap.usecaseui.intentanalysis.service.ObjectInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType.CREATE;

@Log4j2
@Data
@Configuration
@Component
public class IntentManagementFunction {
    protected ActuationModule actuationModule;
    protected DecisionModule decisionModule;
    protected KnowledgeModule knowledgeModule;

    @Autowired
    private FulfillmentInfoService fulfillmentInfoService;

    @Autowired
    private ObjectInstanceService objectInstanceService;

    @Autowired
    private IntentReportService intentReportService;

    @Resource(name = "intentReportExecutor")
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public void receiveIntentAsOwner(IntentGoalBean intentGoalBean){};
    public void receiveIntentAsHandler(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler){};
    public void createReport(String intentId, FulfillmentInfo fulfillmentInfo){};

    protected void saveFulfillmentAndObjectInstance(String intentId, FulfillmentInfo fulfillmentInfo) {
        // save fulfillmentInfo and objectInstance
        fulfillmentInfoService.saveFulfillmentInfo(intentId, fulfillmentInfo);
        objectInstanceService.saveObjectInstances(intentId, fulfillmentInfo);
    }

    protected void generationIntentReport(IntentGoalBean intentGoalBean) {
        if (CREATE != intentGoalBean.getIntentGoalType()) {
            return;
        }
        Intent intent = intentGoalBean.getIntent();
        List<Expectation> intentExpectations = intent.getIntentExpectations();
        List<Expectation> report = intentExpectations.stream()
                .filter(expectation -> ExpectationType.REPORT.equals(expectation.getExpectationType()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(report)) {
            log.info("No expectation of type report is entered");
            return;
        }
        List<ExpectationTarget> expectationTargets = report.get(0).getExpectationTargets();
        if (CollectionUtils.isEmpty(expectationTargets)) {
            log.error("The expectation target is empty,expectationId is {}", report.get(0).getExpectationId());
            return;
        }
        ExpectationTarget expectationTarget = expectationTargets.get(0);
        List<Condition> targetConditions = expectationTarget.getTargetConditions();
        if (CollectionUtils.isEmpty(targetConditions)) {
            log.error("The target condition is empty,expectationId is {}", report.get(0).getExpectationId());
            return;
        }
        Condition condition = targetConditions.get(0);
        try {
            int conditionValue = Integer.parseInt(condition.getConditionValue());
            log.info("Start executing scheduled intent report generation task ");
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> intentReportService.saveIntentReportByIntentId(intent.getIntentId()), 2, conditionValue, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("The exception is {}", e.getMessage());
        }
    }
}

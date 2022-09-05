/*
 * Copyright 2022 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.usecaseui.intentanalysis.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.bean.enums.*;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.FulfilmentInfo;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentServiceTest extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntentServiceTest.class);

    private static final String TEST_INTENT_ID_1 = "intentId1";

    private static final String TEST_INTENT_ID_2 = "intentId2";

    private static final String TEST_INTENT_NAME = "CLL Business intent";

    @Autowired
    private IntentService intentService;

    @Before
    public void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    public void testCreateIntentSuccess() throws IOException {
        Intent intent = new Intent();
        Expectation expectation1 = new Expectation();
        ExpectationTarget target1 = new ExpectationTarget();
        ExpectationObject object1 = new ExpectationObject();
        Context intentContext = new Context();
        FulfilmentInfo intentFulfilmentInfo = new FulfilmentInfo();
        Condition targetCondition1 = new Condition();
        targetCondition1.setConditionId("conditionId");
        targetCondition1.setConditionName("conditionName");
        targetCondition1.setOperator(OperatorType.valueOf("EQUALTO"));
        targetCondition1.setConditionValue("conditionValue");
        List<Condition> targetConditionList = new ArrayList<>();
        targetConditionList.add(targetCondition1);
        target1.setTargetId("targetId");
        target1.setTargetName("targetName");
        target1.setTargetConditions(targetConditionList);
        List<ExpectationTarget> expectationTargetList = new ArrayList<>();
        expectationTargetList.add(target1);
        object1.setObjectType(ObjectType.valueOf("OBJECT1"));
        object1.setObjectInstance("objectInstance");
        expectation1.setExpectationId("expectationId");
        expectation1.setExpectationName("expectationName");
        expectation1.setExpectationType(ExpectationType.valueOf("DELIVERY"));
        expectation1.setExpectationObject(object1);
        expectation1.setExpectationTargets(expectationTargetList);
        List<Expectation> expectationList = new ArrayList<>();
        expectationList.add(expectation1);
        intentContext.setContextId("intentContextId");
        intentContext.setContextName("intentContextName");
        List<Context> intentContextList = new ArrayList<>();
        intentContextList.add(intentContext);
        intentFulfilmentInfo.setFulfilmentStatus(FulfilmentStatus.valueOf("NOT_FULFILLED"));
        intentFulfilmentInfo.setNotFulfilledReason("NotFulfilledReason");
        intentFulfilmentInfo.setNotFulfilledState(NotFulfilledState.valueOf("COMPLIANT"));
        intent.setIntentId("testIntentId");
        intent.setIntentName("testIntentName");
        intent.setIntentContexts(intentContextList);
        intent.setIntentExpectations(expectationList);
        intent.setIntentFulfilmentInfo(intentFulfilmentInfo);

        Intent intentTmp = intentService.createIntent(intent);
        Assert.assertNotNull(intentTmp);
    }

    @Test
    public void testGetIntentListSuccess() {
        List<Intent> intentList = intentService.getIntentList();
        Assert.assertNotNull(intentList);
    }

    @Test
    public void testGetIntentSuccess() {
        Intent intent = intentService.getIntent(TEST_INTENT_ID_1);
        Assert.assertNotNull(intent);
    }

    @Test
    public void testGetIntentByNameSuccess() {
        List<Intent> intentList = intentService.getIntentByName(TEST_INTENT_NAME);
        Assert.assertNotNull(intentList);

    }

    @Test
    public void testUpdateIntentSuccess() {
        Intent intent = intentService.getIntent(TEST_INTENT_ID_1);
        intent.setIntentName("new intent name");
        List<Context> contextList = intent.getIntentContexts();
        Context intentContext = contextList.get(0);
        intentContext.setContextName("new context name");
        contextList.set(0, intentContext);
        intent.setIntentContexts(contextList);
        FulfilmentInfo intentFulfilmentInfo = intent.getIntentFulfilmentInfo();
        intentFulfilmentInfo.setNotFulfilledReason("new reason");
        intent.setIntentFulfilmentInfo(intentFulfilmentInfo);
        List<Expectation> expectationList = intent.getIntentExpectations();
        Expectation expectation = expectationList.get(0);
        expectation.setExpectationName("new expectation name");
        ExpectationObject expectationObject = expectation.getExpectationObject();
        expectationObject.setObjectInstance("new object instance");
        expectation.setExpectationObject(expectationObject);
        List<ExpectationTarget> expectationTargetList = expectation.getExpectationTargets();
        ExpectationTarget expectationTarget = expectationTargetList.get(0);
        expectationTarget.setTargetName("new target name");
        List<Condition> targetConditionList = expectationTarget.getTargetConditions();
        Condition targetCondition = targetConditionList.get(0);
        targetCondition.setConditionName("new conditon name");
        targetConditionList.set(0, targetCondition);
        expectationTarget.setTargetConditions(targetConditionList);
        expectationTargetList.remove(2);
        expectationTargetList.set(0, expectationTarget);
        expectation.setExpectationTargets(expectationTargetList);
        expectationList.set(0, expectation);
        intent.setIntentExpectations(expectationList);

        Intent updatedIntent = intentService.updateIntent(intent);
        Assert.assertEquals("new intent name", updatedIntent.getIntentName());

    }

    @Test
    public void testDeleteIntentSuccess() {
        intentService.deleteIntent(TEST_INTENT_ID_2);
        Intent intent = intentService.getIntent(TEST_INTENT_ID_2);
        Assert.assertNull(intent);
    }
}

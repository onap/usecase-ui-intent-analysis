/*
 *
 *  * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.onap.usecaseui.intentanalysis.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.FulfillmentStatus;
import org.onap.usecaseui.intentanalysis.bean.enums.NotFulfilledState;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentInfo;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;


@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
class ExpectationServiceTest extends AbstractJUnit4SpringContextTests {

    private static final String TEST_INTENT_ID_1 = "intentId1";

    private static final String TEST_INTENT_ID_2 = "intentId2";

    private static final String TEST_INTENT_ID_3 = "intent without affiliate";

    private static final String TEST_EXPECTATION_ID_1 = "expectationId1";

    private static final String TEST_EXPECTATION_ID_2= "expectationId2";

    @Autowired
    private ExpectationService expectationService;

    public Expectation createTestExpectation(String testName) {
        Expectation expectation = new Expectation();

        ExpectationObject object = new ExpectationObject();
        object.setObjectType(ObjectType.valueOf("SLICING"));
        object.setObjectInstance(Collections.singletonList("objectInstance"));

        Condition targetCondition = new Condition();
        targetCondition.setConditionId(testName + "-conditionId");
        targetCondition.setConditionName(testName + "conditionName");
        targetCondition.setOperator(OperatorType.valueOf("EQUALTO"));
        targetCondition.setConditionValue("conditionValue");
        List<Condition> targetConditionList = new ArrayList<>();
        targetConditionList.add(targetCondition);

        ExpectationTarget target = new ExpectationTarget();
        target.setTargetId(testName + "targetId");
        target.setTargetName(testName + "targetName");
        target.setTargetConditions(targetConditionList);
        List<ExpectationTarget> expectationTargetList = new ArrayList<>();
        expectationTargetList.add(target);

        FulfillmentInfo expectationFulfillmentInfo = new FulfillmentInfo();
        expectationFulfillmentInfo.setFulfillmentStatus(FulfillmentStatus.valueOf("NOT_FULFILLED"));
        expectationFulfillmentInfo.setNotFulfilledReason("NotFulfilledReason");
        expectationFulfillmentInfo.setNotFulfilledState(NotFulfilledState.valueOf("COMPLIANT"));

        expectation.setExpectationId(testName + "-expectationId");
        expectation.setExpectationName(testName + "expectationName");
        expectation.setExpectationType(ExpectationType.valueOf("DELIVERY"));
        expectation.setExpectationObject(object);
        expectation.setExpectationTargets(expectationTargetList);
        expectation.setExpectationFulfillmentInfo(expectationFulfillmentInfo);

        return expectation;
    }

    @BeforeEach
    public void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    public void testCreateIntentExpectationSuccess() {
        Expectation expectation = createTestExpectation("testCreateIntentExpectationSuccess");

        try {
            expectationService.createIntentExpectation(expectation, TEST_INTENT_ID_3);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(expectationService.getIntentExpectation(expectation.getExpectationId()));
    }

    @Test
    public void testCreateIntentExpectationListSuccess() {
        List<Expectation> expectationList = new ArrayList<>();
        Expectation expectation = createTestExpectation("testCreateIntentExpectationListSuccess");
        expectationList.add(expectation);

        try {
            expectationService.createIntentExpectationList(expectationList, TEST_INTENT_ID_3);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(expectationService.getIntentExpectation(expectation.getExpectationId()));
    }

    @Test
    public void testDeleteIntentExpectationListSuccess() {
        try {
            expectationService.deleteIntentExpectation(TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNull(expectationService.getIntentExpectation(TEST_EXPECTATION_ID_1));
    }

    @Test
    public void deleteIntentExpectationSuccess() {
        try {
            expectationService.deleteIntentExpectationList(TEST_INTENT_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertTrue(CollectionUtils.isEmpty(expectationService.getIntentExpectationList(TEST_INTENT_ID_2)));
    }

    @Test
    public void testUpdateIntentExpectationListSuccess() {
        List<Expectation> expectationList = new ArrayList<>();
        Expectation expectation = createTestExpectation("testUpdateIntentExpectationListSuccess");
        expectationList.add(expectation);

        try {
            expectationService.updateIntentExpectationList(expectationList, TEST_INTENT_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Expectation updatedExpectation = expectationService.getIntentExpectationList(TEST_INTENT_ID_2).get(0);
        Assert.assertEquals(updatedExpectation.getExpectationId(), expectation.getExpectationId());
    }

    @Test
    public void testGetIntentExpectationListSuccess() {
        List<Expectation> expectationList = expectationService.getIntentExpectationList(TEST_INTENT_ID_1);
        Assert.assertFalse(CollectionUtils.isEmpty(expectationList));
    }

    @Test
    public void testGetIntentExpectationSuccess() {
        Expectation expectationGotten = expectationService.getIntentExpectation(TEST_EXPECTATION_ID_2);
        Assert.assertNotNull(expectationGotten);
    }

}

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
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
class ExpectationObjectServiceTest extends AbstractJUnit4SpringContextTests {

    private static final String TEST_EXPECTATION_ID_1 = "expectationId1";

    private static final String TEST_EXPECTATION_ID_2 = "expectation without affiliate";

    private static final String TEST_EXPECTATION_ID_3 = "expectationId3";

    @Autowired
    private ExpectationObjectService expectationObjectService;

    public ExpectationObject createTestObject(String testName) {
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectType(ObjectType.SLICING);
        expectationObject.setObjectInstance("true");

        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId(testName + "-contextId");
        context.setContextName(testName + "-contextName");
        contextList.add(context);
        expectationObject.setObjectContexts(contextList);

        return expectationObject;
    }

    @BeforeEach
    void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    void testCreateExpectationObjectFalse() {
        ExpectationObject expectationObject = createTestObject("testCreateExpectationObjectFalse");

        try {
            expectationObjectService.createExpectationObject(expectationObject, TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
            String msg = String.format("It already exists an object for the expectation %s, update might work.", TEST_EXPECTATION_ID_1);
            Assert.assertEquals(msg, exception.getMessage());
        }
    }

    @Test
    void testCreateExpectationObjectSuccess() {
        ExpectationObject expectationObject = createTestObject("testCreateExpectationObjectSuccess");

        try {
            expectationObjectService.createExpectationObject(expectationObject, TEST_EXPECTATION_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }
        Assert.assertNotNull(expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_2));
    }

    @Test
    void testGetExpectationObjectSuccess() {
        ExpectationObject expectationObject = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_1);
        Assert.assertNotNull(expectationObject);
    }

    @Test
    void testUpdateExpectationObjectSuccess() {
        String testName = "testUpdateExpectationObjectSuccess_1";
        ExpectationObject expectationObject = createTestObject(testName);

        try {
            expectationObjectService.updateExpectationObject(expectationObject, TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationObject expectationObjectTmp = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_1);
        Assert.assertEquals(expectationObjectTmp.getObjectContexts().get(0).getContextName(), testName + "-contextName");
    }

    @Test
    void testUpdateExpectationObjectToNullSuccess() {
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectType(ObjectType.CCVPN);

        try {
            expectationObjectService.updateExpectationObject(expectationObject, TEST_EXPECTATION_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationObject expectationObjectUpdated = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_2);
        Assert.assertNotNull(expectationObjectUpdated);
    }

    @Test
    void  testDeleteExpectationObjectSuccess() {
        try {
            expectationObjectService.deleteExpectationObject(TEST_EXPECTATION_ID_3);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationObject expectationObject = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_3);
        Assert.assertNull(expectationObject);
    }
}
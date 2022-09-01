package org.onap.usecaseui.intentanalysis.test.service;

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
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.onap.usecaseui.intentanalysis.test.IntentAnalysisApplicationTests;
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

    @Autowired
    private ExpectationObjectService expectationObjectService;

    @BeforeEach
    void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    void testCreateExpectationObjectSuccess() {
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectType(ObjectType.OBJECT1);
        expectationObject.setObjectInstance("true");

        try {
            expectationObjectService.createExpectationObject(expectationObject, TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void testGetExpectationObjectSuccess() {
        ExpectationObject expectationObject = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_1);
        Assert.assertNotNull(expectationObject);
    }

    @Test
    void testUpdateExpectationObjectSuccess_1() {
        ExpectationObject expectationObject = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_1);
        expectationObject.setObjectType(ObjectType.OBJECT2);
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("testContextId");
        context.setContextName("testContextName");
        contextList.add(context);
        expectationObject.setObjectContexts(contextList);

        try {
            expectationObjectService.updateExpectationObject(expectationObject, TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationObject expectationObjectTmp = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_1);
        Assert.assertEquals(expectationObjectTmp.getObjectType(), ObjectType.OBJECT2);
        Assert.assertEquals(expectationObjectTmp.getObjectContexts().get(0).getContextName(), "testContextName");
    }

    @Test
    void testUpdateExpectationObjectSuccess_2() {
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectType(ObjectType.OBJECT2);

        try {
            expectationObjectService.updateExpectationObject(expectationObject, TEST_EXPECTATION_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationObject expectationObjectTmp = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_2);
        Assert.assertNotNull(expectationObjectTmp);
    }

    @Test
    void  testDeleteExpectationObjectSuccess() {
        try {
            expectationObjectService.deleteExpectationObject(TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationObject expectationObject = expectationObjectService.getExpectationObject(TEST_EXPECTATION_ID_1);
        Assert.assertNull(expectationObject);
    }
}
package org.onap.usecaseui.intentanalysis.test.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.test.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
class ContextServiceTest extends AbstractJUnit4SpringContextTests {

    private static final String TEST_INTENT_ID_1 = "intentId1";

    private static final String TEST_INTENT_ID_2 = "intentId2";

    private static final String TEST_INTENT_ID_3 = "intent without affiliate";

    private static final String TEST_EXPECTATION_ID = "expectation without affiliate";

    private static final String TEST_CONTEXT_ID_1 = "d64f3a5f-b091-40a6-bca0-1bc6b1ce8f43";

    @Autowired
    private ContextService contextService;

    public Context createTestContext(String testName) {
        Context context = new Context();
        context.setContextId(testName + "-contextId");
        context.setContextName(testName + "contextName");

        return context;
    }

    public List<Context> createTestContextList(String testName) {
        List<Context> contextList = new ArrayList<>();
        contextList.add(createTestContext(testName));
        return contextList;
    }

    @BeforeEach
    void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    void testCreateContextListSuccess() {
        List<Context> contextList = createTestContextList("testCreateContextListSuccess");

        try {
            contextService.createContextList(contextList, TEST_INTENT_ID_3);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(contextService.getContextList(TEST_INTENT_ID_3));
    }

    @Test
    void testCreateContextSuccess() {
        Context context = createTestContext("testCreateContextSuccess");

        try {
            contextService.createContext(context, TEST_INTENT_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(contextService.getContext(context.getContextId()));
    }

    @Test
    void testDeleteContextSuccess() {
        contextService.deleteContext(TEST_CONTEXT_ID_1);
        Context context = contextService.getContext(TEST_CONTEXT_ID_1);
        Assert.assertNull(context);
    }

    @Test
    void testUpdateContextListSuccess_1() {
        List<Context> contextList = contextService.getContextList(TEST_INTENT_ID_2);
        Context context = contextList.get(0);
        context.setContextName("new context name");

        try {
            contextService.updateContextList(contextList, TEST_INTENT_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Context contextTmp = contextService.getContextList(TEST_INTENT_ID_2).get(0);
        Assert.assertEquals("new context name", contextTmp.getContextName());
    }

    @Test
    void testUpdateContextListSuccess_2() {
        List<Context> contextList = createTestContextList("testUpdateContextListSuccess_2");

        try {
            contextService.updateContextList(contextList, TEST_EXPECTATION_ID);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(contextService.getContextList(TEST_EXPECTATION_ID));
    }

    @Test
    void testGetContextListSuccess() {
        List<Context> contextList = contextService.getContextList(TEST_INTENT_ID_1);
        Assert.assertNotNull(contextList);
    }
}
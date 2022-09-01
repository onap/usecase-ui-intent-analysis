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

    private static final String TEST_INTENT_ID_2 = "intent without affiliate";

    private static final String TEST_CONTEXT_ID_1 = "d64f3a5f-b091-40a6-bca0-1bc6b1ce8f43";

    @Autowired
    private ContextService contextService;

    @BeforeEach
    void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    void testCreateContextListSuccess() {
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("testContextId");
        context.setContextName("testContextName");
        String parentId = "testParentId";
        contextList.add(context);

        try {
            contextService.createContextList(contextList, parentId);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(contextService.getContextList(parentId));
    }

    @Test
    void testCreateContextSuccess() {
        Context context = new Context();
        context.setContextId("testContextId");
        context.setContextName("testContextName");

        try {
            contextService.createContext(context, TEST_INTENT_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(contextService.getContext("testContextId"));
    }

    @Test
    void testDeleteContextSuccess() {
        contextService.deleteContext(TEST_CONTEXT_ID_1);
        Context context = contextService.getContext(TEST_CONTEXT_ID_1);
        Assert.assertNull(context);
    }

    @Test
    void testUpdateContextListSuccess_1() {
        List<Context> contextList = contextService.getContextList(TEST_INTENT_ID_1);
        Context context = contextList.get(0);
        context.setContextName("new context name");

        try {
            contextService.updateContextList(contextList, TEST_INTENT_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Context contextTmp = contextService.getContextList(TEST_INTENT_ID_1).get(0);
        Assert.assertEquals("new context name", contextTmp.getContextName());
    }

    @Test
    void testUpdateContextListSuccess_2() {
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("testContextId");
        context.setContextName("testContextName");
        contextList.add(context);

        try {
            contextService.updateContextList(contextList, TEST_INTENT_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertNotNull(contextService.getContextList(TEST_INTENT_ID_2));
    }

    @Test
    void testGetContextListSuccess() {
        List<Context> contextList = contextService.getContextList("intentId1");
        Assert.assertNotNull(contextList);
    }
}
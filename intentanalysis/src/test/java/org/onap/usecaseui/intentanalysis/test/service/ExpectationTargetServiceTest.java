package org.onap.usecaseui.intentanalysis.test.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.service.ExpectationTargetService;
import org.onap.usecaseui.intentanalysis.test.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
@Slf4j
class ExpectationTargetServiceTest extends AbstractJUnit4SpringContextTests {

    private static final String TEST_EXPECTATION_ID_1 = "expectationId1";

    private static final String TEST_EXPECTATION_ID_2 = "expectation without affiliate";

    private static final String TEST_TARGET_ID_1 = "target1-1";

    @Autowired
    private ExpectationTargetService expectationTargetService;

    ExpectationTarget expectationTarget;

    @BeforeEach
    void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
        Condition targetCondition = new Condition();
        targetCondition.setConditionId("conditionId");
        targetCondition.setConditionName("conditionName");
        targetCondition.setOperator(OperatorType.valueOf("EQUALTO"));
        targetCondition.setConditionValue("conditionValue");
        List<Condition> targetConditionList = new ArrayList<>();
        targetConditionList.add(targetCondition);

        expectationTarget = new ExpectationTarget();
        expectationTarget.setTargetId("testTargetId");
        expectationTarget.setTargetName("testTargetId");
        expectationTarget.setTargetConditions(targetConditionList);
    }

    @Test
    void createExpectationTarget() {
        try {
            expectationTargetService.createExpectationTarget(expectationTarget, TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }
        List<ExpectationTarget> expectationTargetList = expectationTargetService.getExpectationTargetList(
            TEST_EXPECTATION_ID_1);
        List<String> targetIdList = new ArrayList<>();
        for (ExpectationTarget target : expectationTargetList) {
            targetIdList.add(target.getTargetId());
        }

        Assert.assertTrue(targetIdList.contains(expectationTarget.getTargetId()));
    }

    @Test
    void createExpectationTargetList() {
        List<ExpectationTarget> expectationTargetList = new ArrayList<>();
        expectationTargetList.add(expectationTarget);

        try {
            expectationTargetService.createExpectationTargetList(expectationTargetList, TEST_EXPECTATION_ID_2);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        Assert.assertFalse(CollectionUtils.isEmpty(expectationTargetService.getExpectationTargetList(TEST_EXPECTATION_ID_2)));
    }

    @Test
    void getExpectationTargetList() {
        List<ExpectationTarget> expectationTargetList = expectationTargetService.getExpectationTargetList(TEST_EXPECTATION_ID_1);
        Assert.assertFalse(CollectionUtils.isEmpty(expectationTargetList));
    }

    @Test
    void getExpectationTarget() {
        ExpectationTarget target = expectationTargetService.getExpectationTarget(TEST_TARGET_ID_1);
        Assert.assertNotNull(target);
    }

    @Test
    void updateExpectationTargetList() {
        List<ExpectationTarget> expectationTargetList = new ArrayList<>();
        expectationTargetList.add(expectationTarget);

        try {
            expectationTargetService.updateExpectationTargetList(expectationTargetList, TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        ExpectationTarget updatedTarget = expectationTargetService.getExpectationTargetList(TEST_EXPECTATION_ID_1).get(0);
        Assert.assertEquals(expectationTargetList.get(0).getTargetId(), updatedTarget.getTargetId());
    }

    @Test
    void deleteExpectationTarget() {
        try {
            expectationTargetService.deleteExpectationTarget(TEST_TARGET_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        List<ExpectationTarget> expectationTargetList = expectationTargetService.getExpectationTargetList(TEST_EXPECTATION_ID_1);
        for (ExpectationTarget target : expectationTargetList) {
            Assert.assertNotEquals(TEST_TARGET_ID_1, target.getTargetId());
        }
    }

    @Test
    void deleteExpectationTargetList() {
        try {
            expectationTargetService.deleteExpectationTargetList(TEST_EXPECTATION_ID_1);
        } catch (DataBaseException exception) {
            exception.printStackTrace();
        }

        List<ExpectationTarget> expectationTargetList = expectationTargetService.getExpectationTargetList(TEST_EXPECTATION_ID_1);
        Assert.assertTrue(CollectionUtils.isEmpty(expectationTargetList));
    }
}
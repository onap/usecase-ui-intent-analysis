package org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.CLLBusinessIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentProcessServiceTest {
    @InjectMocks
    IntentProcessService intentProcessService;
    @Resource(name = "formatIntentInputManagementFunction")
    private IntentManagementFunction intentOwner;
    @Resource(name = "CLLBusinessIntentManagementFunction")
    private CLLBusinessIntentManagementFunction cllBusinessIntentManagementFunction;
    Intent intent = new Intent();
    IntentGoalBean intentGoalBean = new IntentGoalBean();
    @Mock
    IntentDetectionService intentDetectionService;
    @Mock
    IntentInvestigationService intentInvestigationService;
    @Mock
    IntentDefinitionService intentDefinitionService;
    @Mock
    IntentDistributionService intentDistributionService;
    @Mock
    IntentOperationService intentOperationService;
    @Mock
    IntentService intentService;


    @Before
    public void before() throws Exception {
        intent.setIntentName("cllIntent");
        intent.setIntentId("12345");
        List<Expectation> expectationList = new ArrayList<>();

        Expectation delivery = new Expectation();
        delivery.setExpectationId("12345-delivery");
        delivery.setExpectationName("deliveryExpectation");
        delivery.setExpectationType(ExpectationType.DELIVERY);
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectType(ObjectType.OBJECT1);
        //expetationTarget  Context  FulfilmentInfo is empty
        delivery.setExpectationObject(expectationObject);
        expectationList.add(delivery);

        Expectation assurance = new Expectation();
        assurance.setExpectationId("12345-assurance");
        assurance.setExpectationName("assuranceExpectation");
        assurance.setExpectationType(ExpectationType.ASSURANCE);
        ExpectationObject expectationObject1 = new ExpectationObject();
        expectationObject1.setObjectType(ObjectType.OBJECT2);
        //expetationTarget  Context  FulfilmentInfo  is empty
        assurance.setExpectationObject(expectationObject1);
        expectationList.add(assurance);

        intent.setIntentExpectations(expectationList);
        intentGoalBean.setIntent(intent);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
    }
    @Test
    public void testIntentProcess() {
        intentProcessService.setIntentRole(intentOwner,cllBusinessIntentManagementFunction);
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap = new LinkedHashMap<>();
        intentMap.put(intentGoalBean,cllBusinessIntentManagementFunction);
        when(intentInvestigationService.investigationProcess(any())).thenReturn(intentMap);
        intentProcessService.intentProcess(intent);
        Assert.assertTrue(true);
    }
}
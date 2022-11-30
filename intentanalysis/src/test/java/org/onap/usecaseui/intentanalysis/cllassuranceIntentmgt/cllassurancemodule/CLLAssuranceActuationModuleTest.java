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
package org.onap.usecaseui.intentanalysis.cllassuranceIntentmgt.cllassurancemodule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.adapters.policy.PolicyService;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class CLLAssuranceActuationModuleTest {
    @InjectMocks
    CLLAssuranceActuationModule cllAssuranceActuationModule;
    @Mock
    IntentService intentService;
    @Mock
    private PolicyService policyService;
    @Mock
    private ContextService contextService;
    Intent intent = new Intent();

    @Before
    public void before() throws Exception {
        intent.setIntentName("CLL Delivery Intent");
        List<Expectation> expectationList = new ArrayList<>();
        Expectation deliveryExpectation = new Expectation();
        deliveryExpectation.setExpectationName("CLL Delivery Expectation");
        deliveryExpectation.setExpectationType(ExpectationType.DELIVERY);
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectInstance("");
        deliveryExpectation.setExpectationObject(expectationObject);

        List<ExpectationTarget> targetList = new ArrayList<>();
        ExpectationTarget target1 = new ExpectationTarget();
        target1.setTargetName("bandwidth");

        List<Condition> conditionList = new ArrayList<>();
        Condition con = new Condition();
        con.setConditionName("condition of the cll service bandwidth");
        con.setOperator(OperatorType.EQUALTO);
        con.setConditionValue("1000");
        conditionList.add(con);

        target1.setTargetConditions(conditionList);
        targetList.add(target1);

        deliveryExpectation.setExpectationTargets(targetList);

        expectationList.add(deliveryExpectation);

        Expectation assuranceExceptation = new Expectation();
        assuranceExceptation.setExpectationType(ExpectationType.ASSURANCE);
        ExpectationObject expectationObject2 = new ExpectationObject();
        expectationObject2.setObjectInstance("");
        assuranceExceptation.setExpectationObject(expectationObject2);
        expectationList.add(assuranceExceptation);
        intent.setIntentExpectations(expectationList);

    }
    @Test
    public void testDirectOperation(){
       // Mockito.doNothing().when(policyService).updateIntentConfigPolicy(any(), any(),any());
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent,IntentGoalType.DELETE);
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);

        Mockito.when(intentService.getIntentByName(any())).thenReturn(intentList);

        cllAssuranceActuationModule.directOperation(intentGoalBean);
        Assert.assertTrue(true);
    }
    @Test
    public void testUpdateIntentOperationInfo(){
       // updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean){
        Intent originIntent = new Intent();
        originIntent.setIntentContexts(new ArrayList<>());
        originIntent.setIntentId("12345");
        cllAssuranceActuationModule.updateIntentOperationInfo(originIntent,new IntentGoalBean());
        verify(contextService, times(1)).updateContextList(originIntent.getIntentContexts(), originIntent.getIntentId());
    }
    @Test
    public void testDeleteIntentToDb(){
        Intent intent = new Intent();
        intent.setIntentId("1234");
        Mockito.when(intentService.getSubIntentList(any())).thenReturn(new ArrayList<>());
        cllAssuranceActuationModule.deleteIntentToDb(intent);
        verify(intentService, times(1)).deleteIntent(any());

    }
}
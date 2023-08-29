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
 *
 */
package org.onap.usecaseui.intentanalysis.clldeliveryIntentmgt.clldeliverymodule;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.adapters.so.SOService;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class CLLDeliveryActuationModuleTest {
    @Autowired
    CLLDeliveryActuationModule cllDeliveryActuationModulel;

    @Autowired
    private ExpectationService expectationService;
    @Mock
    private ExpectationObjectService expectationObjectService;
    @Mock
    private ContextService contextService;
    @Mock
    private SOService soService;
    Intent originalIntent = new Intent();
    IntentGoalBean intentGoalBean = new IntentGoalBean();

    @Test
    public void testUpdateIntentOperationInfo() {
        List<Expectation> expectationList = new ArrayList<>();
        Expectation exp = new Expectation();
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectInstance(Collections.singletonList("objectInstance"));
        exp.setExpectationObject(expectationObject);
        expectationList.add(exp);
        originalIntent.setIntentExpectations(expectationList);

        List<Expectation> gbExpectationList = new ArrayList<>();
        Expectation deliveryExpectation = new Expectation();

        ExpectationObject deliveryExpectationObject = new ExpectationObject();
        deliveryExpectationObject.setObjectInstance(Collections.singletonList("deliveryObjectInstance"));

        deliveryExpectation.setExpectationObject(deliveryExpectationObject);
        deliveryExpectation.setExpectationType(ExpectationType.DELIVERY);
        gbExpectationList.add(deliveryExpectation);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
        Intent intent =new Intent();
        intent.setIntentName("delivery intent");
        intent.setIntentExpectations(gbExpectationList);
        intentGoalBean.setIntent(intent);

        //Mockito.doNothing().when(类对象).methodName();
        Mockito.doNothing().when(expectationObjectService).updateExpectationObject(any(), any());
        Mockito.doNothing().when(contextService).updateContextList(any(), any());
        cllDeliveryActuationModulel.updateIntentOperationInfo(originalIntent,intentGoalBean);
    }

    @Test
    public void testGetInstanceId(){
        cllDeliveryActuationModulel.getInstanceId();
        Assert.assertTrue(true);
    }
    @Test
    public void testDirectOperation (){
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
        Intent intent = new Intent();
        List<Expectation> expectationList = new ArrayList<>();
        Expectation expectation = new Expectation();
        expectation.setExpectationId("expectationId1");
        expectation.setExpectationObject(new ExpectationObject());
        List<ExpectationTarget> targetList = new ArrayList<>();
        ExpectationTarget target = new ExpectationTarget();

        List<Condition> conditionList = new ArrayList<>();
        Condition con = new Condition();
        con.setConditionName("source");
        conditionList.add(con);
        target.setTargetConditions(conditionList);
        targetList.add(target);
        expectation.setExpectationTargets(targetList);
        expectationList.add(expectation);
        intent.setIntentExpectations(expectationList);
        intentGoalBean.setIntent(intent);

        //Mockito.doNothing().when(soService).createIntentInstance(any());
        Mockito.when(expectationObjectService.getExpectationObject(any())).thenReturn(new ExpectationObject());
        Mockito.doNothing().when(expectationObjectService).updateExpectationObject(any(),any());
        cllDeliveryActuationModulel.fulfillIntent(intentGoalBean,null);
        Assert.assertTrue(true);
    }

}
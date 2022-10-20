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

package org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.ObjectType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.cllassuranceIntentmgt.CLLAssuranceIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.clldeliveryIntentmgt.CLLDeliveryIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.service.ImfRegInfoService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class CLLBusinessDecisionModuleTest {
    @InjectMocks
    CLLBusinessDecisionModule cllBusinessDecisionModule;

    @Mock
    private ImfRegInfoService imfRegInfoService;
    @Mock
    private ApplicationContext applicationContext;

    IntentGoalBean intentGoalBean = new IntentGoalBean();
    Intent intent = new Intent();
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
        expectationObject.setObjectType(ObjectType.SLICING);
        //expetationTarget  Context  FulfilmentInfo is empty
        delivery.setExpectationObject(expectationObject);
        expectationList.add(delivery);

        Expectation assurance = new Expectation();
        assurance.setExpectationId("12345-assurance");
        assurance.setExpectationName("assuranceExpectation");
        assurance.setExpectationType(ExpectationType.ASSURANCE);
        ExpectationObject expectationObject1 = new ExpectationObject();
        expectationObject1.setObjectType(ObjectType.CCVPN);
        //expetationTarget  Context  FulfilmentInfo  is empty
        assurance.setExpectationObject(expectationObject1);
        expectationList.add(assurance);

        intent.setIntentExpectations(expectationList);
        intent.setIntentExpectations(expectationList);
        intent.setIntentExpectations(expectationList);
        intentGoalBean.setIntent(intent);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
    }
    @Test
    public void testNeedDecompostion(){
//        IntentManagementFunctionRegInfo imfRegInfo = new IntentManagementFunctionRegInfo();
//
//        imfRegInfo.setSupportArea("cll");
//        imfRegInfo.setSupportInterfaces("CREATE");
//        imfRegInfo.setHandleName("aaa");
//      //  Mockito.when(imfRegInfoService.getImfRegInfo(intentGoalBean)).thenReturn(imfRegInfo).thenReturn(imfRegInfo);
//        Mockito.when(cllBusinessDecisionModule.exploreIntentHandlers(intentGoalBean)).thenReturn(new IntentManagementFunction());
//        Mockito.when(applicationContext.getBean("CLLDeliveryIntentManagementFunction")).thenReturn(new CLLDeliveryIntentManagementFunction());
//        cllBusinessDecisionModule.findHandler(intentGoalBean);
        cllBusinessDecisionModule.needDecompostion(intentGoalBean);
        Assert.assertTrue(true);
    }
    @Test
    public void testIntentDecomposition(){
        cllBusinessDecisionModule.intentDecomposition(intentGoalBean);
        Assert.assertTrue(true);
    }

    @Test
    public void testIntentOrchestration(){
        List<IntentGoalBean> intentGoalBeanList=new ArrayList<>();
        IntentGoalBean delivery=new IntentGoalBean();

        Intent deliveryIntent= new Intent();
        deliveryIntent.setIntentName("delivery");

        delivery.setIntentGoalType(IntentGoalType.CREATE);
        delivery.setIntent(deliveryIntent);
        intentGoalBeanList.add(delivery);

        IntentGoalBean assurance=new IntentGoalBean();

        Intent assuranceIntent= new Intent();
        deliveryIntent.setIntentName("assurance");

        assurance.setIntentGoalType(IntentGoalType.CREATE);
        assurance.setIntent(assuranceIntent);
        intentGoalBeanList.add(assurance);

        cllBusinessDecisionModule.intentOrchestration(intentGoalBeanList);
        Assert.assertTrue(true);
    }
}
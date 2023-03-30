/*
 * Copyright (C) 2023 CMCC, Inc. and others. All rights reserved.
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
package org.onap.usecaseui.intentanalysis.formatintentinputMgt;

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
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule.FormatIntentInputActuationModule;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule.FormatIntentInputDecisionModule;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule.FormatIntentInputKnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class FormatIntentInputManagementFunctionTest {
    @InjectMocks
    FormatIntentInputManagementFunction formatIntentInputManagementFunction;
    KnowledgeModule knowledgeModule = mock(FormatIntentInputKnowledgeModule.class);
    DecisionModule decisionModule = mock(FormatIntentInputDecisionModule.class);
    ActuationModule actuationModule = mock(FormatIntentInputActuationModule.class);
    @Mock
    CLLBusinessIntentManagementFunction cllBusinessIntentManagementFunction;
    @Mock
    IntentInterfaceService intentInterfaceService;
    @Mock
    ApplicationContext applicationContext;
    @Mock
    IntentService intentService;
    @Mock
    IntentContextService intentContextService;


    Intent intent = new Intent();
    IntentGoalBean intentGoalBean = new IntentGoalBean();

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
        intentGoalBean.setIntent(intent);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
    }

    @Test
    public void testDetection() {
        formatIntentInputManagementFunction.detection(intentGoalBean);
        Assert.assertTrue(true);
    }

    @Test
    public void testInvestigation() {
        formatIntentInputManagementFunction.investigation(intentGoalBean);
        Assert.assertTrue(true);
    }

    @Test
    public void testImplementIntentCreate() {
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> map = new LinkedHashMap<>();
        map.put(intentGoalBean, cllBusinessIntentManagementFunction);
        formatIntentInputManagementFunction.implementIntent(intent, map);
        Assert.assertTrue(true);
    }

    @Test
    public void testImplementIntentUpdate() {
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> map = new LinkedHashMap<>();
        intentGoalBean.setIntentGoalType(IntentGoalType.UPDATE);
        map.put(intentGoalBean, cllBusinessIntentManagementFunction);
        formatIntentInputManagementFunction.implementIntent(intent, map);
        Assert.assertTrue(true);
    }

    @Test
    public void testImplementIntentDelete() {
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> map = new LinkedHashMap<>();
        intentGoalBean.setIntentGoalType(IntentGoalType.DELETE);
        map.put(intentGoalBean, cllBusinessIntentManagementFunction);
        formatIntentInputManagementFunction.implementIntent(intent, map);
        Assert.assertTrue(true);
    }

    @Test
    public void testUpdateIntentInfo() {
        Intent originalIntent = new Intent();

        originalIntent.setIntentName("cllIntent");
        originalIntent.setIntentId("12345");
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
        originalIntent.setIntentExpectations(expectationList);

        formatIntentInputManagementFunction.updateIntentInfo(originalIntent, intent);
        Assert.assertTrue(true);
    }
}
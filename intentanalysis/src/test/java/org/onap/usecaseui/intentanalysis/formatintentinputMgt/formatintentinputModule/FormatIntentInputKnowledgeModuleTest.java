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

package org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule;

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
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class FormatIntentInputKnowledgeModuleTest {
    @InjectMocks
    FormatIntentInputKnowledgeModule formatIntentInputKnowledgeModule;

    @Mock
    IntentService intentService;
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
        //expetationTarget  Context  FulfillmentInfo is empty
        delivery.setExpectationObject(expectationObject);
        List<ExpectationTarget> expectationTargets = new ArrayList<>();
        ExpectationTarget expectationTarget = new ExpectationTarget();
        expectationTarget.setTargetName("aaaa");
        expectationTargets.add(expectationTarget);
        delivery.setExpectationTargets(expectationTargets);
        expectationList.add(delivery);

        Expectation assurance = new Expectation();
        assurance.setExpectationId("12345-assurance");
        assurance.setExpectationName("assuranceExpectation");
        assurance.setExpectationType(ExpectationType.ASSURANCE);
        ExpectationObject expectationObject1 = new ExpectationObject();
        expectationObject1.setObjectType(ObjectType.CCVPN);
        //expetationTarget  Context  FulfillmentInfo  is empty
        assurance.setExpectationObject(expectationObject1);

        List<ExpectationTarget> expectationTarget2 = new ArrayList<>();
        ExpectationTarget expectationTargetAssu = new ExpectationTarget();
        expectationTargetAssu.setTargetName("aaaa");
        expectationTarget2.add(expectationTargetAssu);
        assurance.setExpectationTargets(expectationTarget2);

        expectationList.add(assurance);

        intent.setIntentExpectations(expectationList);
        intent.setIntentExpectations(expectationList);
        intent.setIntentExpectations(expectationList);

        List<Context> intentContexts = new ArrayList<>();
        Context context = new Context();
        context.setContextName("owninfo");

        List<Condition> conditionList = new ArrayList<>();//ownerName = formatIntentInputManagementFunction"
        Condition condition = new Condition();
        condition.setConditionName("ownerName");
        condition.setOperator(OperatorType.EQUALTO);
        condition.setConditionValue("formatIntentInputManagementFunction");
        conditionList.add(condition);
        context.setContextConditions(conditionList);
        intentContexts.add(context);
        intent.setIntentContexts(intentContexts);

        intentGoalBean.setIntent(intent);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
    }

    @Test
    public void testFormatIntentInputKnowledgeModule() {
        List<Intent> list = new ArrayList<>();
        list.add(intent);
        Mockito.when(intentService.getIntentByName(anyString())).thenReturn(list);
        formatIntentInputKnowledgeModule.intentCognition(intent);
        Assert.assertTrue(true);
    }
}

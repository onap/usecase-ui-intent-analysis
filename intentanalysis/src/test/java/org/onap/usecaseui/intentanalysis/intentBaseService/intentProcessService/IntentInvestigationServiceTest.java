
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

package org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentInvestigationServiceTest {
    @InjectMocks
    IntentInvestigationService intentInvestigationService;

    @Resource(name = "formatIntentInputManagementFunction")
    private IntentManagementFunction intentOwner;
    @Resource(name = "CLLBusinessIntentManagementFunction")
    private CLLBusinessIntentManagementFunction cllBusinessIntentManagementFunction;
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
        intent.setIntentExpectations(expectationList);
        intentGoalBean.setIntent(intent);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
    }
    @Test
    public void testDetectionProcess() {
        intentInvestigationService.setIntentRole(intentOwner, null);
        intentInvestigationService.investigationProcess(intentGoalBean);
        Assert.assertTrue(true);
    }
}

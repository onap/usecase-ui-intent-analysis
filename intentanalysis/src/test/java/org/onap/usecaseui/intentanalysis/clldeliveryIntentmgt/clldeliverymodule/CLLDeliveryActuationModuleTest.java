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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class CLLDeliveryActuationModuleTest {
    @InjectMocks
    CLLDeliveryActuationModule cllDeliveryActuationModulel;
    @Mock
    private ExpectationObjectService expectationObjectService;
    @Mock
    private ContextService contextService;

    Intent originalIntent = new Intent();
    IntentGoalBean intentGoalBean = new IntentGoalBean();

    @Test
    public void testUpdateIntentOperationInfo() {
        List<Expectation> expectationList = new ArrayList<>();
        Expectation exp = new Expectation();
        ExpectationObject expectationObject = new ExpectationObject();
        expectationObject.setObjectInstance("objectInstance");
        exp.setExpectationObject(expectationObject);
        expectationList.add(exp);
        originalIntent.setIntentExpectations(expectationList);

        List<Expectation> gbExpectationList = new ArrayList<>();
        Expectation deliveryExpectation = new Expectation();

        ExpectationObject deliveryExpectationObject = new ExpectationObject();
        deliveryExpectationObject.setObjectInstance("deliveryObjectInstance");

        deliveryExpectation.setExpectationObject(deliveryExpectationObject);
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

}
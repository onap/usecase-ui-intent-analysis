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
package org.onap.usecaseui.intentanalysis.clldeliveryIntentmgt.clldeliverymodule;

import java.util.List;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CLLDeliveryActuationModule extends ActuationModule {
    @Autowired
    IntentService intentService;

    @Override
    public void toNextIntentHandler(IntentGoalBean intentGoalBean, IntentManagementFunction IntentHandler) {

    }

    @Override
    public void directOperation() {

    }

    @Override
    public void interactWithIntentHandle() {

    }

    @Override
    public void fulfillIntent(IntentGoalBean intentGoalBean, IntentManagementFunction intentHandler) {
        this.directOperation();
    }

    public void updateIntentOperationInfo(Intent originIntent, IntentGoalBean intentGoalBean){
        Intent subIntent = intentGoalBean.getIntent();
        if (subIntent.getIntentName().contains("delivery")){
            List<Expectation> deliveryIntentExpectationList = intentGoalBean.getIntent().getIntentExpectations();
            List<Expectation> originIntentExpectationList = originIntent.getIntentExpectations();
            ExpectationObject deliveryExpectationObject = deliveryIntentExpectationList.get(0).getExpectationObject();
            String objectInstance = deliveryExpectationObject.getObjectInstance();

            for (Expectation originExpectation : originIntentExpectationList) {
                ExpectationObject originExpectationObject = originExpectation.getExpectationObject();
                originExpectationObject.setObjectInstance(objectInstance);
            }
        }
        intentService.updateIntent(originIntent);
    }



}

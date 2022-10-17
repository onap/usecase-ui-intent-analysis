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

import java.util.List;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
@Component
public class CLLAssuranceDecisionModule extends DecisionModule {
    @Override
    public void determineUltimateGoal() {

    }

    @Override
    public void updateIntentInfo(Intent originIntent, IntentGoalBean intentGoalBean){

    }

    @Override
    public IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean) {
        return null;
    }


    @Override
    public void decideSuitableAction() {

    }

    @Override
    public void interactWithTemplateDb() {

    }

    @Override
    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> findHandler(IntentGoalBean intentGoalBean) {
        return null;
    }

    @Override
    public void updateIntentWithOriginIntent(Intent originIntent, Intent intent){
        List<Expectation> originIntentExpectationList =  originIntent.getIntentExpectations();

        String instanceId = "";
        for (Expectation expectation : originIntentExpectationList) {
            if (expectation.getExpectationName().contains("assurance")){
                instanceId = expectation.getExpectationObject().getObjectInstance();
                break;
            }
        }

        for (Expectation expectation : intent.getIntentExpectations()) {
            expectation.getExpectationObject().setObjectInstance(instanceId);
        }
    }
}

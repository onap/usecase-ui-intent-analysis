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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentModule;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class KnowledgeModule {
    @Autowired
    private IntentService intentService;
    //Parse, decompose, orchestrate the original intent
   public  abstract IntentGoalBean intentCognition(Intent intent);
    // in distribution, ask permission from imf
    public abstract boolean recieveCreateIntent();
    public abstract boolean recieveUpdateIntent();
    public abstract boolean recieveDeleteIntent();

    public List<String> intentResolution(Intent intent) {
        //db contain original intent
        List<Intent> sameNameIntentList = intentService.getIntentByName(intent.getIntentName());
        List<String> intentIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sameNameIntentList)) {
            //remove context.condition  ownerName = formatIntentInputManagementFunction
            List<Intent> filterIntentList = filterIntent(sameNameIntentList);
            List<Expectation> expectationList = intent.getIntentExpectations();
            for (Intent dbIntent : filterIntentList) {
                String intentId = dbIntent.getIntentId();
                int count = 0;
                for (Expectation expectation : expectationList) {//original expectations
                    //Determine if there is the same ObjectType
                    List<Expectation> sameObjTypeList = dbIntent.getIntentExpectations().stream()
                            .filter(x -> x.getExpectationObject().getObjectType().equals(expectation.getExpectationObject().getObjectType()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(sameObjTypeList)) {
                        //Determine the targetName of the Expectation which hava same ObjectType
                        List<String> targetNameList = expectation.getExpectationTargets()
                                .stream().map(ExpectationTarget::getTargetName).collect(Collectors.toList());
                        for (Expectation dbExpectation : sameObjTypeList) {
                            List<String> dbTargetNameList = dbExpectation.getExpectationTargets()
                                    .stream().map(ExpectationTarget::getTargetName).collect(Collectors.toList());
                            //todo name compare need ai
                            if (dbTargetNameList.containsAll(targetNameList)) {
                                count++;
                                break;
                            }
                        }
                    }
                    if (count == expectationList.size()) {
                        intentIdList.add(intentId);
                        break;
                    }
                }
            }
        }
        return intentIdList;
    }

    public List<Intent> filterIntent(List<Intent> list) {
        //// condition   ownerName = foramtIntentInput
        List<Intent> fiterList = new ArrayList<>();
        for (Intent intent : list) {
            List<Context> ownerInfo = intent.getIntentContexts().stream().filter(x ->
                    StringUtils.equalsIgnoreCase(x.getContextName(), "ownerInfo")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ownerInfo)) {
                for (Context context : ownerInfo) {
                    List<Condition> contextConditions = context.getContextConditions();
                    boolean equals = false;
                    for (Condition condition : contextConditions) {
                        String conditionstr = "ownerName equal to formatIntentInputManagementFunction";
                        String concatStr = condition.getConditionName() + condition.getOperator().name() + condition.getConditionValue();
                        if (StringUtils.equalsIgnoreCase(concatStr.trim(), conditionstr.replaceAll(" ",""))) {
                            fiterList.add(intent);
                            equals = true;
                            break;
                        }
                    }
                    if (equals==true) {
                        break;
                    }
                }
            }
        }
        list.removeAll(fiterList);
        return list;
    }
}

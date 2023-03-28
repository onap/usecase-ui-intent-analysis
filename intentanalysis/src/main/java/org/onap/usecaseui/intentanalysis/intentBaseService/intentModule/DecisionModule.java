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


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGenerateType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
@Slf4j
public abstract class DecisionModule {
    public abstract void determineUltimateGoal();

    // find intentManageFunction
    public abstract IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean);

    public Intent intentObjectDefine(Intent originIntent, Intent intent) {
        log.debug("definition create process start to define intent:" + intent.getIntentName());
        Intent newIntent = new Intent();
        newIntent.setIntentId(CommonUtil.getUUid());
        newIntent.setIntentName(intent.getIntentName());
        newIntent.setIntentGenerateType(IntentGenerateType.SYSTEMGENARATE);

        List<Expectation> originalExpectationList = intent.getIntentExpectations();
        List<Expectation> newExpectationList = new ArrayList<>();
        for (Expectation exp:originalExpectationList) {
            Expectation expectation = new Expectation();
            BeanUtils.copyProperties(exp,expectation);
            newExpectationList.add(expectation);
        }
        List<Expectation> newIdExpectationList = getNewExpectationList(newExpectationList);
        newIntent.setIntentExpectations(newIdExpectationList);
        return newIntent;
    }

    public abstract void decideSuitableAction();

    public abstract void interactWithTemplateDb();

    public abstract LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationCreateProcess(IntentGoalBean intentGoalBean);

    public abstract LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationUpdateProcess(IntentGoalBean intentGoalBean);

    public abstract LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigationDeleteProcess(IntentGoalBean intentGoalBean);

    /**
     * build new Intent with uuid
     *
     * @param originalExpectationList
     * @return
     */
    public List<Expectation> getNewExpectationList(List<Expectation> originalExpectationList) {
        if (CollectionUtils.isEmpty(originalExpectationList)) {
            return Collections.emptyList();
        }
        List<Expectation> newExpectations = new ArrayList<>();
        for (Expectation expectation : originalExpectationList) {
            expectation.setExpectationId(CommonUtil.getUUid());
            //ExpectationObject
            ExpectationObject expectationObject = expectation.getExpectationObject();
            ExpectationObject newExpectationObject = getNewExpectationObject(expectationObject);
            expectation.setExpectationObject(newExpectationObject);
            //ExpectationTarget
            List<ExpectationTarget> expectationTargets = expectation.getExpectationTargets();
            List<ExpectationTarget> newExpTargetList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(expectationTargets)) {
                for (ExpectationTarget expectationTarget : expectationTargets) {
                    ExpectationTarget expTarget =new ExpectationTarget();
                    BeanUtils.copyProperties(expectationTarget,expTarget);
                    expTarget.setTargetId(CommonUtil.getUUid());
                    //targetContexts
                    List<Context> targetContexts = expectationTarget.getTargetContexts();
                    if (CollectionUtils.isNotEmpty(targetContexts)) {
                        List<Context> newTargetContexts = new ArrayList<>();
                        for (Context context : targetContexts) {
                            Context con=new Context();
                            BeanUtils.copyProperties(context,con);
                            Context newContext = getNewContext(con);
                            newTargetContexts.add(newContext);
                        }
                        expTarget.setTargetContexts(newTargetContexts);
                    }
                    //targetConditions
                    List<Condition> targetConditions = expectationTarget.getTargetConditions();
                    if (CollectionUtils.isNotEmpty(targetConditions)) {
                        List<Condition> newTargetConditions = new ArrayList<>();
                        for (Condition condition : targetConditions) {
                            Condition con =new Condition();
                            BeanUtils.copyProperties(condition,con);
                            Condition newCondition = getNewCondition(con);
                            newTargetConditions.add(newCondition);
                        }
                        expTarget.setTargetConditions(newTargetConditions);
                    }
                    newExpTargetList.add(expTarget);
                }
                expectation.setExpectationTargets(newExpTargetList);
            }
            //expectationContexts
            List<Context> expectationContexts = expectation.getExpectationContexts();
            if (CollectionUtils.isNotEmpty(expectationContexts)) {
                List<Context> newEexpectationContexts = new ArrayList<>();
                for (Context context : expectationContexts) {
                    Context newContext = getNewContext(context);
                    newEexpectationContexts.add(newContext);
                }
                expectation.setExpectationContexts(newEexpectationContexts);
            }
            newExpectations.add(expectation);
        }
        return newExpectations;
    }

    public ExpectationObject getNewExpectationObject(ExpectationObject expectationObject) {
        if (null != expectationObject) {
            List<Context> objectContexts = expectationObject.getObjectContexts();
            if (CollectionUtils.isNotEmpty(objectContexts)) {
                List<Context> newObjectContexts = new ArrayList<>();
                for (Context context : objectContexts) {
                    Context newContext = getNewContext(context);
                    newObjectContexts.add(newContext);
                }
                expectationObject.setObjectContexts(newObjectContexts);
                return expectationObject;
            }
        }
        return expectationObject;
    }

    public Condition getNewCondition(Condition condition) {
        condition.setConditionId(CommonUtil.getUUid());
        List<Condition> conditionList = condition.getConditionList();
        if (CollectionUtils.isEmpty(conditionList)) {
            return condition;
        } else {
            for (Condition subCondition : conditionList) {
                getNewCondition(subCondition);
            }
        }
        return condition;
    }

    public Context getNewContext(Context context) {
        context.setContextId(CommonUtil.getUUid());
        List<Condition> contextConditions = context.getContextConditions();
        if (CollectionUtils.isNotEmpty(contextConditions)) {
            List<Condition> newConditionList = new ArrayList<>();
            for (Condition condition : contextConditions) {
                Condition con =new Condition();
                BeanUtils.copyProperties(condition,con);
                newConditionList.add(getNewCondition(con));
            }
            context.setContextConditions(newConditionList);
        }
        return context;
    }


}

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
package org.onap.usecaseui.intentanalysis.clldeliveryIntentmgt;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.Thread.CreateCallable;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.FutureTask;

@Slf4j
@Data
@Component("CLLDeliveryIntentManagementFunction")
public class CLLDeliveryIntentManagementFunction extends IntentManagementFunction {
    @Resource(name = "CLLDeliveryActuationModule")
    public void setActuationModule(ActuationModule actuationModule) {
        this.actuationModule = actuationModule;
    }

    @Resource(name = "CLLDeliveryKnowledgeModule")
    public void setKnowledgeModule(KnowledgeModule knowledgeModule) {
        this.knowledgeModule = knowledgeModule;
    }

    @Resource(name = "CLLDeliveryDecisionModule")
    public void setDecisionModule(DecisionModule decisionModule) {
        this.decisionModule = decisionModule;
    }

    @Autowired
    ApplicationContext applicationContext;
    @Resource(name = "intentTaskExecutor")
    ThreadPoolTaskExecutor executor;

    @Override
    public void receiveIntentAsOwner(IntentGoalBean intentGoalBean) {
    }

    @Override
    public void receiveIntentAsHandler(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler) {
        //ask  knowledgeModole of handler imf for permision and operate
        try {
            log.debug("cllDelivery Intent create begin time:" + LocalDateTime.now());
            CreateCallable createCallable = new CreateCallable(originalIntent, intentGoalBean, handler, applicationContext);
            FutureTask<String> futureTask = new FutureTask<>(createCallable);
            executor.submit(futureTask);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

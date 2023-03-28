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
package org.onap.usecaseui.intentanalysis.Thread;

import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.eventAndPublish.event.IntentCreateEvent;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Callable;

public class CreateCallable implements Callable<String> {
    private Intent originalIntent;
    private IntentGoalBean intentGoalBean;
    private IntentManagementFunction handler;
    private ApplicationContext applicationContext;

    public CreateCallable() {
    }
    public CreateCallable(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler, ApplicationContext applicationContext) {
        this.originalIntent = originalIntent;
        this.intentGoalBean = intentGoalBean;
        this.handler = handler;
        this.applicationContext = applicationContext;
    }

    @Override
    public String call() throws Exception {
        ActuationModule actuationModule = handler.getActuationModule();
        IntentGoalType type = intentGoalBean.getIntentGoalType();
        if (type == IntentGoalType.CREATE) {
            actuationModule.saveIntentToDb(intentGoalBean.getIntent());
        }else if (type==IntentGoalType.UPDATE){
            actuationModule.updateIntentToDb(intentGoalBean.getIntent());
        }else if (type == IntentGoalType.DELETE) {
            actuationModule.deleteIntentToDb(intentGoalBean.getIntent());
        }
        actuationModule.fulfillIntent(intentGoalBean, handler);
        //update origin intent if need
        actuationModule.updateIntentOperationInfo(originalIntent, intentGoalBean);

        String intentStatus = "success";
        IntentCreateEvent intentCreateEvent = new IntentCreateEvent(this, originalIntent, intentGoalBean, handler, intentStatus);
        applicationContext.publishEvent(intentCreateEvent);
        return  intentGoalBean.getIntent().getIntentName() +" Intent operate finished";
    }
}

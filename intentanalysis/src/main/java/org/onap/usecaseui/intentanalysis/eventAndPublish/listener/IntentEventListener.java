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
package org.onap.usecaseui.intentanalysis.eventAndPublish.listener;

import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentEventRecord;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.eventAndPublish.event.IntentCreateEvent;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentEventRecord.IntentEventRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IntentEventListener {
    @Autowired
    private IntentEventRecordService intentEventRecordService;

    @EventListener
    public void listenIntentCreateEvent(IntentCreateEvent intentCreateEvent) throws Exception{
            String intentStatus = intentCreateEvent.getIntentStatus();//create status
            IntentGoalBean intentGoalBean = intentCreateEvent.getIntentGoalBean();//current operate intent
            Intent currentIntent = intentGoalBean.getIntent();
            List<Context> owner_info = currentIntent.getIntentContexts().stream().filter(x ->
                    StringUtils.equals(x.getContextName(), "owner info")).collect(Collectors.toList());
            List<Context> handler_info = currentIntent.getIntentContexts().stream().filter(x ->
                    StringUtils.equals(x.getContextName(), "handler info")).collect(Collectors.toList());
            String owner = owner_info.get(0).getContextConditions().get(0).getConditionValue();
            String handler = handler_info.get(0).getContextConditions().get(0).getConditionValue();

            IntentEventRecord intentEventRecord = new IntentEventRecord();
            intentEventRecord.setIntentId(currentIntent.getIntentId());
            intentEventRecord.setIntentName(currentIntent.getIntentName());
            intentEventRecord.setIntentStatus(intentStatus);
            intentEventRecord.setOperateType(intentGoalBean.getIntentGoalType().name());
            intentEventRecordService.createIntentEventRecord(intentEventRecord,
                    intentCreateEvent.getOriginalIntent().getIntentId());

    }
}

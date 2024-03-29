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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.impl;

import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentInfo;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IntentInterfaceServiceImpl implements IntentInterfaceService {

    @Override
    public boolean createInterface(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler) {
        //ask  knowledgeModole of handler imf for permision
        boolean receive = handler.getKnowledgeModule().recieveCreateIntent();
        LocalDateTime now = LocalDateTime.now();
        if (receive) {
                handler.receiveIntentAsHandler(originalIntent, intentGoalBean, handler);
        }
        return receive;

    }


    @Override
    public boolean updateInterface(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler) {
        boolean receive = handler.getKnowledgeModule().recieveUpdateIntent();
        if (receive) {
            handler.receiveIntentAsHandler(originalIntent, intentGoalBean, handler);
        }
        return true;
    }


    @Override
    public boolean deleteInterface(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler) {
        boolean receive = handler.getKnowledgeModule().recieveDeleteIntent();
        if (receive) {
            handler.receiveIntentAsHandler(originalIntent, intentGoalBean, handler);
        }
        return true;
    }

    @Override
    public boolean reportInterface(IntentManagementFunction imf, String intentId, FulfillmentInfo fulfillmentInfo) {
        imf.createReport(intentId, fulfillmentInfo);
        return true;
    }
}

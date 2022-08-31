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

import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.springframework.stereotype.Service;

@Service
public class IntentInterfaceServiceImpl implements IntentInterfaceService {
    @Override
    public boolean createInterface(Intent intent, IntentManagementFunction imf) {
        //ask  knowledgeModole of handler imf for permision
        imf.getKnowledgeModule().recieveCreateIntent();
        return false;
    }

    @Override
    public boolean updateInterface(Intent intent, IntentManagementFunction imf) {
        imf.getKnowledgeModule().recieveUpdateIntent();
        return false;
    }

    @Override
    public boolean deleteInterface(Intent intent, IntentManagementFunction imf) {
        imf.getKnowledgeModule().recieveDeleteIntent();
        return false;
    }
}

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

import lombok.extern.log4j.Log4j2;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CLLAssuranceKnowledgeModule extends KnowledgeModule {
    @Override
    public IntentGoalBean intentCognition(Intent intent) {
        return null;
    }

    @Override
    public boolean recieveCreateIntent() {
        return true;
    }

    @Override
    public boolean recieveUpdateIntent() {
        return true;
    }

    @Override
    public boolean recieveDeleteIntent() {
        return true;
    }
}

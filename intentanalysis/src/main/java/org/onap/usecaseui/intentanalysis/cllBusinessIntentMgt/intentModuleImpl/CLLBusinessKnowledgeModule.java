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
package org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule;

import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.springframework.stereotype.Service;

@Service
public class CLLBusinessKnowledgeModule implements KnowledgeModule {

    @Override
    public Intent intentCognition(Intent intent) {
         intentResolution();
         //intentReportResolution();
         getSystemStatus();
         //interactWithIntentOwner();
        DetermineDetectionGoal();
         return null;
    }
    //find similar intents in DB
    void intentResolution(){};
    void intentReportResolution(){};
    //query the implementation of intent requirements in the system
    void getSystemStatus(){};
    void interactWithIntentOwner(){};
    //Determine add, delete, modify according to theobject,target and context of the expectation
    void DetermineDetectionGoal(){};
}

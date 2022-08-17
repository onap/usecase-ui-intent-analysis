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
package org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt;


import lombok.Data;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessActuationModule;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessDecisionModule;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessKnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.springframework.stereotype.Component;

@Data
@Component("CLLBusinessIntentManagementFunction")
public class CLLBusinessIntentManagementFunction extends IntentManagementFunction {
    private ActuationModule actuationModule  = new CLLBusinessActuationModule();
    private DecisionModule decisoinModule = new CLLBusinessDecisionModule();
    private KnowledgeModule knowledgeModule = new CLLBusinessKnowledgeModule();


}
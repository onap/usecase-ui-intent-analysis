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
package org.onap.usecaseui.intentanalysis.formatintentinputMgt;

import lombok.Data;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessActuationModule;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessDecisionModule;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessKnowledgeModule;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule.FormatIntentInputActuationModule;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule.FormatIntentInputDecisionModule;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.formatintentinputModule.FormatIntentInputKnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Data
@Component("formatIntentInputManagementFunction")
public class FormatIntentInputManagementFunction extends IntentManagementFunction {

    @Resource(name= "formatIntentInputKnowledgeModule")
    public void setKnowledgeModule(KnowledgeModule knowledgeModule) {
        this.knowledgeModule=knowledgeModule;
    }
    @Resource(name= "formatIntentInputActuationModule")
    public void setKnowledgeModule(ActuationModule actuationModule) {
        this.actuationModule=actuationModule;
    }
    @Resource(name= "formatIntentInputDecisionModule")
    public void setKnowledgeModule(DecisionModule decisionModule) {
        this.decisionModule=decisionModule;
    }
}

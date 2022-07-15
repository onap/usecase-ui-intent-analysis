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
package org.onap.usecaseui.intentanalysis.intentAnalysisService;


import lombok.Data;
import org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl.ActuationModuleImpl;
import org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl.DecisoinModuleImpl;
import org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl.KnownledgeModuleImpl;
import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentProcessService.Function;

@Data
public class IntentAnalysisManagementFunction extends Function {
    private ActuationModule actuationModule  = new ActuationModuleImpl();
    private DecisionModule decisoinModule = new DecisoinModuleImpl();
    private KnowledgeModule knowledgeModule = new KnownledgeModuleImpl();


}

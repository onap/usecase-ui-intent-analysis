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
package org.onap.usecaseui.intentanalysis.intentModule;


import org.onap.usecaseui.intentanalysis.intentProcessService.Function;

public interface DecisionModule {
    void determineUltimateGoal();//
    Function exploreIntentHandlers();
    void intentDefinition();
    void decideSuitableAction();

    //confirm whether the intent needs to be decomposed and orchestrated
    public boolean needDecompostion();

    //call decomposition module
    public void intentDecomposition();

    //call orchestration module
    public void intentOrchestration();


    public void interactWithTemplateDb();
}

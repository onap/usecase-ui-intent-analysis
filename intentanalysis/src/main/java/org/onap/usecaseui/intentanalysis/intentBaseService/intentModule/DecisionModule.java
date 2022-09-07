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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentModule;


import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;

import java.util.LinkedHashMap;

public abstract class DecisionModule {
    public abstract void determineUltimateGoal();

    // find intentManageFunction
    public abstract IntentManagementFunction exploreIntentHandlers(IntentGoalBean intentGoalBean);

    public abstract void intentDefinition();

    public abstract void decideSuitableAction();

    public abstract  void interactWithTemplateDb();


    public abstract LinkedHashMap<IntentGoalBean,IntentManagementFunction>  findHandler(IntentGoalBean intentGoalBean);
}

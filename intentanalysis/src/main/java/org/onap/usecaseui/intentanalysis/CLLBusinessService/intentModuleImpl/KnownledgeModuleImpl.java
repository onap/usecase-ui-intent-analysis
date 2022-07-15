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
package org.onap.usecaseui.intentanalysis.CLLBusinessService.intentModuleImpl;

import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;
import org.springframework.stereotype.Service;

@Service
public class KnownledgeModuleImpl implements KnowledgeModule {
    @Override
    public void intentResolution() {}

    @Override
    public void intentReportResolution() {}

    @Override
    public void getSystemStatus() {}

    @Override
    public void interactWithIntentOwner() {

    }
}

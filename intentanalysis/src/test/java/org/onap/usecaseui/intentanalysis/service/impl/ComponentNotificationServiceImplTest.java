/*
 * Copyright (C) 2023 CMCC, Inc. and others. All rights reserved.
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

package org.onap.usecaseui.intentanalysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.mockito.Mock;
import org.onap.usecaseui.intentanalysis.adapters.policy.PolicyService;
import org.onap.usecaseui.intentanalysis.adapters.so.SOService;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Component
public class ComponentNotificationServiceImplTest implements ApplicationRunner {
    @Autowired
    ComponentNotificationServiceImpl componentNotificationService;

    @Autowired
    FormatIntentInputManagementFunction formatIntentInputManagementFunction;

    @Mock
    private PolicyService policyService;

    @Mock
    private SOService soService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File policyTypeFile = Resources.getResourceAsFile("intent.json");
        String intentStr = FileUtils.readFileToString(policyTypeFile, StandardCharsets.UTF_8);
        Intent intent = JSONObject.parseObject(intentStr, Intent.class);
        formatIntentInputManagementFunction.receiveIntentAsOwner(new IntentGoalBean(intent, IntentGoalType.CREATE));
    }
}

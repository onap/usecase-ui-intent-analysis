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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationObjectMapper;
import org.onap.usecaseui.intentanalysis.service.IntentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentReportServiceTest {
    @Autowired
    private IntentReportService intentReportService;

    @Autowired
    ComponentNotificationServiceImpl componentNotificationService;

    @Autowired
    private ExpectationObjectMapper expectationObjectMapper;

    @Test
    public void getIntentReportByIntentIdTest() {
        List<String> allObjectInstances = expectationObjectMapper.getAllObjectInstances();
        List<String> cll = new ArrayList<>();
        for (String target : allObjectInstances) {
            if (target != null && target.contains("cll")) {
                cll.add(target);
            }
        }
        FulfillmentOperation fulfillmentOperation = new FulfillmentOperation();
        fulfillmentOperation.setObjectInstances(Collections.singletonList(cll.get(0)));
        fulfillmentOperation.setOperation("delivery");
        componentNotificationService.callBack(fulfillmentOperation);
        IntentReport report = intentReportService.getIntentReportByIntentId("testIntentId111");
        Assert.assertNotNull(report);
    }
}

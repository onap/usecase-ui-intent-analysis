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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class CLLBusinessActuationModuleTest {
    @InjectMocks
    CLLBusinessActuationModule cllBusinessActuationModule;
    @Mock
    IntentService intentService;
    @Test
    public void testSaveIntentToDb(){
        List<Context> intentContexts = new ArrayList<>();
        Intent intent = new Intent();
        intent.setIntentContexts(intentContexts);
        cllBusinessActuationModule.saveIntentToDb(intent);

        verify(intentService, times(1)).createIntent(intent);
    }
}
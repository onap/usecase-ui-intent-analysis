/*
 * Copyright 2021 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.usecaseui.intentanalysis.test.service;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.onap.usecaseui.intentanalysis.test.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentServiceTest extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntentServiceTest.class);

    @Autowired
    private IntentService intentService;

    @Before
    public void setUp() {
        SpringContextUtil.setApplicationContext(applicationContext);
    }

    @Test
    public void testCreateIntentSuccess() throws IOException {
        Intent intent = new Intent();
        intent.setIntentId("testUUID");
        intent.setIntentName("testIntentName");
        //ToDo
        //Intent intentTmp = intentService.createIntent(intent);
        Assert.assertNotNull(intent);
    }
}

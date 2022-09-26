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
package org.onap.usecaseui.intentanalysis.bean.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentFunctionType;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportArea;

import java.util.ArrayList;

public class IntentManagementFunctionRegInfoTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetIntentManagementFunctionRegInfoTest() {
        IntentManagementFunctionRegInfo test = new IntentManagementFunctionRegInfo();
        test.getId();
        test.getDescription();
        test.getSupportArea();
        test.getSupportModel();
        test.getSupportInterfaces();
        test.getHandleName();
        test.getIntentFunctionType();

    }

    @Test
    public void testSetIntentManagementFunctionRegInfoTest() {
        IntentManagementFunctionRegInfo test = new IntentManagementFunctionRegInfo();
        test.setId("");
        test.setDescription("");
        test.setSupportArea("");
        test.setSupportModel("");
        test.setSupportInterfaces("");
        test.setHandleName("");
        test.setIntentFunctionType(IntentFunctionType.INTERNALFUNCTION);
    }
}

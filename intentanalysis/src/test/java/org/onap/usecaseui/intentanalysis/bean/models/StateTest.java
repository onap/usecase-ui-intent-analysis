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

public class StateTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetStateTest() {
        State test = new State();
        test.getStateId();
        test.getStateName();
        test.getCondition();
        test.getIsSatisfied();

    }

    @Test
    public void testSetStateTest() {
        State test = new State();
        test.setStateId("");
        test.setStateName("");
        test.setCondition("");
        test.setIsSatisfied(true);
    }
}

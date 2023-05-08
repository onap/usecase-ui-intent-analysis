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
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;

import java.util.ArrayList;

public class ExpectationTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testGetExpectationTest() {
        Expectation test = new Expectation();
        test.getExpectationId();
        test.getExpectationContexts();
        test.getExpectationName();
        test.getExpectationType();
        test.getExpectationFulfillmentInfo();
        test.getExpectationObject();
        test.getExpectationTargets();
    }

    @Test
    public void testSetExpectationTest() {
        Expectation test = new Expectation();
        test.setExpectationId("");
        test.setExpectationContexts(new ArrayList<Context>());
        test.setExpectationName("");
        test.setExpectationTargets(new ArrayList<ExpectationTarget>());
        test.setExpectationType(ExpectationType.ASSURANCE);
        test.setExpectationFulfillmentInfo(new FulfillmentInfo());
        test.setExpectationObject(new ExpectationObject());
    }
}

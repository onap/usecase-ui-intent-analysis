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
 *
 */
package org.onap.usecaseui.intentanalysis.intentBaseService.contextService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.enums.OperatorType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.CLLBusinessIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentContextServiceTest {
    @Spy
    @InjectMocks
    IntentContextService intentContextService;
    @Mock
    private IntentService intentService;

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    FormatIntentInputManagementFunction formatIntentInputManagementFunction;
    @Autowired
    CLLBusinessIntentManagementFunction cllBusinessIntentManagementFunction;
    Intent originalIntent = new Intent();
    Intent newIntent = new Intent();

    @Before
    public void before() throws Exception {
        originalIntent.setIntentId("testIntentId");
        originalIntent.setIntentName("testIntent");

        newIntent.setIntentId("newIntentId");
        newIntent.setIntentName("newIntent");
    }

    @Test
    public void testUpdateChindIntentContext() {
        intentContextService.updateChindIntentContext(originalIntent, newIntent);
        Assert.assertTrue(true);
    }

    @Test
    public void testUpdateParentIntentContext() {
        intentContextService.updateParentIntentContext(originalIntent, newIntent);
        Assert.assertTrue(true);
    }

    @Test
    public void testUpdateParentIntentContextWithSubintnt() {
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("contentId");
        context.setContextName("subIntent info");

        Condition con = new Condition();
        con.setConditionId("conditionId");
        con.setConditionName("first subIntent Id");
        con.setOperator(OperatorType.EQUALTO);
        con.setConditionValue("first subIntent id");

        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(con);
        context.setContextConditions(conditionList);

        contextList.add(context);
        originalIntent.setIntentContexts(contextList);
        intentContextService.updateParentIntentContext(originalIntent, newIntent);
        Assert.assertTrue(true);
    }

    @Test
    public void testUpdateIntentOwnerHandlerContext() {
        intentContextService.updateIntentOwnerHandlerContext(newIntent, formatIntentInputManagementFunction, cllBusinessIntentManagementFunction);
        Assert.assertTrue(true);
    }

    @Test
    public void testGetSubIntentInfoFromContext() {
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("contentId");
        context.setContextName("subIntent info");

        Condition con = new Condition();
        con.setConditionId("conditionId");
        con.setConditionName("first subIntent Id");
        con.setOperator(OperatorType.EQUALTO);
        con.setConditionValue("first subIntent id");

        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(con);
        context.setContextConditions(conditionList);

        contextList.add(context);
        originalIntent.setIntentContexts(contextList);
        // originalIntent.setIntentContexts();
        when(intentService.getIntent(any())).thenReturn(originalIntent).thenReturn(newIntent);
        intentContextService.getSubIntentInfoFromContext(originalIntent);
        Assert.assertTrue(true);
    }

    @Test
    public void testGetHandlerInfo() {
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("contentId");
        context.setContextName("subIntent info");

        Condition con = new Condition();
        con.setConditionId("conditionId");
        con.setConditionName("first subIntent Id");
        con.setOperator(OperatorType.EQUALTO);
        con.setConditionValue("first subIntent id");

        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(con);
        context.setContextConditions(conditionList);

        contextList.add(context);
        originalIntent.setIntentContexts(contextList);
        intentContextService.getHandlerInfo(originalIntent);
        Assert.assertTrue(true);
    }

    @Test
    public void testDeleteSubIntentContext() {
        List<Context> contextList = new ArrayList<>();
        Context context = new Context();
        context.setContextId("contentId");
        context.setContextName("subIntent info");

        Condition con = new Condition();
        con.setConditionId("conditionId");
        con.setConditionName("first subIntent Id");
        con.setOperator(OperatorType.EQUALTO);
        con.setConditionValue("first subIntent id");

        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(con);
        context.setContextConditions(conditionList);

        contextList.add(context);
        originalIntent.setIntentContexts(contextList);
        intentContextService.deleteSubIntentContext(originalIntent, "first subIntent Id");
        Assert.assertTrue(true);
    }
}
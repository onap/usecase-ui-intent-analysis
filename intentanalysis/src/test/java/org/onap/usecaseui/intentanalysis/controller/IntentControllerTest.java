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
package org.onap.usecaseui.intentanalysis.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGenerateType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.CommonException;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class IntentControllerTest {

    @InjectMocks
    IntentController intentController;
    @Mock
    IntentService intentService;
    @Mock
    IntentProcessService intentProcessService;
    @Mock
    FormatIntentInputManagementFunction formatIntentInputManagementFunction;


    @Test
    public void testGetIntentList() {
        intentController.getIntentList();
        verify(intentService, times(1)).getIntentList();
    }

    @Test
    public void testGetIntentById() {
        intentController.getIntentById(anyString());
        verify(intentService, times(1)).getIntent(anyString());
    }

    @Test
    public void testCreateIntent() {
        Intent intent = new Intent();
        intent.setIntentName("cllBussinessIntent");
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);
        Mockito.when(intentProcessService.intentProcess(any())).thenReturn(intentGoalBean);
        intentController.createIntent(intent);
        verify(intentProcessService, times(1)).intentProcess(any());
        verify(intentService, times(1)).createIntent(any());

    }

    @Test
    public void testCreateIntentCommonException() {
        Intent intent = new Intent();
        intent.setIntentName("cllBussinessIntent");
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);
        //Mockito.when(intentProcessService.intentProcess(any())).thenReturn(intentGoalBean);
        when(intentProcessService.intentProcess(any())).thenThrow(new CommonException("MSG", ResponseConsts.RET_UPDATE_DATA_FAIL));
        intentController.createIntent(intent);
        Assert.assertTrue(true);
    }
   @Test
    public void testUpdateIntentById(){
       Intent intent = new Intent();
       intent.setIntentName("cllBussinessIntent");
       IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);

       Mockito.when(intentProcessService.intentProcess(any())).thenReturn(intentGoalBean);
       intentController.updateIntentById(any(),any());
       verify(intentProcessService, times(1)).intentProcess(any());
   }
    @Test
    public void testUpdateIntentByIdCommonException() {
        Intent intent = new Intent();
        intent.setIntentName("cllBussinessIntent");
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);
        //Mockito.when(intentProcessService.intentProcess(any())).thenReturn(intentGoalBean);
        when(intentProcessService.intentProcess(any())).thenThrow(new CommonException("MSG", ResponseConsts.RET_UPDATE_DATA_FAIL));
        intentController.updateIntentById(any(),any());
        Assert.assertTrue(true);
    }

    @Test
    public void testRemoveIntentById(){
        Intent intent = new Intent();
        intent.setIntentName("cllBussinessIntent");
        String id = "intentId";
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);

        Mockito.when(intentService.getIntent(any())).thenReturn(intent);
        Mockito.when(intentProcessService.intentProcess(any())).thenReturn(intentGoalBean);
        intentController.removeIntentById(id);
        verify(intentProcessService, times(1)).intentProcess(any());
    }

    @Test
    public void testRemoveIntentByIdCommonException() {
        Intent intent = new Intent();
        intent.setIntentName("cllBussinessIntent");
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);
        //Mockito.when(intentProcessService.intentProcess(any())).thenReturn(intentGoalBean);
        when(intentProcessService.intentProcess(any())).thenThrow(new CommonException("MSG", ResponseConsts.RET_UPDATE_DATA_FAIL));
        intentController.removeIntentById("intentId");
        Assert.assertTrue(true);
    }

    @Test
    public void testGetIntentListByIntentGenerateType(){
        intentController.getIntentListByIntentGenerateType(anyString());
        verify(intentService, times(1)).getIntentListByUserInput(anyString());
    }
    @Test
    public void  testGetIntentListByIntentGenerateTypeCommoExcption(){
        when(intentService.getIntentListByUserInput(any())).thenThrow(new CommonException("MSG", ResponseConsts.RET_UPDATE_DATA_FAIL));
        intentController.getIntentListByIntentGenerateType(anyString());
        Assert.assertTrue(true);
    }
}
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
package org.onap.usecaseui.intentanalysis.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportArea;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportInterface;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.mapper.IMFRegInfoMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = ImfRegInfoServiceImplTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ImfRegInfoServiceImplTest {

    @InjectMocks
    private ImfRegInfoServiceImpl imfRegInfoServiceImpl;

    @Mock
    private IMFRegInfoMapper imfRegInfoMapper;
    Intent intent = new Intent();
    IntentGoalBean intentGoalBean = new IntentGoalBean();

    @Before
    public void setUp() {
        intent.setIntentId("intentid");
        intent.setIntentName("CLLBusiness");
        intentGoalBean.setIntent(intent);
        intentGoalBean.setIntentGoalType(IntentGoalType.CREATE);
    }

    @Test
    public void itCanInsertIMFRegInfoRegInfo() {
        Mockito.when(imfRegInfoMapper.insertIMFRegInfoRegInfo(any())).thenReturn(1);
        imfRegInfoServiceImpl.insertIMFRegInfoRegInfo(new IntentManagementFunctionRegInfo());
        Assert.assertTrue(true);
    }

    @Test
    public void itCanGetImfRegInfoList() {
        imfRegInfoServiceImpl.getImfRegInfoList();
    }

    @Test
    public void itCanGetImfRegInfoListWithIntentGoalBean() {
        List<IntentManagementFunctionRegInfo> list = new ArrayList<>();
        IntentManagementFunctionRegInfo info = new IntentManagementFunctionRegInfo();
        List<SupportArea> areas = new ArrayList<>();
        areas.add(SupportArea.CLLBUSINESS);
        info.setSupportArea(areas);

        List<SupportInterface> interList = new ArrayList<>();
        interList.add(SupportInterface.CREATE);
        info.setSupportInterfaces(interList);

        list.add(info);

        Mockito.when(imfRegInfoMapper.getImfRegInfoList()).thenReturn(list);
        imfRegInfoServiceImpl.getImfRegInfo(intentGoalBean);
        Assert.assertTrue(true);
    }
}
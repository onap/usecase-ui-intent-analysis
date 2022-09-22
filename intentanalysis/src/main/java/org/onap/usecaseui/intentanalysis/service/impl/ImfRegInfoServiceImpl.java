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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportArea;
import org.onap.usecaseui.intentanalysis.bean.enums.SupportInterface;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.mapper.IMFRegInfoMapper;
import org.onap.usecaseui.intentanalysis.service.ImfRegInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImfRegInfoServiceImpl implements ImfRegInfoService {
    @Autowired
    private IMFRegInfoMapper imfRegInfoMapper;

    @Override
    public int insertIMFRegInfoRegInfo(IntentManagementFunctionRegInfo regInfo) {
        return imfRegInfoMapper.insertIMFRegInfoRegInfo(regInfo);
    }

    @Override
    public List<IntentManagementFunctionRegInfo> getImfRegInfoList() {
        return imfRegInfoMapper.getImfRegInfoList();
    }

    @Override
    public IntentManagementFunctionRegInfo getImfRegInfo(IntentGoalBean intentGoalBean) {
        String intentName = intentGoalBean.getIntent().getIntentName();
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        List<IntentManagementFunctionRegInfo> imfRegInfoList = imfRegInfoMapper.getImfRegInfoList();

        List<IntentManagementFunctionRegInfo> imfList = new ArrayList<>();
        for (IntentManagementFunctionRegInfo imfr : imfRegInfoList) {
            boolean containsArea = false;
            boolean containsInterface = false;
            for (SupportArea area : imfr.getSupportArea()) {
                if (StringUtils.containsIgnoreCase(intentName, area.name())) {
                    containsArea = true;
                    break;
                }
            }
            for (SupportInterface supInterface : imfr.getSupportInterfaces()) {
                if (StringUtils.containsIgnoreCase(supInterface.name(), intentGoalType.name())) {
                    containsInterface = true;
                    break;
                }
            }
            if (containsArea && containsInterface) {
                imfList.add(imfr);
            }
        }
        if (!Optional.ofNullable(imfList).isPresent()) {
            log.info("The intent name is %s not find the corresponding IntentManagementFunction", intentName);
        }
        //TODO call probe  interface  if fail  intentFulfilmentInfo throw exception
        return imfList.get(0);
    }
}
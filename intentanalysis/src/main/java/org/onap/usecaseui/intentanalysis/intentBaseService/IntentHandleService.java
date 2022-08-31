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
package org.onap.usecaseui.intentanalysis.intentBaseService;

import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.service.ImfRegInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class IntentHandleService {
    @Autowired
    private IntentProcessService processService;
    @Autowired
    private ImfRegInfoService imfRegInfoService;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Process the original intent and find the corresponding IntentManagementFunction
     *
     * @param intent  todo
     */
    public void handleOriginalIntent(Intent intent) {
        IntentManagementFunction intentOwner = getOriginalIMF(intent);
        handleIntent(intent, intentOwner);
    }

    public void handleIntent(Intent intent, IntentManagementFunction intentOwner) {
        processService.setIntentRole(intentOwner, null);
        processService.intentProcess(intent);
    }

    public IntentManagementFunction selectIntentManagementFunction(Intent intent) {
        //select the IntentManagementFunctionRegInfo Based on the IMFRegistry information.
        //Only internalFunction support.
        //and based on the IntentManagementFunctionRegInfo, get the right IntentManagementFunction bean.
        //if  no  IntentManagementFunction selected, that means this intent is not supported by this system.
        return null;
    }

    public IntentManagementFunctionRegInfo selectIntentManagementFunctionRegInfo(Intent intent) {
        //select the IntentManagementFunctionRegInfo Based on the IMFRegistry information.
        //Both internalFunction and externalFunction support.
        //This is used to get he IntentManagementFunction for a subIntent decomposition.
        return null;
    }

    public IntentManagementFunction getOriginalIMF(Intent intent) {
        //select IntentManagementFunction based on intent  name
        String intentName = intent.getIntentName();
        List<IntentManagementFunctionRegInfo> imfRegInfoList = imfRegInfoService.getImfRegInfoList();
        List<IntentManagementFunctionRegInfo> list = imfRegInfoList.stream().filter(x -> x.getSupportArea().contains(intentName)).collect(Collectors.toList());
        if (!Optional.ofNullable(list).isPresent()) {
            String msg = String.format("Intent name %s doesn't exist IntentManagementFunction in database.", intent.getIntentName());
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        return (IntentManagementFunction) applicationContext.getBean(list.get(0).getHandleName());
    }
}

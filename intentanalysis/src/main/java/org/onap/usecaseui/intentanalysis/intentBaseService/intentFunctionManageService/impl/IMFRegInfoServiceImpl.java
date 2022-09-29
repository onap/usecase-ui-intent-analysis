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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentFunctionManageService.impl;

import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentFunctionManageService.IMFRegInfoService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.mapper.IMFRegInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service("intentFunctionManageService")
public class IMFRegInfoServiceImpl implements IMFRegInfoService {
    @Override
    public int createFunctionManage(IntentManagementFunctionRegInfo intentManage)  {
        return 0;
    }

    @Override
    public int deleteFunctionManage(String id) {
        return 0;
    }

    @Override
    public int updateIntentManageById(String id, IntentManagementFunctionRegInfo intentManage) {
        return 0;
    }

    @Override
    public List<IntentManagementFunctionRegInfo> getIntentManage() {
        return null;
    }
}

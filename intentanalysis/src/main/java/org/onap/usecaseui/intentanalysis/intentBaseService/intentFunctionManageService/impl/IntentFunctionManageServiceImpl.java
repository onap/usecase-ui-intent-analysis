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

import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagerRegInfo;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentFunctionManageService.IntentFunctionManageService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service("intentFunctionManageService")
public class IntentFunctionManageServiceImpl implements IntentFunctionManageService {
    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public int createFunctionManage(IntentManagerRegInfo intentManage)  {
        return 0;
    }

    @Override
    public int deleteFunctionManage(String id) {
        return 0;
    }

    @Override
    public int updateIntentManageById(String id, IntentManagerRegInfo intentManage) {
        return 0;
    }

    @Override
    public List<IntentManagerRegInfo> getIntentManage() {
        return null;
    }

    public List<IntentManagementFunction> filterHandleFunction(IntentManagerRegInfo managementRegInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String managetFunctionRegName =managementRegInfo.getHandleName();

        IntentManagementFunction function =
                (IntentManagementFunction)applicationContext.getBean(managetFunctionRegName);

        ActuationModule actuationModule = function.getActuationModule();
        actuationModule.sendToNonIntentHandler();
        IntentManagementFunction intentManagementFunction =
                (IntentManagementFunction) Class.forName(managetFunctionRegName)
                        .getDeclaredConstructor().newInstance();
        ActuationModule actuationModule1 = intentManagementFunction.getActuationModule();
        return null;
    }

}

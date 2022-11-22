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

import lombok.extern.log4j.Log4j2;
import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.bean.models.ResultHeader;
import org.onap.usecaseui.intentanalysis.bean.models.ServiceResult;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentFunctionManageService.IMFRegInfoService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.mapper.IMFRegInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RESPONSE_ERROR;
import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RSEPONSE_SUCCESS;

@Log4j2
@Service("intentFunctionManageService")
public class IMFRegInfoServiceImpl implements IMFRegInfoService {

    @Autowired
    IMFRegInfoMapper imfRegInfoMapper;

    @Override
    public ServiceResult createFunctionManage(IntentManagementFunctionRegInfo intentManage) {
        try {
            imfRegInfoMapper.insertIMFRegInfoRegInfo(intentManage);
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "create intentFunctionManageInfo success"));
        } catch (Exception exception) {
            log.error("Execute  create intentFunctionManageInfo Exception:", exception);
            return new ServiceResult(new ResultHeader(RESPONSE_ERROR, exception.getMessage()));
        }
    }

    @Override
    public ServiceResult deleteFunctionManage(String id) {
        try {
            imfRegInfoMapper.deleteFunctionManageById(id);
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "delete intentFunctionManageInfo " + id + " success"));
        } catch (Exception exception) {
            log.error("Execute  delete intentFunctionManageInfo Exception:", exception);
            return new ServiceResult(new ResultHeader(RESPONSE_ERROR, exception.getMessage()));
        }
    }

    @Override
    public ServiceResult updateIntentManageById(String id, IntentManagementFunctionRegInfo intentManage) {
        try{
            imfRegInfoMapper.updateIntentManageById(id,intentManage);
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "update intentFunctionManageInfo " + id + " success"));
        }catch(Exception exception){
            log.error("Execute  update intentFunctionManageInfo Exception:", exception);
            return new ServiceResult(new ResultHeader(RESPONSE_ERROR, exception.getMessage()));
        }
    }

    @Override
    public List<IntentManagementFunctionRegInfo> getIntentManage() {

        return imfRegInfoMapper.getImfRegInfoList();
    }
}

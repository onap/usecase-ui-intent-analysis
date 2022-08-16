/*
 * Copyright 2022 Huawei Technologies Co., Ltd.
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
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.models.FulfilmentInfo;
import org.onap.usecaseui.intentanalysis.mapper.FulfilmentInfoMapper;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;


@Service
@Slf4j
public class FulfilmentInfoServiceImpl implements FulfilmentInfoService {

    @Autowired
    private FulfilmentInfoMapper fulfilmentInfoMapper;

    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    @Override
    public void createFulfilmentInfo(FulfilmentInfo fulfilmentInfo, String parentId) {
        if (fulfilmentInfoMapper.insertFulfilmentInfo(fulfilmentInfo, parentId) < 1) {
            String msg = "Create fulfilmentInfoMapper to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public void deleteFulfilmentInfoByParentId(String parentId) {
    }

    @Override
    public void updateFulfilmentInfoByParentId(FulfilmentInfo fulfilmentInfo, String parentId) {
    }

    @Override
    public FulfilmentInfo getFulfilmentInfoByParentId(String parentId) {
        FulfilmentInfo fulfilmentInfo = fulfilmentInfoMapper.selectFulfilmentInfoById(parentId);
        if (fulfilmentInfo == null) {
            String msg = String.format("FulfilmentInfo: Parent id %s doesn't exist in database.", parentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        return fulfilmentInfo;
    }
}

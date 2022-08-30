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


import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.models.FulfilmentInfo;
import org.onap.usecaseui.intentanalysis.mapper.FulfilmentInfoMapper;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FulfilmentInfoServiceImpl implements FulfilmentInfoService {

    @Autowired
    private FulfilmentInfoMapper fulfilmentInfoMapper;

    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    @Override
    public void createFulfilmentInfo(FulfilmentInfo fulfilmentInfo, String parentId) {
        if (fulfilmentInfo != null) {
            if (fulfilmentInfoMapper.insertFulfilmentInfo(fulfilmentInfo, parentId) < 1) {
                String msg = "Failed to create fulfilment info to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
            }
            log.info("Successfully created fulfilment info to database.");
        }
    }

    @Override
    public void deleteFulfilmentInfo(String parentId) {
        if (fulfilmentInfoService.getFulfilmentInfo(parentId) != null) {
            if (fulfilmentInfoMapper.deleteFulfilmentInfo(parentId) < 1) {
                String msg = "Failed to delete fulfilment info to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted fulfilment info to database.");
        }
    }

    @Override
    public void updateFulfilmentInfo(FulfilmentInfo fulfilmentInfo, String parentId) {

        FulfilmentInfo fulfillmentInfoDB = fulfilmentInfoService.getFulfilmentInfo(parentId);
        if (fulfillmentInfoDB == null && fulfilmentInfo != null) {
            fulfilmentInfoService.createFulfilmentInfo(fulfilmentInfo, parentId);
        } else if (fulfillmentInfoDB != null && fulfilmentInfo == null) {
            fulfilmentInfoService.deleteFulfilmentInfo(parentId);
        } else if (fulfillmentInfoDB != null) {
            if (fulfilmentInfoMapper.updateFulfilmentInfo(fulfilmentInfo, parentId) < 1) {
                String msg = "Failed to update fulfilment info to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
            }
            log.info("Successfully updated fulfilment info to database.");
        }
    }

    @Override
    public FulfilmentInfo getFulfilmentInfo(String parentId) {
        FulfilmentInfo fulfilmentInfo = fulfilmentInfoMapper.selectFulfilmentInfo(parentId);
        if (fulfilmentInfo == null) {
            log.info(String.format("FulfilmentInfo is null, parentId = %s", parentId));
        }
        return fulfilmentInfo;
    }
}

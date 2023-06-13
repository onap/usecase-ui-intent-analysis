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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentInfo;
import org.onap.usecaseui.intentanalysis.mapper.FulfillmentInfoMapper;
import org.onap.usecaseui.intentanalysis.service.FulfillmentInfoService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FulfillmentInfoServiceImpl implements FulfillmentInfoService {

    @Autowired
    private FulfillmentInfoMapper fulfillmentInfoMapper;

    @Autowired
    private FulfillmentInfoService fulfillmentInfoService;

    @Override
    public void createFulfillmentInfo(FulfillmentInfo fulfillmentInfo, String parentId) {
        if (fulfillmentInfo != null) {
            if (fulfillmentInfoMapper.insertFulfillmentInfo(fulfillmentInfo, parentId) < 1) {
                String msg = "Failed to create fulfillment info to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
            }
            log.info("Successfully created fulfillment info to database.");
        }
    }

    @Override
    public void deleteFulfillmentInfo(String parentId) {
        if (fulfillmentInfoService.getFulfillmentInfo(parentId) != null) {
            if (fulfillmentInfoMapper.deleteFulfillmentInfo(parentId) < 1) {
                String msg = "Failed to delete fulfillment info to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted fulfillment info to database.");
        }
    }

    @Override
    public void updateFulfillmentInfo(FulfillmentInfo fulfillmentInfo, String parentId) {

        FulfillmentInfo fulfillmentInfoDB = fulfillmentInfoService.getFulfillmentInfo(parentId);
        if (fulfillmentInfoDB == null && fulfillmentInfo != null) {
            fulfillmentInfoService.createFulfillmentInfo(fulfillmentInfo, parentId);
        } else if (fulfillmentInfoDB != null && fulfillmentInfo == null) {
            fulfillmentInfoService.deleteFulfillmentInfo(parentId);
        } else if (fulfillmentInfoDB != null) {
            if (fulfillmentInfoMapper.updateFulfillmentInfo(fulfillmentInfo, parentId) < 1) {
                String msg = "Failed to update fulfillment info to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
            }
            log.info("Successfully updated fulfillment info to database.");
        }
    }

    @Override
    public FulfillmentInfo getFulfillmentInfo(String parentId) {
        FulfillmentInfo fulfillmentInfo = fulfillmentInfoMapper.selectFulfillmentInfo(parentId);
        if (fulfillmentInfo == null) {
            log.info(String.format("FulfillmentInfo is null, parentId = %s", parentId));
        }
        return fulfillmentInfo;
    }

    @Override
    public void saveFulfillmentInfo(String intentId, FulfillmentInfo eventModel) {
        FulfillmentInfo fulfillmentInfo = fulfillmentInfoService.getFulfillmentInfo(intentId);
        if (fulfillmentInfo != null) {
            fulfillmentInfoService.deleteFulfillmentInfo(intentId);
        }
        fulfillmentInfoService.createFulfillmentInfo(eventModel, intentId);
    }
}

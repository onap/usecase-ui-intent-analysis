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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.enums.FulfilmentInfoParentType;
import org.onap.usecaseui.intentanalysis.bean.models.FulfilmentInfo;
import org.onap.usecaseui.intentanalysis.mapper.FulfilmentInfoMapper;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;


@Service
public class FulfilmentInfoServiceImpl implements FulfilmentInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(FulfilmentInfoServiceImpl.class);

    @Autowired
    private FulfilmentInfoMapper fulfilmentInfoMapper;

    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    @Override
    public void createFulfilmentInfo(FulfilmentInfo fulfilmentInfo,
                                     FulfilmentInfoParentType fulfilmentInfoParentType,
                                     String parentId) {
        fulfilmentInfoMapper.insertFulfilmentInfo(fulfilmentInfo);
        fulfilmentInfoMapper.insertFulfilmentInfoMapping(fulfilmentInfo.getFulfilmentInfoId(),
                                                         fulfilmentInfoParentType, parentId);
    }

    @Override
    public void deleteFulfilmentInfoByParentId(String parentId) {
    }

    @Override
    public void updateFulfilmentInfoByParentId(FulfilmentInfo fulfilmentInfo, String parentId) {
    }

    @Override
    public FulfilmentInfo getFulfilmentInfoByParentId(String parentId) {
        return fulfilmentInfoMapper.selectFulfilmentInfoById(parentId);
    }
}

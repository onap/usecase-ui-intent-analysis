/*
 * Copyright (C) 2023 CMCC, Inc. and others. All rights reserved.
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
import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentInfo;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.ObjectInstanceMapper;
import org.onap.usecaseui.intentanalysis.service.ObjectInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ObjectInstanceServiceImpl implements ObjectInstanceService {
    @Autowired
    private ObjectInstanceMapper objectInstanceMapper;

    @Override
    public void saveObjectInstances(String parentId, List<String> objectInstance) {
        List<String> instances = new ArrayList<>(objectInstance);
        List<String> objectInstancesDb = objectInstanceMapper.getObjectInstances(parentId);
        if (!CollectionUtils.isEmpty(objectInstancesDb)) {
            instances.removeAll(objectInstancesDb);
            if (CollectionUtils.isEmpty(instances)) {
                log.info("The objectInstances already exist in the database");
                return;
            }
        }
        int objectInstanceNum = objectInstanceMapper.insertObjectInstanceList(instances, parentId);
        if (objectInstanceNum < 1) {
            String msg = "Failed to insert objectInstances to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }
}

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
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.models.IntentInstance;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.IntentInstanceMapper;
import org.onap.usecaseui.intentanalysis.service.IntentInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IntentInstanceServiceImpl implements IntentInstanceService {
    @Autowired
    IntentInstanceMapper intentInstanceMapper;

    @Override
    public void createIntentInstance(IntentInstance intentInstance) {
        int num = intentInstanceMapper.insertIntentInstance(intentInstance);
        if (num < 1) {
            String msg = "Failed to insert intent instance to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public String queryIntentInstanceId(String intentId) {
        String intentInstanceId = intentInstanceMapper.selectIntentInstanceByIntentId(intentId);
        if (StringUtils.isEmpty(intentId)) {
            log.error("get intentInstanceId is failed,intentId is {}", intentId);
        }
        return intentInstanceId;
    }
}

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


import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.mapper.IntentMapper;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntentServiceImpl implements IntentService {
    private static Logger LOGGER = LoggerFactory.getLogger(IntentService.class);

    @Autowired
    private IntentMapper intentMapper;

    @Autowired
    private ExpectationService expectationService;

    @Override
    public List<Intent> getIntentList() {
        List<Intent> intentList = intentMapper.selectIntents();
        if (intentList == null || intentList.size() <= 0) {
            String msg = "Intent list doesn't exist in the intent database.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        for (Intent intent : intentList) {
            intent.setExpectationList(expectationService.getExpectationListByIntentId(intent.getIntentId()));
        }
        return intentList;
    }

    @Override
    public Intent getIntentById(String intentId) {
        Intent intent = intentMapper.selectIntentById(intentId);
        if (intent != null) {
            intent.setExpectationList(expectationService.getExpectationListByIntentId(intent.getIntentId()));
            return intent;
        } else {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Intent createIntent(Intent intent) {
        int res = intentMapper.insertIntent(intent);
        if (res < 1) {
            String msg = "Create intent to database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        // saving expectation list into expectation table
        expectationService.createExpectationList(intent.getExpectationList(), intent.getIntentId());
        LOGGER.info("Intent was successfully created.");
        return intent;
    }

    @Override
    public Intent updateIntent(Intent intent) {
        Intent intentDB = intentMapper.selectIntentById(intent.getIntentId());
        if (intentDB == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intent.getIntentId());
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        expectationService.updateExpectationListById(intent.getExpectationList(), intent.getIntentId());
        int res = intentMapper.updateIntent(intent);
        if (res < 1) {
            String msg = "Update intent in database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
        }
        LOGGER.info("Update intent successfully.");
        return intentMapper.selectIntentById(intent.getIntentId());
    }

    @Override
    public void deleteIntentById(String intentId) {
        int res = intentMapper.deleteIntentById(intentId);
        if (res < 1) {
            String msg = "Delete intent in database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
        expectationService.deleteExpectationListById(intentId);
        LOGGER.info("Intent has been deleted successfully.");
    }
}

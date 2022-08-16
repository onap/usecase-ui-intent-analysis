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


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.IntentMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;
import org.onap.usecaseui.intentanalysis.service.IntentService;


@Service
@Slf4j
public class IntentServiceImpl implements IntentService {

    @Autowired
    private IntentMapper intentMapper;

    @Autowired
    private ExpectationService expectationService;

    @Autowired
    private ContextService contextService;

    private ContextParentType contextParentType;

    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Intent createIntent(Intent intent) {
        int res = intentMapper.insertIntent(intent);
        if (res < 1) {
            String msg = "Create intent to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        expectationService.createIntentExpectationList(intent.getIntentExpectations(), intent.getIntentId());
        contextService.createContextList(intent.getIntentContexts(), intent.getIntentId());
        fulfilmentInfoService.createFulfilmentInfo(intent.getIntentFulfilmentInfo(), intent.getIntentId());
        log.debug("Intent was successfully created.");
        return intent;
    }

    @Override
    public List<Intent> getIntentList() {
        List<Intent> intentList = intentMapper.selectIntentList();
        if (intentList == null || intentList.size() <= 0) {
            String msg = "Intent list doesn't exist in the intent database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        for (Intent intent : intentList) {
            if (null != intent) {
                String intentId = intent.getIntentId();
                intent.setIntentExpectations(expectationService.getIntentExpectationListByIntentId(intentId));
                intent.setIntentContexts(contextService.getContextListByParentId(intentId));
                intent.setIntentFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfoByParentId(intentId));
                log.debug("Intent %s was successfully found", intentId);
            }
        }
        return intentList;
    }

    @Override
    public Intent getIntentById(String intentId) {
        Intent intent = intentMapper.selectIntentById(intentId);
        if (intent == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        intent.setIntentExpectations(expectationService.getIntentExpectationListByIntentId(intentId));
        intent.setIntentContexts(contextService.getContextListByParentId(intentId));
        intent.setIntentFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfoByParentId(intentId));
        log.debug("Intent was successfully found");
        return intent;
    }

    @Override
    public Intent updateIntent(Intent intent) {
        Intent intentDB = intentMapper.selectIntentById(intent.getIntentId());
        if (intentDB == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intent.getIntentId());
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        expectationService.updateIntentExpectationListByIntentId(intent.getIntentExpectations(), intent.getIntentId());
        int res = intentMapper.updateIntent(intent);
        if (res < 1) {
            String msg = "Update intent in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
        }
        log.debug("Update intent successfully.");
        return intentMapper.selectIntentById(intent.getIntentId());
    }

    @Override
    public void deleteIntentById(String intentId) {
        int res = intentMapper.deleteIntentById(intentId);
        if (res < 1) {
            String msg = "Delete intent in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
        expectationService.deleteIntentExpectationListByIntentId(intentId);
        log.debug("Intent has been deleted successfully.");
    }
}

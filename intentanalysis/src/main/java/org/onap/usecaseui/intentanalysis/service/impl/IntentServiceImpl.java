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


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
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

    @Autowired
    private IntentService intentService;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Intent createIntent(Intent intent) {
        if (intentMapper.insertIntent(intent) < 1) {
            String msg = "Failed to create intent to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        expectationService.createIntentExpectationList(intent.getIntentExpectations(), intent.getIntentId());
        contextService.createContextList(intent.getIntentContexts(), intent.getIntentId());
        fulfilmentInfoService.createFulfilmentInfo(intent.getIntentFulfilmentInfo(), intent.getIntentId());
        log.info("Successfully created intent to database.");
        return intent;
    }

    @Override
    public List<Intent> getIntentList() {
        List<Intent> intentList = intentMapper.selectIntentList();
        if (CollectionUtils.isEmpty(intentList)) {
            log.info("Intent list is null");
        }
        for (Intent intent : intentList) {
            intent.setIntentExpectations(expectationService.getIntentExpectationList(intent.getIntentId()));
        }
        return intentList;
    }

    @Override
    public Intent getIntent(String intentId) {
        Intent intent = intentMapper.selectIntent(intentId);
        if (intent != null) {
            intent.setIntentExpectations(expectationService.getIntentExpectationList(intentId));
            intent.setIntentContexts(contextService.getContextList(intentId));
            intent.setIntentFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfo(intentId));
        } else {
            log.info(String.format("Intent is null, intentId = %s", intentId));
        }
        return intent;
    }

    @Override
    public Intent updateIntent(Intent intent) {
        String intentId = intent.getIntentId();
        Intent intentFromDB = intentService.getIntent(intentId);
        if (intentFromDB == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        expectationService.updateIntentExpectationList(intent.getIntentExpectations(), intentId);
        contextService.updateContextList(intent.getIntentContexts(), intentId);
        fulfilmentInfoService.updateFulfilmentInfo(intent.getIntentFulfilmentInfo(), intentId);
        if (intentMapper.updateIntent(intent) < 1) {
            String msg = "Failed to update intent to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
        }
        log.info("Successfully updated intent to database.");
        return intentService.getIntent(intentId);
    }

    @Override
    public void deleteIntent(String intentId) {
        Intent intent = intentService.getIntent(intentId);
        if (intent == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        fulfilmentInfoService.deleteFulfilmentInfo(intentId);
        contextService.deleteContextList(intentId);
        expectationService.deleteIntentExpectationList(intentId);
        if (intentMapper.deleteIntent(intentId) < 1) {
            String msg = "Failed to delete intent to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
        log.info("Successfully deleted intent to database.");
    }

    @Override
    public List<Intent> getIntentByName(String intentName) {
        List<Intent> intentList = intentMapper.getIntentByName(intentName);
        if (!CollectionUtils.isEmpty(intentList)) {
            for (Intent intent : intentList) {
                intent.setIntentExpectations(expectationService.getIntentExpectationList(intent.getIntentId()));
                intent.setIntentContexts(contextService.getContextList(intent.getIntentId()));
                intent.setIntentFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfo(intent.getIntentId()));
            }

        } else {
            log.info(String.format("Intent list is null, intentName = %s", intentName));
        }
        return intentList;
    }

    @Override
    public List<String> getSubIntentList(Intent intent){
        List<Context> intentContexts = intent.getIntentContexts();
        List<String> subIntentIds= new ArrayList<>();
        if (CollectionUtils.isNotEmpty(intentContexts)) {
            List<Context> subIntentInfoList = intentContexts.stream().filter(x -> StringUtils.equals(x.getContextName(), "subIntent info")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(subIntentInfoList)) {
                List<Condition> contextConditions = subIntentInfoList.get(0).getContextConditions();
                if (CollectionUtils.isNotEmpty(contextConditions)) {
                    for (Condition con:contextConditions) {
                        subIntentIds.add(con.getConditionValue());
                    }
                }
            }
        }
        return subIntentIds;
    }

    @Override
    public List<Intent> getIntentListByUserInput(String intentGenerateType) {
        List<Intent> intentList = intentMapper.getIntentListByIntentGenerateType(intentGenerateType);
        if (CollectionUtils.isEmpty(intentList)) {
            log.info("Intent list is null");
        }
        for (Intent intent : intentList) {
            intent.setIntentExpectations(expectationService.getIntentExpectationList(intent.getIntentId()));
        }
        return intentList;
    }
}

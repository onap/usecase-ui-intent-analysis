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

import lombok.extern.slf4j.Slf4j;

import org.onap.usecaseui.intentanalysis.bean.enums.ConditionParentType;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.mapper.ContextMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.onap.usecaseui.intentanalysis.service.ConditionService;
import org.springframework.util.CollectionUtils;


@Service
@Slf4j
public class ContextServiceImpl implements ContextService {

    private ConditionParentType conditionParentType;

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ConditionService conditionService;

    @Override
    public void createContextList(List<Context> contextList, String parentId) {
        if (!CollectionUtils.isEmpty(contextList)) {
            for (Context context : contextList) {
                conditionService.createConditionList(context.getContextConditions(), context.getContextId());
            }
            if (contextMapper.insertContextList(contextList, parentId) < 1) {
                String msg = "Failed to create context list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
            }
            log.info("Successfully created context list to database.");
        }


        //contextMapper.insertContextParentList(contextList, parentId);
    }

    @Override
    public void createContext(Context context, String parentId) {
        conditionService.createConditionList(context.getContextConditions(), context.getContextId());
        if (contextMapper.insertContext(context, parentId) < 1) {
            String msg = "Failed to create context to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        log.info("Successfully created context to database.");
    }

    @Override
    public void deleteContextList(String parentId) {
        List<Context> contextList = contextService.getContextList(parentId);
        if (!CollectionUtils.isEmpty(contextList)) {
            for (Context context : contextList) {
                conditionService.deleteConditionList(context.getContextId());
            }
            if (contextMapper.deleteContextList(parentId) < 1) {
                String msg = "Failed to delete context list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted context list to database.");
        }

    }

    @Override
    public void deleteContext(String contextId) {
        Context context = contextService.getContext(contextId);
        if (context != null) {
            conditionService.deleteConditionList(contextId);
            if (contextMapper.deleteContext(contextId) < 1) {
                String msg = "Failed to delete context to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted context to database.");
        }
    }

    @Override
    public void updateContextList(List<Context> contextList, String parentId) {
        List<Context> contextListFromDB = contextService.getContextList(parentId);
        if (!CollectionUtils.isEmpty(contextListFromDB) && CollectionUtils.isEmpty(contextList)) {
            contextService.deleteContextList(parentId);
        } else if (CollectionUtils.isEmpty(contextListFromDB) && !CollectionUtils.isEmpty(contextList)) {
            contextService.createContextList(contextList, parentId);
        } else if (!CollectionUtils.isEmpty(contextListFromDB) && !CollectionUtils.isEmpty(contextList)) {
            List<String> contextIdListFromDB = new ArrayList<>();
            for (Context contextInDB : contextListFromDB) {
                contextIdListFromDB.add(contextInDB.getContextId());
            }
            for (Context context : contextList) {
                if (contextIdListFromDB.contains(context.getContextId())) {
                    if (contextMapper.updateContext(context) < 1) {
                        String msg = "Failed to update context list to database.";
                        log.error(msg);
                        throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                    }
                    contextIdListFromDB.remove(context.getContextId());
                } else {
                    contextService.createContext(context, parentId);
                }
            }
            for (String contextIdFromDB : contextIdListFromDB) {
                contextService.deleteContext(contextIdFromDB);
            }
            log.info("Successfully updated context list to database.");
        }
    }

    @Override
    public List<Context> getContextList(String parentId) {
        List<Context> contextList = contextMapper.selectContextList(parentId);
        if (!CollectionUtils.isEmpty(contextList)) {
            for (Context context : contextList) {
                context.setContextConditions(conditionService.getConditionList(context.getContextId()));
            }
        } else {
            log.info(String.format("Context list is null, parentId = %s", parentId));
        }
        return contextList;
    }

    @Override
    public Context getContext(String contextId) {
        Context context = contextMapper.selectContext(contextId);
        if (context != null) {
            context.setContextConditions(conditionService.getConditionList(contextId));
        } else {
            log.info(String.format("Condition is null, contextId = %s", contextId));
        }
        return context;
    }
}

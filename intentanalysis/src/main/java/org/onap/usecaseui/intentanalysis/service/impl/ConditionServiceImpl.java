/*
 * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
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

import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.ConditionMapper;
import org.onap.usecaseui.intentanalysis.service.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    private ConditionMapper conditionMapper;

    @Autowired
    private ConditionService conditionService;

    @Override
    public void createConditionList(List<Condition> conditionList, String parentId) {
        if (!CollectionUtils.isEmpty(conditionList)) {
            if (conditionMapper.insertConditionList(conditionList, parentId) < 1) {
                String msg = "Failed to create condition list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
            }
            log.info("Successfully created condition list to database.");
            //conditionMapper.insertConditionParentList(conditionList,conditionParentType,parentId);
        }

    }

    @Override
    public void createCondition(Condition condition, String parentId) {
        int res = conditionMapper.insertCondition(condition, parentId);
        if (res < 1) {
            String msg = "Failed to create condition to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        log.info("Successfully created condition to database.");
    }

    @Override
    public void deleteConditionList(String parentId) {
        List<Condition> conditionList = conditionService.getConditionList(parentId);
        if (!CollectionUtils.isEmpty(conditionList)) {
            int res = conditionMapper.deleteConditionList(parentId);
            if (res < 1) {
                String msg = "Failed to delete condition list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted condition list to database.");
        }
    }

    @Override
    public void deleteCondition(String conditionId) {
        Condition condition = conditionService.getCondition(conditionId);
        if (condition != null) {
            if (conditionMapper.deleteCondition(conditionId) < 1) {
                String msg = "Failed to delete condition to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted condition to database.");
        }
    }

    @Override
    public void updateConditionList(List<Condition> conditionList, String parentId) {
        List<Condition> conditionListFromDB = conditionService.getConditionList(parentId);
        if (!CollectionUtils.isEmpty(conditionListFromDB) && CollectionUtils.isEmpty(conditionList)) {
            conditionService.deleteConditionList(parentId);
        } else if (CollectionUtils.isEmpty(conditionListFromDB) && !CollectionUtils.isEmpty(conditionList)) {
            conditionService.createConditionList(conditionList, parentId);
        } else if (!CollectionUtils.isEmpty(conditionListFromDB) && !CollectionUtils.isEmpty(conditionList)) {
            List<String> conditionIdListFromDB = new ArrayList<>();
            for (Condition conditionDB : conditionListFromDB) {
                conditionIdListFromDB.add(conditionDB.getConditionId());
            }
            for (Condition condition : conditionList) {
                if (conditionIdListFromDB.contains(condition.getConditionId())) {
                    if (conditionMapper.updateCondition(condition) < 1) {
                        String msg = "Failed to update condition list to database.";
                        log.error(msg);
                        throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                    }
                    conditionIdListFromDB.remove(condition.getConditionId());
                } else {
                    conditionService.createCondition(condition, parentId);
                }
            }
            for (String conditionIdFromDB : conditionIdListFromDB) {
                conditionService.deleteCondition(conditionIdFromDB);
            }
            log.info("Successfully updated condition list to database.");
        }
    }

    @Override
    public List<Condition> getConditionList(String parentId) {
        List<Condition> conditionList = conditionMapper.selectConditionList(parentId);
        if (CollectionUtils.isEmpty(conditionList)) {
            log.info(String.format("Condition list is null, parentId = %s", parentId));
        }
        return conditionList;
    }

    @Override
    public Condition getCondition(String conditionId) {
        Condition condition = conditionMapper.selectCondition(conditionId);
        if (condition == null) {
            log.info(String.format("Condition is null, conditionId = %s", conditionId));
        }
        return condition;
    }
}
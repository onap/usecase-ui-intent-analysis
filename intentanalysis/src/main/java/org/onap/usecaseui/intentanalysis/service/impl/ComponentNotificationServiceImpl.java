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
import org.onap.usecaseui.intentanalysis.bean.enums.ExpectationType;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.CommonException;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.*;
import org.onap.usecaseui.intentanalysis.service.ComponentNotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ComponentNotificationServiceImpl implements ComponentNotificationService {

    @Autowired
    private ExpectationObjectMapper expectationObjectMapper;

    @Autowired
    private ExpectationMapper expectationMapper;

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private ConditionMapper conditionMapper;

    @Autowired
    private FulfillmentInfoMapper fulfillmentInfoMapper;

    @Autowired
    private ObjectInstanceMapper objectInstanceMapper;

    /**
     * Generate a new FulfillmentInfo based on third-party FulfillmentOperation
     * and save it in the database
     *
     * @param eventModel param
     */
    @Override
    @Transactional(rollbackFor = DataBaseException.class)
    public void callBack(FulfillmentOperation eventModel) {
        if (eventModel == null) {
            String msg = "The obtained fulfillmentInfo is null";
            throw new CommonException(msg, ResponseConsts.EMPTY_PARAM);
        }
        String operation = eventModel.getOperation();
        List<String> objectInstances = eventModel.getObjectInstances();
        if (CollectionUtils.isEmpty(objectInstances) || StringUtils.isEmpty(operation)) {
            String msg = "The obtained objectInstances or operation are empty";
            throw new CommonException(msg, ResponseConsts.EMPTY_PARAM);
        }
        log.info("Get objectInstances is {}", objectInstances);
        List<String> expectationIds = expectationObjectMapper.getExpectationIdByObjectInstance(objectInstances.get(0));
        if (CollectionUtils.isEmpty(expectationIds)) {
            String msg = "Get expectationId is null from database";
            log.error(msg);
            return;
        }
        log.info("ExpectationId is {}", expectationIds);
        String intentId = null;
        for (String expectationId : expectationIds) {
            ExpectationType expectationType = getExpectationType(operation);
            intentId = expectationMapper.getIntentIdByExpectationId(expectationId, expectationType);
            if (StringUtils.isNotEmpty(intentId)) {
                break;
            }
        }

        if (StringUtils.isEmpty(intentId)) {
            String msg = "Get intentId is null from database";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }

        String parentByIntentId = findParentByIntentId(findParentByIntentId(intentId));
        log.info("The parentByIntentId is {}", parentByIntentId);

        saveFulfillmentInfo(parentByIntentId, eventModel);
    }

    private void saveFulfillmentInfo(String intentId, FulfillmentOperation eventModel) {
        FulfillmentInfo fulfillmentInfo = fulfillmentInfoMapper.selectFulfillmentInfo(intentId);
        if (fulfillmentInfo != null) {
            fulfillmentInfoMapper.deleteFulfillmentInfo(intentId);
        }
        FulfillmentInfo newInfo = new FulfillmentInfo();
        BeanUtils.copyProperties(eventModel, newInfo);
        int num = fulfillmentInfoMapper.insertFulfillmentInfo(newInfo, intentId);
        if (num < 1) {
            String msg = "Failed to insert fulfillmentInfo to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        List<String> instances = eventModel.getObjectInstances();
        List<String> objectInstancesDb = objectInstanceMapper.getObjectInstances(intentId);
        if (!CollectionUtils.isEmpty(objectInstancesDb)) {
            instances.removeAll(objectInstancesDb);
            if (CollectionUtils.isEmpty(instances)) {
                log.info("The objectInstances already exist in the database");
                return;
            }
        }
        int objectInstanceNum = objectInstanceMapper.insertObjectInstanceList(instances, intentId);
        if (objectInstanceNum < 1) {
            String msg = "Failed to insert objectInstances to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    public String findParentByIntentId(String intentId) {
        List<Context> contexts = contextMapper.selectContextList(intentId);
        if (CollectionUtils.isEmpty(contexts)) {
            log.error("Get context is empty,intentId is {}", intentId);
            String msg = "Get Contexts is empty from database";
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        List<Context> collect = contexts.stream()
                .filter(context -> "parentIntent info".equalsIgnoreCase(context.getContextName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect) || collect.size() != 1) {
            log.error("This intent has not parent intent,intentId is {}", intentId);
            String msg = "Get Context is empty from database";
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        Context context = collect.get(0);
        List<Condition> conditions = conditionMapper.selectConditionList(context.getContextId());
        if (CollectionUtils.isEmpty(conditions) || StringUtils.isEmpty(conditions.get(0).getConditionValue())) {
            String msg = "Get conditions is empty from database";
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        return conditions.get(0).getConditionValue();
    }

    private ExpectationType getExpectationType(String operation) {
        if (operation.contains("Assurance")) {
            return ExpectationType.ASSURANCE;
        }
        return ExpectationType.DELIVERY;
    }
}

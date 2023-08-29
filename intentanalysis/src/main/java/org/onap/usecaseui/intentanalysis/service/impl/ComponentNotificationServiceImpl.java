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
import org.onap.usecaseui.intentanalysis.cllassuranceIntentmgt.CLLAssuranceIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.clldeliveryIntentmgt.CLLDeliveryIntentManagementFunction;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.CommonException;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.mapper.*;
import org.onap.usecaseui.intentanalysis.service.ComponentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class ComponentNotificationServiceImpl implements ComponentNotificationService {
    @Autowired
    private ExpectationMapper expectationMapper;

    @Autowired
    private ApplicationContext applicationContext;

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
        List<String> expectationIds = objectInstanceMapper.getParentIdByInstance(objectInstances.get(0));
        if (CollectionUtils.isEmpty(expectationIds)) {
            String msg = "Get expectationId is null from database";
            log.error(msg);
            return;
        }
        log.info("ExpectationId is {}", expectationIds);
        String intentId = null;
        ExpectationType expectationType = getExpectationType(operation);
        for (String expectationId : expectationIds) {
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

        IntentManagementFunction function;
        if (expectationType == ExpectationType.ASSURANCE) {
            function = (IntentManagementFunction) applicationContext.getBean(CLLAssuranceIntentManagementFunction.class.getSimpleName());
        } else {
            function = (IntentManagementFunction) applicationContext.getBean(CLLDeliveryIntentManagementFunction.class.getSimpleName());
        }
        function.createReport(intentId, eventModel);
    }

    private ExpectationType getExpectationType(String operation) {
        if (operation.contains("Assurance")) {
            return ExpectationType.ASSURANCE;
        }
        return ExpectationType.DELIVERY;
    }
}

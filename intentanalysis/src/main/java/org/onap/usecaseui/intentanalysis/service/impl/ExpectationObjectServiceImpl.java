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
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationObjectMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.springframework.util.CollectionUtils;


@Service
@Slf4j
public class ExpectationObjectServiceImpl implements ExpectationObjectService {

    private ContextParentType contextParentType;

    @Autowired
    private ExpectationObjectMapper expectationObjectMapper;

    @Autowired
    private ExpectationObjectService expectationObjectService;

    @Autowired
    private ContextService contextService;

    @Override
    public void createExpectationObject(ExpectationObject expectationObject, String expectationId) {
        contextService.createContextList(expectationObject.getObjectContexts(),
                expectationObjectMapper.selectExpectationObjectId(expectationId));
        if (expectationObjectMapper.insertExpectationObject(expectationObject, expectationId) < 1) {
            String msg = "Failed to create expectation object to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        log.info("Successfully created expectation object to database.");
    }

    @Override
    public ExpectationObject getExpectationObject(String expectationId) {
        ExpectationObject expectationObject = expectationObjectMapper.selectExpectationObject(expectationId);
        String expectationObjectId = expectationObjectMapper.selectExpectationObjectId(expectationId);
        List<Context> contextList = contextService.getContextList(expectationObjectId);
        if (!CollectionUtils.isEmpty(contextList)) {
            expectationObject.setObjectContexts(contextService.getContextList(expectationObjectId));
        } else {
            log.info(String.format("Expectation object is null, expectationObjectId = %s", expectationObjectId));
        }
        return expectationObject;
    }

    @Override
    public void updateExpectationObject(ExpectationObject expectationObject, String expectationId) {
        ExpectationObject expectationObjectFromDB = expectationObjectService.getExpectationObject(expectationId);
        if (expectationObject == null && expectationObjectFromDB != null) {
            expectationObjectService.deleteExpectationObject(expectationId);
        } else if (expectationObject != null && expectationObjectFromDB == null) {
            expectationObjectService.createExpectationObject(expectationObject, expectationId);
        } else if (expectationObject != null) {
            contextService.updateContextList(expectationObject.getObjectContexts(),
                    expectationObjectMapper.selectExpectationObjectId(expectationId));
            if (expectationObjectMapper.updateExpectationObject(expectationObject, expectationId) < 1) {
                String msg = "Failed to update expectation object to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
            }
            log.info("Successfully updated expectation object to database.");
        }

    }

    @Override
    public void deleteExpectationObject(String expectationId) {
        ExpectationObject expectationObject = expectationObjectService.getExpectationObject(expectationId);
        if (expectationObject != null) {
            contextService.deleteContextList(expectationObjectMapper.selectExpectationObjectId(expectationId));
            if (expectationObjectMapper.deleteExpectationObject(expectationId) < 1) {
                String msg = "Failed to update expectation object to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted expectation object to database.");
        }
    }
}

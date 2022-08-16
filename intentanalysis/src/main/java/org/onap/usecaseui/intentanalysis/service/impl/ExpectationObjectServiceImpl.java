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


import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationObject;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationObjectMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;


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
        expectationObjectMapper.insertExpectationObject(expectationObject, expectationId);
        contextService.createContextList(expectationObject.getObjectContexts(), expectationId);
    }

    public ExpectationObject getExpectationObjectByExpectationId(String expectationId) {
        ExpectationObject expectationObject = expectationObjectMapper.selectIntentExpectationObjectByExpectationId(expectationId);
        if (expectationObject == null) {
            String msg = String.format("ExpectationObject: expectation id %s doesn't exist in database.", expectationId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        expectationObject.setObjectContexts(contextService.getContextListByParentId(expectationId));

        return expectationObject;
    }
}

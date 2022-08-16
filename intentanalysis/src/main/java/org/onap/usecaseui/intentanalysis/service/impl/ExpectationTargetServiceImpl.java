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
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationTargetMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationTargetService;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;


@Service
@Slf4j
public class ExpectationTargetServiceImpl implements ExpectationTargetService {

    private ContextParentType contextParentType;

    @Autowired
    private ExpectationTargetMapper expectationTargetMapper;

    @Autowired
    private ExpectationTargetService expectationTargetService;

    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    @Autowired
    private ContextService contextService;

    @Override
    public void createExpectationTarget(ExpectationTarget expectationTarget, String expectationId) {
        expectationTargetMapper.insertExpectationTarget(expectationTarget, expectationId);
        contextService.createContextList(expectationTarget.getTargetContexts(), expectationTarget.getTargetId());
        fulfilmentInfoService.createFulfilmentInfo(expectationTarget.getTargetFulfilmentInfo(),
                                                   expectationTarget.getTargetId());
    }

    @Override
    public void createExpectationTargetList(List<ExpectationTarget> expectationTargets, String expectationId) {
        for (ExpectationTarget expectationTarget : expectationTargets) {
            if (null != expectationTarget) {
                expectationTargetService.createExpectationTarget(expectationTarget, expectationId);
            }
        }
    }

    @Override
    public List<ExpectationTarget> getExpectationTargetListByExpectationId(String expectationId) {
        List<ExpectationTarget> expectationTargetList = expectationTargetMapper.selectIntentExpectationTargetsByExpectationId(expectationId);
        if (null == expectationTargetList) {
            String msg = String.format("Target: Expectation id %s doesn't exist in database.", expectationId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        for (ExpectationTarget expectationTarget : expectationTargetList) {
            if (null != expectationTarget) {
                expectationTarget.setTargetContexts(contextService.getContextListByParentId(expectationId));
                expectationTarget.setTargetFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfoByParentId(expectationId));
            }
        }
        return expectationTargetList;
    }
}

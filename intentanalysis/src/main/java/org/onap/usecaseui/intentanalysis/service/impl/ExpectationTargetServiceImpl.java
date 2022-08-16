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

import org.onap.usecaseui.intentanalysis.bean.enums.ConditionParentType;
import org.onap.usecaseui.intentanalysis.service.ConditionService;
import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationTargetMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationTargetService;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;


@Service
@Slf4j
public class ExpectationTargetServiceImpl implements ExpectationTargetService {

    private ConditionParentType conditionParentType;

    @Autowired
    private ExpectationTargetMapper expectationTargetMapper;

    @Autowired
    private ExpectationTargetService expectationTargetService;


    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private ConditionService conditionService;

    @Override
    public void createExpectationTarget(ExpectationTarget expectationTarget, String expectationId) {
        if (expectationTargetMapper.insertExpectationTarget(expectationTarget, expectationId) < 1) {
            String msg = "Create expectation to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        contextService.createContextList(expectationTarget.getTargetContexts(), expectationTarget.getTargetId());
        fulfilmentInfoService.createFulfilmentInfo(expectationTarget.getTargetFulfilmentInfo(),
                                                   expectationTarget.getTargetId());
        conditionService.createConditionList(expectationTarget.getTargetConditions(),
                                             conditionParentType.EXPECTATION_TARGET, expectationId);
    }

    @Override
    public void createExpectationTargetList(List<ExpectationTarget> expectationTargetList, String expectationId) {
        for (ExpectationTarget expectationTarget : expectationTargetList) {
            if (expectationTarget != null) {
                expectationTargetService.createExpectationTarget(expectationTarget, expectationId);
            }
        }
    }

    @Override
    public List<ExpectationTarget> getExpectationTargetListByExpectationId(String expectationId) {
        List<ExpectationTarget> expectationTargetList = expectationTargetMapper.selectExpectationTargetListByExpectationId(expectationId);
        if (expectationTargetList == null) {
            String msg = String.format("Target: Expectation id %s doesn't exist in database.", expectationId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        for (ExpectationTarget expectationTarget : expectationTargetList) {
            if (expectationTarget != null) {
                String targetId = expectationTarget.getTargetId();
                expectationTarget.setTargetContexts(contextService.getContextListByParentId(targetId));
                expectationTarget.setTargetFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfoByParentId(targetId));
            }
        }
        return expectationTargetList;
    }
}

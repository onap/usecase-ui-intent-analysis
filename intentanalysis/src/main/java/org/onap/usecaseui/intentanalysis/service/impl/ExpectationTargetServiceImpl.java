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
import org.onap.usecaseui.intentanalysis.service.ConditionService;
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
import org.springframework.util.CollectionUtils;


@Service
@Slf4j
public class ExpectationTargetServiceImpl implements ExpectationTargetService {

    private ContextParentType contextParentType;

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
        String expectationTargetId = expectationTarget.getTargetId();
        contextService.createContextList(expectationTarget.getTargetContexts(), expectationTargetId);
        fulfilmentInfoService.createFulfilmentInfo(expectationTarget.getTargetFulfilmentInfo(),
                expectationTargetId);
        conditionService.createConditionList(expectationTarget.getTargetConditions(), expectationTargetId);
        if (expectationTargetMapper.insertExpectationTarget(expectationTarget, expectationId) < 1) {
            String msg = "Failed to create expectation target to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        log.info("Successfully created expectation target to database.");

    }

    @Override
    public void createExpectationTargetList(List<ExpectationTarget> expectationTargetList, String expectationId) {
        if (!CollectionUtils.isEmpty(expectationTargetList)) {
            for (ExpectationTarget expectationTarget : expectationTargetList) {
                String expectationTargetId = expectationTarget.getTargetId();
                contextService.createContextList(expectationTarget.getTargetContexts(), expectationTargetId);
                fulfilmentInfoService.createFulfilmentInfo(expectationTarget.getTargetFulfilmentInfo(),
                        expectationTargetId);
                conditionService.createConditionList(expectationTarget.getTargetConditions(), expectationTargetId);
            }
            if (expectationTargetMapper.insertExpectationTargetList(expectationTargetList, expectationId) < 1) {
                String msg = "Failed to create expectation target list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
            }
            log.info("Successfully created expectation target list to database.");
        }

    }

    @Override
    public List<ExpectationTarget> getExpectationTargetList(String expectationId) {
        List<ExpectationTarget> expectationTargetList = expectationTargetMapper.selectExpectationTargetList(expectationId);
        if (!CollectionUtils.isEmpty(expectationTargetList)) {
            for (ExpectationTarget expectationTarget : expectationTargetList) {
                String expectationTargetId = expectationTarget.getTargetId();
                expectationTarget.setTargetConditions(conditionService.getConditionList(expectationTargetId));
                expectationTarget.setTargetContexts(contextService.getContextList(expectationTargetId));
                expectationTarget.setTargetFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfo(expectationTargetId));
            }
        } else {
            log.info(String.format("Expectation target list is null, expectationId = %s", expectationId));
        }

        return expectationTargetList;
    }

    @Override
    public ExpectationTarget getExpectationTarget(String expectationTargetId) {
        ExpectationTarget expectationTarget = expectationTargetMapper.selectExpectationTarget(expectationTargetId);
        if (expectationTarget != null) {
            expectationTarget.setTargetConditions(conditionService.getConditionList(expectationTargetId));
            expectationTarget.setTargetContexts(contextService.getContextList(expectationTargetId));
            expectationTarget.setTargetFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfo(expectationTargetId));
        } else {
            log.info(String.format("Expectation target is null, expectationTargetId = %s", expectationTargetId));
        }
        return expectationTarget;
    }

    @Override
    public void updateExpectationTargetList(List<ExpectationTarget> expectationTargetList, String expectationId) {
        List<ExpectationTarget> expectationTargetListFromDB = expectationTargetService.getExpectationTargetList(expectationId);
        if (!CollectionUtils.isEmpty(expectationTargetListFromDB) && CollectionUtils.isEmpty(expectationTargetList)) {
            expectationTargetService.deleteExpectationTargetList(expectationId);
        } else if (CollectionUtils.isEmpty(expectationTargetListFromDB) && !CollectionUtils.isEmpty(expectationTargetList)) {
            expectationTargetService.createExpectationTargetList(expectationTargetList, expectationId);
        } else if (!CollectionUtils.isEmpty(expectationTargetListFromDB) && !CollectionUtils.isEmpty(expectationTargetList)) {
            List<String> expectationTargetIdListFromDB = new ArrayList<>();
            for (ExpectationTarget expectationTargetDB : expectationTargetListFromDB) {
                expectationTargetIdListFromDB.add(expectationTargetDB.getTargetId());
            }

            for (ExpectationTarget expectationTarget : expectationTargetList) {
                String expectationTargetId = expectationTarget.getTargetId();
                if (expectationTargetIdListFromDB.contains(expectationTargetId)) {
                    contextService.updateContextList(expectationTarget.getTargetContexts(), expectationTargetId);
                    fulfilmentInfoService.updateFulfilmentInfo(expectationTarget.getTargetFulfilmentInfo(), expectationTargetId);
                    conditionService.updateConditionList(expectationTarget.getTargetConditions(), expectationTargetId);
                    if (expectationTargetMapper.updateExpectationTarget(expectationTarget, expectationTargetId) < 1) {
                        String msg = "Failed to update expectation target list to database.";
                        log.error(msg);
                        throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                    }
                    expectationTargetIdListFromDB.remove(expectationTargetId);
                } else {
                    expectationTargetService.createExpectationTarget(expectationTarget, expectationTargetId);
                }
            }
            for (String expectationTargetIdFromDB : expectationTargetIdListFromDB) {
                expectationTargetService.deleteExpectationTarget(expectationTargetIdFromDB);
            }
            log.info("Successfully updated expectation target list to database.");
        }

    }

    @Override
    public void deleteExpectationTarget(String expectationTargetId) {
        ExpectationTarget expectationTarget = expectationTargetService.getExpectationTarget(expectationTargetId);
        if (expectationTarget != null) {
            contextService.deleteContextList(expectationTargetId);
            fulfilmentInfoService.deleteFulfilmentInfo(expectationTargetId);
            conditionService.deleteConditionList(expectationTargetId);
            if (expectationTargetMapper.deleteExpectationTarget(expectationTargetId) < 1) {
                String msg = "Failed to delete expectation target to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted expectation target to database.");
        }
    }

    @Override
    public void deleteExpectationTargetList(String expectationId) {
        List<ExpectationTarget> expectationTargetList = expectationTargetService.getExpectationTargetList(expectationId);
        if (!CollectionUtils.isEmpty(expectationTargetList)) {
            for (ExpectationTarget expectationTarget : expectationTargetList) {
                String expectationTargetId = expectationTarget.getTargetId();
                contextService.deleteContextList(expectationTargetId);
                fulfilmentInfoService.deleteFulfilmentInfo(expectationTargetId);
                conditionService.deleteConditionList(expectationTargetId);
            }
            if (expectationTargetMapper.deleteExpectationTargetList(expectationId) < 1) {
                String msg = "Failed to delete expectation target list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted expectation target list to database.");
        }

    }


}

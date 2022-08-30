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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.onap.usecaseui.intentanalysis.service.ExpectationTargetService;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;
import org.springframework.util.CollectionUtils;


@Service
@Slf4j
public class ExpectationServiceImpl implements ExpectationService {

    @Autowired
    private ExpectationMapper expectationMapper;

    @Autowired
    private ExpectationService expectationService;

    @Autowired
    private ExpectationObjectService expectationObjectService;

    @Autowired
    private ExpectationTargetService expectationTargetService;

    @Autowired
    private ContextService contextService;

    @Autowired
    private FulfilmentInfoService fulfilmentInfoService;

    private ContextParentType contextParentType;

    @Override
    public void createIntentExpectationList(List<Expectation> intentExpectationList, String intentId) {
        for (Expectation expectation : intentExpectationList) {
            if (expectation != null) {
                String expectationId = expectation.getExpectationId();
                expectationObjectService.createExpectationObject(expectation.getExpectationObject(),
                        expectationId);
                expectationTargetService.createExpectationTargetList(expectation.getExpectationTargets(),
                        expectationId);
                contextService.createContextList(expectation.getExpectationContexts(), expectationId);
                fulfilmentInfoService.createFulfilmentInfo(expectation.getExpectationFulfilmentInfo(),
                        expectationId);
            }
        }
        if (expectationMapper.insertIntentExpectationList(intentExpectationList, intentId) < 1) {
            String msg = "Failed to create expectation list to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        log.info("Successfully created expectation list to database.");
    }

    @Override
    public void createIntentExpectation(Expectation expectation, String intentId) {
        expectationObjectService.createExpectationObject(expectation.getExpectationObject(),
                expectation.getExpectationId());
        expectationTargetService.createExpectationTargetList(expectation.getExpectationTargets(),
                expectation.getExpectationId());
        contextService.createContextList(expectation.getExpectationContexts(),
                expectation.getExpectationId());
        fulfilmentInfoService.createFulfilmentInfo(expectation.getExpectationFulfilmentInfo(),
                expectation.getExpectationId());

        if (expectationMapper.insertIntentExpectation(expectation, intentId) < 1) {
            String msg = "Failed to create expectation to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        log.info("Successfully created expectation to database.");
    }

    @Override
    public List<Expectation> getIntentExpectationList(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectIntentExpectationList(intentId);
        if (!CollectionUtils.isEmpty(expectationList)) {
            for (Expectation expectation : expectationList) {
                String expectationId = expectation.getExpectationId();
                expectation.setExpectationObject(expectationObjectService.getExpectationObject(expectationId));
                expectation.setExpectationTargets(expectationTargetService.getExpectationTargetList(expectationId));
                expectation.setExpectationContexts(contextService.getContextList(expectationId));
                expectation.setExpectationFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfo(expectationId));
            }
        } else {
            log.info(String.format("Expectation list is null, intentId = %s", intentId));
        }
        return expectationList;
    }

    @Override
    public Expectation getIntentExpectation(String expectationId) {
        Expectation expectation = expectationMapper.selectIntentExpectation(expectationId);
        if (expectation != null) {
            expectation.setExpectationObject(expectationObjectService.getExpectationObject(expectationId));
            expectation.setExpectationTargets(expectationTargetService.getExpectationTargetList(expectationId));
            expectation.setExpectationContexts(contextService.getContextList(expectationId));
            expectation.setExpectationFulfilmentInfo(fulfilmentInfoService.getFulfilmentInfo(expectationId));
        } else {
            log.info(String.format("Expectation is null, expectationId = %s", expectationId));
        }
        return expectation;
    }

    @Override
    public void updateIntentExpectationList(List<Expectation> intentExpectationList, String intentId) {
        List<Expectation> expectationListFromDB = expectationService.getIntentExpectationList(intentId);
        if (CollectionUtils.isEmpty(expectationListFromDB) && !CollectionUtils.isEmpty(intentExpectationList)) {
            expectationService.createIntentExpectationList(intentExpectationList, intentId);
        } else if (!CollectionUtils.isEmpty(expectationListFromDB) && CollectionUtils.isEmpty(intentExpectationList)) {
            expectationService.deleteIntentExpectationList(intentId);
        } else if (!CollectionUtils.isEmpty(expectationListFromDB) && !CollectionUtils.isEmpty(intentExpectationList)) {
            List<String> expectationIdListFromDB = new ArrayList<>();
            for (Expectation expectationDB : expectationListFromDB) {
                expectationIdListFromDB.add(expectationDB.getExpectationId());
            }

            for (Expectation expectation : intentExpectationList) {
                if (expectationIdListFromDB.contains(expectation.getExpectationId())) {
                    expectationObjectService.updateExpectationObject(expectation.getExpectationObject(), expectation.getExpectationId());
                    expectationTargetService.updateExpectationTargetList(expectation.getExpectationTargets(), expectation.getExpectationId());
                    contextService.updateContextList(expectation.getExpectationContexts(), expectation.getExpectationId());
                    fulfilmentInfoService.updateFulfilmentInfo(expectation.getExpectationFulfilmentInfo(), expectation.getExpectationId());
                    if (expectationMapper.updateIntentExpectation(expectation) < 1) {
                        String msg = "Failed to update expectation to database.";
                        log.error(msg);
                        throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                    }
                    expectationIdListFromDB.remove(expectation.getExpectationId());
                } else {
                    expectationService.createIntentExpectation(expectation, intentId);
                }
            }
            for (String expectationIdFromDB : expectationIdListFromDB) {
                expectationService.deleteIntentExpectation(expectationIdFromDB);
            }
            log.info("Successfully updated expectation list to database.");
        }

    }

    @Override
    public void deleteIntentExpectation(String expectationId) {
        Expectation expectation = expectationService.getIntentExpectation(expectationId);
        if (expectation != null) {
            expectationObjectService.deleteExpectationObject(expectationId);
            expectationTargetService.deleteExpectationTargetList(expectationId);
            contextService.deleteContextList(expectationId);
            fulfilmentInfoService.deleteFulfilmentInfo(expectationId);
            if (expectationMapper.deleteIntentExpectation(expectationId) < 1) {
                String msg = "Failed to delete expectation to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted expectation to database.");
        }

    }

    @Override
    public void deleteIntentExpectationList(String intentId) {
        List<Expectation> expectationList = expectationService.getIntentExpectationList(intentId);
        if (!CollectionUtils.isEmpty(expectationList)) {
            for (Expectation expectation : expectationList) {
                String expectationId = expectation.getExpectationId();
                expectationObjectService.deleteExpectationObject(expectationId);
                expectationTargetService.deleteExpectationTargetList(expectationId);
                contextService.deleteContextList(expectationId);
                fulfilmentInfoService.deleteFulfilmentInfo(expectationId);
            }
            if (expectationMapper.deleteIntentExpectationList(intentId) < 1) {
                String msg = "Failed to delete expectation list to database.";
                log.error(msg);
                throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
            }
            log.info("Successfully deleted expectation list to database.");
        }
    }
}

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
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.ExpectationObjectService;
import org.onap.usecaseui.intentanalysis.service.ExpectationTargetService;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;


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
    public void createIntentExpectations(List<Expectation> intentExpectations, String intentId) {
        for (Expectation expectation : intentExpectations) {
            if (null != expectation) {
                expectationObjectService.createObject(expectation.getExpectationObject(),
                                                      expectation.getExpectationId());
                expectationTargetService.createTargets(expectation.getExpectationTargets(),
                                                       expectation.getExpectationId());
                contextService.createContextList(expectation.getExpectationContexts(),
                                                 contextParentType.EXPECTATION,
                                                 expectation.getExpectationId());
                fulfilmentInfoService.createFulfilmentInfo(expectation.getExpectationFulfilmentInfo(),
                                                           expectation.getExpectationId());
            }
        }
        int res = expectationMapper.insertIntentExpectations(intentExpectations, intentId);
        if (res < 1) {
            String msg = "Create expectation to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public void insertIntentExpectation(Expectation expectation, String intentId) {
        int res = expectationMapper.insertIntentExpectation(expectation, intentId);
        if (res < 1) {
            String msg = "Create expectation to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public List<Expectation> getIntentExpectationsByIntentId(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectIntentExpectationsByIntentId(intentId);
        if (expectationList == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        int res = expectationMapper.deleteIntentExpectationsByIntentId(intentId);
        if (res < 1) {
            String msg = "Delete expectation in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
        return expectationList;
    }

    @Override
    public void updateIntentExpectationsByIntentId(List<Expectation> intentExpectations, String intentId) {
        List<Expectation> expectationDBList = expectationMapper.selectIntentExpectationsByIntentId(intentId);
        if (expectationDBList == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        List<String> expectationDBIdList = new ArrayList<>();
        for (Expectation expectationDB : expectationDBList) {
            expectationDBIdList.add(expectationDB.getExpectationId());
        }

        for (Expectation expectation : intentExpectations) {
            if (expectationDBIdList.contains(expectation.getExpectationId())) {
                int res = expectationMapper.updateIntentExpectation(expectation);
                if (res < 1) {
                    String msg = "Update expectation in database failed.";
                    log.error(msg);
                    throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                }
                expectationDBIdList.remove(expectation.getExpectationId());
            } else {
                expectationService.insertIntentExpectation(expectation, intentId);
            }
        }
        for (String expectationDBId : expectationDBIdList) {
            expectationService.deleteIntentExpectationById(expectationDBId);
        }
        log.info("Expectations are successfully updated.");
    }

    @Override
    public void deleteIntentExpectationById(String expectationId) {
        int res = expectationMapper.deleteIntentExpectationById(expectationId);
        if (res < 1) {
            String msg = "Delete expectation in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
    }

    @Override
    public void deleteIntentExpectationsByIntentId(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectIntentExpectationsByIntentId(intentId);
        if (expectationList == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        int res = expectationMapper.deleteIntentExpectationsByIntentId(intentId);
        if (res < 1) {
            String msg = "Delete expectation in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
    }
}
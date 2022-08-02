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


import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.State;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationMapper;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;


import java.util.ArrayList;
import java.util.List;

@Service
public class ExpectationServiceImpl implements ExpectationService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExpectationServiceImpl.class);
    @Autowired
    private ExpectationMapper expectationMapper;

    @Autowired
    private StateService stateService;

    @Autowired
    private ExpectationService expectationService;

    @Override
    public void createExpectationList(List<Expectation> expectationList, String intentId) {
        for (Expectation expectation : expectationList) {
            if (null != expectation) {
                stateService.createStateList(expectation.getStateList(), expectation.getExpectationId());
            }
        }
        int res = expectationMapper.insertExpectationList(expectationList, intentId);
        if (res < 1) {
            String msg = "Create expectation to database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public List<Expectation> getExpectationListByIntentId(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        if (expectationList == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        for (Expectation expectation : expectationList) {
            List<State> stateList = stateService.getStateListByExpectationId(expectation.getExpectationId());
            expectation.setStateList(stateList);
        }
        return expectationList;
    }

    @Override
    public void deleteExpectationListById(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        if (expectationList == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        int res = expectationMapper.deleteExpectationByIntentId(intentId);
        if (res < 1) {
            String msg = "Delete expectation in database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
        for (Expectation expectation : expectationList) {
            stateService.deleteStateListByExpectationId(expectation.getExpectationId());
        }
    }

    @Override
    public void updateExpectationListById(List<Expectation> expectationList, String intentId) {
        List<Expectation> expectationDBList = expectationMapper.selectExpectationByIntentId(intentId);
        if (expectationDBList == null) {
            String msg = String.format("Intent id %s doesn't exist in database.", intentId);
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        List<String> expectationDBIdList = new ArrayList<>();
        for (Expectation expectationDB : expectationDBList) {
            expectationDBIdList.add(expectationDB.getExpectationId());
        }

        for (Expectation expectation : expectationList) {
            if (expectationDBIdList.contains(expectation.getExpectationId())) {
                stateService.updateStateListByExpectationId(expectation.getStateList(), expectation.getExpectationId());
                int res = expectationMapper.updateExpectation(expectation);
                if (res < 1) {
                    String msg = "Update expectation in database failed.";
                    LOGGER.error(msg);
                    throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                }
                expectationDBIdList.remove(expectation.getExpectationId());
            } else {
                expectationService.insertExpectation(expectation, intentId);
            }
        }
        for (String expectationDBId : expectationDBIdList) {
            expectationService.deleteExpectationById(expectationDBId);
        }
        LOGGER.info("Expectations are successfully updated.");
    }

    @Override
    public void insertExpectation(Expectation expectation, String intentId) {
        int res = expectationMapper.insertExpectation(expectation, intentId);
        if (res < 1) {
            String msg = "Create expectation to database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public void deleteExpectationById(String expectationId) {
        int res = expectationMapper.deleteExpectationById(expectationId);
        if (res < 1) {
            String msg = "Delete expectation in database failed.";
            LOGGER.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
    }
}

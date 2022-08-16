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
import org.onap.usecaseui.intentanalysis.bean.models.State;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.StateMapper;
import org.onap.usecaseui.intentanalysis.service.StateService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StateServiceImpl implements StateService {

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private StateService stateService;

    @Override
    public void createStateList(List<State> stateList, String expectationId) {
        int res = stateMapper.insertStateList(stateList, expectationId);
        if (res < 1) {
            String msg = "Create state to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public List<State> getStateListByExpectationId(String expectationId) {
        List<State> stateList = stateMapper.selectStateByExpectation(expectationId);
        if (null == stateList) {
            String msg = String.format("State: Expectation id %s doesn't exist in database.", expectationId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        return stateList;
    }

    @Override
    public void deleteStateListByExpectationId(String expectationId) {
        int res = stateMapper.deleteStateByExpectationId(expectationId);
        if (res < 1) {
            String msg = "Delete state in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
    }

    @Override
    public void updateStateListByExpectationId(List<State> stateList, String expectationId) {
        List<State> stateDBList = stateMapper.selectStateByExpectation(expectationId);
        if (stateDBList == null) {
            String msg = String.format("Expectation id %s doesn't exist in database.", expectationId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        List<String> stateDBIdList = new ArrayList<>();
        for (State stateDB : stateDBList) {
            stateDBIdList.add(stateDB.getStateId());
        }
        for (State state : stateList) {
            if (stateDBIdList.contains(state.getStateId())) {
                int res = stateMapper.updateState(state);
                if (res < 1) {
                    String msg = "Update state in database failed.";
                    log.error(msg);
                    throw new DataBaseException(msg, ResponseConsts.RET_UPDATE_DATA_FAIL);
                }
                stateDBIdList.remove(state.getStateId());
            } else {
                stateService.insertState(state, expectationId);
            }
        }
        for (String stateDBId : stateDBIdList) {
            stateService.deleteStateById(stateDBId);
        }
        log.debug("States are successfully updated.");
    }

    @Override
    public void insertState(State state, String expectationId) {
        int res = stateMapper.insertState(state, expectationId);
        if (res < 1) {
            String msg = "Create state to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public void deleteStateById(String stateId) {
        int res = stateMapper.deleteStateById(stateId);
        if (res < 1) {
            String msg = "Delete state in database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_DELETE_DATA_FAIL);
        }
    }
}

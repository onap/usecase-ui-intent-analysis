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


import org.onap.usecaseui.intentanalysis.bean.models.State;
import org.onap.usecaseui.intentanalysis.mapper.StateMapper;
import org.onap.usecaseui.intentanalysis.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private static Logger LOGGER = LoggerFactory.getLogger(StateServiceImpl.class);

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private StateService stateService;

    @Override
    public void createStateList(List<State> stateList, String expectationId) {
        stateMapper.insertStateList(stateList, expectationId);
    }

    @Override
    public List<State> getStateListByExpectationId(String expectationId) {
        List<State> stateList = stateMapper.selectStateByExpectation(expectationId);
        return stateList;
    }

    @Override
    public void deleteStateListByExpectationId(String expectationId) {
        stateMapper.deleteStateByExpectationId(expectationId);
    }

    @Override
    public void updateStateListByExpectationId(List<State> stateList, String expectationId) {
        List<State> stateDBList = stateMapper.selectStateByExpectation(expectationId);
        if (stateDBList == null) {
            LOGGER.error("Expectation ID {} doesn't exist in database.", expectationId);
            throw new IllegalArgumentException("This expectation ID doesn't exist in database.");
        }
        List<String> stateDBIdList = new ArrayList<>();
        for (State stateDB : stateDBList) {
            stateDBIdList.add(stateDB.getStateId());
        }
        for (State state : stateList) {
            if (stateDBIdList.contains(state.getStateId())) {
                stateMapper.updateState(state);
                stateDBIdList.remove(state.getStateId());
            } else {
                stateService.insertState(state, expectationId);
            }
        }
        for (String stateDBId : stateDBIdList) {
            stateService.deleteStateById(stateDBId);
        }
        LOGGER.info("States are successfully updated.");
    }

    @Override
    public void insertState(State state, String expectationId) {
        stateMapper.insertState(state, expectationId);
    }

    @Override
    public void deleteStateById(String stateId) {
        stateMapper.deleteStateById(stateId);
    }
}

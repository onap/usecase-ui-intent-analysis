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


import org.onap.usecaseui.intentanalysis.bean.po.StatePo;
import org.onap.usecaseui.intentanalysis.mapper.StateMapper;
import org.onap.usecaseui.intentanalysis.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    @Autowired
    private StateMapper stateMapper;

    @Override
    public void createStateList(List<StatePo> stateList, String expectationId) {
        for (StatePo state : stateList) {
            state.setStatePoId(expectationId);
        }
         stateMapper.insertState(stateList);
    }

    @Override
    public List<StatePo> getStateListByExpectationId(String expectationId) {
        List<StatePo> stateList = stateMapper.selectStateByExpectation(expectationId);
        return stateList;
    }

    @Override
    public void deleteStateListByExpectationId(String expectationId) {
        stateMapper.deleteStateByExpectationId(expectationId);
    }

    @Override
    public void updateStateListByExpectationId(List<StatePo> statePoList, String expectationId){
    };
}

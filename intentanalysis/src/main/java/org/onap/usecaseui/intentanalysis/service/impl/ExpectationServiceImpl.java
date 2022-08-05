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
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.State;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationMapper;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExpectationServiceImpl implements ExpectationService {

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
        expectationMapper.insertExpectationList(expectationList, intentId);
    }

    @Override
    public List<Expectation> getExpectationListByIntentId(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        for (Expectation expectation : expectationList) {
            List<State> stateList = stateService.getStateListByExpectationId(expectation.getExpectationId());
            expectation.setStateList(stateList);
        }
        return expectationList;
    }

    @Override
    public void deleteExpectationListById(String intentId) {
        List<Expectation> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        expectationMapper.deleteExpectationByIntentId(intentId);
        for (Expectation expectation : expectationList) {
            stateService.deleteStateListByExpectationId(expectation.getExpectationId());
        }
    }

    @Override
    public void updateExpectationListById(List<Expectation> expectationList, String intentId) {
        List<Expectation> expectationDBList = expectationMapper.selectExpectationByIntentId(intentId);
        if (expectationDBList == null) {
            log.error("Intent ID {} doesn't exist in database.", intentId);
            throw new IllegalArgumentException("This intent ID doesn't exist in database.");
        }
        List<String> expectationDBIdList = new ArrayList<>();
        for (Expectation expectationDB : expectationDBList) {
            expectationDBIdList.add(expectationDB.getExpectationId());
        }

        for (Expectation expectation : expectationList) {
            if (expectationDBIdList.contains(expectation.getExpectationId())) {
                stateService.updateStateListByExpectationId(expectation.getStateList(), expectation.getExpectationId());
                expectationMapper.updateExpectation(expectation);
                expectationDBIdList.remove(expectation.getExpectationId());
            } else {
                expectationService.insertExpectation(expectation, intentId);
            }
        }
        for (String expectationDBId : expectationDBIdList) {
            expectationService.deleteExpectationById(expectationDBId);
        }
        log.info("Expectations are successfully updated.");
    }

    @Override
    public void insertExpectation(Expectation expectation, String intentId) {
        expectationMapper.insertExpectation(expectation, intentId);
    }

    @Override
    public void deleteExpectationById(String expectationId) {
        expectationMapper.deleteExpectationById(expectationId);
    }
}

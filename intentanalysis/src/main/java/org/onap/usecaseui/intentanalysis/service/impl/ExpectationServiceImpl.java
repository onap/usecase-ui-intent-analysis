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


import org.onap.usecaseui.intentanalysis.bean.po.ExpectationPo;
import org.onap.usecaseui.intentanalysis.bean.po.StatePo;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationMapper;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Service
public class ExpectationServiceImpl implements ExpectationService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExpectationServiceImpl.class);
    @Autowired
    private ExpectationMapper expectationMapper;

    @Autowired
    private StateService stateService;

    @Override
    public void createExpectationList(List<ExpectationPo> expectationPoList, String intentId) {
        for (ExpectationPo expectationPo : expectationPoList) {
            if (null != expectationPo) {
                expectationPo.setIntentPoId(intentId);
                stateService.createStateList(expectationPo.getStatePoList(), expectationPo.getExpectationPoId());
            }
        }
        expectationMapper.insertExpectation(expectationPoList);
    }

    @Override
    public List<ExpectationPo> getExpectationListByIntentId(String intentId) {
        List<ExpectationPo> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        for (ExpectationPo expectation : expectationList) {
            List<StatePo> stateList =  stateService.getStateListByExpectationId(expectation.getExpectationPoId());
            expectation.setStatePoList(stateList);
        }
        return expectationList;
    }

    @Override
    public void deleteExpectationListById(String intentId) {
        List<ExpectationPo> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        expectationMapper.deleteExpectationByIntentId(intentId);
        for (ExpectationPo expectation : expectationList) {
            stateService.deleteStateListByExpectationId(expectation.getExpectationPoId());
        }
    }

    @Override
    public void updateExpectationListById(List<ExpectationPo> expectationPoList, String intentId) {
        List<ExpectationPo> expectationList = expectationMapper.selectExpectationByIntentId(intentId);
        if (expectationList == null) {
            LOGGER.error("Intent ID {} doesn't exist in database.", intentId);
            throw new IllegalArgumentException("This intent ID doesn't exist in database.");
        }
        expectationMapper.updateExpectation(expectationPoList);
        LOGGER.info("Expectations are successfully updated.");
    }
}

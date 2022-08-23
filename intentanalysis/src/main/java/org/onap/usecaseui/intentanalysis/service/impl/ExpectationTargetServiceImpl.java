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

import org.onap.usecaseui.intentanalysis.bean.enums.ConditionParentType;
import org.onap.usecaseui.intentanalysis.service.ConditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;
import org.onap.usecaseui.intentanalysis.mapper.ExpectationTargetMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.ExpectationTargetService;
import org.onap.usecaseui.intentanalysis.service.FulfilmentInfoService;


@Service
public class ExpectationTargetServiceImpl implements ExpectationTargetService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExpectationTargetServiceImpl.class);

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
    public void createTarget(ExpectationTarget expectationTarget, String expectationId) {
        expectationTargetMapper.insertExpectationTarget(expectationTarget, expectationId);
        contextService.createContextList(expectationTarget.getTargetContexts(),
                                         contextParentType.EXPECTATION_TARGET,
                                         expectationTarget.getTargetId());
        fulfilmentInfoService.createFulfilmentInfo(expectationTarget.getTargetFulfilmentInfo(),
                                                   expectationTarget.getTargetId());
        conditionService.createConditionList(expectationTarget.getTargetConditions(),conditionParentType.EXPECTATION_TARGET,expectationId);
    }

    @Override
    public void createTargets(List<ExpectationTarget> expectationTargets, String expectationId) {
        for (ExpectationTarget expectationTarget : expectationTargets) {
            if (null != expectationTarget) {
                expectationTargetService.createTarget(expectationTarget, expectationId);
            }
        }
    }
}

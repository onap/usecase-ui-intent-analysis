/*
 * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
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
import org.onap.usecaseui.intentanalysis.bean.enums.ConditionParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;
import org.onap.usecaseui.intentanalysis.mapper.ConditionMapper;
import org.onap.usecaseui.intentanalysis.service.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    private ConditionMapper conditionMapper;

    @Autowired
    private ConditionService conditionService;

    @Override
    public void createConditionList(List<Condition> conditionList, ConditionParentType conditionParentType, String parentId) {
        conditionMapper.insertConditionList(conditionList);
        conditionMapper.insertConditionParentList(conditionList, conditionParentType, parentId);
    }

    @Override
    public void insertCondition(Condition condition, String parentId) {

    }

    @Override
    public void deleteConditionListByParentId(String parentId) {

    }

    @Override
    public void deleteConditionById(String conditionId) {

    }

    @Override
    public void updateConditionListByParentId(List<Condition> conditionList, String parentId) {

    }

    @Override
    public List<Condition> getConditionListByParentId(String parentId) {
        return conditionMapper.selectConditionByParentId(parentId);
    }
}

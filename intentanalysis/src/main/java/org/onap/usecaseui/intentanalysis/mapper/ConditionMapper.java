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

package org.onap.usecaseui.intentanalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.onap.usecaseui.intentanalysis.bean.enums.ConditionParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Condition;

import java.util.List;

@Mapper
public interface ConditionMapper {

    int insertConditionList(@Param(value = "conditionList") List<Condition> conditionList,
                             @Param(value = "parentId") String parentId);

    int insertCondition(@Param(value = "condition")Condition condition,
                        @Param(value = "parentId") String parentId);

    void insertConditionParentList(@Param(value = "conditionList") List<Condition> conditionList,
                                   @Param(value = "parentType") ConditionParentType conditionParentType,
                                   @Param(value = "parentId") String parentId);

    List<Condition> selectConditionList(@Param(value = "parentId") String parentId);

    Condition selectCondition(@Param(value = "conditionId") String conditionId);

    int updateCondition(@Param(value = "condition")Condition condition);

    int deleteCondition(@Param(value = "conditionId") String conditionId);

    int deleteConditionList(@Param(value = "parentId") String parentId);
}
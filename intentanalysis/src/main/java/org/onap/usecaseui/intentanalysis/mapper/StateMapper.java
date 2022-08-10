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

package org.onap.usecaseui.intentanalysis.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.State;


@Mapper

public interface StateMapper {

    int insertStateList(@Param(value = "stateList") List<State> state,
                        @Param(value = "expectationId") String expectationId);

    List<State> selectStateByExpectation(String expectationId);

    int deleteStateByExpectationId(String expectationId);

    int updateState(State state);

    int insertState(@Param(value = "state") State state, @Param(value = "expectationId") String expectationId);

    int deleteStateById(String stateId);
}

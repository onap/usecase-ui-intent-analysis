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
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;


@Mapper
public interface ExpectationTargetMapper {

    int insertExpectationTarget(@Param(value = "expectationTarget") ExpectationTarget expectationTarget,
                                 @Param(value = "expectationId") String expectationId);

    int insertExpectationTargetList(@Param(value = "expectationTargetList") List<ExpectationTarget> expectationTargetList,
                                @Param(value = "expectationId") String expectationId);

    List<ExpectationTarget> selectExpectationTargetList(
            @Param(value = "expectationId") String expectationId);

    ExpectationTarget selectExpectationTarget(@Param(value = "expectationTargetId") String expectationTargetId);

    int updateExpectationTarget(@Param(value = "expectationTarget") ExpectationTarget expectationTarget,
                                @Param(value = "expectationTargetId") String expectationTargetId);

    int deleteExpectationTarget(@Param(value = "expectationTargetId") String expectationTargetId);

    int deleteExpectationTargetList(@Param(value = "expectationId") String expectationId);
}

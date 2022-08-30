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
import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Context;


@Mapper
public interface ContextMapper {

    int insertContextList(@Param(value = "contextList") List<Context> contextList,
                          @Param(value = "parentId") String parentId);

    int insertContextParentList(@Param(value = "contextList") List<Context> contextList,
                                @Param(value = "parentId") String parentId);

    List<Context> selectContextList(@Param(value = "parentId") String parentId);

    Context selectContext(@Param(value = "contextId") String contextId);

    int insertContext(@Param(value = "context") Context context,
                      @Param(value = "parentId") String parentId);

    int updateContext(@Param(value = "context") Context context);

    int deleteContext(@Param(value = "contextId") String contextId);

    int deleteContextList(@Param(value = "parentId") String parentId);
}

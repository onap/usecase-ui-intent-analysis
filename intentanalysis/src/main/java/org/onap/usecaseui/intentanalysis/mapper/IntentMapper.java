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

import org.apache.ibatis.annotations.Mapper;
import org.onap.usecaseui.intentanalysis.bean.po.IntentPo;

import java.util.List;

@Mapper
public interface IntentMapper {

    void insertIntent(IntentPo intentPo);

    void updateIntent(IntentPo intentPo);

    IntentPo selectIntentById(String intentId);

    List<IntentPo> selectIntents();

    void deleteIntentById(String intentId);
}

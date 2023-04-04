/*
 * Copyright (C) 2023 CMCC, Inc. and others. All rights reserved.
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

import org.apache.ibatis.annotations.Param;
import org.onap.usecaseui.intentanalysis.bean.models.IntentEventRecord;

import java.util.List;

public interface IntentEventRecordMapper {
    int insertIntentRecord(@Param(value = "intentEventRecord") IntentEventRecord intentEventRecord,
                           @Param(value="parentId")String parentId);

    IntentEventRecord getIntentEventRecordByIntentId(@Param(value = "intentId") String intentId,
                                                    @Param(value = "operateType") String operateType);

    List<IntentEventRecord> getIntentEventRecordByPid(@Param(value="parentId")String parentId,
                                           @Param(value = "operateType")String operateType);
}

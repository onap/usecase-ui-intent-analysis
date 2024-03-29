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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentEventRecord;

import org.onap.usecaseui.intentanalysis.bean.models.IntentEventRecord;
import org.onap.usecaseui.intentanalysis.mapper.IntentEventRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntentEventRecordService {
    @Autowired
    IntentEventRecordMapper intentEventRecordMapper;

    // insert into record table
    public void createIntentEventRecord(IntentEventRecord intentEventRecord,String parentId){
        intentEventRecordMapper.insertIntentRecord(intentEventRecord,parentId);

    }
    // get record by intent nameid， status
    public IntentEventRecord getIntentEventRecordByIntentId(String intentId,String operateType){
       return intentEventRecordMapper.getIntentEventRecordByIntentId(intentId,operateType);
    }

    /**
     * get intentEventRecord by parentId and operateType
     * @param parentId parentId
     * @param operateType operateTypr
     * @return List<IntentEventRecord>
     */
    public List<IntentEventRecord> getRecordByPid(String parentId,String operateType){
        return intentEventRecordMapper.getIntentEventRecordByPid(parentId,operateType);
    }
}


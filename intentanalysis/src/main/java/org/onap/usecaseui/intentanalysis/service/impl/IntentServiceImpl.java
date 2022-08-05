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


import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.mapper.IntentMapper;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IntentServiceImpl implements IntentService {
    @Autowired
    private IntentMapper intentMapper;

    @Autowired
    private ExpectationService expectationService;

    @Override
    public List<Intent> getIntentList() {
        List<Intent> intentList = intentMapper.selectIntents();
        if (intentList == null || intentList.size() <= 0) {
            return new ArrayList<>();
        }
        for (Intent intent : intentList) {
            intent.setExpectationList(expectationService.getExpectationListByIntentId(intent.getIntentId()));
        }
        return intentList;
    }

    @Override
    public Intent getIntentById(String intentId) {
        Intent intent = intentMapper.selectIntentById(intentId);
        if (intent != null) {
            intent.setExpectationList(expectationService.getExpectationListByIntentId(intent.getIntentId()));
            return intent;
        } else {
            String msg = "Intent Id requested doesn't exist in the intent database";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Intent createIntent(Intent intent) {
        intentMapper.insertIntent(intent);
        // saving expectation list into expectation table
        expectationService.createExpectationList(intent.getExpectationList(), intent.getIntentId());
        log.info("Intent was successfully created.");
        return intent;
    }

    @Override
    public Intent updateIntent(Intent intent) {
        Intent intentDB = intentMapper.selectIntentById(intent.getIntentId());
        if (intentDB == null) {
            log.error("intent id {} not exists in db.", intent.getIntentId());
        }
        expectationService.updateExpectationListById(intent.getExpectationList(), intent.getIntentId());
        intentMapper.updateIntent(intent);
        log.info("update intent successfully.");
        return intentMapper.selectIntentById(intent.getIntentId());
    }

    @Override
    public void deleteIntentById(String intentId) {
        intentMapper.deleteIntentById(intentId);
        expectationService.deleteExpectationListById(intentId);
        log.info("intent has been deleted successfully.");
    }
}

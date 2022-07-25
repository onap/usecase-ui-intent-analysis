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


import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.po.IntentPo;
import org.onap.usecaseui.intentanalysis.mapper.IntentMapper;
import org.onap.usecaseui.intentanalysis.service.ExpectationService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntentServiceImpl implements IntentService {
    private static Logger LOGGER = LoggerFactory.getLogger(IntentService.class);

    @Autowired
    private IntentMapper intentMapper;

    @Autowired
    private ExpectationService expectationService;

    @Override
    public List<Intent> getIntentList() {
        List<Intent> intentList = new ArrayList<>();
        List<IntentPo> intentPoList = intentMapper.selectIntents();
        if (intentPoList == null || intentPoList.size() <= 0) {
            return intentList;
        }
        for (IntentPo intentPo : intentPoList) {
            if (intentPo != null) {
                intentPo.setExpectationPoList(expectationService.getExpectationListByIntentId(intentPo.getIntentPoId()));
                intentList.add(intentPo.transferToIntent());
            }
        }
        return intentList;
    }

    @Override
    public Intent getIntentById(String intentId) {
        IntentPo intentPo = intentMapper.selectIntentById(intentId);
        if (intentPo != null) {
            intentPo.setExpectationPoList(expectationService.getExpectationListByIntentId(intentPo.getIntentPoId()));
            return intentPo.transferToIntent();
        } else {
            String msg = "Intent Id requested doesn't exist in the intent database";
            LOGGER.error(msg);
            throw new IllegalArgumentException(msg);
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Intent createIntent(Intent intent) {
        IntentPo intentPo = intent.transferToIntentPo();
        intentMapper.insertIntent(intentPo);
        // saving expectation list into expectation table
        expectationService.createExpectationList(intentPo.getExpectationPoList(), intentPo.getIntentPoId());
        LOGGER.info("Intent was successfully created.");
        return intent;
    }

    @Override
    public Intent updateIntent(Intent intent) {
        String intentId = intent.getIntentId();
        IntentPo intentPo = intentMapper.selectIntentById(intentId);
        if (intentPo == null) {
            LOGGER.error("intent id {} not exists in db.", intentId);
        }
        intentMapper.updateIntent(intentPo);
        LOGGER.info("update intent successfully.");
        return intentMapper.selectIntentById(intentId).transferToIntent();
    }

    @Override
    public void deleteIntentById(String intentId) {
        intentMapper.deleteIntentById(intentId);
        expectationService.deleteExpectationListById(intentId);
    }
}

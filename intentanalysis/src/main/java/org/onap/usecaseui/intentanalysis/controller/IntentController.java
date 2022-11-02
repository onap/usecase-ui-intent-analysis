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

package org.onap.usecaseui.intentanalysis.controller;


import io.swagger.models.auth.In;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/intents")
public class IntentController {

    private static final String INTENT_ID = "intentId";

    @Autowired
    private IntentService intentService;

    @Autowired
    private IntentProcessService processService;
    @Autowired
    FormatIntentInputManagementFunction formatIntentInputManagementFunction;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Intent>> getIntentList() {
        return ResponseEntity.ok(intentService.getIntentList());
    }

    @GetMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Intent> getIntentById(
            @PathVariable(INTENT_ID) String intentId) {
        return ResponseEntity.ok(intentService.getIntent(intentId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Intent> createIntent(@RequestBody Intent intent) {

        processService.setIntentRole(formatIntentInputManagementFunction, null);
        //save original intent
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);
        IntentGoalBean newIntentGoalBean = processService.intentProcess(intentGoalBean);
        return ResponseEntity.ok(intentService.createIntent(newIntentGoalBean.getIntent()));
    }

    @PutMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Intent> updateIntentById(
            @PathVariable(INTENT_ID) String intentId,
            @RequestBody Intent intent) {

        processService.setIntentRole(formatIntentInputManagementFunction, null);
        //save original intent

        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.UPDATE);
        IntentGoalBean newIntentGoalBean = processService.intentProcess(intentGoalBean);
        return ResponseEntity.ok(intentService.updateIntent(newIntentGoalBean.getIntent()));
    }

    @DeleteMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeIntentById(@PathVariable(INTENT_ID) String intentId) {

        processService.setIntentRole(formatIntentInputManagementFunction, null);
        Intent intent = intentService.getIntent(intentId);
        IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.DELETE);
        processService.intentProcess(intentGoalBean);
    }

}

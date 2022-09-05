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


import java.util.List;

import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.service.IntentService;


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
        return ResponseEntity.ok(intentService.createIntent(intent));
    }

    @PutMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Intent> updateIntentById(
            @PathVariable(INTENT_ID) String intentId,
            @RequestBody Intent intent) {
        return ResponseEntity.ok(intentService.updateIntent(intent));
    }

    @DeleteMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeIntentById(@PathVariable(INTENT_ID) String intentId) {
        intentService.deleteIntent(intentId);
    }

    @PostMapping(value="/handleIntent",produces = MediaType.APPLICATION_JSON_VALUE)
    public void handleIntent(@RequestBody Intent intent) {
        processService.setIntentRole(formatIntentInputManagementFunction, null);
        processService.intentProcess(intent);
    }
}

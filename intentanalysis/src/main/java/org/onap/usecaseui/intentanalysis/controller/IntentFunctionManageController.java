/*
 *
 *  * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.onap.usecaseui.intentanalysis.controller;

import org.onap.usecaseui.intentanalysis.bean.models.IntentManagementFunctionRegInfo;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentFunctionManageService.IntentFunctionManageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/intentFunctionManage")
public class IntentFunctionManageController {
    @Resource(name = "intentFunctionManageService")
    IntentFunctionManageService intentFunctionManageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createIntentManage(@RequestBody IntentManagementFunctionRegInfo intentManage) {
        return ResponseEntity.ok(intentFunctionManageService.createFunctionManage(intentManage));
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteIntentManage(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(intentFunctionManageService.deleteFunctionManage(id));
    }

    @PutMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateIntentManageById(
            @PathVariable(value = "id") String id, @RequestBody IntentManagementFunctionRegInfo intentManage) {
        return ResponseEntity.ok(intentFunctionManageService.updateIntentManageById(id, intentManage));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IntentManagementFunctionRegInfo>> getIntentManageByID() {
        return ResponseEntity.ok(intentFunctionManageService.getIntentManage());
    }

}

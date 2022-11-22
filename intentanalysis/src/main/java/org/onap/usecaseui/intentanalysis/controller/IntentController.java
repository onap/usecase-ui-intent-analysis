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


import lombok.extern.log4j.Log4j2;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGenerateType;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.bean.models.ResultHeader;
import org.onap.usecaseui.intentanalysis.bean.models.ServiceResult;
import org.onap.usecaseui.intentanalysis.exception.CommonException;
import org.onap.usecaseui.intentanalysis.formatintentinputMgt.FormatIntentInputManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService.IntentProcessService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RESPONSE_ERROR;
import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RSEPONSE_SUCCESS;

@Log4j2
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
    public ServiceResult getIntentById(
    @PathVariable(INTENT_ID) String intentId) {
        return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "get Intent success"),
        intentService.getIntent(intentId));

    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult createIntent(@RequestBody Intent intent) {
        ResultHeader resultHeader = new ResultHeader();
        Intent returnIntent = new Intent();
        log.info("Execute create intent %s start",intent.getIntentName());
        try {
            processService.setIntentRole(formatIntentInputManagementFunction, null);
            IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.CREATE);
            IntentGoalBean newIntentGoalBean = processService.intentProcess(intentGoalBean);

            newIntentGoalBean.getIntent().setIntentGenerateType(IntentGenerateType.USERINPUT);
            returnIntent = intentService.createIntent(newIntentGoalBean.getIntent());
            resultHeader.setResult_code(RSEPONSE_SUCCESS);
            resultHeader.setResult_message("create intent success");
            log.info("Execute create intent finished");
        } catch (CommonException exception) {
            log.error("Execute create intent Exception:", exception);
            resultHeader.setResult_code(exception.getRetCode());
            resultHeader.setResult_message(exception.getMessage());
        } catch (Exception exception) {
            log.error("Execute create intent Exception:", exception);
            resultHeader.setResult_code(RESPONSE_ERROR);
            resultHeader.setResult_message(exception.getMessage());
        }
        return new ServiceResult(resultHeader, returnIntent);
    }

    @PutMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult updateIntentById(@PathVariable(INTENT_ID) String intentId,
                                          @RequestBody Intent intent) {
        log.info("Execute update intent start");
        try {
            processService.setIntentRole(formatIntentInputManagementFunction, null);
            IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.UPDATE);
            IntentGoalBean newIntentGoalBean = processService.intentProcess(intentGoalBean);
            Intent reIntent = intentService.updateIntent(newIntentGoalBean.getIntent());
            log.info("Execute update intent finished");
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "update intent success"), reIntent);
        } catch (CommonException exception) {
            log.error("Execute update intent Exception:", exception);
            return new ServiceResult(new ResultHeader(exception.getRetCode(), exception.getMessage()));
        } catch (Exception exception) {
            log.error("Execute update intent Exception:", exception);
            return new ServiceResult(new ResultHeader(RESPONSE_ERROR, exception.getMessage()));
        }
    }

    @DeleteMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult removeIntentById(@PathVariable(INTENT_ID) String intentId) {
        log.info("Execute delete intent start");
        try {
            processService.setIntentRole(formatIntentInputManagementFunction, null);
            Intent intent = intentService.getIntent(intentId);
            IntentGoalBean intentGoalBean = new IntentGoalBean(intent, IntentGoalType.DELETE);
            processService.intentProcess(intentGoalBean);
            log.info("Execute delete intent finished");
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "delete intent success"));
        } catch (CommonException exception) {
            log.error("Execute delete intent Exception:", exception);
            return new ServiceResult(new ResultHeader(exception.getRetCode(), exception.getMessage()));
        }
    }

    @GetMapping(value = {"/intentGenerateType/{intentGenerateType}"}, produces = "application/json")
    public ServiceResult getIntentListByIntentGenerateType(
            @PathVariable(value = "intentGenerateType") String intentGenerateType) {
        try {
            List<Intent> list = intentService.getIntentListByUserInput(intentGenerateType);
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "query success"), list);
        } catch (CommonException exception) {
            return new ServiceResult(new ResultHeader(exception.getRetCode(), exception.getMessage()));
        }
    }

}

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
package org.onap.usecaseui.intentanalysis.controller;

import lombok.extern.log4j.Log4j2;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.onap.usecaseui.intentanalysis.service.IntentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RSEPONSE_SUCCESS;

@Log4j2
@RestController
@RequestMapping("/intentReport")
public class IntentReportController {
    @Autowired
    private IntentReportService intentReportService;

    @GetMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult getIntentById(
            @PathVariable("intentId") String intentId) {
        IntentReport report = intentReportService.getIntentReportByIntentId(intentId);
        return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "get report success"),
                report);
    }
}

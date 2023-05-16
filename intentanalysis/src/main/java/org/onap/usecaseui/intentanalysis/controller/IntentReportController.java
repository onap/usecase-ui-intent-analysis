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
import org.onap.usecaseui.intentanalysis.bean.enums.FulfillmentStatus;
import org.onap.usecaseui.intentanalysis.bean.enums.NotFulfilledState;
import org.onap.usecaseui.intentanalysis.bean.models.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RSEPONSE_SUCCESS;

@Log4j2
@RestController
@RequestMapping("/intentReport")
public class IntentReportController {

    @GetMapping(value = "/{intentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult getIntentById(
            @PathVariable("intentId") String intentId) {
        // test connection with ui,real log
        IntentReport report = new IntentReport();
        report.setIntentReportId("12345");
        report.setIntentReference("intentReort");
        //report.setFulfillmentInfos();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        report.setReportTime(dateFormat.format(date));
        FulfillmentInfo fu1= new FulfillmentInfo();
        fu1.setFulfillmentId("fulfillmentInfo1");
        fu1.setFulfillmentStatus(FulfillmentStatus.FULFILLED);
        fu1.setObjectInstances(Arrays.asList("instance1", "instance2", "instance3"));
        FulfillmentInfo fu2= new FulfillmentInfo();
        fu2.setFulfillmentId("fulfillmentInfo2");
        fu2.setFulfillmentStatus(FulfillmentStatus.NOT_FULFILLED);
        fu2.setNotFulfilledState(NotFulfilledState.DEGRADED);
        fu2.setNotFulfilledReason("not fulfilled Reason");
        fu2.setObjectInstances(Arrays.asList("instance1", "instance2"));
        List<FulfillmentInfo> list = new ArrayList<>();
        list.add(fu1);
        list.add(fu2);
        report.setFulfillmentInfos(list);

        return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "get report success"),
                report);

    }

}

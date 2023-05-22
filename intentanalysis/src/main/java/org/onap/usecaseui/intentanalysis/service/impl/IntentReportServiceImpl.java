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

package org.onap.usecaseui.intentanalysis.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentInfo;
import org.onap.usecaseui.intentanalysis.bean.models.IntentReport;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.IntentReportFulfillmentInfoMapper;
import org.onap.usecaseui.intentanalysis.mapper.IntentReportMapper;
import org.onap.usecaseui.intentanalysis.mapper.ObjectInstanceMapper;
import org.onap.usecaseui.intentanalysis.service.FulfillmentInfoService;
import org.onap.usecaseui.intentanalysis.service.IntentReportService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class IntentReportServiceImpl implements IntentReportService {

    @Autowired
    private FulfillmentInfoService fulfillmentInfoService;

    @Autowired
    private ObjectInstanceMapper objectInstanceMapper;

    @Autowired
    private IntentReportFulfillmentInfoMapper intentReportFulfillmentInfoMapper;

    @Autowired
    private IntentReportMapper intentReportMapper;

    @Override
    public IntentReport getIntentReportByIntentId(String intentId) {
        FulfillmentInfo fulfillmentInfo = fulfillmentInfoService.getFulfillmentInfo(intentId);
        System.out.println(fulfillmentInfo);
        if (fulfillmentInfo == null) {
            log.error("get fulfillmentInfo is failed,intentId is {}", intentId);
            String msg = "get fulfillmentInfo is failed";
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        fulfillmentInfo.setFulfillmentId(intentId);
        List<String> objectInstances = objectInstanceMapper.getObjectInstances(intentId);
        if (CollectionUtils.isEmpty(objectInstances)) {
            log.error("get objectInstance is failed,intentId is {}", intentId);
            String msg = "get objectInstance is failed";
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        String uUid = CommonUtil.getUUid();
        fulfillmentInfo.setObjectInstances(objectInstances);
        IntentReport intentReport = new IntentReport();
        intentReport.setIntentReportId(uUid);
        intentReport.setIntentReference("intentReference");
        intentReport.setFulfillmentInfos(Collections.singletonList(fulfillmentInfo));
        intentReport.setReportTime(CommonUtil.getTime());

        int num = intentReportMapper.insertIntentReport(intentReport);
        if (num < 1) {
            String msg = "Failed to insert intent report to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        int fulfillmentNum = intentReportFulfillmentInfoMapper.insertIntentReportFulfillment(fulfillmentInfo, uUid);
        if (fulfillmentNum < 1) {
            String msg = "Failed to insert fulfillmentInfo to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        return intentReport;
    }
}

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
import org.onap.usecaseui.intentanalysis.bean.models.ResultHeader;
import org.onap.usecaseui.intentanalysis.bean.models.ServiceResult;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.onap.usecaseui.intentanalysis.mapper.IntentReportFulfillmentInfoMapper;
import org.onap.usecaseui.intentanalysis.mapper.IntentReportMapper;
import org.onap.usecaseui.intentanalysis.mapper.ObjectInstanceMapper;
import org.onap.usecaseui.intentanalysis.service.FulfillmentInfoService;
import org.onap.usecaseui.intentanalysis.service.IntentInstanceService;
import org.onap.usecaseui.intentanalysis.service.IntentReportService;
import org.onap.usecaseui.intentanalysis.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.onap.usecaseui.intentanalysis.common.ResponseConsts.RSEPONSE_SUCCESS;

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

    @Autowired
    private IntentInstanceService intentInstanceService;

    @Override
    @Transactional(rollbackFor = DataBaseException.class)
    public ServiceResult getIntentReportByIntentId(String intentId) {
        FulfillmentInfo fulfillmentInfo = getFulfillmentInfo(intentId);

        if (fulfillmentInfo == null) {
            return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "The intent has not fulfillmentInfo"),
                    new IntentReport());
        }
        fulfillmentInfo.setObjectInstances(getInstances(intentId));
        IntentReport intentReport = new IntentReport();
        intentReport.setIntentReportId(CommonUtil.getUUid());
        intentReport.setIntentReference(intentInstanceService.queryIntentInstanceId(intentId));
        intentReport.setFulfillmentInfos(Collections.singletonList(fulfillmentInfo));
        intentReport.setReportTime(CommonUtil.getTime());

        saveIntentReport(intentReport, fulfillmentInfo);
        return new ServiceResult(new ResultHeader(RSEPONSE_SUCCESS, "Get report success"),
                intentReport);
    }

    /**
     * Generate intention reports on a regular basis and save them in the database
     *
     * @param intentId intentId
     */
    @Override
    @Transactional(rollbackFor = DataBaseException.class)
    public void saveIntentReportByIntentId(String intentId) {
        FulfillmentInfo fulfillmentInfo = fulfillmentInfoService.getFulfillmentInfo(intentId);
        if (fulfillmentInfo == null) {
            log.error("The fulfillmentInfo is null");
            return;
        }
        IntentReport intentReport = new IntentReport();
        intentReport.setIntentReportId(CommonUtil.getUUid());
        intentReport.setIntentReference(intentInstanceService.queryIntentInstanceId(intentId));
        intentReport.setReportTime(CommonUtil.getTime());
        saveIntentReport(intentReport, fulfillmentInfo);
    }

    @Override
    public List<String> getIntentReportIds(String intentReference) {
        List<String> intentReportIds = intentReportMapper.getIntentReportIds(intentReference);
        if (CollectionUtils.isEmpty(intentReportIds)) {
            log.error("get intentReportId is empty,intentReference is {}", intentReference);
        }
        return intentReportIds;
    }

    private FulfillmentInfo getFulfillmentInfo(String intentId) {
        FulfillmentInfo fulfillmentInfo = fulfillmentInfoService.getFulfillmentInfo(intentId);
        log.info("fulfillmentInfo is {}", fulfillmentInfo);
        if (fulfillmentInfo == null) {
            log.error("get fulfillmentInfo is failed,intentId is {}", intentId);
        }
        return fulfillmentInfo;
    }

    private List<String> getInstances(String intentId) {
        List<String> objectInstances = objectInstanceMapper.getObjectInstances(intentId);
        if (CollectionUtils.isEmpty(objectInstances)) {
            log.error("get objectInstance is failed,intentId is {}", intentId);
        }
        return objectInstances.stream().distinct().collect(Collectors.toList());
    }

    private void saveIntentReport(IntentReport intentReport, FulfillmentInfo fulfillmentInfo) {
        int num = intentReportMapper.insertIntentReport(intentReport);
        if (num < 1) {
            String msg = "Failed to insert intent report to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
        int fulfillmentNum = intentReportFulfillmentInfoMapper.insertIntentReportFulfillment(fulfillmentInfo, intentReport.getIntentReportId());
        if (fulfillmentNum < 1) {
            String msg = "Failed to insert fulfillmentInfo to database.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }
}

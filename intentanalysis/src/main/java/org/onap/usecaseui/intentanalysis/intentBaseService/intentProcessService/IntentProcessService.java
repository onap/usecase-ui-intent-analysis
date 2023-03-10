/*
 * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
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
package org.onap.usecaseui.intentanalysis.intentBaseService.intentProcessService;

import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class IntentProcessService {
    @Autowired
    IntentDetectionService intentDetectionService;
    @Autowired
    IntentInvestigationService intentInvestigationService;
    @Autowired
    IntentDefinitionService intentDefinitionService;
    @Autowired
    IntentDistributionService intentDistributionService;
    @Autowired
    IntentOperationService intentOperationService;
    @Autowired
    IntentService intentService;
    private IntentManagementFunction intentOwner;
    private IntentManagementFunction intentHandler;


    public void setIntentRole(IntentManagementFunction intentOwner, IntentManagementFunction intentHandler) {
        if (intentOwner != null) {
            this.intentOwner = intentOwner;
        }
        if (intentHandler != null) {
            this.intentHandler = intentHandler;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public IntentGoalBean intentProcess(IntentGoalBean originIntentGoalBean) {

        intentDetectionService.setIntentRole(intentOwner, intentHandler);
        IntentGoalBean newIntentGoalBean = intentDetectionService.detectionProcess(originIntentGoalBean);

        //investigation process Decomposition
        intentInvestigationService.setIntentRole(intentOwner, intentHandler);
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> intentMap =
                intentInvestigationService.investigationProcess(newIntentGoalBean);

        Iterator<Map.Entry<IntentGoalBean, IntentManagementFunction>> iterator = intentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<IntentGoalBean, IntentManagementFunction> next = iterator.next();
            //definition process  save subintent
            intentDefinitionService.setIntentRole(intentOwner, next.getValue());
            //obtain newID IntentGoalBean               
            IntentGoalBean newIdIntentGoalBean = intentDefinitionService.definitionPorcess(originIntentGoalBean.getIntent(), next);

            //distribution process
            intentDistributionService.setIntentRole(intentOwner, intentHandler);
            intentDistributionService.distributionProcess(next);

            intentOperationService.setIntentRole(intentOwner, next.getValue());
            intentOperationService.operationProcess(originIntentGoalBean.getIntent(), newIdIntentGoalBean);
        }
        //delete second intent
        if (StringUtils.equals(originIntentGoalBean.getIntentGoalType().name(),IntentGoalType.DELETE.name())){
                intentService.deleteIntent(originIntentGoalBean.getIntent().getIntentId());
        }
        return newIntentGoalBean;
    }
}

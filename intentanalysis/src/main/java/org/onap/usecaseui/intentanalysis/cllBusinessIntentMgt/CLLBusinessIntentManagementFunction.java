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
package org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt;


import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.bean.enums.IntentGoalType;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentEventRecord;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.exception.CommonException;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.contextService.IntentContextService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentEventRecord.IntentEventRecordService;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentinterfaceservice.IntentInterfaceService;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.onap.usecaseui.intentanalysis.service.IntentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
@Component("CLLBusinessIntentManagementFunction")
public class CLLBusinessIntentManagementFunction extends IntentManagementFunction {

    @Resource(name = "CLLBusinessActuationModule")
    public void setActuationModule(ActuationModule actuationModule) {
        this.actuationModule = actuationModule;
    }

    @Resource(name = "CLLBusinessKnowledgeModule")
    public void setKnowledgeModule(KnowledgeModule knowledgeModule) {
        this.knowledgeModule = knowledgeModule;
    }

    @Resource(name = "CLLBusinessDecisionModule")
    public void setDecisionModule(DecisionModule decisionModule) {
        this.decisionModule = decisionModule;
    }

    @Autowired
    public IntentContextService intentContextService;
    @Autowired
    IntentInterfaceService intentInterfaceService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ContextService contextService;
    @Autowired
    IntentService intentService;
    @Autowired
    IntentEventRecordService intentEventRecordService;

    @Resource(name = "intentTaskExecutor")
    ThreadPoolTaskExecutor executor;

    @Override
    public void receiveIntentAsOwner(IntentGoalBean intentGoalBean) {
        IntentGoalBean originIntentGoalBean = detection(intentGoalBean);
        LinkedHashMap<IntentGoalBean, IntentManagementFunction> linkedMap = investigation(originIntentGoalBean);
        implementIntent(intentGoalBean.getIntent(), linkedMap);
    }

    @Override
    public void receiveIntentAsHandler(Intent originalIntent, IntentGoalBean intentGoalBean, IntentManagementFunction handler) {
        ActuationModule actuationModule = handler.getActuationModule();
        IntentGoalType type = intentGoalBean.getIntentGoalType();
        //before dataBase operate
        handler.receiveIntentAsOwner(intentGoalBean);

        if (type == IntentGoalType.CREATE) {
            actuationModule.saveIntentToDb(intentGoalBean.getIntent());
        } else if (type == IntentGoalType.UPDATE) {
            actuationModule.updateIntentToDb(intentGoalBean.getIntent());
        } else if (type == IntentGoalType.DELETE) {
            //before delete cllBusinessIntent check subintent deleted
            boolean deleteFinished = false;
            Intent cllIntent = intentGoalBean.getIntent();
            //subIntent size and delete Intent size
            List<Context> list = cllIntent.getIntentContexts().stream().filter(x ->
                    StringUtils.equals(x.getContextName(), "subIntent info")).collect(Collectors.toList());
            int subIntentSize = list.get(0).getContextConditions().size();
            while (!deleteFinished) {
                List<IntentEventRecord> deleteList = intentEventRecordService.getRecordByPid(cllIntent.getIntentId(),
                        IntentGoalType.DELETE.name());
                if (subIntentSize <= deleteList.size()) {
                    deleteFinished = true;
                }
            }
            actuationModule.deleteIntentToDb(intentGoalBean.getIntent());
        }
    }

    public IntentGoalBean detection(IntentGoalBean intentGoalBean) {
        Intent originIntent = intentGoalBean.getIntent();
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        if (intentGoalType == IntentGoalType.CREATE) {
            return knowledgeModule.intentCognition(originIntent);
        } else if (intentGoalType == IntentGoalType.UPDATE) {
            return new IntentGoalBean(intentGoalBean.getIntent(), IntentGoalType.UPDATE);
        } else {
            return new IntentGoalBean(intentGoalBean.getIntent(), IntentGoalType.DELETE);
        }
    }

    public LinkedHashMap<IntentGoalBean, IntentManagementFunction> investigation(IntentGoalBean intentGoalBean) {
        IntentGoalType intentGoalType = intentGoalBean.getIntentGoalType();
        if (intentGoalType == IntentGoalType.CREATE) {
            return decisionModule.investigationCreateProcess(intentGoalBean);
        } else if (intentGoalType == IntentGoalType.UPDATE) {
            return decisionModule.investigationUpdateProcess(intentGoalBean);
        } else {
            return decisionModule.investigationDeleteProcess(intentGoalBean);
        }
    }

    @SneakyThrows
    public boolean implementIntent(Intent originIntent, LinkedHashMap<IntentGoalBean, IntentManagementFunction> linkedIntentMap) {
        Iterator<Map.Entry<IntentGoalBean, IntentManagementFunction>> iterator = linkedIntentMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<IntentGoalBean, IntentManagementFunction> next = iterator.next();
            IntentGoalBean newIntentGoalBean = next.getKey();
            IntentGoalType intentGoalType = newIntentGoalBean.getIntentGoalType();
            IntentManagementFunction handler = next.getValue();
            if (intentGoalType == IntentGoalType.CREATE) {
                Intent newIdIntent = decisionModule.intentObjectDefine(originIntent, next.getKey().getIntent());
                intentContextService.updateIntentOwnerHandlerContext(newIdIntent, this, next.getValue());
                intentContextService.updateParentIntentContext(originIntent, newIdIntent);
                intentContextService.updateChindIntentContext(originIntent, newIdIntent);
                // contextService.updateContextList(originIntent.getIntentContexts(), originIntent.getIntentId());
                //intent-Distribution-create
                boolean isAcceptCreate = intentInterfaceService.createInterface(originIntent,
                        new IntentGoalBean(newIdIntent, IntentGoalType.CREATE), next.getValue());
                // TODO: 2023/3/27  isParallel status need deal before distribution
                boolean isParallel = false;
                if (!isParallel) {
                    //Block and regularly query whether the event is published
                    boolean isPublish = false;
                    int count = 1;
                    while (!isPublish) {
                        // Set the timeout to be 60min
                        if (count >= 3600) {
                            throw new CommonException("Timed out for implementing intent", 500);
                        }
                        log.debug("Try to get intentEventRecord");
                        IntentEventRecord record = intentEventRecordService.getIntentEventRecordByIntentId(
                                                        newIdIntent.getIntentId(), intentGoalType.toString());
                        if (record != null) {
                            isPublish = true;
                            log.debug("Successfully got Intent Event Record from DB.");
                        } else {
                            log.debug("Index " + count + ": None Intent Event Record been got. Will try again.");
                        }
                        count++;
                        Thread.sleep(1000);
                    }
                }
                // return isAcceptCreate;
            } else if (intentGoalType == IntentGoalType.UPDATE) {
                //define process  just send probe interface
                // intent-Distribution-update
                boolean isAcceptupdate = intentInterfaceService.updateInterface(originIntent,
                        next.getKey(), next.getValue());
            } else {
                // actuationModule.deleteIntentToDb(next.getKey().getIntent());
                // intent-Distribution-delete
                boolean isAcceptDelete = intentInterfaceService.deleteInterface(originIntent,
                        next.getKey(), next.getValue());
            }
        }
        return false;
    }

}

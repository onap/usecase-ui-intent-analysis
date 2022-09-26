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
package org.onap.usecaseui.intentanalysis.cllassuranceIntentmgt.cllassurancemodule;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.onap.usecaseui.intentanalysis.adapters.aai.apicall.AAIAPICall;
import org.onap.usecaseui.intentanalysis.adapters.aai.apicall.AAIAuthConfig;
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import org.onap.usecaseui.intentanalysis.bean.models.IntentGoalBean;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.util.RestfulServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;

@Log4j2
@Component
public class CLLAssuranceKnowledgeModule extends KnowledgeModule {
    private AAIAPICall aaiapiCall;
    @Autowired
    AAIAuthConfig aaiAuthConfig;

    public AAIAPICall getAaiApiCall() {
        if (null == aaiapiCall) {
            this.aaiapiCall = RestfulServices.create(AAIAPICall.class,
                    aaiAuthConfig.getUserName(), aaiAuthConfig.getPassword());
        }
        return this.aaiapiCall;
    }

    public void setAAIApiCall(AAIAPICall aaiApiCall) {
        this.aaiapiCall = aaiApiCall;
    }

    @Override
    public IntentGoalBean intentCognition(Intent intent) {
        return null;
    }

    @Override
    public boolean recieveCreateIntent() {
        return false;
    }

    @Override
    public boolean recieveUpdateIntent() {
        return false;
    }

    @Override
    public boolean recieveDeleteIntent() {
        return false;
    }

    /**
     * healthy check
     */
    int getSystemStatus(Intent intent) {
        try {
            if (CollectionUtils.isEmpty(intent.getIntentExpectations())) {
                return -1;
            }
            String objectInstance = intent.getIntentExpectations().get(0).getExpectationObject().getObjectInstance();
            if (StringUtils.isEmpty(objectInstance)){
                return -1;
            }
            Response<JSONObject> response = getAaiApiCall().getInstanceInfo(objectInstance).execute();
            log.debug(response.toString());
            if (response.isSuccessful()) {
                // TODO: 2022/9/20 judge by the return result
            }
            log.error("getIntentInstance Create Statue Error:" + response.toString());
            return -1;
        } catch (Exception ex) {
            log.error("Details:" + ex.getMessage());
            return 0;
        }
    }
}

/*
 * Copyright 2023 Huawei Technologies Co., Ltd.
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

package org.onap.usecaseui.intentanalysis.adapters.dcae.dmaap;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.onap.usecaseui.intentanalysis.adapters.dmaap.NotificationCallback;
import org.onap.usecaseui.intentanalysis.adapters.dmaap.NotificationEventModel;
import org.onap.usecaseui.intentanalysis.bean.enums.FulfillmentStatus;
import org.onap.usecaseui.intentanalysis.bean.enums.NotFulfilledState;
import org.onap.usecaseui.intentanalysis.bean.models.FulfillmentOperation;
import org.onap.usecaseui.intentanalysis.service.ComponentNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DCAENotificationCallback implements NotificationCallback {
    private static final Logger logger = LoggerFactory.getLogger(DCAENotificationCallback.class);

    private static final String IntentOperation = "Assurance";

    @Autowired
    ComponentNotificationService componentNotificationService;

    @Override
    public void activateCallBack(String msg) {
        logger.info("Received event from DCAE: \n" + msg);
        NotificationEventModel event = (new Gson()).fromJson(msg, NotificationEventModel.class);
        FulfillmentOperation info = new FulfillmentOperation();
        info.setOperation(IntentOperation);
        info.setFulfillmentStatus(FulfillmentStatus.NOT_FULFILLED);
        List<String> instances = new ArrayList<>();
        instances.add(event.getEntity().getId());
        info.setObjectInstances(instances);
        info.setNotFulfilledState(NotFulfilledState.FULFILMENTFAILED);
        info.setNotFulfilledReason(event.getEntity().getReason());
        componentNotificationService.callBack(info);
    }
}

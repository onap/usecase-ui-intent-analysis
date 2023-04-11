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
import org.onap.usecaseui.intentanalysis.adapters.dmaap.NotificationCallback;
import org.onap.usecaseui.intentanalysis.adapters.dmaap.NotificationEventModel;
import org.onap.usecaseui.intentanalysis.adapters.policy.dmaap.PolicyNotificationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DCAENotificationCallback implements NotificationCallback {
    private static final Logger logger = LoggerFactory.getLogger(DCAENotificationCallback.class);
    @Override
    public void activateCallBack(String msg) {
        logger.info("Received event from DCAE: \n" + msg);
        NotificationEventModel event = (new Gson()).fromJson(msg, NotificationEventModel.class);
        //Todo analyze the event and Report to the Intent Flow;
    }
}

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
import org.onap.usecaseui.intentanalysis.adapters.dmaap.NotificationEventModel;
import org.onap.usecaseui.intentanalysis.service.IntentNotificationService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IntentNotificationServiceImpl implements IntentNotificationService {
    /**
     * receive  DECA and Policy  event and transform to fulfillmentInfo
     * @param notificationEventModel
     */
    @Override
    public void callBack(NotificationEventModel notificationEventModel) {

        log.info("intentNotifictionService begin");
    }
}
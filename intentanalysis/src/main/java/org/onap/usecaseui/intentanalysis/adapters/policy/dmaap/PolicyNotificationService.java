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
package org.onap.usecaseui.intentanalysis.adapters.policy.dmaap;

import org.onap.usecaseui.intentanalysis.adapters.dmaap.MRTopicMonitor;
import org.springframework.stereotype.Service;

@Service
public class PolicyNotificationService {

    //config of policy dmaap event subscribe
    private static final String MONITOR_CONFIG_FILE = "policy_dmaap_config.json";

    public PolicyNotificationService(){
        init();
    }

    private void init(){
        MRTopicMonitor monitor = new MRTopicMonitor(MONITOR_CONFIG_FILE, new PolicyNotificationCallback());
       // monitor.start();
    }
}

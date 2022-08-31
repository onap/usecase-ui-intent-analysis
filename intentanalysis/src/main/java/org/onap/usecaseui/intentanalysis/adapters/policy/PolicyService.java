/*
 * Copyright 2022 Huawei Technologies Co., Ltd.
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
package org.onap.usecaseui.intentanalysis.adapters.policy;

public interface PolicyService {
    /**
     * Add modify CLL bandwidth policy
     * @return
     */
    boolean createAndDeployModifyCLLPolicy();

    /**
     * Remove modify CLL bandwidth policy
     * @return
     */
    boolean undeployAndRemoveModifyCLLPolicy();

    /**
     * Update the intent configuration policy. Configuration policy is used to control the monitor of the CLL service bandwidth
     * When closedLoopStatus set as true, DCAE will get the configuration and monitor the bandwidth of the specified CLL.
     * When closedLoopStatus set as false, DCAE will stop monitor the bandwidth of the specified CLL.
     * @param cllId The CLL service instance
     * @param originalBW original bandwidth of the CLL service, it is used for DCAE to compare the CLL current bandwidth.
     * @param closedLoopStatus  True: start  monitor, False: stop monitor
     * @return
     */
    boolean updateIntentConfigPolicy(String cllId, String originalBW, boolean closedLoopStatus);

}

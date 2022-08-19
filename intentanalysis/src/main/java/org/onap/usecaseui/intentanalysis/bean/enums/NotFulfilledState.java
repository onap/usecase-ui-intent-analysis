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

package org.onap.usecaseui.intentanalysis.bean.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum NotFulfilledState {
    ACKNOWLEDGED(0, "acknowledge"),
    COMPLIANT(1, "compliant"),
    DEGRADED(2, "degraded"),
    SUSPENDED(3, "suspended"),
    TERMINATED(4, "terminated"),
    FULFILMENTFAILED(5, "fulfilmentfailed");

    private int index;

    private String desc;

    NotFulfilledState(int index, String desc) {
        this.index = index;
        this.desc = desc;
    }
}

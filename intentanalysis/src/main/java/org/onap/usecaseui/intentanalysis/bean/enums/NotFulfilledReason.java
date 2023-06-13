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

package org.onap.usecaseui.intentanalysis.bean.enums;

import lombok.Getter;

@Getter
public enum NotFulfilledReason {
    INSUFFICIENT_BAND_WITH(0, "Insufficient bandwidth resources", "Insufficient bandwidth resources", "Insufficient bandwidth resources"),
    SERVICE_DOWNTIME(1, "Service downtime", "Service downtime", "Service downtime");

    private int index;

    private String reason;

    private String businessReason;

    private String formatReason;

    NotFulfilledReason(int index, String reason, String businessReason, String formatReason) {
        this.index = index;
        this.reason = reason;
        this.businessReason = businessReason;
        this.formatReason = formatReason;
    }
}

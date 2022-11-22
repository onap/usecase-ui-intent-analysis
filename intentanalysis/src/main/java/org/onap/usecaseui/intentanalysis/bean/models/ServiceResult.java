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
package org.onap.usecaseui.intentanalysis.bean.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceResult {

    private ResultHeader result_header;
    private Object result_body;

    public ServiceResult() {
    }

    public ServiceResult(ResultHeader result_header, Object result_body) {
        this.result_header = result_header;
        this.result_body = result_body;
    }

    public ServiceResult(ResultHeader result_header) {
        this.result_header = result_header;
    }
}

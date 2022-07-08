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

package com.onap.usecaseui.intentanalysis.bean;

import com.onap.usecaseui.intentanalysis.bean.dto.StateDto;
import lombok.Data;

@Data
public class StatePo {

    private String stateId;

    private String stateName;

    private String condition;

    private String expectationId;

    private Boolean isSatisfied;

    public StateDto transferToStateDto() {
        StateDto stateDto = new StateDto();
        stateDto.setStateId(this.stateId);
        stateDto.setStateName(this.stateName);
        stateDto.setIsSatisfied(this.isSatisfied);
        stateDto.setCondition(this.condition);
        return stateDto;
    }
}

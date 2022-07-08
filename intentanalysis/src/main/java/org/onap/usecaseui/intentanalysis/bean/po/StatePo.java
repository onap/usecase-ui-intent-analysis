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

package org.onap.usecaseui.intentanalysis.bean.po;

import org.onap.usecaseui.intentanalysis.bean.models.State;
import lombok.Data;

@Data
public class StatePo {

    private String statePoId;

    private String statePoName;

    private String condition;

    private String expectationPoId;

    private Boolean isSatisfied;

    public State transferToState() {
        State state = new State();
        state.setStateId(this.statePoId);
        state.setStateName(this.statePoName);
        state.setIsSatisfied(this.isSatisfied);
        state.setCondition(this.condition);
        return state;
    }
}

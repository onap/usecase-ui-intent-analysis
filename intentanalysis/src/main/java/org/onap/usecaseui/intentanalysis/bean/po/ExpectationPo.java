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

import org.onap.usecaseui.intentanalysis.bean.models.Expectation;
import org.onap.usecaseui.intentanalysis.bean.models.State;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExpectationPo {

    private String expectationPoId;

    private String expectationPoName;

    private String targetMOI;

    private String intentPoId;

    List<StatePo> statePoList;

    public Expectation transferToExpectation() {
        Expectation expectation = new Expectation();
        expectation.setExpectationId(this.expectationPoId);
        expectation.setExpectationName(this.expectationPoName);
        expectation.setTargetMOI(this.targetMOI);
        expectation.setStateList(getStateList());
        return expectation;
    }

    private List<State> getStateList() {
        List<State> stateList = new ArrayList<>();
        if (null == this.statePoList) {
            return stateList;
        }
        for (StatePo statePo : this.statePoList) {
            stateList.add(statePo.transferToState());
        }
        return stateList;
    }
}

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
package org.onap.usecaseui.intentanalysis.bean.models;

import org.onap.usecaseui.intentanalysis.bean.po.ExpectationPo;
import org.onap.usecaseui.intentanalysis.bean.po.StatePo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class Expectation {
    private String expectationId;

    private String expectationName;

    private String targetMOI;

    List<State> stateList;

    public ExpectationPo transferToExpectationPo() {
        ExpectationPo expectationPo = new ExpectationPo();
        expectationPo.setExpectationPoId(this.expectationId);
        expectationPo.setExpectationPoName(this.expectationName);
        expectationPo.setTargetMOI(this.targetMOI);
        expectationPo.setStatePoList(getStatePoList());
        return expectationPo;
    }

    private List<StatePo> getStatePoList() {
        List<StatePo> statePoList = new ArrayList<>();
        if (null == this.stateList) {
            return statePoList;
        }
        for (State state : this.stateList) {
            statePoList.add(state.transferToStatePo());
        }
        return statePoList;
    }
}

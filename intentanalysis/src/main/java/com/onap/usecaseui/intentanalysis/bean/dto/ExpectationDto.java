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
package com.onap.usecaseui.intentanalysis.bean.dto;

import com.onap.usecaseui.intentanalysis.bean.ExpectationPo;
import com.onap.usecaseui.intentanalysis.bean.StatePo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data

public class ExpectationDto {
    private String expectationId;

    private String expectationName;

    private String targetMOI;

    List<StateDto> stateDtoList;

    public ExpectationPo transferToExpectationPo() {
        ExpectationPo expectation = new ExpectationPo();
        expectation.setExpectationId(this.expectationId);
        expectation.setExpectationName(this.expectationName);
        expectation.setTargetMOI(this.targetMOI);
        expectation.setStatePoList(getStatePoList());
        return expectation;
    }

    List<StatePo> getStatePoList() {
        List<StatePo> statePoList = new ArrayList<>();
        for (StateDto stateDto: this.stateDtoList) {
            statePoList.add(stateDto.transferToStatePo());
        }
        return statePoList;
    }
}

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
package org.onap.usecaseui.intentanalysis.bean;

import org.onap.usecaseui.intentanalysis.bean.dto.ExpectationDto;
import org.onap.usecaseui.intentanalysis.bean.dto.StateDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExpectationPo {

    private String expectationId;

    private String expectationName;

    private String targetMOI;

    private String intentId;

    List<StatePo> statePoList;

    public ExpectationDto transferToExpectationDto() {
        ExpectationDto expectationDto = new ExpectationDto();
        expectationDto.setExpectationId(this.expectationId);
        expectationDto.setExpectationName(this.expectationName);
        expectationDto.setTargetMOI(this.targetMOI);
        expectationDto.setStateDtoList(getStateDtoList());
        return expectationDto;
    }

    private List<StateDto> getStateDtoList() {
        List<StateDto> stateDtoList = new ArrayList<>();
        for (StatePo statePo: this.statePoList) {
            stateDtoList.add(statePo.transferToStateDto());
        }
        return stateDtoList;
    }
}

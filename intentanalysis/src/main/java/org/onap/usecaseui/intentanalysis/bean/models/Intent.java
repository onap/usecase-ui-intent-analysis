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
import org.onap.usecaseui.intentanalysis.bean.po.IntentPo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class Intent {
    private String intentId;

    private String intentName;

    private List<Expectation> expectationList;

    public IntentPo transferToIntentPo() {
        IntentPo intentPo = new IntentPo();
        intentPo.setIntentPoId(this.intentId);
        intentPo.setIntentPoName(this.intentName);
        intentPo.setExpectationPoList(getExpectationPoList());
        return intentPo;
    }

    private List<ExpectationPo> getExpectationPoList() {
        List<ExpectationPo> expectationPoList = new ArrayList<>();
        if (null == this.expectationList) {
            return expectationPoList;
        }
        for (Expectation expectation : this.expectationList) {
            expectationPoList.add(expectation.transferToExpectationPo());
        }
        return expectationPoList;
    }
}

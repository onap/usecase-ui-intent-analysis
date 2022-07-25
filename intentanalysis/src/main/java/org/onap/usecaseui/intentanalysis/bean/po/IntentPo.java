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
import org.onap.usecaseui.intentanalysis.bean.models.Intent;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IntentPo {

    private String intentPoId;

    private String intentPoName;

    private List<ExpectationPo> expectationPoList;

    public Intent transferToIntent() {
        Intent intent = new Intent();
        intent.setIntentId(this.intentPoId);
        intent.setIntentName(this.intentPoName);

        intent.setExpectationList(getExpectationList());
        return intent;
    }

    private List<Expectation> getExpectationList() {
        List<Expectation> expectationList = new ArrayList<>();
        if (null == this.expectationPoList ) {
            return expectationList;
        }
        for (ExpectationPo expectationPo : this.expectationPoList) {
            expectationList.add(expectationPo.transferToExpectation());
        }
        return expectationList;
    }
}

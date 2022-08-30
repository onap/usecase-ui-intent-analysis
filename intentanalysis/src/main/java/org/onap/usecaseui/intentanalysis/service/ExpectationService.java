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

package org.onap.usecaseui.intentanalysis.service;


import java.util.List;
import org.onap.usecaseui.intentanalysis.bean.models.Expectation;


public interface ExpectationService {

    void createIntentExpectationList(List<Expectation> intentExpectationList, String intentId);

    void createIntentExpectation(Expectation expectation, String intentId);

    void deleteIntentExpectationList(String intentId);

    void deleteIntentExpectation(String expectationId);

    void updateIntentExpectationList(List<Expectation> intentExpectationList, String intentId);

    List<Expectation> getIntentExpectationList(String intentId);

    Expectation getIntentExpectation(String expectationId);
}

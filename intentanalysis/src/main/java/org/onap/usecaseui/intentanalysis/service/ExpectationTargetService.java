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
import org.onap.usecaseui.intentanalysis.bean.models.ExpectationTarget;


public interface ExpectationTargetService {

    void createExpectationTarget(ExpectationTarget expectationTarget, String expectationId);

    void createExpectationTargetList(List<ExpectationTarget> expectationTargetList, String expectationId);

    List<ExpectationTarget> getExpectationTargetList(String expectationId);

    ExpectationTarget getExpectationTarget(String expectationTargetId);

    void updateExpectationTargetList(List<ExpectationTarget> expectationTargetList, String expectationId);

    void deleteExpectationTarget(String expectationTargetId);

    void deleteExpectationTargetList(String expectationId);
}

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
import org.onap.usecaseui.intentanalysis.bean.models.Intent;


public interface IntentService {
    List<Intent> getIntentList();

    Intent getIntent(String intentId);

    Intent createIntent(Intent intent);

    Intent updateIntent(Intent intent);

    void deleteIntent(String intentId);

    List<Intent> getIntentByName(String name);

    List<String> getSubIntentList(Intent intent);

    List<Intent> getIntentListByUserInput(String intentGenerateType);

    String findParentByIntentId(String intentId);
}

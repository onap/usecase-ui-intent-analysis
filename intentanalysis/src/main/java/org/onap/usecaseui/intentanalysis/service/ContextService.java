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

import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Context;


public interface ContextService {

    void createContextList(List<Context> contextList, ContextParentType contextParentType, String parentId);

    void insertContext(Context context, String parentId);

    void deleteContextListByParentId(String parentId);

    void deleteContextById(String contextId);

    void updateContextListByParentId(List<Context> contextList, String parentId);

    List<Context> getContextListByParentId(String parentId);
}

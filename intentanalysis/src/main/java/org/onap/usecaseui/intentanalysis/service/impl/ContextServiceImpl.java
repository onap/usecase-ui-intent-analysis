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

package org.onap.usecaseui.intentanalysis.service.impl;


import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.onap.usecaseui.intentanalysis.bean.enums.ContextParentType;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.mapper.ContextMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class ContextServiceImpl implements ContextService {

    private static Logger LOGGER = LoggerFactory.getLogger(ContextServiceImpl.class);

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private ContextService contextService;

    @Override
    public void createContextList(List<Context> contextList, ContextParentType contextParentType, String parentId) {
        contextMapper.insertContextList(contextList);
        contextMapper.insertContextParentList(contextList, contextParentType, parentId);
    }

    @Override
    public void insertContext(Context context, String parentId) {
    }

    @Override
    public void deleteContextListByParentId(String parentId) {
    }

    @Override
    public void deleteContextById(String contextId) {
    }

    @Override
    public void updateContextListByParentId(List<Context> contextList, String parentId) {
    }

    @Override
    public List<Context> getContextListByParentId(String parentId) {
        return contextMapper.selectContextByParentId(parentId);
    }
}

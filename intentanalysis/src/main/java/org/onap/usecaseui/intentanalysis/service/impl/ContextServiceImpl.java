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
import lombok.extern.slf4j.Slf4j;
import org.onap.usecaseui.intentanalysis.common.ResponseConsts;
import org.onap.usecaseui.intentanalysis.exception.DataBaseException;
import org.springframework.stereotype.Service;
import org.onap.usecaseui.intentanalysis.bean.models.Context;
import org.onap.usecaseui.intentanalysis.mapper.ContextMapper;
import org.onap.usecaseui.intentanalysis.service.ContextService;
import org.springframework.beans.factory.annotation.Autowired;


@Service
@Slf4j
public class ContextServiceImpl implements ContextService {

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private ContextService contextService;

    @Override
    public void createContextList(List<Context> contextList, String parentId) {
        if (contextMapper.insertContextList(contextList, parentId) < 1) {
            String msg = "Create context list to database failed.";
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_INSERT_DATA_FAIL);
        }
    }

    @Override
    public void createContext(Context context, String parentId) {
    }

    @Override
    public void deleteContextById(String contextId) {
    }

    @Override
    public void updateContextListByParentId(List<Context> contextList, String parentId) {
    }

    @Override
    public List<Context> getContextListByParentId(String parentId) {
        List<Context> contextList = contextMapper.selectContextByParentId(parentId);
        if (contextList == null) {
            String msg = String.format("Context: Intent id %s doesn't exist in database.", parentId);
            log.error(msg);
            throw new DataBaseException(msg, ResponseConsts.RET_QUERY_DATA_EMPTY);
        }
        return contextList;
    }
}

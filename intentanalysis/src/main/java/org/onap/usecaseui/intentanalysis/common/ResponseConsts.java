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
package org.onap.usecaseui.intentanalysis.common;

public final class ResponseConsts {
    private ResponseConsts() {
        throw new IllegalStateException("ResponseConsts class");
    }

    /**
     * insert data to database failed.
     */
    public static final int RET_INSERT_DATA_FAIL = 10001;

    /**
     * update data from database failed.
     */
    public static final int RET_UPDATE_DATA_FAIL = 10002;

    /**
     * query data from database is empty.
     */
    public static final int RET_QUERY_DATA_EMPTY = 10003;

    /**
     * delete data from database failed.
     */
    public static final int RET_DELETE_DATA_FAIL = 10004;

    /**
     * obtain the corresponding IntentManagementFunction failed
     */
    public static final int RET_FIND_CORRESPONDING_FAIL  = 10005;
    /**
     * response success
     */
    public static  final int RSEPONSE_SUCCESS = 200;

    /**
     * response error
     */
    public static  final int RESPONSE_ERROR = 500;

    /**
     * empty param
     */
    public static final int EMPTY_PARAM = 10006;
}

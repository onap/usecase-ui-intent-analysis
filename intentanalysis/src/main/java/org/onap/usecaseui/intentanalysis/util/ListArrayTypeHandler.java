/*
 * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
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
package org.onap.usecaseui.intentanalysis.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MappedJdbcTypes({ JdbcType.ARRAY })
@MappedTypes({ Object.class })
public class ListArrayTypeHandler extends BaseTypeHandler<List<?>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<?> parameter, JdbcType jdbcType) throws SQLException {
        //  JDBC type is required
        Array array = ps.getConnection().createArrayOf("TEXT", parameter.toArray());
        try {
            ps.setArray(i, array);
        } finally {
            array.free();
        }
    }

    @Override
    public List<?> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return extractArray(rs.getArray(columnName));
    }

    @Override
    public List<?> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return extractArray(rs.getArray(columnIndex));
    }

    @Override
    public List<?> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return extractArray(cs.getArray(columnIndex));
    }

    protected List<?> extractArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        }
        Object javaArray = array.getArray();
        array.free();
        return new ArrayList<>(Arrays.asList((Object[])javaArray));
    }
}
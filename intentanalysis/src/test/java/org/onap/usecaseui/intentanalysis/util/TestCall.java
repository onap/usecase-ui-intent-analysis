/*
 * Copyright 2022 Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.usecaseui.intentanalysis.util;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestCall<T> implements Call<T> {

    private int code;

    private T result;

    private String errorMsg;

    private TestCall(int code, T result, String errorMsg) {
        this.code = code;
        this.result = result;
        this.errorMsg = errorMsg;
    }

    @Override
    public Response<T> execute() throws IOException {
        if (code >= 200 && code < 300) {
            return Response.success(code, result);
        }
        if (code >= 400 && code < 500) {
            return Response.error(code, ResponseBody.create(MediaType.parse("application/json"), errorMsg));
        }
        throw new IOException("Exception happens");
    }

    public static <T> TestCall<T> successfulCall(T result) {
        return new TestCall<>(200, result, null);
    }

    public static <T> TestCall<T> failedCall(String errorMsg) {
        return new TestCall<>(400, null, errorMsg);
    }

    public static <T> TestCall<T> exceptionCall() {
        return new TestCall<>(-1, null, null);
    }

    @Override
    public void enqueue(Callback<T> callback) {

    }

    @Override
    public boolean isExecuted() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public Call<T> clone() {
        return null;
    }

    @Override
    public Request request() {
        return null;
    }

}

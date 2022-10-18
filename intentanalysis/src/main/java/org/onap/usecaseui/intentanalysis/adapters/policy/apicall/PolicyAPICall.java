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

package org.onap.usecaseui.intentanalysis.adapters.policy.apicall;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PolicyAPICall {

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @POST("/policy/api/v1/policytypes")
    Call<ResponseBody> createPolicyType(@Body RequestBody body);

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @POST("/policy/api/v1/policytypes/{policyType}/versions/{policyVersion}/policies")
    Call<ResponseBody> createPolicy(@Path("policyType") String policyType,
        @Path("policyVersion") String policyVersion, @Body RequestBody body);

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @POST("/policy/pap/v1/pdps/policies")
    Call<ResponseBody> deployPolicy(@Body RequestBody body);

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @DELETE("/policy/pap/v1/pdps/policies/{policyName}")
    Call<ResponseBody> undeployPolicy(@Path("policyName") String policyName);

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @DELETE("/policy/api/v1/policies/{policyName}/versions/{policyVersion}")
    Call<ResponseBody> removePolicy(@Path("policyName") String policyName, @Path("policyVersion") String policyVersion);

    @Headers({
        "Accept: application/json",
        "Content-Type: application/json"
    })
    @GET(
        "/policy/api/v1/policytypes/{policyType}/versions/{policyTypeVersion}/policies/{policyName}/versions/{policyVersion}")
    Call<ResponseBody> getPolicy(@Path("policyType") String policyType,
        @Path("policyTypeVersion") String policyTypeVersion, @Path("policyName") String policyName,
        @Path("policyVersion") String policyVersion);

}

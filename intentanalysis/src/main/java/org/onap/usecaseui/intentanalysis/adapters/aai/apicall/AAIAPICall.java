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
package org.onap.usecaseui.intentanalysis.adapters.aai.apicall;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AAIAPICall {
    @GET("/aai/v24/network/network-routes")
    Call<JSONObject> queryNetworkRoute();

    @Headers({
            "X-TransactionId: 9999",
            "X-FromAppId: MSO",
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("/aai/v24/business/customers/customer/IBNCustomer/service-subscriptions/service-subscription/IBN/service-instances/service-instance/{resource-service-id}")
    Call<JSONObject> getInstanceInfo(@Path("resource-service-id") String resourceServiceId);
}

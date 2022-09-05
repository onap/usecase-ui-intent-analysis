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
import javax.servlet.http.HttpServletRequest;

import okhttp3.RequestBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAPICall;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class RestFulServicesTest {

    @Test
    public void testCreateSuccess() {
        PolicyAPICall call = RestfulServices.create("https://localhost/testurl/", PolicyAPICall.class);
        Assert.assertNotNull(call);
    }

    @Test
    public void testCreateWithAuthSuccess() {
        PolicyAPICall call = RestfulServices.create(PolicyAPICall.class, "testUser", "testPwd");
        Assert.assertNotNull(call);
    }

    @Test
    public void testGetMSBAddressSuccess() {
        String msbAddress = RestfulServices.getMSBIAGAddress();
        Assert.assertNotNull(msbAddress);
    }

    @Test
    public void testExtractBodySuccess() throws IOException {
        HttpServletRequest request = new MockHttpServletRequest();
        RequestBody requestBody = RestfulServices.extractBody(request);
        Assert.assertNotNull(requestBody);
    }
}

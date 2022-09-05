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

package org.onap.usecaseui.intentanalysis.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import okhttp3.RequestBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAPICall;
import org.onap.usecaseui.intentanalysis.test.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.RestfulServices;
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

/*
 *
 *  * Copyright (C) 2022 CMCC, Inc. and others. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.onap.usecaseui.intentanalysis.adapters.so;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.adapters.aai.apicall.AAIAPICall;
import org.onap.usecaseui.intentanalysis.adapters.so.apicall.SOAPICall;
import org.onap.usecaseui.intentanalysis.adapters.so.impl.SOServiceImpl;
import org.onap.usecaseui.intentanalysis.bean.models.CCVPNInstance;
import org.onap.usecaseui.intentanalysis.util.TestCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class SOServiceTest {

    private CCVPNInstance ccvpnInstance;

    @Autowired
    private SOServiceImpl soService;

    private SOAPICall soapiCall;

    private AAIAPICall aaiapiCall;

    @Before
    public void init(){
        soService = new SOServiceImpl();
        ccvpnInstance = new CCVPNInstance();
        soapiCall = mock(SOAPICall.class);
        soService.setSoApiCall(soapiCall);
        aaiapiCall = mock(AAIAPICall.class);
        soService.setAAIApiCall(aaiapiCall);
    }

    @Test
    public void testCreateCCVPNInstanceFailedCCVPNInstanceIsNull() throws IOException {
        ccvpnInstance = null;
        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCreateCCVPNInstanceFailedException() throws IOException {
        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCreateCCVPNInstanceFailedJobIdNull() throws IOException {
        JSONObject mockedSuccessJSONObject = mock(JSONObject.class);
        when(soapiCall.createIntentInstance(any())).thenReturn(TestCall.successfulCall(mockedSuccessJSONObject));

        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDeleteIntentInstanceFailed() throws IOException {
        int result = soService.deleteIntentInstance(anyString());
        Assert.assertEquals(0, result);
    }

    @Test
    public void testDeleteIntentInstanceSuccess() throws IOException {
        JSONObject mockedSuccessJSONObject = mock(JSONObject.class);
        when(soapiCall.deleteIntentInstance(any())).thenReturn(TestCall.successfulCall(mockedSuccessJSONObject));

        int result = soService.deleteIntentInstance("testId");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCreateCCVPNInstanceGetCreateStatusFailed() throws IOException {
        JSONObject mockedSuccessJSONObject = mock(JSONObject.class);
        when(soapiCall.createIntentInstance(any())).thenReturn(TestCall.successfulCall(mockedSuccessJSONObject));
        when(soapiCall.createIntentInstance(any()).execute().body().getString(anyString())).thenReturn("testJobId");
        when(aaiapiCall.getInstanceInfo(anyString())).thenReturn(TestCall.successfulCall(mockedSuccessJSONObject));

        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCreateCCVPNInstanceSuccess() throws IOException {
        JSONObject mockedSuccessJSONObject = mock(JSONObject.class);
        when(soapiCall.createIntentInstance(any())).thenReturn(TestCall.successfulCall(mockedSuccessJSONObject));
        when(soapiCall.createIntentInstance(any()).execute().body().getString(anyString())).thenReturn("testJobId");
        when(aaiapiCall.getInstanceInfo(anyString())).thenReturn(TestCall.successfulCall(mockedSuccessJSONObject));
        when(aaiapiCall.getInstanceInfo(anyString()).execute().body().getString(anyString())).thenReturn("created");

        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(1, result);
    }
}

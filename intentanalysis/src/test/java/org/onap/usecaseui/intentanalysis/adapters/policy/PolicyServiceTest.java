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

package org.onap.usecaseui.intentanalysis.adapters.policy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.IOException;
import mockit.MockUp;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAPICall;
import org.onap.usecaseui.intentanalysis.adapters.policy.impl.PolicyServiceImpl;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.util.TestCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class PolicyServiceTest {

    private static final int CREATE_POLICY_TYPE_FAILED = 0x01;

    private static final int CREATE_POLICY_FAILED = 0x02;

    private static final int DEPLOY_POLICY_FAILED = 0x04;

    private static final int UNDEPLOY_POLICY_FAILED = 0x08;

    private static final int DELETE_POLICY_FAILED = 0x10;

    private static final int QUERY_POLICY_NOT_EXIST = 0x20;

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyServiceTest.class);

    @Autowired
    private PolicyServiceImpl policyService;

    private PolicyAPICall policyAPICall;

    private MockUp mockup;

    @Test
    public void testCreateAndDeployCLLPolicySuccess() throws IOException {
        mockUpPolicyApiCall(0);
        boolean result = policyService.createAndDeployModifyCLLPolicy();
        Assert.assertTrue(result);
    }


    @Test
    public void testCreateAndDeployCLLPolicyFailedCreateFailed() throws IOException {
        mockUpPolicyApiCall(CREATE_POLICY_FAILED);
        boolean result = policyService.createAndDeployModifyCLLPolicy();
        Assert.assertFalse(result);
    }

    @Test
    public void testCreateAndDeployCLLPolicyFailedDeployFailed() throws IOException {
        mockUpPolicyApiCall(DEPLOY_POLICY_FAILED);
        boolean result = policyService.createAndDeployModifyCLLPolicy();
        Assert.assertFalse(result);
    }

    @Test
    public void testUndeployAndRemoveCLLPolicySuccess() throws IOException {
        mockUpPolicyApiCall(0);
        boolean result = policyService.undeployAndRemoveModifyCLLPolicy();
        Assert.assertTrue(result);
    }

    @Test
    public void testUndeployAndRemoveCLLPolicySuccessPolicyNotExist() throws IOException {
        mockUpPolicyApiCall(QUERY_POLICY_NOT_EXIST);
        boolean result = policyService.undeployAndRemoveModifyCLLPolicy();
        Assert.assertTrue(result);
    }

    @Test
    public void testUpdateIntentConfigPolicySuccess() throws IOException {
        mockUpPolicyApiCall(0);
        boolean result = policyService.updateIntentConfigPolicy("testCLLID", "1000", "true");
        Assert.assertTrue(result);
    }

    @Test
    public void testUpdateIntentConfigPolicySuccessPolicyNotExist(){
        mockUpPolicyApiCall(QUERY_POLICY_NOT_EXIST);
        boolean result = policyService.updateIntentConfigPolicy("testCLLID", "1000", "true");
        Assert.assertTrue(result);
    }

    @Test
    public void testUpdateIntentConfigPolicyFailedCreatePolicyTypeFailed(){
        mockUpPolicyApiCall(CREATE_POLICY_TYPE_FAILED);
        boolean result = policyService.updateIntentConfigPolicy("testCLLID", "1000", "true");
        Assert.assertFalse(result);
    }

    @Test
    public void testUpdateIntentConfigPolicyFailedCreatePolicyFailed(){
        mockUpPolicyApiCall(CREATE_POLICY_FAILED);
        boolean result = policyService.updateIntentConfigPolicy("testCLLID", "1000", "true");
        Assert.assertFalse(result);
    }

    @Test
    public void testUpdateIntentConfigPolicyFailedDeployPolicyFailed(){
        mockUpPolicyApiCall(DEPLOY_POLICY_FAILED);
        boolean result = policyService.updateIntentConfigPolicy("testCLLID", "1000", "true");
        Assert.assertFalse(result);
    }

    private void mockUpPolicyApiCall(int failedFlag) {
        policyAPICall = mock(PolicyAPICall.class);
        policyService.setPolicyAPICall(policyAPICall);
        ResponseBody mockedSuccessResponse = ResponseBody.create(MediaType.parse("application/json"),
            "Test Success Result");
        if ((CREATE_POLICY_FAILED & failedFlag) > 0) {
            when(policyAPICall.createPolicy(anyString(), anyString(), any())).thenReturn(
                TestCall.failedCall("Create policy failed"));
        } else {
            when(policyAPICall.createPolicy(anyString(), anyString(), any())).thenReturn(
                TestCall.successfulCall(mockedSuccessResponse));
        }
        if ((CREATE_POLICY_TYPE_FAILED & failedFlag) > 0) {
            when(policyAPICall.createPolicyType(any())).thenReturn(TestCall.failedCall("Create policy type failed"));
        } else {
            when(policyAPICall.createPolicyType(any())).thenReturn(TestCall.successfulCall(mockedSuccessResponse));
        }

        if ((DEPLOY_POLICY_FAILED & failedFlag) > 0) {
            when(policyAPICall.deployPolicy(any())).thenReturn(TestCall.failedCall("Deploy policy failed"));
        } else {
            when(policyAPICall.deployPolicy(any())).thenReturn(TestCall.successfulCall(mockedSuccessResponse));
        }

        if ((UNDEPLOY_POLICY_FAILED & failedFlag) > 0) {
            when(policyAPICall.undeployPolicy(anyString())).thenReturn(TestCall.failedCall("Undeploy policy failed"));
        } else {
            when(policyAPICall.undeployPolicy(anyString())).thenReturn(TestCall.successfulCall(mockedSuccessResponse));
        }

        if ((DELETE_POLICY_FAILED & failedFlag) > 0) {
            when(policyAPICall.removePolicy(anyString(), anyString())).thenReturn(
                TestCall.failedCall("Delete policy failed"));
        } else {
            when(policyAPICall.removePolicy(anyString(), anyString())).thenReturn(
                TestCall.successfulCall(mockedSuccessResponse));
        }

        if ((QUERY_POLICY_NOT_EXIST & failedFlag) > 0) {
            when(policyAPICall.getPolicy(anyString(), anyString(), anyString(), anyString())).thenReturn(
                TestCall.failedCall("Get policy failed"));
        } else {
            when(policyAPICall.getPolicy(anyString(), anyString(), anyString(), anyString())).thenReturn(
                TestCall.successfulCall(mockedSuccessResponse));
        }
    }
}

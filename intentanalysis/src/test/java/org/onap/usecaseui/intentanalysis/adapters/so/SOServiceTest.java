package org.onap.usecaseui.intentanalysis.adapters.so;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.adapters.aai.apicall.AAIAPICall;
import org.onap.usecaseui.intentanalysis.adapters.policy.apicall.PolicyAPICall;
import org.onap.usecaseui.intentanalysis.adapters.so.apicall.SOAPICall;
import org.onap.usecaseui.intentanalysis.adapters.so.impl.SOServiceImpl;
import org.onap.usecaseui.intentanalysis.bean.models.CCVPNInstance;
import org.onap.usecaseui.intentanalysis.util.TestCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Response;
import org.mockito.MockitoAnnotations;

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

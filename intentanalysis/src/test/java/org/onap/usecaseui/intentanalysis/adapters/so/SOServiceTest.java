package org.onap.usecaseui.intentanalysis.adapters.so;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.usecaseui.intentanalysis.IntentAnalysisApplicationTests;
import org.onap.usecaseui.intentanalysis.adapters.so.impl.SOServiceImpl;
import org.onap.usecaseui.intentanalysis.bean.models.CCVPNInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = IntentAnalysisApplicationTests.class)
@RunWith(SpringRunner.class)
public class SOServiceTest {

    private CCVPNInstance ccvpnInstance;

    private SOServiceImpl soService;

    @Test
    public void testCreateCCVPNInstanceFailedCCVPNInstanceIsNull() throws IOException {
        soService = new SOServiceImpl();
        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCreateCCVPNInstanceFailedException() throws IOException {
        soService = new SOServiceImpl();
        ccvpnInstance = new CCVPNInstance();

        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCreateCCVPNInstanceSuccess() throws IOException {
        soService = mock(SOServiceImpl.class);
        when(soService.createCCVPNInstance(ccvpnInstance)).thenReturn(1);

        int result = soService.createCCVPNInstance(ccvpnInstance);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testDeleteIntentInstanceSuccess() throws IOException {
        soService = mock(SOServiceImpl.class);
        when(soService.deleteIntentInstance(anyString())).thenReturn(1);

        int result = soService.deleteIntentInstance(anyString());
        Assert.assertEquals(1, result);
    }

    @Test
    public void testDeleteIntentInstanceFailed() throws IOException {
        soService = mock(SOServiceImpl.class);
        when(soService.deleteIntentInstance(anyString())).thenReturn(0);

        int result = soService.deleteIntentInstance(anyString());
        Assert.assertEquals(0, result);
    }

}

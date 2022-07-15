package org.onap.usecaseui.intentanalysis.CLLBusinessService.intentModuleImpl;


import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentProcessService.IntentAnalysisFunction;
import org.onap.usecaseui.intentanalysis.intentProcessService.IntentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActuationModuleImpl implements ActuationModule {
    @Autowired
    IntentProcessService processService;

    @Override
    public void sendToIntentHandler(IntentAnalysisFunction intentHandler) {
        processService.setIntentRole(intentHandler, null);
        processService.intentProcess();
    }

    @Override
    public void sendToNonIntentHandler() {
    }

    @Override
    public void interactWithIntentHandle() {

    }

    @Override
    public void saveIntentToDb() {
    }
}

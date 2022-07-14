package org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl;


import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentProcessService.Function;
import org.onap.usecaseui.intentanalysis.intentProcessService.IntentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActuationModuleImpl implements ActuationModule {
    @Autowired
    IntentProcessService processService;

    @Override
    public void sendToIntentHandler(Function intentHandler) {
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

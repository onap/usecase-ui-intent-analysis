package org.onap.usecaseui.intentanalysis.intentProcessService;


import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntentDistributionService {
    @Autowired
    private IntentAnalysisFunction intentHandler;
    private IntentAnalysisFunction intentOwner;

    public void setIntentRole(IntentAnalysisFunction intentOwner, IntentAnalysisFunction intentHandler){
        if (intentOwner!= null){
            this.intentOwner = intentOwner;
        }
        if (intentHandler!= null){
            this.intentHandler= intentHandler;
        }
    }

    public void distributionProcess() {
        ActuationModule intentActuationModule = intentHandler.getActuationModule();

        intentActuationModule.sendToIntentHandler(intentHandler);
    }

}

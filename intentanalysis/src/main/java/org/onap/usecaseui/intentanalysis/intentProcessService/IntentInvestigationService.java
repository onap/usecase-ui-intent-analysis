package org.onap.usecaseui.intentanalysis.intentProcessService;

import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.springframework.stereotype.Service;

@Service
public class IntentInvestigationService {
    private Function intentHandler;
    private Function intentOwner;

    public void setIntentRole(Function intentOwner,Function intentHandler){
        if (intentOwner!= null){
            this.intentOwner = intentOwner;
        }
        if (intentHandler!= null){
            this.intentHandler= intentHandler;
        }
    }

    public void investigationProcess() {
        DecisionModule intentDecisionModule = intentOwner.getDecisionModule();
        intentDecisionModule.needDecompostion();
        intentDecisionModule.intentDecomposition();
        intentDecisionModule.intentOrchestration();
        intentDecisionModule.decideSuitableAction();
        intentDecisionModule.exploreIntentHandlers();//返回handler
    }


}

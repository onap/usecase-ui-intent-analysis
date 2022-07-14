package org.onap.usecaseui.intentanalysis.intentProcessService;


import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.springframework.stereotype.Service;

@Service
public class IntentDefinitionService {

    private Function intentHandler;
    private Function intentOwner;

    public void setIntentRole(Function intentOwner, Function intentHandler) {
        if (intentOwner != null) {
            this.intentOwner = intentOwner;
        }
        if (intentHandler != null) {
            this.intentHandler = intentHandler;
        }
    }

    public void definitionPorcess() {
        DecisionModule intentDecisionModule = intentOwner.getDecisionModule();
        ActuationModule intentActuationModule = intentOwner.getActuationModule();
        intentDecisionModule.intentDefinition();
        intentActuationModule.saveIntentToDb();
    }
}

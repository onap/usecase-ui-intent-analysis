package org.onap.usecaseui.intentanalysis.intentProcessService;

import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;
import org.springframework.stereotype.Service;

@Service
public class IntentDetectionService {

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

    public void detectionProcess() {
        KnowledgeModule ownerKnowledgeModule = intentOwner.getKnowledgeModule();
        ownerKnowledgeModule.intentResolution();
        ownerKnowledgeModule.intentReportResolution();
        ownerKnowledgeModule.getSystemStatus();
        ownerKnowledgeModule.interactWithIntentOwner();

    }
}

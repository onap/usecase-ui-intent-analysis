package org.onap.usecaseui.intentanalysis.intentProcessService;

import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;
import org.springframework.stereotype.Service;

@Service
public class IntentDetectionService {

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

    public void detectionProcess() {
        KnowledgeModule ownerKnowledgeModule = intentOwner.getKnowledgeModule();
        ownerKnowledgeModule.intentResolution();
        ownerKnowledgeModule.intentReportResolution();
        ownerKnowledgeModule.getSystemStatus();
        ownerKnowledgeModule.interactWithIntentOwner();

    }
}

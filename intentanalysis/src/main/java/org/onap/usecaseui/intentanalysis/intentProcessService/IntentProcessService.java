package org.onap.usecaseui.intentanalysis.intentProcessService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntentProcessService {
    @Autowired
    IntentDetectionService intentDetectionServiceImpl;
    @Autowired
    IntentInvestigationService intentInvestigationService;
    @Autowired
    IntentDefinitionService intentDefinitionService;
    @Autowired
    IntentDistributionService intentDistributionService;
    @Autowired
    IntentOperationService intentOperationService;

    private IntentAnalysisFunction intentOwner;
    private IntentAnalysisFunction intentHandler;


    public void setIntentRole(IntentAnalysisFunction intentOwner, IntentAnalysisFunction intentHandler){
        if (intentOwner!= null){
            this.intentOwner = intentOwner;
        }
        if (intentHandler!= null){
            this.intentHandler= intentHandler;
        }
    }
    public void intentProcess() {
        intentDetectionServiceImpl.setIntentRole(intentOwner,intentHandler);
        intentDetectionServiceImpl.detectionProcess();

        //investigation process
        intentInvestigationService.setIntentRole(intentOwner,intentHandler);
        intentInvestigationService.investigationProcess();//List<handler>?

        //definition process
        intentDefinitionService.setIntentRole(intentOwner,intentHandler);
        intentDefinitionService.definitionPorcess();

        //distribution process
        intentDistributionService.setIntentRole(intentOwner,intentHandler);
        intentDistributionService.distributionProcess();

        //operation process
        intentOperationService.setIntentRole(intentOwner,intentHandler);
        intentOperationService.operationProcess();
    }


}

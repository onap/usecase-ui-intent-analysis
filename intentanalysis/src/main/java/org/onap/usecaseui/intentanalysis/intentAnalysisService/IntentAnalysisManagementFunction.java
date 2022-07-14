package org.onap.usecaseui.intentanalysis.intentAnalysisService;


import lombok.Data;
import org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl.ActuationModuleImpl;
import org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl.DecisoinModuleImpl;
import org.onap.usecaseui.intentanalysis.intentAnalysisService.intentModuleImpl.KnownledgeModuleImpl;
import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentProcessService.Function;

@Data
public class IntentAnalysisManagementFunction extends Function {
    private ActuationModule actuationModule  = new ActuationModuleImpl();
    private DecisionModule intentAnlysisDecisoin = new DecisoinModuleImpl();
    private KnowledgeModule intentAnlysisKnowledge = new KnownledgeModuleImpl();


}

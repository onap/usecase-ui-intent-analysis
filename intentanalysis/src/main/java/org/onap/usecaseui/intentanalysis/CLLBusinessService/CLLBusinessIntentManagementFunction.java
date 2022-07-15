package org.onap.usecaseui.intentanalysis.CLLBusinessService;


import lombok.Data;
import org.onap.usecaseui.intentanalysis.CLLBusinessService.intentModuleImpl.ActuationModuleImpl;
import org.onap.usecaseui.intentanalysis.CLLBusinessService.intentModuleImpl.DecisoinModuleImpl;
import org.onap.usecaseui.intentanalysis.CLLBusinessService.intentModuleImpl.KnownledgeModuleImpl;
import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentProcessService.IntentAnalysisFunction;

@Data
public class CLLBusinessIntentManagementFunction extends IntentAnalysisFunction {
    private ActuationModule actuationModule  = new ActuationModuleImpl();
    private DecisionModule decisoinModule = new DecisoinModuleImpl();
    private KnowledgeModule knowledgeModule = new KnownledgeModuleImpl();


}

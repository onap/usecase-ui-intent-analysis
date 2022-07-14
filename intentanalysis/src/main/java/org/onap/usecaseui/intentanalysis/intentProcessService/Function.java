package org.onap.usecaseui.intentanalysis.intentProcessService;


import lombok.Data;
import org.onap.usecaseui.intentanalysis.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentModule.KnowledgeModule;

@Data
public class Function {
    private ActuationModule actuationModule;
    private DecisionModule decisionModule;
    private KnowledgeModule knowledgeModule;
}

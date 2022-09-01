package org.onap.usecaseui.intentanalysis.deliveryIntentmgt;

import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessActuationModule;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessDecisionModule;
import org.onap.usecaseui.intentanalysis.cllBusinessIntentMgt.cllBusinessModule.CLLBusinessKnowledgeModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.IntentManagementFunction;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.ActuationModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentBaseService.intentModule.KnowledgeModule;

public class DeliveryManagementFunction extends IntentManagementFunction {
    private ActuationModule actuationModule  = new CLLBusinessActuationModule();
    private DecisionModule decisoinModule = new CLLBusinessDecisionModule();
    private KnowledgeModule knowledgeModule = new CLLBusinessKnowledgeModule();
}

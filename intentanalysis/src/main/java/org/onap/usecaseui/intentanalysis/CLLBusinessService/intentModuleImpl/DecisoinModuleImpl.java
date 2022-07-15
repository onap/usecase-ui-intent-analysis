package org.onap.usecaseui.intentanalysis.CLLBusinessService.intentModuleImpl;


import org.onap.usecaseui.intentanalysis.intentModule.DecisionModule;
import org.onap.usecaseui.intentanalysis.intentProcessService.IntentAnalysisFunction;
import org.springframework.stereotype.Service;

@Service
public class DecisoinModuleImpl implements DecisionModule {
    @Override
    public void determineUltimateGoal() {}

    @Override
    public IntentAnalysisFunction exploreIntentHandlers() {

        return null;

    }

    @Override
    public void intentDefinition() {}

    @Override
    public void decideSuitableAction() {}

    @Override
    public boolean needDecompostion() {
        return false;
    }

    @Override
    public void intentDecomposition() {}

    @Override
    public void intentOrchestration() {}

    @Override
    public void interactWithTemplateDb() {}
}

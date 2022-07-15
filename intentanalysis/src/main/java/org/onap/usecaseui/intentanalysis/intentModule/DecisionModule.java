package org.onap.usecaseui.intentanalysis.intentModule;


import org.onap.usecaseui.intentanalysis.intentProcessService.IntentAnalysisFunction;

public interface DecisionModule {
    void determineUltimateGoal();//
    IntentAnalysisFunction exploreIntentHandlers();
    void intentDefinition();
    void decideSuitableAction();

    //confirm whether the intent needs to be decomposed and orchestrated
    public boolean needDecompostion();

    //call decomposition module
    public void intentDecomposition();

    //call orchestration module
    public void intentOrchestration();


    public void interactWithTemplateDb();
}

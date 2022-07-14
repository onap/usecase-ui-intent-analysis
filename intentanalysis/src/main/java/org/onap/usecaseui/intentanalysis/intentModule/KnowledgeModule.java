package org.onap.usecaseui.intentanalysis.intentModule;

public interface KnowledgeModule {
    void intentResolution();
    void intentReportResolution();
    void getSystemStatus();
    void interactWithIntentOwner();
    //actuationModel & knownledgeModel interact
}

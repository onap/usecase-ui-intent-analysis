package org.onap.usecaseui.intentanalysis.intentModule;

import org.onap.usecaseui.intentanalysis.intentProcessService.Function;

public interface ActuationModule {
    //actuationModel & knownledgeModel interact
    void sendToIntentHandler(Function IntentHandler);
    void sendToNonIntentHandler();//直接操作
    void interactWithIntentHandle();
    //Save intent information to the intent instance database
    void saveIntentToDb();

}

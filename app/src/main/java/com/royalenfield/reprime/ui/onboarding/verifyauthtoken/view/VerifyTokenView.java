package com.royalenfield.reprime.ui.onboarding.verifyauthtoken.view;

public interface VerifyTokenView {

    void onVerifyTokenSuccess();

    void onVerifyTokenFailure();

    void onGenerateTokenSuccess(long time,String reqId);

    void onGenerateTokenFailure();
}

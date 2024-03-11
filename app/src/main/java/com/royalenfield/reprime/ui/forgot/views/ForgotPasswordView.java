package com.royalenfield.reprime.ui.forgot.views;

import com.royalenfield.reprime.base.REMvpView;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface ForgotPasswordView extends REMvpView {

    void onOtpSendSuccess();

    void onOtpSendFailure(String errorMessage);

    void onEmailOrPhoneNumberEmpty();

    void onInvalidPhoneNumber();

    void onInvalidEmailId();

    void hideInlineError();

}

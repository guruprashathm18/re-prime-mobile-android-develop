package com.royalenfield.reprime.ui.resetpassword.views;

import com.royalenfield.reprime.base.REMvpView;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface ResetPasswordView extends REMvpView {

    void onResetSuccess();

    void onResetFailure(String errorMessage);

    void onAllEmptyFields();

    void onOtpEmpty();

    void onNewPasswordEmpty();

    void onConfirmPasswordEmpty();

    void onNewPasswordInvalid();

    void onConfirmPasswordInvalid();

    void onPasswordConfirmationError();

}

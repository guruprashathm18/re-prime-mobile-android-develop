package com.royalenfield.reprime.ui.riderprofile.views;

import com.royalenfield.reprime.base.REMvpView;

public interface ChangePasswordView extends REMvpView {

    void onPaswordsMismatch();

    void onDuplicatePassword();

    void onChangePasswordSuccess();

    void onChangePasswordFailure();

    void onNewPasswordInvalid();

    void onConfirmPasswordInvalid();
}

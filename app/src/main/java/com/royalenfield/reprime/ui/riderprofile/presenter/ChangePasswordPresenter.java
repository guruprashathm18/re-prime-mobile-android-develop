package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.ui.riderprofile.interactor.ChangePasswordInteractor;
import com.royalenfield.reprime.ui.riderprofile.listeners.ChangePasswordListener;
import com.royalenfield.reprime.ui.riderprofile.views.ChangePasswordView;
import com.royalenfield.reprime.utils.REUtils;

public class ChangePasswordPresenter implements IChangePasswordPresenter, ChangePasswordListener {

    private ChangePasswordView mChangePasswordView;
    private ChangePasswordInteractor mChangePasswordInteractor;

    public ChangePasswordPresenter(ChangePasswordView changePasswordView, ChangePasswordInteractor changePasswordInteractor) {
        mChangePasswordView = changePasswordView;
        mChangePasswordInteractor = changePasswordInteractor;
    }

    private boolean checkPaswords(String newPassword, String confirmpassword) {
        if(!REUtils.isValidPassword(newPassword)){
            if (mChangePasswordView != null) {
                mChangePasswordView.onNewPasswordInvalid();
            }
            return false;
        }
        if(!REUtils.isValidPassword(confirmpassword)){
            if (mChangePasswordView != null) {
                mChangePasswordView.onConfirmPasswordInvalid();
            }
            return false;
        }

        if (newPassword.equals(confirmpassword)) {
            return true;
        } else {
            if (mChangePasswordView != null) {
                mChangePasswordView.onPaswordsMismatch();
            }
        }
        return false;
    }

    private boolean checkDuplicate(String oldPassword,String newPassword){
        if (!oldPassword.equals(newPassword)){
            return true;
        }else{
            mChangePasswordView.onDuplicatePassword();
        }
        return false;
    }

    @Override
    public void validateFields(String oldpassword, String newpassword, String confirmPassword) {
        if (checkPaswords(newpassword, confirmPassword) && checkDuplicate(oldpassword,newpassword) && mChangePasswordView != null) {
           mChangePasswordView.showLoading();
           mChangePasswordInteractor.onChangePassword(oldpassword,newpassword,"", this);
        }
    }

    @Override
    public void onSuccess() {
        mChangePasswordView.hideLoading();
        mChangePasswordView.onChangePasswordSuccess();
    }

    @Override
    public void onFailure(String errorMessage) {
        mChangePasswordView.hideLoading();
        mChangePasswordView.onChangePasswordFailure();
    }
}

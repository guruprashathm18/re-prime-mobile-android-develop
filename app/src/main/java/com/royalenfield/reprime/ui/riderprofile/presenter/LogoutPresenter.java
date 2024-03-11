package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.ui.riderprofile.interactor.LogoutInteractor;
import com.royalenfield.reprime.ui.riderprofile.listeners.OnLogoutResponseListener;
import com.royalenfield.reprime.ui.riderprofile.views.LogoutView;

public class LogoutPresenter implements ILogoutPresenter, OnLogoutResponseListener {

    private LogoutView mLogoutView;
    private LogoutInteractor mLogoutIntector;

    public LogoutPresenter(LogoutView logoutView, LogoutInteractor logoutInteractor) {
        mLogoutView = logoutView;
        mLogoutIntector = logoutInteractor;
    }



    @Override
    public void logout() {
        mLogoutView.showLoading();
        mLogoutIntector.logout(this);
    }

    @Override
    public void onSuccess() {
        mLogoutView.hideLoading();
        mLogoutView.onLogoutSuccess();
    }

    @Override
    public void onFailure(String errorMessage) {
        mLogoutView.hideLoading();
        mLogoutView.onLogoutFailure(errorMessage);
    }

    @Override
    public void forgetMe() {
        mLogoutView.showLoading();
        mLogoutIntector.forgotMe(this);
    }

    @Override
    public void onForgotSuccess() {
        mLogoutView.hideLoading();
        mLogoutView.onForgotSuccess();
    }

    @Override
    public void onForgotFailure(String errorMessage) {
        mLogoutView.hideLoading();
        mLogoutView.onForgotFailure(errorMessage);

    }
}

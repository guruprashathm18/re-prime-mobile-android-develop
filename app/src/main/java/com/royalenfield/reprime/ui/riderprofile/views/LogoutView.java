package com.royalenfield.reprime.ui.riderprofile.views;

import com.royalenfield.reprime.base.REMvpView;


public interface LogoutView extends REMvpView {

    void onLogoutSuccess();

    void onLogoutFailure(String errorMessage);

    void onForgotSuccess();

    void onForgotFailure(String errorMessage);



}

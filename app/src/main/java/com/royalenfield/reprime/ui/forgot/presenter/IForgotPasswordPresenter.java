package com.royalenfield.reprime.ui.forgot.presenter;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface IForgotPasswordPresenter {

    void sendOtp(String userID);

    void onDestroy();

    void onInlineErrorVisibility(String userID);
}

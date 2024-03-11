package com.royalenfield.reprime.ui.riderprofile.interactor;

import android.accounts.NetworkErrorException;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.otp.OtpResponse;
import com.royalenfield.reprime.models.response.web.signup.ConsentResponse;
import com.royalenfield.reprime.ui.forgot.interactor.IForgotPasswordInteractor;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.riderprofile.listeners.OnLogoutResponseListener;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

/**
 *
 * Class handles to do api calls to interact with the RE server to logout from app.
 */

public class LogoutInteractor implements ILogoutInteractor {
    private static final String TAG = LogoutInteractor.class.getSimpleName();
private final String contentType="application/x-www-form-urlencoded";
    @Override
    public void logout( final OnLogoutResponseListener onLogoutResponseListener) {

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .logout()
                .enqueue(new BaseCallback<OtpResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess()) {
                                onLogoutResponseListener.onSuccess();
                            } else {
                                onLogoutResponseListener.onFailure(response.body().getMessage());
                            }
                        } else {
                             onLogoutResponseListener.onFailure(response.code() + " : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        if (t instanceof NetworkErrorException) {
                            onLogoutResponseListener.onFailure(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure));
                        } else {
                            onLogoutResponseListener.onFailure(t.getMessage());
                        }
                    }
                });

    }

    @Override
    public void forgotMe(OnLogoutResponseListener onLogoutResponseListener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .forgetMe()
                .enqueue(new BaseCallback<ConsentResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ConsentResponse> call, @NotNull Response<ConsentResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess()) {
                                onLogoutResponseListener.onForgotSuccess();
                            } else {
                                onLogoutResponseListener.onForgotFailure(response.body().getMessage());
                            }
                        } else {
                            onLogoutResponseListener.onForgotFailure(response.code() + " : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ConsentResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        if (t instanceof NetworkErrorException) {
                            onLogoutResponseListener.onForgotFailure(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure));
                        } else {
                            onLogoutResponseListener.onForgotFailure(t.getMessage());
                        }
                    }
                });

    }
}

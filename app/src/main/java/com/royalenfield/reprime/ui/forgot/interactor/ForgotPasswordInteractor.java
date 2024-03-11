package com.royalenfield.reprime.ui.forgot.interactor;

import android.accounts.NetworkErrorException;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.otp.OtpResponse;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.models.request.web.forgotpassword.OtpRequest;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

/**
 * @author BOP1KOR on 12/19/2018.
 * <p>
 * Class handles to do api calls to interact with the RE server to generate the otp.
 */

public class ForgotPasswordInteractor implements IForgotPasswordInteractor {
    private static final String TAG = ForgotPasswordInteractor.class.getSimpleName();

    @Override
    public void sendOtp(String userId, final OnSendOtpResponseListener onSendOtpResponseListener) {

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .sendOtp(userId)
                .enqueue(new BaseCallback<OtpResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<OtpResponse> call, @NotNull Response<OtpResponse> response) {
                        super.onResponse(call, response);
                        RELog.d(TAG, "Forgot password Otp response code : "+ response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess()) {
                                RELog.d(TAG, "Forgot password sendOtp : " + response.code() + " : "+ response.body().getMessage());
                                onSendOtpResponseListener.onSuccess();
                            } else {
                                onSendOtpResponseListener.onFailure(response.body().getMessage());
                            }
                        } else {
                            RELog.d(TAG, "Forgot password sendOtp : " + response.code() + " : "+ response.message());
                            onSendOtpResponseListener.onFailure(response.code() + " : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OtpResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        RELog.d(TAG, "Forgot password otp send failure : "+ t.getMessage());
                        if (t instanceof NetworkErrorException) {
                            onSendOtpResponseListener.onFailure(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure));
                        } else {
                            onSendOtpResponseListener.onFailure(t.getMessage());
                        }
                    }
                });

    }
}

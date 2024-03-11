package com.royalenfield.reprime.ui.resetpassword.interactor;

import android.accounts.NetworkErrorException;
import android.util.Log;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.otp.ResetPasswordResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.resetpassword.listeners.OnResetPasswordResponseListener;
import com.royalenfield.reprime.models.request.web.resetpassword.ResetPasswordRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author BOP1KOR on 12/19/2018.
 * <p>
 * Class handles to reset password api calls to interact with the RE server to create the new password for the user.
 */

public class ResetPasswordInteractor implements IResetPasswordInteractor {
    private static final String TAG = ResetPasswordInteractor.class.getSimpleName();

    @Override
    public void resetPassword(String userId, String newPassword, String otp, final OnResetPasswordResponseListener onResetPasswordResponseListener) {

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .resetPassword(new ResetPasswordRequest(userId, newPassword, otp))
                .enqueue(new BaseCallback<ResetPasswordResponse>() {
                    @Override
                    public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "Reset password response code : " + response.code());
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess()) {
                                Log.d("API", "Reset password onResponse : " + response.code() + " : " + response.body().getMessage());
                                onResetPasswordResponseListener.onSuccess();
                            } else {
                                Log.d("API", "Reset password  onResponse: " + response.code() + " : " + response.body().getMessage());
                                onResetPasswordResponseListener.onFailure(response.body().getMessage());
                            }
                        } else {
                            Log.d("API", "Reset password onResponse : " + response.code() + " : " + response.message());
                            onResetPasswordResponseListener.onFailure(response.code() + " : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "Reset password  failure : " + t.getMessage());
                        if (t instanceof NetworkErrorException) {
                            onResetPasswordResponseListener.onFailure(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure));
                        } else {
                            onResetPasswordResponseListener.onFailure(t.getMessage());
                        }
                    }
                });

    }
}

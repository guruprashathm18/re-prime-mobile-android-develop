package com.royalenfield.reprime.ui.riderprofile.interactor;

import android.util.Log;
import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.UpdateProfile.ChangePassword;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.riderprofile.listeners.ChangePasswordListener;
import com.royalenfield.reprime.models.request.web.changepassword.ChangePasswordRequest;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordInteractor implements IChangePasswordInteractor {
    private static final String TAG = ChangePasswordInteractor.class.getSimpleName();

    String contentType = "application/json";
    String jwtToken = REApplication.getInstance().getUserTokenDetails();

    @Override
    public void onChangePassword(String prevPassword, String password, String confirmPassword, ChangePasswordListener changePasswordListener) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .changePassword(contentType, jwtToken, new ChangePasswordRequest(prevPassword, password, confirmPassword))
                .enqueue(new BaseCallback<ChangePassword>() {
                    @Override
                    public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                        super.onResponse(call, response);
                        Log.d("API", "password response code : " + response.code() + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {
                                //REApplication.getInstance().setUserLoginDetails(response.body());
                                Log.d("API", "password changed");
                                changePasswordListener.onSuccess();
                            } else {
                                changePasswordListener.onFailure(REUtils.getErrorMessageFromCode(response.body().getCode()));
                            }
                        } else {
                            // error case
                            changePasswordListener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }

                    }

                    @Override
                    public void onFailure(Call<ChangePassword> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "password change failure : " + t.getMessage());
                        changePasswordListener.onFailure(REUtils.getErrorMessageFromCode(0));

                    }
                });
    }

}

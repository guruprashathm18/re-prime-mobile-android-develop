package com.royalenfield.firebase.fcm;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.firebase.FirebaseTokenRequest;
import com.royalenfield.reprime.models.response.web.firebasetoken.FirebaseTokenResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FCMTokenRegistrationInteractor implements IFCMTokenRegistrationInteractor {

    //private String jwtToken = getJwtToken();

    @Override
    public void sendNotification(FirebaseTokenRequest firebaseTokenRequest, final FCMTokenRegistrationListener notificationServiceListener) {
        Log.d("API", "saveDeviceToken called :" + firebaseTokenRequest.getDeviceToken());
        REApplication
                .getInstance()
                .getRENotificationApiInstance()
                .getNotificationApi()
                .sendDeviceToken(REConstants.RE_APP_ID, firebaseTokenRequest)
                .enqueue(new BaseCallback<FirebaseTokenResponse>() {
                    @Override
                    public void onResponse(Call<FirebaseTokenResponse> call,
                                           Response<FirebaseTokenResponse> response) {
                        super.onResponse(call,response);
                        Log.d("API", "saveDeviceToken response code : " + response.code() + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body().getStatus() == REConstants.API_SUCCESS_CODE) {
                            notificationServiceListener.onFCMTokenSentSuccess();
                        } else {
                            notificationServiceListener.onFCMTokenSentFailure("ServerError Occured : " + response.code());
                        }
                    }


                    @Override
                    public void onFailure(Call<FirebaseTokenResponse> call, Throwable t) {
                        super.onFailure(call,t);
                        Log.d("API", "saveDeviceToken failure : " + t.getMessage());
                        if (t instanceof NetworkErrorException) {
                            notificationServiceListener.onFCMTokenSentFailure(REApplication.getAppContext().
                                    getResources().getString(R.string.network_failure));
                        } else {
                            notificationServiceListener.onFCMTokenSentFailure("ServerError Occured : " + t.getMessage());
                        }
                    }
                });
    }
}

package com.royalenfield.reprime.ui.onboarding.verifyauthtoken.interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.generatetoken.GenerateTokenRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.generatetoken.GenerateTokenResponse;
import com.royalenfield.reprime.models.response.web.login.LoginData;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.verifytoken.VerifyTokenResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.view.GenerateTokenView;
import com.royalenfield.reprime.ui.onboarding.verifyauthtoken.view.VerifyTokenView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.*;

/**
 * Interactor for verifyAuthToken
 */
public class VerifyAuthTokenInteractor {

    private VerifyTokenView mVerifyTokenView;
    private String mJWTToken, mRefreshToken, mUserID, mPhoneNo;

    public VerifyAuthTokenInteractor(VerifyTokenView verifyTokenView) {
        mVerifyTokenView = verifyTokenView;
        try {
            mJWTToken = REApplication.getInstance()
                    .getUserTokenDetails();
            mRefreshToken = REPreference.getInstance().getString(REApplication.getAppContext(), REFRESH_TOKEN_KEY);
            mUserID = REPreference.getInstance().getString(REApplication.getAppContext(), USERID_KEY);
            mPhoneNo = REPreference.getInstance().getString(REApplication.getAppContext(), MOBILE_NO_KEY);
          //  verifyAuthToken();
        } catch (PreferenceException e) {
            RELog.e(e);
        }
    }

    /**
     * API call for verifyAuthToken
     */
    public void verifyAuthToken() {
        long requestTime=System.currentTimeMillis();
        REApplication.getInstance().getREWebsiteApiInstance().getWebsiteAPI().verifyToken()
                .enqueue(new BaseCallback<VerifyTokenResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VerifyTokenResponse> call,
                                           @NonNull Response<VerifyTokenResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "VerifyToken response code : " + response.code() +
                                "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                // Setting JWT to model

                                REUtils.setDataFromVerifyTokenToUserInfo(response.body());
                                mVerifyTokenView.onVerifyTokenSuccess();
                            } else if (!response.body().getSuccess() && response.body().
                                    getCode() == REConstants.API_UNAUTHORIZED || !response.body().getSuccess() && response.body().
                                    getCode() == REConstants.TOKEN_EXPIRED) {
                                generateToken();
                            } else {
                                // error case
                                mVerifyTokenView.onVerifyTokenFailure();
                            }
                        } else {
                            // error case
                            mVerifyTokenView.onVerifyTokenFailure();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VerifyTokenResponse> call, @NonNull Throwable t) {
                      //  super.onFailure(call, t);
                        Log.d("API", "VerifyToken failure : " + t.getMessage());
                        generateFailureLogs(call,t,(System.currentTimeMillis()-requestTime)/1000);
                        mVerifyTokenView.onVerifyTokenFailure();
                    }
                });
    }

    /**
     * API call for generating new JWT token
     */
    public void generateToken() {
       long reqTime= System.currentTimeMillis();
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .generateToken(new GenerateTokenRequest(mUserID, mRefreshToken))
                .enqueue(new BaseCallback<GenerateTokenResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GenerateTokenResponse> call,
                                           @NonNull Response<GenerateTokenResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "Login Generate token response code : " + response.code() +
                                "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                try {
                                    //Rewriting the preference with new JWT token
                                    LoginResponse loginResponse = new Gson().fromJson(REPreference.getInstance().getString(REApplication.getAppContext(), USERDATA), LoginResponse.class);
                                    if (loginResponse != null && loginResponse.getData() != null) {
                                        LoginData data = loginResponse.getData();
										if(response.body().getSuccess()) {
											data.setJwtAccessToken((String) response.body().getData());
											loginResponse.setData(data);
											REUtils.setDataToUserInfo(loginResponse);
											mVerifyTokenView.onGenerateTokenSuccess(System.currentTimeMillis()-reqTime,response.body().getRequestId());

										}
										else{
											generateLogs(response,null,null);
											mVerifyTokenView.onGenerateTokenFailure();
										}
                                         }
                                } catch (PreferenceException e) {
                                    RELog.e(e);
                                    mVerifyTokenView.onGenerateTokenFailure();
                                }
                               } else {
								generateLogs(response,null,null);
								mVerifyTokenView.onGenerateTokenFailure();
                            }
                        } else {
                            mVerifyTokenView.onGenerateTokenFailure();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GenerateTokenResponse> call, @NonNull Throwable t) {
                       // super.onFailure(call, t);
                       long responseTime=System.currentTimeMillis();
                        Log.d("API", "Generate token failure : " + t.getMessage());
                        generateFailureLogs(call,t,(responseTime-reqTime)/1000);
                        mVerifyTokenView.onGenerateTokenFailure();
                    }
                });
    }
}

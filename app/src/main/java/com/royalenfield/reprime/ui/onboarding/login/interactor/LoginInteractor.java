package com.royalenfield.reprime.ui.onboarding.login.interactor;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.logs.Body;
import com.royalenfield.reprime.models.request.logs.LogRequest;
import com.royalenfield.reprime.models.request.logs.Message;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDetailRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleDetailResponse;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.login.listeners.OnLoginFinishedListener;
import com.royalenfield.reprime.models.request.web.login.LoginRequest;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

/**
 * @author BOP1KOR on 12/7/2018.
 * <p>
 * Class handles login api calls to interact with the RE server returns success or failure response.
 */

public class LoginInteractor implements ILoginInteractor {

    //    @Override
//    public void login(String username, String password, final OnLoginFinishedListener loginFinishedListener) {
//        Log.d("API", "Login API called");
//        REApplication
//                .getInstance()
//                .getREWebsiteApiInstance()
//                .getWebsiteAPI()
//                .login(new LoginRequest(username, password, "user"))
//                .enqueue(new Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                        Log.d("API", "Login response code : " + response.code() + "  response : " + new Gson().toJson(response.body()));
//                        if (response.isSuccessful() && response.body() != null) {
//                            if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {
//                                REApplication.getInstance().setUserLoginDetails(response.body());
//                                setJwtToken(response.body());
//                                loginFinishedListener.onSuccess();
//                            } else {
//                                loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(response.body().getCode()));
//                            }
//                        } else {
//                            // error case
//                            loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResponse> call, Throwable t) {
//                        Log.d("API", "Login failure : " + t.getMessage());
//                        loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
//
//                    }
//                });
//    }
//

    @Override
    public void login(String credentials, final OnLoginFinishedListener loginFinishedListener) {
        long reqTime= System.currentTimeMillis();
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .login(new LoginRequest(credentials, "user"))
                .enqueue(new BaseCallback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        super.onResponse(call,response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                Log.e("LOGIN","RESPONSE");

//                                if (REUtils.validateLoginResponse(response.body())) {
//                                    REUtils.setDataToUserInfo(response.body());
//                                    loginFinishedListener.onSuccess();
//                                } else {
//                                    loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(response.body().getCode()));
//                                }
                                if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {

                                    REUtils.setDataToUserInfo(response.body());
                                    String showDataSanitation=REUtils.getFeatures().getDefaultFeatures().getShowDataSanitization();
                                    if(showDataSanitation==null) {
                                        if (REApplication.getInstance().Country.equalsIgnoreCase(REConstants.COUNTRY_INDIA))
                                            showDataSanitation = FEATURE_ENABLED;
                                        else
                                            showDataSanitation = FEATURE_DISABLED;
                                    }
                                    if(showDataSanitation.equalsIgnoreCase(FEATURE_ENABLED)&&
                                            (response.body().getData().getUser().getVerifiedAccount().equalsIgnoreCase(REConstants.KEY_ACCOUNT_PENDING)||response.body().getData().getUser().getVerifiedAccount().equalsIgnoreCase(REConstants.KEY_ACCOUNT_VERIFIED)))
                                        loginFinishedListener.onAccountPending();
                                    else
                                    loginFinishedListener.onSuccess();

                                } else {
                                    loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(response.body().getCode()));
                                }
                            } else {


                                loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(response.body().getCode()));
                            }
                        } else {
                            // error case
                            loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        //super.onFailure(call,t);
                        generateFailureLogs(call,t,(System.currentTimeMillis()-reqTime)/1000);
                        loginFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void getVehicleDetails(String mobileNumber, final OnLoginFinishedListener loginFinishedListener) {

        String accessToken = REApplication.getInstance()
                .getUserTokenDetails();

        VehicleDetailRequest vehicleDetailRequest=new VehicleDetailRequest();
        vehicleDetailRequest.setAppId(REConstants.APP_ID);
        vehicleDetailRequest.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        vehicleDetailRequest.setLoggedInUserMobileNo( REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone());
        vehicleDetailRequest.setLoggedInUserEmail( REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
        vehicleDetailRequest.setLoggedInUserName( REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName()+" "+REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName());


        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .getVehicleDetails(REConstants.APP_ID, accessToken, vehicleDetailRequest)
                .enqueue(new BaseCallback<VehicleDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VehicleDetailResponse> call, @NonNull Response<VehicleDetailResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {
                                if (response.body().getCode() == REConstants.API_SUCCESS_CODE) {
                                    loginFinishedListener.onVehicleDetailSuccess(response.body().getData().getVehicleInfo());
                                } else {
                                    loginFinishedListener.onVehicleDetailFail(response.body().getCode());
                                }
                            } else if (response.body().getCode() == REConstants.API_SUCCESS_NO_DATA_CODE) {
                                loginFinishedListener.onVehicleDetailSuccess(null);
                            } else {
                                loginFinishedListener.onVehicleDetailFail(response.body().getCode());
                            }
                        } else {
                            // error case
                            loginFinishedListener.onVehicleDetailFail(response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VehicleDetailResponse> call, @NonNull Throwable t) {
                        super.onFailure(call, t);
                        loginFinishedListener.onVehicleDetailFail(0);
                    }
                });
    }

}

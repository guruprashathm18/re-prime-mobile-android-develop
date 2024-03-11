package com.royalenfield.bluetooth.otap.interactor;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.otap.OtapGetDeviceinfoApiRequest;
import com.royalenfield.reprime.models.request.web.otap.RemoveTripperDeviceApiRequest;
import com.royalenfield.reprime.models.request.web.otap.UpdateTripperDeviceApiRequest;
import com.royalenfield.reprime.models.request.web.tbtauthentication.TbtAuthRequest;
import com.royalenfield.reprime.models.response.web.otap.OTAPFeedbackResponse;
import com.royalenfield.reprime.models.response.web.otap.OtapGetDeviceinfoApiResopnse;
import com.royalenfield.reprime.models.response.web.otap.OtapSaveDeviceinfoApiRequest;
import com.royalenfield.reprime.models.response.web.otap.OtapSaveDeviceinfoApiResponse;
import com.royalenfield.reprime.ui.home.navigation.listener.OTAPGetDeviceListApiListner;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

public class OtapInteractor implements IOtapInteractor {
    @Override
    public void saveFeedback(String firmwareId, String deviceId, int status) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .otapFeedback(null)
                .enqueue(new Callback<OTAPFeedbackResponse>() {
                    @Override
                    public void onResponse(Call<OTAPFeedbackResponse> call,
                                           Response<OTAPFeedbackResponse> response) {
                        RELog.d("API","saveFeedback :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            RELog.e("SaveFeedback","Response :"+ new Gson().toJson(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<OTAPFeedbackResponse> call, Throwable t) {
                        RELog.e("SaveFeedback","onFailure :"+ t.getMessage());
                    }
                });
    }

    //Get Otap Device list from Api
    public void getDeviceListFromApi(OtapGetDeviceinfoApiRequest otapGetDeviceinfoApiRequest, OTAPGetDeviceListApiListner otapGetDeviceListApiListner) {

        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getTBTApi().otapGetDeviceListFromApi(otapGetDeviceinfoApiRequest)
                .enqueue(new BaseCallback<OtapGetDeviceinfoApiResopnse>() {
                    @Override
                    public void onResponse(Call<OtapGetDeviceinfoApiResopnse> call, Response<OtapGetDeviceinfoApiResopnse> response) {
                        super.onResponse(call, response);
                        if (response.body() == null) {
                            //tbtAutenticationListner.authkeyfailure();
                            return;
                        }
                        RELog.e("GetTripperDeviceListApi",response.body().getMessage());
                        if (response.isSuccessful() && response.body() != null ) {
                            if (response.body().getCode().equals(REConstants.API_SUCCESS_CODE)) {
                                otapGetDeviceListApiListner.getTripperDeviceListFromApiSuccess(response.body().getData());
                            } else {
                                otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                            }
                        } else {
                            otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                        }

                    }

                    @Override
                    public void onFailure(Call<OtapGetDeviceinfoApiResopnse> call, Throwable t) {
                        super.onFailure(call, t);
                        otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                    }
                });
    }
    //Save Otap Device list to cloud Api
    public void saveDeviceListFromPreferenceApi(OtapSaveDeviceinfoApiRequest otapSaveDeviceinfoApiRequest) {

        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getTBTApi().otapSaveDeviceListFromPreferenceToApi(otapSaveDeviceinfoApiRequest)
                .enqueue(new BaseCallback<OtapSaveDeviceinfoApiResponse>() {
                    @Override
                    public void onResponse(Call<OtapSaveDeviceinfoApiResponse> call, Response<OtapSaveDeviceinfoApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.body() == null) {
                            //tbtAutenticationListner.authkeyfailure();
                            return;
                        }
                       RELog.e("SaveTriperDeviceListApi",response.body().getMessage());
                        if (response.isSuccessful() && response.body() != null ) {
                            if (response.body().getCode().equals(REConstants.API_SUCCESS_CODE)) {
                                RELog.e("SaveTriperDeviceListApi",response.body().getMessage());
                               // otapGetDeviceListApiListner.getTripperDeviceListFromApiSuccess(response.body().getData());
                            } else {
                                RELog.e("SaveTriperDeviceListApi",response.body().getMessage());
                                //otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                            }
                        } else {
                            RELog.e("SaveTriperDeviceListApi","Null");
                           // otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                        }

                    }

                    @Override
                    public void onFailure(Call<OtapSaveDeviceinfoApiResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        RELog.e("SaveTriperDeviceListApi","onFailure");
                        //otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                    }
                });
    }
    //Remove tripper device api call - will remove the tripper from the cloud database

    public void removeTripperFromCloud(RemoveTripperDeviceApiRequest removeTripperDeviceApiRequest) {

        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getTBTApi().removeTBTDeviceApi(removeTripperDeviceApiRequest)
                .enqueue(new BaseCallback<OtapSaveDeviceinfoApiResponse>() {
                    @Override
                    public void onResponse(Call<OtapSaveDeviceinfoApiResponse> call, Response<OtapSaveDeviceinfoApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.body() == null) {
                            //tbtAutenticationListner.authkeyfailure();
                            return;
                        }
                        RELog.e("RemoveTripperDeviceListApi",response.body().getMessage());
                        if (response.isSuccessful() && response.body() != null ) {
                            if (response.body().getCode().equals(REConstants.API_SUCCESS_CODE)) {
                               // otapGetDeviceListApiListner.getTripperDeviceListFromApiSuccess(response.body().getData());
                            } else {
                                //otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                            }
                        } else {
                            //otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                        }

                    }

                    @Override
                    public void onFailure(Call<OtapSaveDeviceinfoApiResponse> call, Throwable t) {
                        super.onFailure(call, t);
                       // otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                    }
                });
    }
    //Update Tripper device api call

    public void updateTripperDeviceInCloud(UpdateTripperDeviceApiRequest updateTripperDeviceApiRequest) {

        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getTBTApi().updateTBTDeviceApi(updateTripperDeviceApiRequest)
                .enqueue(new BaseCallback<OtapSaveDeviceinfoApiResponse>() {
                    @Override
                    public void onResponse(Call<OtapSaveDeviceinfoApiResponse> call, Response<OtapSaveDeviceinfoApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.body() == null) {
                            //tbtAutenticationListner.authkeyfailure();
                            return;
                        }
                        RELog.e("UpdateTripperDeviceListApi",response.body().getMessage());
                        if (response.isSuccessful() && response.body() != null ) {
                            if (response.body().getCode().equals(REConstants.API_SUCCESS_CODE)) {
                                // otapGetDeviceListApiListner.getTripperDeviceListFromApiSuccess(response.body().getData());
                            } else {
                                //otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                            }
                        } else {
                            //otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                        }

                    }

                    @Override
                    public void onFailure(Call<OtapSaveDeviceinfoApiResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        // otapGetDeviceListApiListner.getTripperDeviceListFromApiFailed();
                    }
                });
    }
}

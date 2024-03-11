package com.royalenfield.reprime.ui.home.navigation.interactor;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.tbtauthentication.TbtAuthRequest;
import com.royalenfield.reprime.models.response.web.otap.OtapSaveDeviceinfoApiResponse;
import com.royalenfield.reprime.models.response.web.tbtauthentication.TbtAuthResponse;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryDetailsApiRequest;
import com.royalenfield.reprime.ui.home.navigation.listener.TBTAutenticationListner;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Call;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

public class RENavigationInteractor {

    public void getTbtAuthkey(TbtAuthRequest tbtAuthRequest, TBTAutenticationListner tbtAutenticationListner) {

        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getTBTApi().getTbtAuthKey(tbtAuthRequest)
                .enqueue(new BaseCallback<TbtAuthResponse>() {
                    @Override
                    public void onResponse(Call<TbtAuthResponse> call, Response<TbtAuthResponse> response) {
                        super.onResponse(call, response);
                        if (response.body() == null) {
                            tbtAutenticationListner.authkeyfailure();
                            return;
                        }
                        RELog.e("GetTBTAuthKey_Service",response.body().getResult());
                        if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                            if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                tbtAutenticationListner.setAuthenticationKey(response.body().getResult());
                            } else {
                                tbtAutenticationListner.authkeyfailure();
                            }
                        } else {
                            tbtAutenticationListner.authkeyfailure();
                        }

                    }

                    @Override
                    public void onFailure(Call<TbtAuthResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        tbtAutenticationListner.authkeyfailure();
                    }
                });
    }
    //Navigation summary Save Api
    public void saveNavigationSummaryDetails(SaveTripSummaryDetailsApiRequest saveTripSummaryDetailsApiRequest) {

        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getTBTApi().saveTripSummaryApi(saveTripSummaryDetailsApiRequest)
                .enqueue(new BaseCallback<OtapSaveDeviceinfoApiResponse>() {
                    @Override
                    public void onResponse(Call<OtapSaveDeviceinfoApiResponse> call, Response<OtapSaveDeviceinfoApiResponse> response) {
                        super.onResponse(call, response);
                        if (response.body() == null) {
                            //tbtAutenticationListner.authkeyfailure();
                            return;
                        }
                        RELog.e("saveTripSummaryApi",response.body().getMessage());
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
                        //tbtAutenticationListner.authkeyfailure();
                        RELog.e("saveTripSummaryApi","TripSummaryFailed");
                    }
                });
    }
}

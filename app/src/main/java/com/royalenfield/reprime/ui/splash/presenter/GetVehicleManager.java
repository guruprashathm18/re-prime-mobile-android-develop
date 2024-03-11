package com.royalenfield.reprime.ui.splash.presenter;

import androidx.annotation.NonNull;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDetailRequest;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleDetailResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

public class GetVehicleManager {

    private static GetVehicleManager getVehicleManager;


    public static GetVehicleManager getInstance() {
        if (getVehicleManager == null) {
            getVehicleManager = new GetVehicleManager();
        }
        return getVehicleManager;
    }


    public void getVehicleDetails(final GetVehicleListener getVehicleListener) {

        String accessToken = REApplication.getInstance()
                .getUserTokenDetails();
        VehicleDetailRequest vehicleDetailRequest = new VehicleDetailRequest();
        vehicleDetailRequest.setAppId(REConstants.APP_ID);
        vehicleDetailRequest.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        vehicleDetailRequest.setLoggedInUserMobileNo(REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone());
        vehicleDetailRequest.setLoggedInUserEmail(REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
        vehicleDetailRequest.setLoggedInUserName(REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName() + " " + REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName());
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
                                    getVehicleListener.onFirebaseVehicleDetailSuccess(response.body().getData().getVehicleInfo());
                                } else {
                                    getVehicleListener.onFirebaseVehicleDetailFailure();
                                }
                            } else if (response.body().getCode() == REConstants.API_SUCCESS_NO_DATA_CODE) {
                                getVehicleListener.onFirebaseVehicleDetailSuccess(null);
                            } else {
                                getVehicleListener.onFirebaseVehicleDetailFailure();
                            }
                        } else {
                            // error case
                            getVehicleListener.onFirebaseVehicleDetailFailure();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VehicleDetailResponse> call, @NonNull Throwable t) {
                        super.onFailure(call, t);
                        getVehicleListener.onFirebaseVehicleDetailFailure();
                    }
                });
    }
}

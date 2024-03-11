package com.royalenfield.reprime.ui.onboarding.login.listeners;

import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

/**
 * @author BOP1KOR on 12/12/2018.
 */

public interface OnLoginFinishedListener {
    void onSuccess();
    void onAccountPending();

    void onFailure(String errorMessage);

    void onVehicleDetailSuccess(VehicleData[] vehicleInfo);

    void onVehicleDetailFail(int code);

    void onImageUrlObtained(List<VehicleDataModel> vehicleInfo);
}

package com.royalenfield.reprime.ui.home.homescreen.listener;

import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

public interface OnVehicleDetailsFinishedListener {
    void onVehicleDetailSuccess(VehicleData[] vehicleInfo);
    void onVehicleDetailSuccessFireStore(VehicleDetailResponseFirestore vehicleDetailResponseFirestore);
    void onVehicleDetailFail(int code);

    void onImageUrlObtained(List<VehicleDataModel> vehicleInfo);
}

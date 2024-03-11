package com.royalenfield.reprime.ui.home.homescreen.listener;

import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

public interface OnRealtimeVehicledetailListner {

    void onVehicleDetailSuccessFireStore(List<VehicleDataModel> list);

}

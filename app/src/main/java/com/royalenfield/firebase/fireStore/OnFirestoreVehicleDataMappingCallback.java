package com.royalenfield.firebase.fireStore;

import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

public interface OnFirestoreVehicleDataMappingCallback {
    void onVehicleMappingSuccess(List<VehicleDataModel> vehicleModel,int position);
}

package com.royalenfield.firebase.fireStore;

import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

/**
 * Interface for Firestore vehicledetails call
 */
public interface OnFirestoreVehicleListCallback {

    void onFirestoreVehicleDetailsSuccess(List<VehicleDataModel> list);

    void onFirestoreVehicleDetailsFailure(String message);
}

package com.royalenfield.reprime.ui.splash.presenter;

import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;

public interface GetVehicleListener {

    void onFirebaseVehicleDetailSuccess(VehicleData[] vehicleInfo);

    void onFirebaseVehicleDetailFailure();

}

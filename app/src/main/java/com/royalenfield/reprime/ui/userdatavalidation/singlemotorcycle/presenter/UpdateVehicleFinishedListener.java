package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter;

public interface UpdateVehicleFinishedListener {
    void onUpdateSuccess();

    void onUpdateFail(String errorMessageFromCode);
}

package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners;

import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;

public interface OnVerifyDetailListener {
    void onSuccess(String code, String message, AddVehicleResponse addVehicleResponse);

    void onFailure(String errorMessage);
}

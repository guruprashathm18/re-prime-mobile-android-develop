package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views;


import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;

public interface AddVehicleView{

    void onPurchaseDateSelected(String date);
    void enableVerifyButton();
    void disableVerifyButton();
    void onSuccessResponse(String code, String message, AddVehicleResponse addVehicleResponse);
    void onnInvalidChassisNumber();
    void onnInvalidRegistrationNumber();
    void onFailureResponse(String message);
    void hideRegistrationNumberError();
    void hideChassisNumberError();
}

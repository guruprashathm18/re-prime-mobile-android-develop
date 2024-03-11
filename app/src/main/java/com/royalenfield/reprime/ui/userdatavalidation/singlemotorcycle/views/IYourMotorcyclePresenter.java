package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views;

import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.view.UpdateVehicleData;

public interface IYourMotorcyclePresenter {
    void onConfirmButtonClicked(UpdateVehicleData updateVehicleData, String updatedRegistrationNum);

    void onDestroy();

    boolean isRegistrationNumberValid(String registerationNum);
}

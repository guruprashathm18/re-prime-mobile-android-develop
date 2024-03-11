package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor;

import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnVerifyDetailListener;

public interface IAddVehicleInteractor {

    void verifyVehicleDetail(String date, String engineNumber, OnVerifyDetailListener onVerifyDetailListener);

}

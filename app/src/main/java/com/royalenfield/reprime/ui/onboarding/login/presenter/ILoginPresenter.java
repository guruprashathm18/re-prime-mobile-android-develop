package com.royalenfield.reprime.ui.onboarding.login.presenter;

import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

/**
 * @author BOP1KOR on 12/12/2018.
 */

public interface ILoginPresenter {
    /**
     * Does the username password check and calls the login api if both are valid.
     *
     * @param username username.
     * @param password password.
     */
    void validateLogin(String username, String password);

    void getVehicleDetails(String mobileNo);

    List<VehicleDataModel> mapVehicleResponseToVehicleViewModel(VehicleData[] vehicleInfo);
}

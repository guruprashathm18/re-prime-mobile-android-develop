package com.royalenfield.reprime.ui.onboarding.login.interactor;

import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.login.listeners.OnLoginFinishedListener;

import java.util.List;

/**
 * @author BOP1KOR on 12/12/2018.
 * An Interactor helps models cross application boundaries such as networks or serialization
 * This LoginInteractor knows nothing about a UI or the LoginPresenter
 */

public interface ILoginInteractor {
    /**
     * Validate the login credentials and allows user to log in if success.
     *
     * @param username              user name.
     * @param password              user password.
     * @param loginFinishedListener Interactor listener {@link OnLoginFinishedListener } to update the view on api success or failure.
     */
    //void login(String username, String password, OnLoginFinishedListener loginFinishedListener);

    /**
     * Validate the login credentials and allows user to log in if success.
     *
     * @param credentials           credentials.
     * @param loginFinishedListener Interactor listener {@link OnLoginFinishedListener } to update the view on api success or failure.
     */
    void login(String credentials, OnLoginFinishedListener loginFinishedListener);

    void getVehicleDetails(String mobileNumber,  final OnLoginFinishedListener loginFinishedListener);

}

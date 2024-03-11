package com.royalenfield.reprime.ui.onboarding.login.views;

import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

/**
 * @author BOP1KOR on 12/7/2018.
 * <p>
 * LoginView, implemented by Login Activity,
 * will contain a reference to the presenter.
 * The only thing that the LoginView will do is to call a
 * method from the Presenter every time there is an interface action
 */

public interface LoginView extends REMvpView {

    void onUsernamePasswordEmpty();

    void onUsernameEmptyError();

    void onPasswordEmptyError();

    void onLoginSuccess();

    void onFailure(String errorMessage);

    void hideUserNameError();

    void hidePasswordError();

    void invalidUserId();

    void onAccountPending();

//    void onVehicleDetailSuccess(List<VehicleDataModel> list);
//
//    void onVehicleDetailFail(int errorMessageFromCode);
}

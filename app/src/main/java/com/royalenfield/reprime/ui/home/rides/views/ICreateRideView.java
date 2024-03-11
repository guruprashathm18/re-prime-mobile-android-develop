package com.royalenfield.reprime.ui.home.rides.views;

import com.royalenfield.reprime.base.REMvpView;

public interface ICreateRideView extends REMvpView {

    void onCreateRideSuccess();

    void onFailure(String errorMessage);
}

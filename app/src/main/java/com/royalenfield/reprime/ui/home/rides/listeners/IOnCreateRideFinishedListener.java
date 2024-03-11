package com.royalenfield.reprime.ui.home.rides.listeners;

public interface IOnCreateRideFinishedListener {

    void onSuccess();

    void onFailure(String errorMessage);
}

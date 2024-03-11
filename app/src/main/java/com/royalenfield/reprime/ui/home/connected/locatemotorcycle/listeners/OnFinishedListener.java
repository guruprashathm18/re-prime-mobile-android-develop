package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.listeners;

import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.ConnectedResponse;

public interface OnFinishedListener {

    void onSuccess(ConnectedResponse payload);

    void onFailure(String errorMessage);
}

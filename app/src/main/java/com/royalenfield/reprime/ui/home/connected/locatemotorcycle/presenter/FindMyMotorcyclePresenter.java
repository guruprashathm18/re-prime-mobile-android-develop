package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.presenter;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.fragments.FindMyMotorcycleFragment;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.interactor.FindMyMotorcycleInteractor;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.listeners.OnFinishedListener;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.ConnectedResponse;
import com.royalenfield.reprime.utils.REUtils;

import ua.naiksoftware.stomp.StompClient;


public class FindMyMotorcyclePresenter implements OnFinishedListener {

    private static final String TAG = FindMyMotorcyclePresenter.class.getSimpleName();
    private final FindMyMotorcycleFragment mView;
    private final FindMyMotorcycleInteractor interactor;

    public FindMyMotorcyclePresenter(FindMyMotorcycleFragment mView, FindMyMotorcycleInteractor interactor) {
        this.mView = mView;
        this.interactor = interactor;
    }

    public void fetchDeviceData(String deviceId,boolean shoeLoader) {
        if (deviceId == null) {
            return;
        }

        if (REApplication.CODE_STUB_DEMO) {
            getStubbedData();
        } else {
            if(shoeLoader)
            mView.showLoading();
            interactor.fetchDeviceData(deviceId, this);
        }
    }

    private void getStubbedData() {
        ConnectedResponse deviceLatestLocation = new Gson().fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(),
                "device_latest_location.json"), ConnectedResponse.class);
        onSuccess(deviceLatestLocation);
    }

    public StompClient getStompClient() {
        return interactor.getStompclient();
    }

    public void disconnectStompClient() {
        interactor.disconnectStompClient();
    }


    public void topicUnSubscription(String deviceId) {
        interactor.topicUnSubscription(deviceId);
    }

    @Override
    public void onSuccess(ConnectedResponse payload) {
        mView.hideLoading();
        mView.serviceSuccess(payload);
    }

    public void getDataInEveryTenSec(String deviceId) {
        interactor.fetchDataInEveryTenSecs(deviceId);
    }


    @Override
    public void onFailure(String errorMessage) {
        mView.hideLoading();
        mView.serviceFails(errorMessage);
    }
}

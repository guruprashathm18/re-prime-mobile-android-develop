package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.interactor.CreateRideInteractor;
import com.royalenfield.reprime.ui.home.rides.views.ICreateRideView;

public class CreateRidePresenter implements RidesListeners.IOnCreateRideFinishedListener, ICreateRidePresenter {

    private CreateRideInteractor mCreateRideInteractor;
    private ICreateRideView mCreateRideView;

    public CreateRidePresenter(ICreateRideView mCreateRideView, CreateRideInteractor mCreateRideInteractor) {
        this.mCreateRideInteractor = mCreateRideInteractor;
        this.mCreateRideView = mCreateRideView;
    }

    @Override
    public void createRide(CreateRideRequest createRideRequest, String filePath) {
        mCreateRideView.showLoading();
        // This is for api call
        mCreateRideInteractor.createRide(createRideRequest, filePath, this);
    }

    @Override
    public void onSuccess() {
        if (mCreateRideView != null) {
            mCreateRideView.onCreateRideSuccess();
            mCreateRideView.hideLoading();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mCreateRideView != null) {
            mCreateRideView.onFailure(errorMessage);
            mCreateRideView.hideLoading();
        }
    }

    public void onDestroy() {
        mCreateRideView = null;
    }

}

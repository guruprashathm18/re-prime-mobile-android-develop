package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.interactor.RidesInteractor;
import com.royalenfield.reprime.ui.home.rides.views.RidesView;

/**
 * Presenter for fetch checkIns API
 */
public class RidesPresenter implements RidesListeners.OnCheckInAPIFinishedListener, RidesListeners.ICheckInPresenter,
        RidesListeners.OnPublishRideAPIFinishedListener, RidesListeners.OnUpdateRideStatusAPIFinishedListener {

    private RidesView mRidesView;
    private RidesInteractor mRidesInteractor;

    public RidesPresenter(RidesView ridesView, RidesInteractor ridesInteractor) {
        this.mRidesView = ridesView;
        this.mRidesInteractor = ridesInteractor;
    }

    @Override
    public void getCheckIns(String rideId) {
        if (mRidesView != null) mRidesView.showLoading();
        mRidesInteractor.getCheckIns(rideId, this);
    }


    @Override
    public void onSuccess(CheckInResponse response) {
        if (mRidesView != null) {
            mRidesView.onGetCheckInSuccess(response);
            mRidesView.hideLoading();
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mRidesView != null) {
            mRidesView.onGetCheckInFailure(errorMessage);
            mRidesView.hideLoading();
        }
    }

    @Override
    public void publishRide(String rideId) {
        if (mRidesView != null) mRidesView.showLoading();
        mRidesInteractor.publishRide(rideId, this);
    }

    @Override
    public void startRide(String rideId) {
        if (mRidesView != null) mRidesView.showLoading();
        mRidesInteractor.startRide(rideId, this);
    }

    @Override
    public void endRide(String rideId) {
        if (mRidesView != null) mRidesView.showLoading();
        mRidesInteractor.endRide(rideId, this);
    }

    @Override
    public void onPublishRideSuccess() {
        if (mRidesView != null) {
            mRidesView.hideLoading();
            mRidesView.onPublishRideSuccess();
        }
    }

    @Override
    public void onPublishRideFailure(String errorMessage) {
        if (mRidesView != null) {
            mRidesView.hideLoading();
            mRidesView.onPublishRideFailure(errorMessage);
        }
    }


    @Override
    public void onUpdateRideStatusSuccess() {
        if (mRidesView != null) {
            mRidesView.onUpdateRideStatusSuccess();
        }
    }

    @Override
    public void onUpdateRideStatusFailure(String errorMessage) {
        if (mRidesView != null) {
            mRidesView.hideLoading();
            mRidesView.onUpdateRideStatusFailure(errorMessage);
        }
    }
}

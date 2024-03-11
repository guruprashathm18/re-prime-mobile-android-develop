package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.interactor.JoinRideInteractor;
import com.royalenfield.reprime.ui.home.rides.views.JoinRideView;

public class JoinRidePresenter implements RidesListeners.OnAPICallFinishedListener, IJoinRidePresenter {

    private JoinRideView mJoinRideView;
    private JoinRideInteractor mJoinRideInteractor;

    public JoinRidePresenter(JoinRideView joinRideView, JoinRideInteractor joinRideInteractor) {
        this.mJoinRideView = joinRideView;
        this.mJoinRideInteractor = joinRideInteractor;
    }

    @Override
    public void onSuccess() {
        if (mJoinRideView != null) mJoinRideView.onJoinRideSuccess();
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mJoinRideView != null) mJoinRideView.onJoinRideFailure(errorMessage);
    }

    @Override
    public void joinRide(String rideType, String rideId) {
        mJoinRideInteractor.joinRide(rideType, rideId, this);
    }
}

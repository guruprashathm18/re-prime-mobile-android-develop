package com.royalenfield.reprime.ui.home.rides.interactor;


import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;

public interface IJoinRideInteractor {

    void joinRide(String rideType, String rideId, RidesListeners.OnAPICallFinishedListener listener);
}

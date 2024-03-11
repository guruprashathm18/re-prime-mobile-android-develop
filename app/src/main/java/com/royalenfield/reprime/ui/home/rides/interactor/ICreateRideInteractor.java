package com.royalenfield.reprime.ui.home.rides.interactor;

import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.ui.home.rides.listeners.IOnCreateRideFinishedListener;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;

import java.util.ArrayList;

public interface ICreateRideInteractor {

    void createRide(CreateRideRequest createRideRequest, String filePath, final RidesListeners.IOnCreateRideFinishedListener listener);

}
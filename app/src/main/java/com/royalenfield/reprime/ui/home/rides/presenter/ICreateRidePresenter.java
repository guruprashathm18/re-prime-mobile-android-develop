package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;

import java.util.ArrayList;

public interface ICreateRidePresenter {

    void createRide(CreateRideRequest createRideRequest, String filePath);
}

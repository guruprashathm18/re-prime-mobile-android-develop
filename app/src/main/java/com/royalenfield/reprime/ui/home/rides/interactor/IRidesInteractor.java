package com.royalenfield.reprime.ui.home.rides.interactor;

import com.royalenfield.reprime.ui.home.rides.listeners.OnRidesResponseListener;
import com.royalenfield.reprime.models.request.web.rides.DealerRideRequest;
import com.royalenfield.reprime.models.request.web.rides.MarqueeRideRequest;
import com.royalenfield.reprime.models.request.web.rides.PopularRideRequest;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface IRidesInteractor {
    void getDealerRideList(DealerRideRequest dealerRideRequest, OnRidesResponseListener ridesResponseListener);

    void getMarqueeList(MarqueeRideRequest marqueeRideRequest, OnRidesResponseListener ridesResponseListener);

    void getPopularRides(PopularRideRequest marqueeRequest, OnRidesResponseListener ridesResponseListener);
}

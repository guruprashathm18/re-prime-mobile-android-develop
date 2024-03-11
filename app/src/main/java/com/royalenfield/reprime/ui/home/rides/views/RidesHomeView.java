package com.royalenfield.reprime.ui.home.rides.views;

import com.royalenfield.reprime.base.REMvpView;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface RidesHomeView extends REMvpView {

    void onDealerRideResponse();

    void onDealerRideFailure(String errorMessage);

    void onPopularRideResponse();

    void onPopularRideResponseFailure(String throwable);

    void onMarqueeRideResponse();

    void onMarqueeRideResponseFailure(String errorMessage);

    void onUserCreatedRideResponse();

    void onUserCreatedRideResponseFailure(String errorMessage);

}

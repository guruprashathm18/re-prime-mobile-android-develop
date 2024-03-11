package com.royalenfield.reprime.ui.home.rides.listeners;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface OnRidesResponseListener {

    void onDealerRideResponse();

    void onDealerRideFailure(String errorMessage);

    void onMarqueeResponseSuccess();

    void onMarqueeResponseFailure(String string);

    void onPopularResponseSuccess();

    void onPopularResponseFailure(String string);

}

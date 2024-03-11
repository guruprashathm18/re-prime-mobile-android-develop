package com.royalenfield.reprime.ui.home.rides.views;

import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface RidesView extends REMvpView {

    void onGetCheckInSuccess(CheckInResponse response);

    void onGetCheckInFailure(String errorMessage);

    void onPublishRideSuccess();

    void onPublishRideFailure(String errorMessage);

    void onUpdateRideStatusSuccess();

    void onUpdateRideStatusFailure(String errorMessage);
}

package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.ui.home.rides.views.CheckInRowView;

public interface ICheckInRowPresenter {

    /**
     * For binding the data to check-in view
     *
     * @param listPosition
     * @param checkInRowView
     */

    void onBindCheckInViewAtPosition(int listPosition, CheckInRowView checkInRowView);

    /**
     * For getting the list item count
     * @return
     */
    int getCheckInCount(CheckInResponse response);

}

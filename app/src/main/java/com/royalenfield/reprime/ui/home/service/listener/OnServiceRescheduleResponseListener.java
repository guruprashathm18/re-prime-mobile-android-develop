package com.royalenfield.reprime.ui.home.service.listener;

/**
 * @author BOP1KOR on 1/30/2019.
 */

public interface OnServiceRescheduleResponseListener {
    void onRescheduleSuccess(String bookingNo);

    void onRescheduleFailure(String errorMessage);
}

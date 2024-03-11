package com.royalenfield.reprime.ui.riderprofile.views;

import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.response.web.booking.BookingResponse;

public interface BookingView extends REMvpView {

    void bookingSuccess(BookingResponse response);

    void bookingFailure(String error);

    void bookingGMASuccess(BookingResponse response);

    void bookingGMAFailure(String error);
}

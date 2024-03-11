package com.royalenfield.reprime.ui.riderprofile.listeners;

import com.royalenfield.reprime.models.response.web.booking.BookingResponse;

public interface GMABookingDataListener {
    void onGMASuccess(BookingResponse response);

    void onGMAFailure(String errorMessage);
}

package com.royalenfield.reprime.ui.riderprofile.listeners;

import com.royalenfield.reprime.models.response.web.booking.BookingResponse;
import com.royalenfield.reprime.models.response.web.booking.Datum;

public interface BookingDataListener {
    void onSuccess(BookingResponse response);

    void onFailure(String errorMessage);
}

package com.royalenfield.reprime.ui.riderprofile.interactor;

import com.royalenfield.reprime.ui.riderprofile.listeners.BookingDataListener;
import com.royalenfield.reprime.ui.riderprofile.listeners.GMABookingDataListener;

public interface IBookingInteractor {
    public void getBookingData(String guid, BookingDataListener listener);
    public void getGMABookingData(String guid, GMABookingDataListener listener);
}

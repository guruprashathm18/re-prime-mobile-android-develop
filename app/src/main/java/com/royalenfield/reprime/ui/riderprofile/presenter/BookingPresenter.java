package com.royalenfield.reprime.ui.riderprofile.presenter;

import com.royalenfield.reprime.models.response.web.booking.BookingResponse;
import com.royalenfield.reprime.ui.riderprofile.interactor.BookingIntrector;
import com.royalenfield.reprime.ui.riderprofile.listeners.BookingDataListener;
import com.royalenfield.reprime.ui.riderprofile.listeners.GMABookingDataListener;
import com.royalenfield.reprime.ui.riderprofile.views.BookingView;

public class BookingPresenter implements IBookingPresenter, BookingDataListener, GMABookingDataListener {

    private BookingIntrector bookingIntrector;
    private BookingView bookingView;

    public BookingPresenter(BookingView bookingView, BookingIntrector bookingIntrector) {
        this.bookingView = bookingView;
        this.bookingIntrector = bookingIntrector;
    }

    @Override
    public void getBookingData(String guId) {
        bookingIntrector.getBookingData(guId, this);
    }

    @Override
    public void onSuccess(BookingResponse response) {
        bookingView.bookingSuccess(response);
    }

    @Override
    public void onFailure(String errorMessage) {
        bookingView.bookingFailure(errorMessage);
    }

    @Override
    public void getGMABookingData(String guId) {
        bookingIntrector.getGMABookingData(guId, this);
    }

    @Override
    public void onGMASuccess(BookingResponse response) {
        bookingView.bookingGMASuccess(response);
    }

    @Override
    public void onGMAFailure(String errorMessage) {
        bookingView.bookingGMAFailure(errorMessage);
    }
}

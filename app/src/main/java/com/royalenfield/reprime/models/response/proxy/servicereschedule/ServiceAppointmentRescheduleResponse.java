package com.royalenfield.reprime.models.response.proxy.servicereschedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceAppointmentRescheduleResponse {

    @SerializedName("bookingno")
    @Expose
    private String bookingno;
    @SerializedName("message")
    @Expose
    private String message;

    public String getBookingno() {
        return bookingno;
    }

    public void setBookingno(String bookingno) {
        this.bookingno = bookingno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

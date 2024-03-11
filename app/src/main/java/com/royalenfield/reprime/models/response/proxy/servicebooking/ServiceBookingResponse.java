package com.royalenfield.reprime.models.response.proxy.servicebooking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceBookingResponse {

    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("bookingno")
    @Expose
    private String bookingno;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("message")
    @Expose
    private String message;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBookingno() {
        return bookingno;
    }

    public void setBookingno(String bookingno) {
        this.bookingno = bookingno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

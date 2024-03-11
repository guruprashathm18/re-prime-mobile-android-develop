package com.royalenfield.reprime.models.request.web.userdetails;

/**
 * This is the Request for sending to retrofit api call
 */
public class UserDetailsRequest {

    private String mobilenumber;

    private String userid;

    public UserDetailsRequest(String mobile, String userid) {
        this.mobilenumber = mobile;
        this.userid = userid;
    }

    public String getMobile() {
        return mobilenumber;
    }

    public void setMobile(String mobile) {
        this.mobilenumber = mobile;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

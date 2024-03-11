package com.royalenfield.reprime.models.request.web.otp;

public class SendOtpRequest {

    private String mobile;
    private String callingCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }
}

package com.royalenfield.reprime.models.request.web.forgotpassword;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public class OtpRequest {

    private String phoneNo;

    public OtpRequest(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return "OtpRequest{" +
                "phoneNo='" + phoneNo + '\'' +
                '}';
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}

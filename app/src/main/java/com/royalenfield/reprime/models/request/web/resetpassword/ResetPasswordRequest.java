package com.royalenfield.reprime.models.request.web.resetpassword;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public class ResetPasswordRequest {

    private String username;

    private String password;

    private String otp;

    public ResetPasswordRequest(String phoneNo, String password, String otp) {
        this.username = phoneNo;
        this.password = password;
        this.otp = otp;
    }

    public String getPhoneNo() {
        return username;
    }

    public void setPhoneNo(String phoneNo) {
        this.username = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "phoneNo='" + username + '\'' +
                ", password='" + password + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}

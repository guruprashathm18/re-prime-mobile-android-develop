package com.royalenfield.reprime.models.request.web.signup;

public class VerifyAccountRequest {
    private String userId;

    private String otp;

    private String username;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public VerifyAccountRequest() {
    }

    public VerifyAccountRequest(String userId, String otp,String username) {
        this.userId = userId;
        this.otp = otp;
        this.username=username;
    }

}

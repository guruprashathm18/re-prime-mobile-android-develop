package com.royalenfield.reprime.ui.userdatavalidation.otp.presenter;

import com.royalenfield.reprime.models.request.web.otp.SendOtpRequest;

public interface IOtpPresenter {

    void submitOtp(String secondaryPhone, String otp,String callingCode,String email);



void sendOTP(String secondaryDialingCode, String secondaryPhone);

}

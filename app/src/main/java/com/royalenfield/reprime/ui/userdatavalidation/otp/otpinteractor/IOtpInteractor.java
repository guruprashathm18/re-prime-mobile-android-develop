package com.royalenfield.reprime.ui.userdatavalidation.otp.otpinteractor;

import com.royalenfield.reprime.models.request.web.otp.SendOtpRequest;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.userdatavalidation.otp.presenter.OnSecondaryUpdateCallback;
import com.royalenfield.reprime.ui.userdatavalidation.otp.presenter.OnUpdateSecondaryFinishListener;
import com.royalenfield.reprime.ui.userdatavalidation.otp.presenter.OtpPresenter;
import com.royalenfield.reprime.ui.userdatavalidation.otp.views.OnSubmitOtpListener;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.OnUpdateContactFinishedListener;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.UpdateContactRequestModel;

public interface IOtpInteractor {
    void requestForOtp(SendOtpRequest req, OnSendOtpResponseListener listener);

    void submitOtp(String fName,String lName,String secondaryPhone, String otp,String callingCode, String email,OnSubmitOtpListener listener);


}

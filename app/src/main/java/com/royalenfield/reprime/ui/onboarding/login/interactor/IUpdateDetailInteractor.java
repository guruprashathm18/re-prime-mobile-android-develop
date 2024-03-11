package com.royalenfield.reprime.ui.onboarding.login.interactor;

import com.royalenfield.reprime.ui.onboarding.login.listeners.OnUpdateDetailFinishListner;
import com.royalenfield.reprime.ui.onboarding.signup.listeners.OnCountryDataListener;
import com.royalenfield.reprime.ui.onboarding.signup.listeners.OnSignUpFinishedListener;

public interface IUpdateDetailInteractor {

    void verifyPersonalDetail(String fName, String lName, String callingCode, String phone, String email, OnUpdateDetailFinishListner updateDetailFinishListner);
    void updatePersonalDetail(String fName, String lName, String callingCode, String phone, String email,String otp, OnUpdateDetailFinishListner updateDetailFinishListner);

}

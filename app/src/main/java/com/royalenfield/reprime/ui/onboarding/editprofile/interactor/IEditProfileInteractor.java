package com.royalenfield.reprime.ui.onboarding.editprofile.interactor;

import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;

public interface IEditProfileInteractor {

    void updateProfile(String fName, String lName, String emailId, String phone, String dob,
                       String city, String password, String aboutMe, OnEditProfileFinishedListener editProfileFinishedListener);

    void getProfileDetails(String userId, OnEditProfileFinishedListener editProfileFinishedListener);
}

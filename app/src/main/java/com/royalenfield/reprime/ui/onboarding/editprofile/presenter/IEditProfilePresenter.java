package com.royalenfield.reprime.ui.onboarding.editprofile.presenter;

public interface IEditProfilePresenter {

    //Does the validation of fields
    void validateEditProfile(String emailId, String phone, String firstName, String lastName,
                             String dob, String city, String aboutme, String password);


}

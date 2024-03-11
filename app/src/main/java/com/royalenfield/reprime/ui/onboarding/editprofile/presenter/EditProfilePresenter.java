package com.royalenfield.reprime.ui.onboarding.editprofile.presenter;

import android.util.Log;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.ui.onboarding.editprofile.interactor.EditProfileInteractor;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.editprofile.views.EditProfileView;
import com.royalenfield.reprime.utils.REUtils;

/*
 * transfers data between model and view
 * */

public class EditProfilePresenter implements IEditProfilePresenter, OnEditProfileFinishedListener {

    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    private EditProfileView mEditProfileView;
    private EditProfileInteractor mEditProfileInteractor;

    public EditProfilePresenter(EditProfileView mEditProfileView, EditProfileInteractor mEditProfileInteractor) {
        this.mEditProfileView = mEditProfileView;
        this.mEditProfileInteractor = mEditProfileInteractor;
    }

    @Override
    public void onUpdateSuccess() {
        if (mEditProfileView != null) {
            mEditProfileView.onUpdateSuccess();
        }
    }

    @Override
    public void onUpdateFailure(String errorMessage) {
        if (mEditProfileView != null) {
            mEditProfileView.onUpdateFailure(errorMessage);
        }
    }
/*

    @Override
    public void onGetProfileDetailsSuccess() {
        if (mEditProfileView != null) {
            mEditProfileView.onGetProfileDetailsSuccess();
        }
    }

    @Override
    public void onGetProfileDetailsFailure(String errorMessage) {
        if (mEditProfileView != null) {
            mEditProfileView.onGetProfileDetailsFailure(errorMessage);
        }
    }*/

    @Override
    public void validateEditProfile(String fName, String lName, String emailId, String phone,
                                    String dob, String city, String aboutme, String password) {
        if (isFieldsEmpty(emailId, phone, fName, lName, dob, city, aboutme, password)) {
            return;
        }
        Log.d(TAG, "onClick: " + "CLICK");
        if (!REUtils.isValidMobileNumber(phone) && mEditProfileView != null) {
            Log.d(TAG, "Invalid mobile number");
            mEditProfileView.invalidPhone();
            return;
        } else if (!REUtils.isValidEmailId(emailId) && mEditProfileView != null) {
            Log.d(TAG, "Invalid email id");
            mEditProfileView.invalidEmail();
            return;
        }
        if (mEditProfileView != null) {
            mEditProfileView.showLoading();
        }
        Log.d(TAG, "UpdateProfileDetails API called");
        mEditProfileInteractor.updateProfile(fName, lName, emailId, phone, dob, city, password, aboutme, this);
    }

    /*
     * checks if fields are empty
     * returns true if one of them is empty
     * returns false otherwise
     * */
    private boolean isFieldsEmpty(String emailId, String phone, String firstName, String lastName, String dob, String city, String aboutme, String password) {
        if (mEditProfileView != null) {
            if (emailId.isEmpty() && phone.isEmpty() && firstName.isEmpty() && lastName.isEmpty() && dob.isEmpty() && city.isEmpty() && password.isEmpty()) {
                mEditProfileView.onFieldsEmpty();
                return true;
            }
            if (emailId.isEmpty()) {
                mEditProfileView.onEmailEmptyError();
                return true;
            }
            if (phone.isEmpty()) {
                mEditProfileView.onPhoneEmptyError();
                return true;
            }
            if (firstName.isEmpty()) {
                mEditProfileView.onFirstNameEmptyError();
                return true;
            }
            if (lastName.isEmpty()) {
                mEditProfileView.onLastNameEmptyError();
                return true;
            }
            if (dob.isEmpty()) {
                mEditProfileView.onDobEmptyError();
                return true;
            }
            if (city.isEmpty()) {
                mEditProfileView.onCityEmptyError();
                return true;
            }
            if (password.isEmpty()) {
                //TODO need to uncomment
                mEditProfileView.onPasswordEmptyError();
                return true;
            }
        }
        return false;
    }

    public void onDestroy() {
        mEditProfileView = null;
    }

    /*
     * checks the length of fields and sets the visibility of the error textview to false
     * */
    public void checkErrorViewVisibility(String emailId, String phone, String firstName, String lastName, String dob, String city, String aboutme, String password) {
        if (emailId.length() >= 1) {
            mEditProfileView.hideEmailError();
        }
        if (phone.length() >= 1) {
            mEditProfileView.hidePhoneError();
        }
        if (firstName.length() >= 1) {
            mEditProfileView.hideFirstNameError();
        }
        if (lastName.length() >= 1) {
            mEditProfileView.hideLastNameError();
        }
        if (dob.length() >= 1) {
            mEditProfileView.hideDoBError();
        }
        if (city.length() >= 1) {
            mEditProfileView.hideCityError();
        }
        if (password.length() >= 1) {
            mEditProfileView.hidePasswordError();
        }
    }

    /**
     * Updates the local REUserModelStore
     *
     * @param firstName first name rider.
     * @param lastName  last name of rider
     * @param email     email id.
     * @param dob       date of birth.
     * @param phone     phone number.
     * @param city      city.
     */
    public void updateLoginUserDataModelStore(String firstName, String lastName, String email, String dob, String phone, String city, String aboutMe) {
        if (REUserModelStore.getInstance() != null) {
            REUserModelStore.getInstance().setFname(firstName);
            REUserModelStore.getInstance().setLname(lastName);
            REUserModelStore.getInstance().setEmail(email);
            REUserModelStore.getInstance().setPhoneNo(phone);
            REUserModelStore.getInstance().setDob(dob);
            REUserModelStore.getInstance().setCity(city);
            REUserModelStore.getInstance().setAboutMe(aboutMe);
        }
    }

    @Override
    public void onGetProfileDetailsSuccess() {

    }

    @Override
    public void onGetProfileDetailsFailure(String errorCode) {

    }
}

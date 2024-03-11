package com.royalenfield.reprime.ui.onboarding.editprofile.views;

/*
 * this is implemented by the edit profile activity
 * this calls the method from presenter when there is an interface action
 * */

import com.royalenfield.reprime.base.REMvpView;

public interface EditProfileView extends REMvpView {

    void onFieldsEmpty();

    void onEmailEmptyError();

    void onPhoneEmptyError();

    void onFirstNameEmptyError();

    void onLastNameEmptyError();

    void onDobEmptyError();

    void onCityEmptyError();

    void onPasswordEmptyError();

    void onUpdateSuccess();

    void onUpdateFailure(String errorMessage);

    void hideEmailError();

    void hidePhoneError();

    void hideFirstNameError();

    void hideLastNameError();

    void hideDoBError();

    void hideCityError();

    void hidePasswordError();

    void invalidEmail();

    void invalidPhone();

/*    void onGetProfileDetailsSuccess();

    void onGetProfileDetailsFailure(String errorMessage);*/
}

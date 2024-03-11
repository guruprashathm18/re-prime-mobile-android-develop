package com.royalenfield.reprime.ui.userdatavalidation.popup.views;

import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;

import java.util.List;

public interface PopUpView {

    void showErrorOnEmergency(int id);

    void showErrorOnEmail(int id);

    void showErrorOnPrimaryNumber(int id);

    void detailsAreValid(boolean isAnyApiCalled,boolean isUpdatePrimaryNumber);

    void hideEmailError();

    void hideEmergencyError();

    void showConfirmationScreen(int id);

    void showErrorOnSecondary1(int errorId);


    void onStateDataObtained(List<StateModel> stateList);

    void onCountryDataObtained(List<CountryModel> countryModels);

    void onCityObtained(List<CityModel> cityList);

    void showErrorOnPinCode(int error);

    void setSecondaryNumOnView(String number);

    void setSecondaryCountryCodeOnView(String callingCode);

    void setEmergencyNumOnView(String number);

    void setEmergencyCountryCodeOnView(String callingCode);

    void showErrorOnAddress1(int error);

    void showErrorOnAddress2(int error);

    void showErrorOnCountry(int error);

    void showErrorOnState(int error);

    void showErrorOnCity(int error);

    void showLoader();

    void showTryAgainError(String errorMsg);

    void hideLoader();

    void setPrimaryPhoneOnView(String number);

    void setPrimaryCountryCode(String callingCode);

    void showOtpWithSwapped();

    void showErrorOnView(String message);

    void uncheckedSwapping();
    void updateSuccess();

    void updateFailure(String errorMessage);
}

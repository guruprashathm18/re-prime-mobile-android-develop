package com.royalenfield.reprime.ui.userdatavalidation.popup.presenter;

import android.text.Editable;

import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;

import java.util.HashMap;

public interface IPopUpPresenter {

    // Validate fields
    // Show error if not valid
    // Show different screen if valid
    void onConfirmButtonClicked(String emergency, String email, String primaryPhone, CountryModel mSelectedEmergencyCode
            , CountryModel s1CountryCode, CountryModel selectedCountry
            , StateModel selectedState, CityModel selectedCity, String pinCode, String add1, String add2, boolean isPrimaryChecked);

    void onDestroy();

    boolean isEmailValid(String email);

    void getStateData(String code);

    void getCityData(String stateCode, String countryCode);

    void getUserContactDetails();

    void checkIsSwappable(String secondaryPhone1, String callingCode);

    void checkPrimaryIsUpdatable( String callingCode, String phone,String email);


}

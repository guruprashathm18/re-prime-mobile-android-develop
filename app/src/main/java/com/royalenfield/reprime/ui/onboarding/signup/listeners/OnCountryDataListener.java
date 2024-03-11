package com.royalenfield.reprime.ui.onboarding.signup.listeners;

import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;

import java.util.List;

public interface OnCountryDataListener {

    void onSuccess(List<CountryModel> listData);

    void onFailure(String errorMessage);


}

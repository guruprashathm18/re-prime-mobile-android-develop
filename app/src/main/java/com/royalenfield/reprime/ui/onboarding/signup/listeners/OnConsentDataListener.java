package com.royalenfield.reprime.ui.onboarding.signup.listeners;

import com.royalenfield.reprime.models.response.web.signup.RequestConsent;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;

import java.util.List;

public interface OnConsentDataListener {
    void onSuccess(RequestConsent listData);

    void onFailure(String errorMessage);
}

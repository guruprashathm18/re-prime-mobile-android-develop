package com.royalenfield.reprime.ui.onboarding.login.views;

import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;

import java.util.List;

public interface UpdateDetailView extends REMvpView {
    void invalidEmail();

    void invalidMobile(boolean isMobile);

    void hideEmailError();

    void hidePhoneError();

    void countryData(List<CountryModel> countryData);

    void updateSuccess();

    void updateFailure(String errorMessage);
}

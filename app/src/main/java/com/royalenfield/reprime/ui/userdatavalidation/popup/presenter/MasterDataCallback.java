package com.royalenfield.reprime.ui.userdatavalidation.popup.presenter;

import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;

import java.util.ArrayList;
import java.util.List;

public interface MasterDataCallback {
    void onStateDataObtained(ArrayList<StateModel> stateList);

    void onCountryModelsObtained(List<CountryModel> countryModels);

    void onCityDataObtained(List<CityModel> cityList);
}

package com.royalenfield.firebase.realTimeDatabase;

import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CityModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.StateModel;

import java.util.ArrayList;
import java.util.List;

public interface OnStateandCityResponseCallback {

    void onStateListSuccess(ArrayList<StateModel> stateList);

    void onCityListSuccess(List<CityModel> cityList);


}

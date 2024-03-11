package com.royalenfield.reprime.ui.home.homescreen.presenter;

import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.List;

public interface IHomeActivityPresenter {
void getFirebaseCustomToken();
    List<VehicleDataModel> mapVehicleResponseToVehicleViewModel(VehicleData[] vehicleInfo);
    List<VehicleDataModel> mapVehicleFirestoreResponseToVehicleViewModel(VehicleDetailResponseFirestore vehicleDetailResponseFirestore);
}

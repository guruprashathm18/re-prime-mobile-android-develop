package com.royalenfield.reprime.ui.home.homescreen.view;

import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDResponseData;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IMotorcycleFragmentView {
    void onProvisionUpdateStatusSuccess(String msg);
    void onProvisionUpdateStatusFailure(String msg);
    void onDataSuccess(OBDResponseData payload);
    void onDataFailure(String errorMsg);
    void updateDeviceData(PairingMotorcycleResponseModel.GetDeviceData deviceData);
    void pairingDataFail(String msg);
    void updateSettingsData(SettingResponseModel settings);
    void checkConsentOfDevice();
    void consentSettingsUpdated(String msg);




}

package com.royalenfield.reprime.ui.home.connected.motorcycle.presenter;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDResponseData;
import com.royalenfield.reprime.ui.home.connected.motorcycle.fragment.VehicleSettingsFragment;
import com.royalenfield.reprime.ui.home.connected.motorcycle.interactor.MotorcycleInteractor;
import com.royalenfield.reprime.ui.home.connected.motorcycle.listener.MotorcycleListener;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.ProvisionUpdateStatusRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingRequestModelRead;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel;
import com.royalenfield.reprime.ui.home.homescreen.view.IMotorcycleFragmentView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ua.naiksoftware.stomp.StompClient;


public class MotorcyclePresenter implements MotorcycleListener {
    private IMotorcycleFragmentView mView;
    private MotorcycleInteractor interactor;

    public MotorcyclePresenter(IMotorcycleFragmentView mView, MotorcycleInteractor interactor) {
        this.mView = mView;
        this.interactor = interactor;
    }

//    public MotorcyclePresenter(VehicleSettingsFragment vehicleSettingsFragment, MotorcycleInteractor interactor) {
//        this.mFragmentView = vehicleSettingsFragment;
//        this.interactor = interactor;
//    }

    public void fetchOBDDataPoints(String deviceId) {
        if (deviceId == null) {
            return;
        }
       /* if (!mView.isLoaderShowing()) {
            mView.showLoading();
        }*/
        interactor.fetchOBDDataPoints(deviceId, this);
    }

    public void unSubscribeDeviceId(String deviceId) {
        if (deviceId == null) {
            return;
        }
        interactor.topicUnSubscription(deviceId);
    }

    public void getDataInEveryTenSec(String deviceId) {
        interactor.fetchDataInEveryTenSecs(deviceId);
    }

    public StompClient getStompClient() {
        return interactor.getStompclient();
    }

    public void disconnectStompClient() {
        interactor.disconnectStompClient();
    }

    @Override
    public void onSuccess(@NotNull OBDResponseData payload) {
        mView.onDataSuccess(payload);
    }

    @Override
    public void onFailure(@NotNull String errorMessage) {
        mView.onDataFailure(errorMessage);
    }

    /*Get Pairing key for Authentication and Provisioning*/
    public void getPairingKey(String chassisNo, String token,String calling,String mobile, String guid) {
//        mView.showLoading();
        if (REApplication.CODE_STUB_DEMO) {
            onPairingSuccess(interactor.getPairingStubData());
        } else {
            fetchPairingKeyMethod(chassisNo, token,calling,mobile,guid);
        }
    }

    private void fetchPairingKeyMethod(String chassisNo, String token,String calling,String mobile,String guid) {
        PairingMotorcycleRequestModel requestModel = new PairingMotorcycleRequestModel();
        requestModel.setVin(chassisNo);
        requestModel.setToken(token);
		requestModel.setCallingCode(calling);
		requestModel.setMobileNo(mobile);
		requestModel.setGuid(guid);
        interactor.fetchPairingKey(requestModel, this);
    }

    @Override
    public void onPairingSuccess(PairingMotorcycleResponseModel.@NotNull GetDeviceData deviceData) {
        mView.updateDeviceData(deviceData);
        // mView.hideLoading();
    }

    @Override
    public void onPairingFailure(@NotNull String errorMessage) {
        mView.pairingDataFail(errorMessage);
        // mView.hideLoading();
    }

    /*Device Setting Read or Write*/
    public void readSettings(String guID, String deviceSerialNo) {
        SettingRequestModelRead requestModel = new SettingRequestModelRead(guID, deviceSerialNo);
        if (REApplication.CODE_STUB_DEMO) {
            onReadSettingsSuccess(interactor.getSettingStubData());
        } else {
            interactor.readSettings(requestModel, this);
        }
    }

    public void updateSettings(String guID, String deviceSerialNo, boolean location, boolean notification) {
        if (guID.isEmpty()) {
            guID = REUserModelStore.getInstance().getUserId();
        }
        SettingRequestModel requestModel = getSettingsModel(guID, deviceSerialNo, location, notification);
        if (REApplication.CODE_STUB_DEMO) {
            onUpdateSettingsSuccess("");
        } else {
            interactor.updateSettings(requestModel, this);
        }
    }

    @Override
    public void onReadSettingsSuccess(@NotNull SettingResponseModel settings) {
        mView.updateSettingsData(settings);
        mView.checkConsentOfDevice();
    }

    @Override
    public void onUpdateSettingsSuccess(String msg) {

        if (mView != null) {

            mView.consentSettingsUpdated(msg);
        }
    }

    @Override
    public void onReadSettingsFailure(@Nullable String errorMessage) {
        if (mView != null)
        mView.onProvisionUpdateStatusFailure(errorMessage);

    }

    @Override
    public void onUpdateSettingsFailure(@NotNull String errorMessage) {
        if (mView != null)
        mView.onProvisionUpdateStatusFailure(errorMessage);
    }

    private SettingRequestModel getSettingsModel(String guID, String deviceSerialNo, boolean location, boolean notification) {
        return new SettingRequestModel(guID, deviceSerialNo, location, notification);
    }

    public void provisionUpdateStatus(ProvisionUpdateStatusRequestModel requestModel) {

        interactor.provisionUpdateStatus(requestModel, this);

    }

    @Override
    public void onProvisionUpdateStatusSuccess(@NotNull String msg) {
        if (mView != null)
            mView.onProvisionUpdateStatusSuccess(msg);
    }

    @Override
    public void onProvisionUpdateStatusFailure(@NotNull String errorMessage) {
        mView.onProvisionUpdateStatusFailure(errorMessage);

    }


}

package com.royalenfield.reprime.ui.home.homescreen.presenter;

import android.util.Log;

import com.royalenfield.firebase.fireStore.OnFirestoreVehicleListCallback;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.UserVehicle;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.VehicleDetailResponseFirestore;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.home.homescreen.interactor.HomeActivityInteractor;
import com.royalenfield.reprime.ui.home.homescreen.listener.FirebaseCustomTokenListener;
import com.royalenfield.reprime.ui.home.homescreen.listener.OnVehicleDetailsFinishedListener;
import com.royalenfield.reprime.ui.onboarding.LogResponse;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

public  class HomeActivityPresenter implements IHomeActivityPresenter, FirebaseCustomTokenListener {

    private static final String TAG = HomeActivityPresenter.class.getSimpleName();
    private HomeActivityInteractor homeActivityInteractor;
    private FirebaseCustomTokenListener callback;
	private long startTime;
	public long startTimeVehicle;
	public  String typeOfVehiclecall;
	public HomeActivityPresenter( HomeActivityInteractor homeActivityInteractor,FirebaseCustomTokenListener callback) {
        this.homeActivityInteractor = homeActivityInteractor;
        this.callback=callback;
    }



    @Override
    public void getFirebaseCustomToken() {
		startTime=System.currentTimeMillis();
		Log.e("RE_LOGGER","get Custom token api call start");
        homeActivityInteractor.getFirebaseCustomToken(this);
    }

    @Override
    public List<VehicleDataModel> mapVehicleResponseToVehicleViewModel(VehicleData[] vehicleInfo) {
        List<VehicleDataModel> list = new ArrayList<>();
        if (vehicleInfo != null) {
            for (VehicleData vehicleData : vehicleInfo) {
                list.add(new HomeActivityPresenter.VehicleDataMapper().map(vehicleData));
            }
        }
        return list;
    }

    @Override
    public List<VehicleDataModel> mapVehicleFirestoreResponseToVehicleViewModel(VehicleDetailResponseFirestore vehicleDetailResponseFirestore) {
        List<VehicleDataModel> list = new ArrayList<>();
        boolean flag = false;
        if (vehicleDetailResponseFirestore != null) {
            for (UserVehicle userVehicle : vehicleDetailResponseFirestore.getUserVehicles()) {
                if (userVehicle.getVehicleStatus().equalsIgnoreCase("VERIFIED")) {
                    list.add(new HomeActivityPresenter.VehicleDataMapper().mapFirestoreData(userVehicle));
                    flag = true;
                }
            }
            if (flag) {
                REUtils.CHECK_VEHICLE_PENDING = false;
            } else if (vehicleDetailResponseFirestore.getUserVehicles().size() > 0 && !flag) {
                REUtils.CHECK_VEHICLE_PENDING = true;
            }
        }
        return list;
    }

    public static class VehicleDataMapper {

        public VehicleDataModel map(VehicleData vehicleData) {
            VehicleDataModel dataModel = new VehicleDataModel();
            dataModel.setUserName(vehicleData.getUserName());
            dataModel.setChaissisNo(vehicleData.getChaissisNo());
            dataModel.setDateOfMfg(vehicleData.getDateOfMfg());
            dataModel.setEngineNo(vehicleData.getEngineNo());
            dataModel.setId(vehicleData.getId());
            dataModel.setPurchaseDate(vehicleData.getPurchaseDate());
            dataModel.setMobileNo(vehicleData.getMobileNo());
            dataModel.setModelCode(vehicleData.getModelCode());
            dataModel.setModelName(vehicleData.getModelName());
            dataModel.setRegistrationNo(vehicleData.getRegistrationNo());
            dataModel.setInvoiceNo(vehicleData.getInvoiceNo());
            dataModel.setAlternateNo(vehicleData.getAlternateNo());
            dataModel.setIsConnected(vehicleData.isConnectedMotorcycle());
            return dataModel;
        }

        public VehicleDataModel mapFirestoreData(UserVehicle userVehicle) {
            VehicleDataModel dataModel = new VehicleDataModel();
            dataModel.setUserName(userVehicle.getUserName());
            dataModel.setChaissisNo(userVehicle.getChassisNo());
            dataModel.setDateOfMfg(userVehicle.getDateOfMfg());
            dataModel.setEngineNo(userVehicle.getEngineNo());
            dataModel.setPurchaseDate(userVehicle.getPurchaseDate());
            dataModel.setModelCode(userVehicle.getVehicleModelCode());
            dataModel.setModelName(userVehicle.getVehicleModelName());
            dataModel.setRegistrationNo(userVehicle.getRegistrationNo());
            dataModel.setVehicleUpdateStatus(userVehicle.getVehicleUpdateStatus());
            dataModel.setIsConnected(userVehicle.getConnectedMotorcycle());
            return dataModel;
        }
    }

    @Override
    public void onFirebaseCustomTokenSuccess(String token,String reqId) {
		Log.e("RE_LOGGER","get custom token api end");
		REUtils.sendResponseLog("Custom token API", reqId, (double)(System.currentTimeMillis()-startTime) / 1000);

		if(callback!=null)
    callback.onFirebaseCustomTokenSuccess(token,reqId);
    }

    @Override
    public void onFirebaseCustomFailure(String error) {
        if(callback!=null)
            callback.onFirebaseCustomFailure(error);
    }

	@Override
	public void onFirebaseAuthSuccess() {
		if(callback!=null)
			callback.onFirebaseAuthSuccess();
	}
}

package com.royalenfield.reprime.ui.onboarding.login.presenter;

import android.util.Base64;
import android.util.Log;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleData;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.login.interactor.LoginInteractor;
import com.royalenfield.reprime.ui.onboarding.login.listeners.OnLoginFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.views.LoginView;
import com.royalenfield.reprime.utils.REUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BOP1KOR on 12/7/2018.
 * The {@link LoginPresenter} is responsible to act as the middle man between {@link LoginView} and Model.
 * It retrieves data from the Model and returns it formatted to the View.
 * But unlike the typical MVC, it also decides what happens when you interact with the View.
 */

public class LoginPresenter implements ILoginPresenter, OnLoginFinishedListener {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private LoginView mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginView mLoginView, LoginInteractor loginInteractor) {
        this.mLoginView = mLoginView;
        this.mLoginInteractor = loginInteractor;

    }

    @Override
    public void validateLogin(String username, String password) {
        if (isUserNamePasswordEmpty(username, password)) {
            return;
        }
        if (REUtils.isMobileNumber(username)) {
            if (!REUtils.isValidMobileNumber(username) && mLoginView != null) {
                Log.d(TAG, "Invalid mobile number");
                mLoginView.invalidUserId();
                return;
            }
        } else if (!REUtils.isValidEmailId(username) && mLoginView != null) {
            Log.d(TAG, "Invalid email id");
            mLoginView.invalidUserId();
            return;
        }
        if (mLoginView != null) {
            mLoginView.showLoading();
        }
	//	REApplication.getInstance().startTrace(String.valueOf(REApplication.TRACES.user_login));
        String credentials = username + ":" + password;
        byte[] data = credentials.getBytes(StandardCharsets.UTF_8);
        // Converting user credentials to BASE64
        Log.e("LOGIN","API CALL START");
        mLoginInteractor.login(Base64.encodeToString(data, Base64.NO_WRAP), this);
        //mLoginInteractor.login(username, password, this);
    }

    @Override
    public void getVehicleDetails(String mobileNo) {
        mLoginInteractor.getVehicleDetails(mobileNo, this);
    }

    @Override
    public List<VehicleDataModel> mapVehicleResponseToVehicleViewModel(VehicleData[] vehicleInfo) {

        List<VehicleDataModel> list = new ArrayList<>();

        if (vehicleInfo!=null) {
            for (VehicleData vehicleData : vehicleInfo) {
                list.add(new VehicleDataMapper().map(vehicleData));
            }
        }
        return list;
    }

    /**
     * Checks if username or password empty.
     *
     * @param username username
     * @param password password
     * @return true if any one of the empty else false.
     */
    public boolean isUserNamePasswordEmpty(String username, String password) {
        if (mLoginView != null) {
            if (username.isEmpty() && password.isEmpty()) {
                Log.d(TAG, "username password is empty");
                mLoginView.onUsernamePasswordEmpty();
                return true;
            }
            if (username.isEmpty()) {
                Log.d(TAG, "username is empty");
                mLoginView.onUsernameEmptyError();
                return true;
            }
            if (password.isEmpty()) {
                Log.d(TAG, "password is empty");
                mLoginView.onPasswordEmptyError();
                return true;
            }
        }

        return false;
    }


    @Override
    public void onSuccess() {
        if (mLoginView != null) {
            Log.e("VehicleDetails:", "Presenter login Success");
            mLoginView.onLoginSuccess();
        }
    }

    @Override
    public void onAccountPending() {
        if (mLoginView != null) {
            Log.e("VehicleDetails:", "Presenter account Pending");
            mLoginView.onAccountPending();
        }
    }

    @Override
    public void onVehicleDetailSuccess(VehicleData[] vehicleInfo) {

        List<VehicleDataModel> list = mapVehicleResponseToVehicleViewModel(vehicleInfo);
//        mLoginInteractor.fetchVehicleImagesCorrespondingToVehicleData(this, list);
        if (mLoginView != null) {
          //  mLoginView.onVehicleDetailSuccess(list);
        }
    }

    @Override
    public void onVehicleDetailFail(int code) {
        if (mLoginView != null) {
          //  mLoginView.onVehicleDetailFail(code);
        }
    }

    @Override
    public void onImageUrlObtained(List<VehicleDataModel> list) {
        if (mLoginView != null) {
         //   mLoginView.onVehicleDetailSuccess(list);
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mLoginView != null) {
            mLoginView.onFailure(errorMessage);
            mLoginView.hideLoading();
        }
    }

    public void onDestroy() {
        mLoginView = null;
    }

    /**
     * Checks the username password length and make associated error text view invisible if it is correct.
     *
     * @param username username.
     * @param password password.
     */
    public void checkErrorViewVisibility(String username, String password) {
        if (username.length() >= 1) {
            mLoginView.hideUserNameError();
        }
        if (password.length() >= 1) {
            mLoginView.hidePasswordError();
        }
    }


/*    public void getProfileDetailsFromServer() {
        LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
        if (loginResponse != null && loginResponse.getData() != null && loginResponse.getData().getUser() != null) {
            Log.d("API", "Website : getProfileDetails API called");
                    new EditProfileInteractor().getProfileDetails(loginResponse.getData().getUser().getUserId(), null);
        }
    }*/


    private class VehicleDataMapper {

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
            return dataModel;
        }
    }
}

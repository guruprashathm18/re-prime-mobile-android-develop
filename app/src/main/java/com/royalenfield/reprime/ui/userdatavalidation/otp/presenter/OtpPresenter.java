package com.royalenfield.reprime.ui.userdatavalidation.otp.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.royalenfield.firebase.fcm.FCMTokenRegistrationInteractor;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.firebase.FirebaseTokenRequest;
import com.royalenfield.reprime.models.request.web.otp.SendOtpRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.login.LoginData;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.login.UpdatePersonalDetailResponse;
import com.royalenfield.reprime.models.response.web.login.User;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.userdatavalidation.otp.activity.IOtpView;
import com.royalenfield.reprime.ui.userdatavalidation.otp.otpinteractor.OtpInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.otp.views.OnSubmitOtpListener;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.SwapConfigRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.UpdateContactRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class OtpPresenter implements IOtpPresenter, OnSecondaryUpdateCallback
        , OnSubmitOtpListener, OnSendOtpResponseListener {

    private final IOtpView mIOtpView;
    private final OtpInteractor mOtpInteractor;
    private static final String KEY_SECONDARY = "KEY_SECONDARY";
    private String countryCode;
    private String secondaryPhone;


    public OtpPresenter(IOtpView IOtpView, OtpInteractor otpInteractor) {
        mIOtpView = IOtpView;
        mOtpInteractor = otpInteractor;
    }

    @Override
    public void submitOtp(String secondaryPhone, String otp, String callingCode,String email) {
        this.secondaryPhone=secondaryPhone;
        LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
        LoginData loginData = loginResponse.getData();
        User user = loginData.getUser();
        mOtpInteractor.submitOtp(user.getFirstName(),user.getLastName(),secondaryPhone, otp, callingCode, email,this);
    }


    @Override
    public void onSubmitSuccess() {
        if (mIOtpView != null) {
            LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
            LoginData loginData = loginResponse.getData();
            User user = loginData.getUser();
            callNumberswapApi(user.getPhone(),secondaryPhone);

            user.setSecondary(user.getPhone());
            if (secondaryPhone != null)
                user.setPhone(secondaryPhone);
            secondaryPhone = null;
            REUtils.setFCMTokenSent(false);

            loginData.setJwtAccessToken(REUtils.getJwtToken());
            loginResponse.setData(loginData);
            //Used this for updating in shared preference
            REUtils.setDataToUserInfo(loginResponse);
            mIOtpView.saveFCMTokenOnServer();
            mIOtpView.onOtpSubmitted();
            if (REUtils.isUserLoggedIn()) {
                REUtils.getProfileDetailsFromServer(null);
            }
        }
    }


    @Override
    public void onSubmitFail(String message) {
        if (mIOtpView != null) {
            mIOtpView.showInlineError(message);
        }
    }

    @Override
    public void onSwapSuccess() {


    }

    public void callNumberswapApi(String oldPh,String newPh){
        SwapConfigRequestModel model=new SwapConfigRequestModel();
        model.setNewPhoneNo(newPh);
        model.setOldPhoneNo(oldPh);
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getConfiguratorApi()
                .setSecondaryToPrimary(model)
                .enqueue(new BaseCallback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {

                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        super.onFailure(call,t);
                       // onUpdateDetailFinishListner.onUpdateDetailFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });

    }

    public void updateUserDataValidationFlag() {
//        Map<String, Object> popupFlags = new HashMap<>();
//        popupFlags.put(REConstants.SHOW_USER_VALIDATION_POPUP, false);
//        popupFlags.put(REConstants.SHOW_VEHICLE_ONBOARDING_POPUP, false);
//        FirestoreManager.getInstance().updateUserSettingFirebaseField(popupFlags);
    }

    @Override
    public void onSwapFail(String message) {
        if (mIOtpView != null) {
            mIOtpView.showError(message);
        }
    }

    @Override
    public void onUpdateContactSuccess(String secondaryPhone, String callingCode) {

    }

    @Override
    public void onUpdateContactFail(String message) {
        if (mIOtpView != null) {
            mIOtpView.showError(message);
        }
    }

    @Override
    public void sendOTP(String secondaryDialingCode, String secondaryPhone) {
        SendOtpRequest req = new SendOtpRequest();
        req.setCallingCode(secondaryDialingCode);
        req.setMobile(secondaryPhone);
        mOtpInteractor.requestForOtp(req, this);
    }

    @Override
    public void onSuccess() {
        mIOtpView.onOTPSuccess();
    }

    @Override
    public void onFailure(String errorMessage) {
        mIOtpView.onSendOtpFail(errorMessage);
    }
}

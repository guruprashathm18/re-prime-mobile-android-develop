package com.royalenfield.reprime.ui.riderprofile.presenter;

import android.util.Log;
import com.royalenfield.reprime.ui.riderprofile.listeners.RiderProfilePicUploadListner;
import com.royalenfield.reprime.ui.riderprofile.views.UploadProfilePicView;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.profile.UploadProfilePic;
import com.royalenfield.reprime.ui.riderprofile.interactor.RiderProfileUploadInteractor;

public class RiderProfileUploadPresenter implements IRiderProfileUploadPresenter, RiderProfilePicUploadListner {
    private UploadProfilePicView mUploadProfilePicView;
    private RiderProfileUploadInteractor riderProfileUploadInteractor;
    private LoginResponse mUserLoginData;

    public RiderProfileUploadPresenter(UploadProfilePicView mUploadProfilePicView, RiderProfileUploadInteractor riderProfileUploadInteractor) {
        this.mUploadProfilePicView = mUploadProfilePicView;
        this.riderProfileUploadInteractor = riderProfileUploadInteractor;
        mUserLoginData = REApplication.getInstance().getUserLoginDetails();
    }

    @Override
    public void uploadRiderProfileImage(String imageData) {
        if (mUserLoginData != null && mUserLoginData.getData() != null && mUserLoginData.getData().getUser() != null) {
            Log.d("API", "Website upload image API called");
            riderProfileUploadInteractor.uploadProfileImage(mUserLoginData.getData().getUser().getUserId(), imageData, this);
        } else {
            mUploadProfilePicView.onUploadPicFailure("Unable to load profile image.");
        }
    }

    @Override
    public void onUploadPicSuccess(UploadProfilePic mUploadProfilePic) {
        mUploadProfilePicView.onUploadPicSuccess(mUploadProfilePic.getData().getProfilePicture());
    }

    @Override
    public void onUploadPicFailure(String failureMsg) {
        mUploadProfilePicView.onUploadPicFailure(failureMsg);
    }
}

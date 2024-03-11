package com.royalenfield.reprime.ui.riderprofile.interactor;

import android.util.Log;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.profile.UploadProfilePic;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.riderprofile.listeners.RiderProfilePicUploadListner;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiderProfileUploadInteractor implements IRiderProfileUploadInteractor {

    @Override
    public void uploadProfileImage(String userId, String imageData, final RiderProfilePicUploadListner miderProfilePicUploadListner) {
        String contentType = "application/x-www-form-urlencoded";
        String jwtToken = REApplication.getInstance().getUserTokenDetails();
        REApplication.getInstance().getREWebsiteApiInstance().getWebsiteAPI().uploadProfilePic(jwtToken, contentType, userId, imageData).enqueue(new BaseCallback<UploadProfilePic>() {
            @Override
            public void onResponse(Call<UploadProfilePic> call, Response<UploadProfilePic> response) {
                super.onResponse(call, response);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {
                        miderProfilePicUploadListner.onUploadPicSuccess(response.body());
                    } else {
                        miderProfilePicUploadListner.onUploadPicFailure(REUtils.getErrorMessageFromCode(response.body().getCode()));
                    }
                } else {
                    // error case
                    miderProfilePicUploadListner.onUploadPicFailure(REUtils.getErrorMessageFromCode(0));
                }
            }

            @Override
            public void onFailure(Call<UploadProfilePic> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("API", "uploadProfileImage failure : " + t.getMessage());
                miderProfilePicUploadListner.onUploadPicFailure(REUtils.getErrorMessageFromCode(0));
            }
        });

    }
}

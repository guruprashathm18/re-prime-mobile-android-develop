package com.royalenfield.reprime.ui.onboarding.editprofile.interactor;

import android.accounts.NetworkErrorException;
import android.util.Log;
import com.google.gson.Gson;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.updateprofile.UpdateProfileRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.UpdateProfile.UpdateProfile;
import com.royalenfield.reprime.models.response.web.profile.GetUserResponse;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * handles API calls
 * */

public class EditProfileInteractor implements IEditProfileInteractor {

    /**
     * This is used to update the profile data
     *
     * @param fName                       : first name
     * @param lName                       : last name
     * @param emailId                     : emailId
     * @param phone                       : phone No
     * @param dob                         : date of birth
     * @param city                        : city
     * @param password                    : password
     * @param editProfileFinishedListener : Listener Interface
     */
    @Override
    public void updateProfile(String fName, String lName, String emailId, String phone,
                              String dob, String city, String password, String aboutMe, final OnEditProfileFinishedListener editProfileFinishedListener) {
        String userId = "";
        if (REUserModelStore.getInstance() != null) {
            userId = REUserModelStore.getInstance().getUserId();
        }
        String contentType = "application/json";
        String jwtToken = REUtils.getJwtToken();
        if (userId != null) {
            REApplication
                    .getInstance()
                    .getREWebsiteApiInstance()
                    .getWebsiteAPI()
                    .updateProfileData(contentType, jwtToken, new UpdateProfileRequest(fName, lName,
                            emailId, phone, dob, city, password, userId, aboutMe))
                    .enqueue(new BaseCallback<UpdateProfile>() {
                        @Override
                        public void onResponse(Call<UpdateProfile> call, Response<UpdateProfile> response) {
                            super.onResponse(call, response);
                            Log.d("API", "UpdateProfile response code : " + response.code());
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getSuccess()) {
                                    editProfileFinishedListener.onUpdateSuccess();
                                } else if (response.body().getCode() == REConstants.API_UNAUTHORIZED) {
                                    REUtils.navigateToLogin();
                                } else {
                                    editProfileFinishedListener.onUpdateFailure(REUtils.getErrorMessageFromCode(0));
                                }
                            } else {
                                // error case
                                switch (response.code()) {
                                    case 404:
                                        editProfileFinishedListener.onUpdateFailure(REUtils.getErrorMessageFromCode(0));
                                        break;
                                    case 500:
                                        Log.d("API", "UpdateProfile response code : " + response.code() + "  response : " + new Gson().toJson(response.body()));
                                        editProfileFinishedListener.onUpdateFailure(REUtils.getErrorMessageFromCode(0));
                                        break;
                                    default:
                                        Log.d("API", "UpdateProfile response code : " + response.code() + "  response : " + new Gson().toJson(response.body()));
                                        editProfileFinishedListener.onUpdateFailure(REUtils.getErrorMessageFromCode(0));
                                        break;
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<UpdateProfile> call, Throwable t) {
                            super.onFailure(call, t);
                            Log.d("API", "UpdateProfile failure : " + t.getMessage());
                            if (t instanceof NetworkErrorException) {
                                editProfileFinishedListener.onUpdateFailure(REApplication.getAppContext().
                                        getResources().getString(R.string.network_failure));
                            } else {
                                editProfileFinishedListener.onUpdateFailure(t.getMessage());
                            }
                        }
                    });
        }
    }

    /**
     * This is to get the profile data
     *
     * @param userId                      rider user id
     * @param editProfileFinishedListener
     */
    @Override
    public void getProfileDetails(String userId, final OnEditProfileFinishedListener editProfileFinishedListener) {
        FirestoreManager.getInstance().getUserProfileData(editProfileFinishedListener);

    }

}

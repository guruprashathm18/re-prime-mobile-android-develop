package com.royalenfield.reprime.ui.onboarding.login.interactor;

import androidx.annotation.NonNull;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.web.login.UpdatePersonalDetailResponse;
import com.royalenfield.reprime.ui.onboarding.login.listeners.OnUpdateDetailFinishListner;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateDetailInteractor implements IUpdateDetailInteractor {

    @Override
    public void verifyPersonalDetail(String fName, String lName, String callingCode, String phone, String email, OnUpdateDetailFinishListner onUpdateDetailFinishListner) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .verifyPersonalDetail(phone,callingCode.substring(1, callingCode.length()),email)
                .enqueue(new BaseCallback<UpdatePersonalDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdatePersonalDetailResponse> call, @NonNull Response<UpdatePersonalDetailResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {

                                if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {

                                      onUpdateDetailFinishListner.onUpdateDetailSuccess();
                                } else {
                                    onUpdateDetailFinishListner.onUpdateDetailFailure(response.body().getMessage());
                                }
                            } else {
                                // error case
                                onUpdateDetailFinishListner.onUpdateDetailFailure(response.body().getMessage());
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<UpdatePersonalDetailResponse> call, @NonNull Throwable t) {
                        super.onFailure(call,t);
                        onUpdateDetailFinishListner.onUpdateDetailFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void updatePersonalDetail(String fName, String lName, String callingCode, String phone, String email, String otp,OnUpdateDetailFinishListner onUpdateDetailFinishListner) {
        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .updatePersonalDetail(fName,lName,phone,callingCode,email,otp)
                .enqueue(new BaseCallback<UpdatePersonalDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdatePersonalDetailResponse> call, @NonNull Response<UpdatePersonalDetailResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getSuccess() && response.body().
                                    getCode() == REConstants.API_SUCCESS_CODE) {

                                if (response.body().getSuccess() && response.body().getCode() == REConstants.API_SUCCESS_CODE) {

                                    onUpdateDetailFinishListner.onUpdateDetailSuccess();
                                } else {
                                    onUpdateDetailFinishListner.onUpdateDetailFailure(response.body().getMessage());
                                }
                            } else {
                                // error case
                                onUpdateDetailFinishListner.onUpdateDetailFailure(response.body().getMessage());
                            }
                        }
                        else{
                            onUpdateDetailFinishListner.onUpdateDetailFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<UpdatePersonalDetailResponse> call, @NonNull Throwable t) {
                        super.onFailure(call,t);
                        onUpdateDetailFinishListner.onUpdateDetailFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }


}

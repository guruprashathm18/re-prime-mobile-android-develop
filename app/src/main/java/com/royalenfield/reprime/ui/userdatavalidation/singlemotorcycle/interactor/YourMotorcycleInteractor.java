package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.interactor;

import androidx.annotation.NonNull;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.vehicledetails.UpdateVehicleDetailRequest;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDataRequestModel;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.vehicledetails.UpdateVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter.UpdateVehicleFinishedListener;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class YourMotorcycleInteractor {


    public void updateVehicleDetails(List<VehicleDataRequestModel> vehicleDataList, UpdateVehicleFinishedListener finishedListener) {

        String phoneNum = REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getPhone();

        String accessToken = REApplication.getInstance()
                .getUserTokenDetails();

        String guid = REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getUserId();

        String userName =getUsername();
        String email;
         email = REUserModelStore.getInstance().getEmail() ;
if(email==null||email.equals(""))
    email=REApplication.getInstance()
            .getUserLoginDetails().getData().getUser().getEmail();
        UpdateVehicleDetailRequest  request = new UpdateVehicleDetailRequest(REConstants.APP_ID, phoneNum, vehicleDataList);

        request.setGuid(guid);
        request.setLoggedInUserName(userName);
        request.setLoggedInUserEmail(email);


        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .updateVehicleDetails(accessToken, REConstants.APP_ID, request)
                .enqueue(new BaseCallback<UpdateVehicleResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateVehicleResponse> call, @NonNull Response<UpdateVehicleResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == REConstants.API_SUCCESS_CODE  || response.body().getCode() == REConstants.API_SUCCESS_NO_DATA_CODE) {
                                if (response.body().getCode() == REConstants.API_SUCCESS_CODE  || response.body().getCode() == REConstants.API_SUCCESS_NO_DATA_CODE) {
                                    finishedListener.onUpdateSuccess();
                                } else {
                                    finishedListener.onUpdateFail(REUtils.getErrorMessageFromCode(response.body().getCode()));
                                }
                            } else {
                                finishedListener.onUpdateFail(REUtils.getErrorMessageFromCode(response.body().getCode()));
                            }
                        } else {
                            // error case
                            finishedListener.onUpdateFail(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UpdateVehicleResponse> call, @NonNull Throwable t) {
                        super.onFailure(call, t);
                        finishedListener.onUpdateFail(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    private String getUsername() {
        StringBuilder strBuilder=new StringBuilder();
        strBuilder.append( REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getFirstName());
        if(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getLastName()!=null&&REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getLastName().length()>0)
            strBuilder.append(" ").append(REApplication.getInstance()
                    .getUserLoginDetails().getData().getUser().getLastName());
        return strBuilder.toString();

    }
}

package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.vehicleonboarding.VerifyVehicleRequest;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnVerifyDetailListener;
import com.royalenfield.reprime.utils.REConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleInteractor implements IAddVehicleInteractor {

    @Override
    public void verifyVehicleDetail(String chassisNumber, String registrationNumber, OnVerifyDetailListener onVerifyDetailListener) {

        VerifyVehicleRequest verifyVehicleRequest=new VerifyVehicleRequest();
        verifyVehicleRequest.setChassisNumber(chassisNumber);
        verifyVehicleRequest.setRegistrationNumber(registrationNumber);
        verifyVehicleRequest.setAppId(REConstants.APP_ID);
        verifyVehicleRequest.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        verifyVehicleRequest.setLoggedInUserMobileNo( REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone());
        verifyVehicleRequest.setLoggedInUserEmail( REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
        verifyVehicleRequest.setLoggedInUserName( REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName()+" "+REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName());

        String jwtToken = "";
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            jwtToken = REApplication.getInstance().getUserTokenDetails();
        }

//        UploadDocRequest uploadDocRequest=new UploadDocRequest();
//        uploadDocRequest.setEngineNo(engineNumber);
//        uploadDocRequest.setPurchaseDate(date);
//        uploadDocRequest.setMobileno("9712129222");
//        uploadDocRequest.setUserId("");

        //REApplication.getInstance().setUploadDocRequest(uploadDocRequest);

        REApplication
                .getInstance()
                .getREWebsiteApiInstance()
                .getWebsiteAPI()
                .verifyVehicleDetail(jwtToken, verifyVehicleRequest)
                .enqueue(new BaseCallback<AddVehicleResponse>() {
                    @Override
                    public void onResponse(Call<AddVehicleResponse> call, Response<AddVehicleResponse> response) {
                        super.onResponse(call, response);
                        AddVehicleResponse addVehicleResponse =response.body();
                        if(addVehicleResponse !=null){
                            if (addVehicleResponse.getCode().equalsIgnoreCase("200")&&!addVehicleResponse.getError()&&addVehicleResponse.getData()!=null){
                                //REApplication.getInstance().setAddVehicleResponse(addVehicleResponse);
                                onVerifyDetailListener.onSuccess(addVehicleResponse.getCode(),addVehicleResponse.getErrorMessage(),addVehicleResponse);
                            }else {
                                onVerifyDetailListener.onSuccess(addVehicleResponse.getCode(),addVehicleResponse.getErrorMessage(),null);
                            }
                        }else {
                            onVerifyDetailListener.onFailure("");
                        }

                    }

                    @Override
                    public void onFailure(Call<AddVehicleResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        onVerifyDetailListener.onFailure(t.getMessage());
                    }
                });


    }
}

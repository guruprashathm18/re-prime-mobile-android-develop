package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter;


import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor.AddVehicleInteractor;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.listeners.OnVerifyDetailListener;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views.AddVehicleView;
import com.royalenfield.reprime.utils.REUtils;

public class AddVehiclePresenter implements IAddVehiclePresenter, OnVerifyDetailListener {


    private static final String TAG = AddVehiclePresenter.class.getSimpleName();
    private AddVehicleView addVehicleView;
    private AddVehicleInteractor addVehicleInteractor;


    public AddVehiclePresenter(AddVehicleView addVehicleView, AddVehicleInteractor addVehicleInteractor) {
        this.addVehicleView = addVehicleView;
        this.addVehicleInteractor = addVehicleInteractor;
    }

    @Override
    public void validatePurchaseDate(String date) {
        if(date.length()>0){
            addVehicleView.onPurchaseDateSelected(date);
        }

    }

    @Override
    public boolean validateAddVehicleDetails(String chassisNumber, String registrationNumber,boolean isVarifyClicked) {
        boolean isVarified=true;
        if(!isVarifyClicked) {
            if (chassisNumber.length() > 0 || registrationNumber.length() > 0) {
                if (chassisNumber.length() > 0) {
                    addVehicleView.hideChassisNumberError();
                } else {
                    addVehicleView.hideChassisNumberError();
                    addVehicleView.enableVerifyButton();
                }
                if (registrationNumber.length() > 0) {
                    addVehicleView.hideRegistrationNumberError();
                } else {
                    addVehicleView.hideRegistrationNumberError();
                    addVehicleView.enableVerifyButton();
                }

            } else {
                addVehicleView.hideChassisNumberError();
                addVehicleView.hideRegistrationNumberError();
                addVehicleView.disableVerifyButton();
            }
        }
        else{
            if(chassisNumber.length()>0){
                if(REUtils.VALID_ENGINE_NUMBER_REGEX.matcher(chassisNumber).find()||!(chassisNumber.matches("^.*[0-9].*$")&&chassisNumber.matches("^.*[a-zA-Z].*$"))){
                    addVehicleView.onnInvalidChassisNumber();
                    isVarified=false;
                }
                else {
                    addVehicleView.hideChassisNumberError();

                }
            }

            if(registrationNumber.length()>0){
                if(REUtils.VALID_ENGINE_NUMBER_REGEX.matcher(registrationNumber).find()||registrationNumber.length()<8||!(registrationNumber.matches("^.*[0-9].*$")&&registrationNumber.matches("^.*[a-zA-Z].*$"))){
                    addVehicleView.onnInvalidRegistrationNumber();
                    isVarified=false;
                }
                else {
                    addVehicleView.hideRegistrationNumberError();

                }
            }


        }

        return isVarified;

    }

    @Override
    public void validateVehicleDetail(String chassisNumber, String registrationNumber) {
        addVehicleInteractor.verifyVehicleDetail(chassisNumber,registrationNumber,this);

    }

    public void onDestroy() {
        addVehicleView = null;
    }


    @Override
    public void onSuccess(String code, String message, AddVehicleResponse addVehicleResponse) {
        if(addVehicleView!=null)
            addVehicleView.onSuccessResponse( code, message,addVehicleResponse);

    }

    @Override
    public void onFailure(String errorMessage) {
        if(addVehicleView!=null)
        addVehicleView.onFailureResponse(errorMessage);

    }
}

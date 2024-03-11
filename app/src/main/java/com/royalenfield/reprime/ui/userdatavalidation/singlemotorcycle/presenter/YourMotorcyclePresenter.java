package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDataRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.activity.YourMotorcyclesActivity;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.interactor.YourMotorcycleInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.view.UpdateVehicleData;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views.IYourMotorcyclePresenter;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.views.YourMotorcycleView;

import java.util.ArrayList;
import java.util.List;

public class YourMotorcyclePresenter implements IYourMotorcyclePresenter, UpdateVehicleFinishedListener {

    private final YourMotorcycleInteractor mInteractor;
    private YourMotorcycleView mYourMotorcycleView;

    public YourMotorcyclePresenter(YourMotorcycleView yourMotorcycleView, YourMotorcycleInteractor interactor) {
        mYourMotorcycleView = yourMotorcycleView;
        mInteractor = interactor;
    }

    @Override
    public void onConfirmButtonClicked(UpdateVehicleData vehicleData, String updatedRegistrationNum) {

        if (mYourMotorcycleView!=null) {
            if (isRegistrationNumberValid(updatedRegistrationNum)) {
//                mYourMotorcycleView.showConfirmationScreen();

                if (vehicleData.getRegistrationNumber().equals(updatedRegistrationNum) && !vehicleData.isRemoveBike()) {
                    mYourMotorcycleView.showConfirmationScreen(R.string.thankyou_msg);
                } else {
                    List<VehicleDataRequestModel> list = prepareVehicleData(vehicleData, updatedRegistrationNum);
                    mInteractor.updateVehicleDetails(list, this);
                }
            }
        }
    }

    private List<VehicleDataRequestModel> prepareVehicleData(UpdateVehicleData updateVehicleData, String updatedRegNum) {
        List<VehicleDataRequestModel> requestModels = new ArrayList<>();
        requestModels.add(new VehicleDataRequestModel(updateVehicleData.getEngineNo()
                , updateVehicleData.getModelName(), updateVehicleData.getRegistrationNumber()
                , updateVehicleData.isRemoveBike(), updatedRegNum,updateVehicleData.getChassisNo()));
        return requestModels;
    }


    public boolean isRegistrationNumberValid(String registrationNum) {
        boolean isRegistrationNumValid = false;

        if (mYourMotorcycleView!=null) {
            if (TextUtils.isEmpty(registrationNum)) {
                mYourMotorcycleView.showEmptyError();
            } else if (registrationNum.length()<8
                    || registrationNum.length() > 10) {
                mYourMotorcycleView.showNumRangeError();
            } else if (!registrationNum.matches("[a-zA-Z0-9]+")) {
                mYourMotorcycleView.showAlphaNumericError();
            } else if (!registrationNum.matches("^.*[0-9].*$")) {
                mYourMotorcycleView.showNumberError();
            } else if (!registrationNum.matches("^.*[a-zA-Z].*$")) {
                mYourMotorcycleView.showLettersError();
            }  else {
                isRegistrationNumValid = true;
                mYourMotorcycleView.hideRegistartionError();
            }
        }
        return isRegistrationNumValid;
    }

    public void onDestroy() {
        mYourMotorcycleView = null;
    }

    @Override
    public void onUpdateSuccess() {
        if (mYourMotorcycleView!=null) {
                mYourMotorcycleView.showConfirmationScreen(R.string.updated_and_will_be_reflected_in_application_soon);
        }
    }

    @Override
    public void onUpdateFail(String errorMessageFromCode) {
    }
}

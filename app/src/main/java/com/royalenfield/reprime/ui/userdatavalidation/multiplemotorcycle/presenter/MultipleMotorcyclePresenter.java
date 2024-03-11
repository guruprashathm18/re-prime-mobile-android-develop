package com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.presenter;

import android.content.Context;
import android.util.Log;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDataRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.activity.MultipleMotorcycleView;
import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.view.MotorcyclesItem;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.interactor.YourMotorcycleInteractor;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.presenter.UpdateVehicleFinishedListener;
import com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.view.UpdateVehicleData;

import java.util.ArrayList;
import java.util.List;

public class MultipleMotorcyclePresenter implements IMultipleMotorcyclePresenter, UpdateVehicleFinishedListener {

    private final MultipleMotorcycleView mMultipleMotorcycleView;
    private final YourMotorcycleInteractor mInteractor;

    public MultipleMotorcyclePresenter(MultipleMotorcycleView multipleMotorcycleView, YourMotorcycleInteractor interactor) {
        mMultipleMotorcycleView = multipleMotorcycleView;
        mInteractor = interactor;
    }

    @Override
    public void onConfirmButtonClicked(ArrayList<MotorcyclesItem> originalList, ArrayList<MotorcyclesItem> updatedList) {

        List<VehicleDataRequestModel> list = prepareVehicleData(originalList, updatedList);
        mInteractor.updateVehicleDetails(list, this);

    }

    private List<VehicleDataRequestModel> prepareVehicleData(List<MotorcyclesItem> originalList
            , List<MotorcyclesItem> updatedList) {
        List<VehicleDataRequestModel> requestModels = new ArrayList<>();

        for (int i =0 ; i<originalList.size() ;i++) {
            requestModels.add(new VehicleDataRequestModel(updatedList.get(i).getmEngineNum()
                    , updatedList.get(i).getModelName(), originalList.get(i).getmRegistrationNumber()
                    , updatedList.get(i).ismIsNotOwn(), updatedList.get(i).getmRegistrationNumber(), updatedList.get(i).getmChassisNumber()));
        }

        return requestModels;
    }


    @Override
    public void onUpdateSuccess() {
        if (mMultipleMotorcycleView!=null) {
            mMultipleMotorcycleView.showConfirmationScreenWithMsgId(R.string.updated_and_will_be_reflected_in_application_soon);
        }
    }

    @Override
    public void onUpdateFail(String errorMessageFromCode) {
    }
}

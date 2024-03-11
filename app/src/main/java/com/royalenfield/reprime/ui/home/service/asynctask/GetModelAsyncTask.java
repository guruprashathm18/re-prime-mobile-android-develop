package com.royalenfield.reprime.ui.home.service.asynctask;

import android.os.AsyncTask;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Gets the modelName of vehicle by taking registrationNo as input
 */
public class GetModelAsyncTask extends AsyncTask<Void, Void, String> {

    private String mRegNo;
    private ServiceAsyncTaskListeners.GetModelFromRegNo mListener;

    public GetModelAsyncTask(String regNo,
                             ServiceAsyncTaskListeners.GetModelFromRegNo listener) {
        this.mRegNo = regNo;
        this.mListener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        List<VehicleDataModel> mVehicleDetails = new ArrayList<>();
        mVehicleDetails = REServiceSharedPreference.getVehicleData(REApplication.getAppContext());
        if (mVehicleDetails != null && mVehicleDetails.size() > 0) {
            for (VehicleDataModel vehicleDetailsResponse : mVehicleDetails) {
                String regNo = vehicleDetailsResponse.getRegistrationNo();
                if (regNo != null && !regNo.isEmpty() && !regNo.equals("null") && regNo.equals(mRegNo)) {
                    String modelCode = vehicleDetailsResponse.getModelCode();
                    return vehicleDetailsResponse.getMapVehicleData().get(modelCode);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String modelCode) {
        mListener.onGetModelComplete(modelCode);
    }
}

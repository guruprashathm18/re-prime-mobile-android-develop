package com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor;

import android.util.Log;
import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.ServiceProgressResponse;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.listener.OnFinishedListener;
import com.royalenfield.reprime.models.request.proxy.service.serviceprogress.VehicleServiceProgressRequest;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceProgressDetailsInteractor implements IServiceAppointmentDetailsInteractor {


    @Override
    public void getVehicleServiceInProgressList(String mobileNo, final OnFinishedListener onFinishedListener) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .getServiceProgressList(REUtils.getJwtToken(),REConstants.APP_ID,
                        new VehicleServiceProgressRequest(mobileNo))
                .enqueue(new BaseCallback<ServiceProgressResponse>() {
                    @Override
                    public void onResponse(Call<ServiceProgressResponse> call,
                                           Response<ServiceProgressResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "ServiceAppointmentListDetails :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == (REConstants.API_SUCCESS_CODE)) {
                                REApplication.getInstance().setServiceProgressListResponse(response.body().getData());
                                onFinishedListener.onSuccess();
                            } else {
                                onFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            onFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                            //onFinishedListener.onFailure(response.code() + " : " + response.message());

                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceProgressResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "ServiceAppointmentListDetails failure : " + t.getMessage());
                        onFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}

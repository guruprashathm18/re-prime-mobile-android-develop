package com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.service.cancelservice.ServiceCancelRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.proxy.cancelserviceappointment.CancelResponse;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.listener.OnFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAppointmentCancelInteractor implements IServiceAppointmentCancelInteractor {

    @Override
    public void cancelServiceAppointment(String CaseNo, boolean isDummySlots, final OnFinishedListener onFinishedListener) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .cancelServiceAppointment(REUtils.getJwtToken(), REConstants.APP_ID,
                        new ServiceCancelRequest(CaseNo, REUtils.
                                trimCountryCodeFromMobile(REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone()),
                                "Yes", isDummySlots))
                .enqueue(new BaseCallback<CancelResponse>() {
                    @Override
                    public void onResponse(Call<CancelResponse> call,
                                           Response<CancelResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "ServiceAppointmentCancel :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                REApplication.getInstance().setServiceBookingResponse(null);
                                REApplication.getInstance().setServiceProgressListResponse(null);
                                onFinishedListener.onSuccess();
                            } else if (response.body().getStatus().equals(REConstants.API_DMS_FAILURE_CODE)) {
                                onFinishedListener.onFailure(response.body().getResponse().getMessage());
                            } else {
                                onFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            onFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                        }

                    }

                    @Override
                    public void onFailure(Call<CancelResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "ServiceAppointmentCancel failure : " + t.getMessage());
                        onFinishedListener.onFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}

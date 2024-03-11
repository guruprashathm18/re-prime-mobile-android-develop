package com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.service.reschedule.ServiceAppointmentRescheduleRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.proxy.servicebooking.BookingResponse;
import com.royalenfield.reprime.ui.home.service.listener.OnServiceRescheduleResponseListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAppointmentRescheduleInteractor implements IServiceAppointmentRescheduleInteractor {

    @Override
    public void rescheduleServiceAppointment(String AppointmentId, String AppointmentDate,
                                             String AttachmentKey, String CustomerRemarks,
                                             String DropAddress, String SlotID, String IsPickUpDrop,
                                             String PickUpAddress, String ServiceType,
                                             String Creation_Source, String BranchID, String regNo, boolean isDummySlots, final OnServiceRescheduleResponseListener onServiceRescheduleResponseListener) {
        //Create Reschedule service booking request.
        ServiceAppointmentRescheduleRequest serviceRescheduleRequest = new ServiceAppointmentRescheduleRequest(AppointmentId,
                AppointmentDate, AttachmentKey, CustomerRemarks,
                DropAddress, SlotID, IsPickUpDrop, PickUpAddress, ServiceType, Creation_Source, BranchID,
                REUtils.trimCountryCodeFromMobile(REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone()), regNo, "Yes", isDummySlots);
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .rescheduleServiceAppointment(REUtils.getJwtToken(), REConstants.APP_ID,
                        serviceRescheduleRequest)
                .enqueue(new BaseCallback<BookingResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<BookingResponse> call,
                                           @NotNull Response<BookingResponse> response) {
                        super.onResponse(call,response);
                        Log.d("API", "ServiceAppointmentReschedule :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
                            if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                if (response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                                    REApplication.getInstance().setServiceBookingResponse(response.body().getResponse());
                                    REApplication.getInstance().setServiceSlotResponse(null);
                                    onServiceRescheduleResponseListener.onRescheduleSuccess(response.body().getResponse().get(0).getBookingno());
                                } else {
                                    onServiceRescheduleResponseListener.onRescheduleFailure(REUtils.getErrorMessageFromCode(0));
                                }
                            } else if (response.body().getStatus().equals(REConstants.API_DMS_FAILURE_CODE)) {
                                onServiceRescheduleResponseListener.onRescheduleFailure(response.body().getResponse().get(0).getMessage());
                            } else {
                                onServiceRescheduleResponseListener.onRescheduleFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            if (response.body().getResult().equalsIgnoreCase("failed")){
                                String message = "Failed";
                                message = response.body().getErrorMessage();
                                onServiceRescheduleResponseListener.onRescheduleFailure(message);
                            } else {
                                onServiceRescheduleResponseListener.onRescheduleFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<BookingResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "ServiceAppointmentReschedule failure : " + t.getMessage());
                        onServiceRescheduleResponseListener.onRescheduleFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}

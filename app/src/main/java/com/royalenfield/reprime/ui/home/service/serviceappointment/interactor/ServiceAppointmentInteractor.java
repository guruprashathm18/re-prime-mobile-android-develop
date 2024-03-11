package com.royalenfield.reprime.ui.home.service.serviceappointment.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.service.cancelservice.ServiceCancelRequest;
import com.royalenfield.reprime.models.request.proxy.service.paymentgateway.PaymentGatewayRequest;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingStatus;
import com.royalenfield.reprime.models.request.proxy.service.serviceestimate.ServiceEstimateRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.proxy.paymentgateway.PaymentGatewayResponse;
import com.royalenfield.reprime.models.response.proxy.serviceestimate.ServiceEstimateResponse;
import com.royalenfield.reprime.models.response.proxy.servicestatus.ServiceStatusResponse;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.VehicleServiceProgressListResponse;
import com.royalenfield.reprime.ui.home.service.serviceappointment.listener.OnAppointmentFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAppointmentInteractor implements IServiceAppointmentInteractor {
    @Override
    public void getServiceEstimate(String regNo, OnAppointmentFinishedListener listener) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .getServiceEstimate(REUtils.getJwtToken(), REConstants.APP_ID,
                        new ServiceEstimateRequest(regNo))
                .enqueue(new BaseCallback<ServiceEstimateResponse>() {
                    @Override
                    public void onResponse(Call<ServiceEstimateResponse> call,
                                           Response<ServiceEstimateResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "ServiceAppointmentCancel :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                listener.onServiceEstimateSuccess(response.body().getTotalEstimate());
                            } else {
                                listener.onServiceEstimateFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            listener.onServiceEstimateFailure(REUtils.getErrorMessageFromCode(0));
                            //onFinishedListener.onFailure(response.code() + " : " + response.message());

                        }

                    }

                    @Override
                    public void onFailure(Call<ServiceEstimateResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "ServiceAppointmentCancel failure : " + t.getMessage());
                        listener.onServiceEstimateFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    /**
     * Gives the service status by taking caseNo as input parameter
     *
     * @param caseNo   : String
     * @param listener : OnAppointmentFinishedListener
     */
    @Override
    public void getServiceStatus(String caseNo, OnAppointmentFinishedListener listener) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .getServiceStatus(REUtils.getJwtToken(), REConstants.APP_ID,
                        new ServiceBookingStatus(caseNo, REUtils.
                                trimCountryCodeFromMobile(REApplication.getInstance().
                                        getUserLoginDetails().getData().getUser().getPhone()), ""))
                .enqueue(new BaseCallback<ServiceStatusResponse>() {
                    @Override
                    public void onResponse(Call<ServiceStatusResponse> call,
                                           Response<ServiceStatusResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "ServiceStatus :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                List<VehicleServiceProgressListResponse> serviceAPPList = new ArrayList<>();
                                VehicleServiceProgressListResponse serviceProgressListResponse = new VehicleServiceProgressListResponse();
                                serviceProgressListResponse.setAppointmentDate(response.body().getResponse().getAppointmentDate());
                                serviceProgressListResponse.setDealerInfo(response.body().getResponse().getBranchID());
                                serviceProgressListResponse.setCustomerRemarks(response.body().getResponse().getCustomerRemarks());
                                serviceProgressListResponse.setPickupAddres(response.body().getResponse().getPickUpAddress());
                                serviceProgressListResponse.setPaybleamount("NA");
                                serviceProgressListResponse.setModelName("");
                                serviceProgressListResponse.setRegistrationNumber(response.body().getResponse().getRegNo());
                                serviceProgressListResponse.setJobcardStatus("");
                                serviceProgressListResponse.setPickUpTime(response.body().getResponse().getPickUpTime());
                                serviceProgressListResponse.setServiceBookingType(response.body().getResponse().getServiceBookingType());
                                serviceAPPList.add(serviceProgressListResponse);
                                REApplication.getInstance().setServiceProgressListResponse(serviceAPPList);
                                listener.onServiceStatusSuccess();
                            } else {
                                listener.onServiceStatusFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            // error case
                            listener.onServiceStatusFailure(REUtils.getErrorMessageFromCode(0));
                        }

                    }

                    @Override
                    public void onFailure(Call<ServiceStatusResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "ServiceAppointmentCancel failure : " + t.getMessage());
                        listener.onServiceEstimateFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    @Override
    public void generatePaymentMessage(String txnAmount, String caseId, String currencyType, OnAppointmentFinishedListener listener) {
        REApplication
                .getInstance()
                .getRENotificationApiInstance()
                .getPaymentApi()
                .paymentGateWay(REConstants.APP_ID, new PaymentGatewayRequest(txnAmount, caseId, currencyType,
                        REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId()))
                .enqueue(new BaseCallback<PaymentGatewayResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<PaymentGatewayResponse> call,
                                           @NotNull Response<PaymentGatewayResponse> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getMsg() != null &&
                                    !response.body().getMsg().equals("Amount mismatched")) {
                                listener.onGeneratePaymentSuccess(response.body().getMsg());
                            } else {
                                listener.onGeneratePaymentFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            listener.onGeneratePaymentFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<PaymentGatewayResponse> call, @NotNull Throwable t) {
                        super.onFailure(call, t);
                        listener.onGeneratePaymentFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }
}

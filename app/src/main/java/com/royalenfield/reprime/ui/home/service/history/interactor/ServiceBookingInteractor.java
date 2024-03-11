package com.royalenfield.reprime.ui.home.service.history.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.service.SearchServiceCentreRequest;
import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;
import com.royalenfield.reprime.models.request.proxy.service.slot.PickupandDropServiceSlotRequest;
import com.royalenfield.reprime.models.request.proxy.service.slot.ServiceSlotRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMaster;
import com.royalenfield.reprime.models.response.proxy.servicebooking.BookingResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupDoorstepServiceSlotResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupandDoorstepServiceSlot;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotListResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotResponse;
import com.royalenfield.reprime.ui.home.service.history.listener.ServiceBookingListener;
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import com.royalenfield.reprime.utils.RELog;

public class ServiceBookingInteractor implements IServiceBookingInteractor {

    @Override
    public void doServiceBooking(ServiceBookingRequest serviceBookingRequestList, final ServiceBookingListener onServiceBookingListener) {
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .doServiceBooking(REUtils.getJwtToken(), REConstants.APP_ID,
                        serviceBookingRequestList)
                .enqueue(new BaseCallback<BookingResponse>() {
                    @Override
                    public void onResponse(Call<BookingResponse> call,
                                           Response<BookingResponse> response) {
                        super.onResponse(call, response);
                        Log.d("ServiceBookingRequest", "ServiceBookingDetails :" + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null && response.body().getResponse() != null) {
                            if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                String mBookingNo = response.body().getResponse().get(0).getBookingno();
                                if (mBookingNo != null && mBookingNo.isEmpty()) {
                                    onServiceBookingListener.onServiceBookingFailure(REUtils.getErrorMessageFromCode(0));
                                } else if (mBookingNo != null){
                                    REApplication.getInstance().setServiceBookingResponse(response.body().getResponse());
                                    REApplication.getInstance().setServiceSlotResponse(null);
                                    onServiceBookingListener.onServiceBookingSuccess();
                                } else {
                                    onServiceBookingListener.onServiceBookingFailure(REUtils.getErrorMessageFromCode(0));
                                }
                            } else if (response.body().getStatus().equals(REConstants.API_DMS_FAILURE_CODE)) {
                                onServiceBookingListener.onServiceBookingFailure(response.body().
                                        getResponse().get(0).getMessage());
                            } else {
                                    onServiceBookingListener.onServiceBookingFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        } else {
                            if (response.body().getResult().equalsIgnoreCase("failed")){
                                String message = "Failed";
                                message = response.body().getErrorMessage();
                                onServiceBookingListener.onServiceBookingFailure(message);
                            } else {
                                onServiceBookingListener.onServiceBookingFailure(REUtils.getErrorMessageFromCode(0));
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<BookingResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        Log.d("API", "ServiceBookingRequest failure : " + t.getMessage());
                        onServiceBookingListener.onServiceBookingFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }


    @Override
    public void getBookingSlots(String appointmentDate, String branchID, final ServiceBookingListener onServiceBookingListener) {
        String isdummyslots = REUtils.isDummySlotsEnabled();
        boolean isDummySlots = Boolean.parseBoolean(isdummyslots);
        REApplication
                .getInstance()
                .getREServiceApiInstance()
                .getServiceApi()
                .getBookingSlots(REUtils.getJwtToken(),
                        REConstants.RE_APP_ID, new ServiceSlotRequest(appointmentDate, branchID,isDummySlots))
                .enqueue(new BaseCallback<ServiceSlotResponse>() {
                    @Override
                    public void onResponse(Call<ServiceSlotResponse> call,
                                           Response<ServiceSlotResponse> response) {
                        super.onResponse(call, response);
                        Log.d("API", "getBookingSlots response code : " + response.code()
                                + "  response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData().size() > 0 && response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                                List<ServiceSlotListResponse> serviceSlotListResponses = groupTimeSlotValues(response.body().getData());
                                REApplication.getInstance().setServiceSlotResponse(serviceSlotListResponses);
                                onServiceBookingListener.onServiceTimeSlotSuccess();
                            } else {
                                //Setting data to null on failure
                                REApplication.getInstance().setServiceSlotResponse(null);
                                onServiceBookingListener.onServiceTimeSlotFailure(REApplication.getAppContext().
                                        getResources().getString(R.string.text_noslots_contact_servicecenter));
                            }
                        } else {
                            //Setting data to null on failure
                            REApplication.getInstance().setServiceSlotResponse(null);
                            onServiceBookingListener.onServiceTimeSlotFailure(REUtils.getErrorMessageFromCode(0));
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceSlotResponse> call, Throwable t) {
                        super.onFailure(call, t);
                        //Setting data to null on failure
                        REApplication.getInstance().setServiceSlotResponse(null);
                        Log.d("API", "ServiceBookingRequest failure : " + t.getMessage());
                        onServiceBookingListener.onServiceTimeSlotFailure(REUtils.getErrorMessageFromCode(0));
                    }
                });
    }

    /**
     * @param mServiceTimeSlotResponse sloResponse.
     * @return returns List of sloResponse which are grouped by timeSlotValue.
     * Grouped resourceId will be set to List<String> listOfResourceId and while booking s ervice always first resourceId from the list would be taken.
     * Assumption :: Api will always return  available(not -booked) resourceId and if all the resourceId are booked then in that case api will not return timeSlot value.
     * Implementation :: List will be sorted based on startTime and then grouped based on timeSlotValue.
     */
    @Override
    public List<ServiceSlotListResponse> groupTimeSlotValues(List<ServiceSlotListResponse> mServiceTimeSlotResponse) {
        List<ServiceSlotListResponse> slotResponseListWithGroupedResourceIDs = null;
        ArrayList<String> groupOfResourceIDsList;
        if (mServiceTimeSlotResponse != null && mServiceTimeSlotResponse.size() > 0) {
            //compare logic and put the data in three dimensional array.
            final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Collections.sort(mServiceTimeSlotResponse, new Comparator<ServiceSlotListResponse>() {
                @Override
                public int compare(ServiceSlotListResponse serviceSlotResponse, ServiceSlotListResponse t1) {
                    try {
                        return inputFormat.parse(serviceSlotResponse.getStartTimeSlot()).compareTo(inputFormat.parse(t1.getStartTimeSlot()));
                    } catch (Exception e) {
                    }
                    return 0;
                }
            });
            slotResponseListWithGroupedResourceIDs = new ArrayList<ServiceSlotListResponse>();
            groupOfResourceIDsList = new ArrayList<String>();
            for (int i = 0; i <= mServiceTimeSlotResponse.size() - 1; i++) {
                String time = REUtils.getTimeSlots(mServiceTimeSlotResponse.get(i).getStartTimeSlot());
                String slotId = mServiceTimeSlotResponse.get(i).getResourceId();
                if (i != mServiceTimeSlotResponse.size() - 1 && time.equalsIgnoreCase(REUtils.getTimeSlots(mServiceTimeSlotResponse.get(i + 1).getStartTimeSlot()))) {    //same timeSlot value(i==i+1)
                    groupOfResourceIDsList.add(slotId);
                } else if (i == mServiceTimeSlotResponse.size() - 1 && time.equalsIgnoreCase(REUtils.getTimeSlots(mServiceTimeSlotResponse.get(i - 1).getStartTimeSlot()))) {
                    groupOfResourceIDsList.add(slotId);
                    mServiceTimeSlotResponse.get(i).setListOfResourceId(groupOfResourceIDsList);
                    slotResponseListWithGroupedResourceIDs.add(mServiceTimeSlotResponse.get(i));
                } else {
                    if (groupOfResourceIDsList != null && groupOfResourceIDsList.size() == 0) {      //One slot value
                        groupOfResourceIDsList.add(slotId);
                        mServiceTimeSlotResponse.get(i).setListOfResourceId(groupOfResourceIDsList);
                        slotResponseListWithGroupedResourceIDs.add(mServiceTimeSlotResponse.get(i));
                        groupOfResourceIDsList.clear();
                    } else if (groupOfResourceIDsList != null && groupOfResourceIDsList.size() > 0) {                    // i!=i+1;
                        mServiceTimeSlotResponse.get(i).setListOfResourceId(groupOfResourceIDsList);
                        slotResponseListWithGroupedResourceIDs.add(mServiceTimeSlotResponse.get(i));
                        groupOfResourceIDsList.clear();
                    }
                }
            }
        }
        return slotResponseListWithGroupedResourceIDs;
    }

    @Override
    public void getServiceCenters(ServiceCenterRequest serviceCenterRequest, ServiceBookingListener onServiceBookingListener) {
        try {
            REApplication
                    .getInstance()
                    .getREServiceApiInstance()
                    .getServiceApi()
                    .getServiceCenterList(REUtils.getJwtToken(), REConstants.APP_ID,
                            serviceCenterRequest)
                    .enqueue(new BaseCallback<DealerMaster>() {
                        @Override
                        public void onResponse(Call<DealerMaster> call,
                                               Response<DealerMaster> response) {
                            super.onResponse(call, response);
                            Log.d("ServiceCenterRequest", "API :" + response.code()
                                    + "  response : " + new Gson().toJson(response.body()));
                            if (response.isSuccessful() && response.body() != null &&
                                    response.body().getStatus() == REConstants.API_SUCCESS_CODE) {
                                if (response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                                    onServiceBookingListener.onServiceCenterFetchSuccess(response.body().getResponse());
                                } else {
                                    onServiceBookingListener.onServiceCenterFetchFailed("");
                                }
                            } else {
                                onServiceBookingListener.onServiceCenterFetchFailed("");
                            }
                        }


                        @Override
                        public void onFailure(Call<DealerMaster> call, Throwable t) {
                            super.onFailure(call, t);
                            Log.d("API", "ServiceBookingRequest failure : " + t.getMessage());
                        }
                    });
        } catch (Exception e) {
            //As the API structure and params ma changes catch is added
            RELog.e(e);
            if (onServiceBookingListener != null)
                onServiceBookingListener.onServiceCenterFetchFailed("");
        }
    }

    public void getServiceCentreSearchResults(SearchServiceCentreRequest searchServiceCentreRequest, ServiceBookingListener onServiceBookingListener) {
        try {
            REApplication
                    .getInstance()
                    .getREServiceApiInstance()
                    .getServiceApi()
                    .getSearchServiceCenterList(REUtils.getJwtToken(), REConstants.APP_ID,
                            searchServiceCentreRequest)
                    .enqueue(new BaseCallback<DealerMaster>() {
                        @Override
                        public void onResponse(Call<DealerMaster> call,
                                               Response<DealerMaster> response) {
                            super.onResponse(call, response);
                            Log.d("SearchServicCentrReq", "API :" + response.code()
                                    + "  response : " + new Gson().toJson(response.body()));
                            if (response.isSuccessful() && response.body() != null &&
                                    response.body().getStatus() == REConstants.API_SUCCESS_CODE) {
                                if (response.body().getResponse() != null && response.body().getResponse().size() > 0) {
                                    onServiceBookingListener.onServiceCenterFetchSuccess(response.body().getResponse());
                                } else {
                                    onServiceBookingListener.onServiceCenterFetchFailed("");
                                }
                            } else {
                                onServiceBookingListener.onServiceCenterFetchFailed("");
                            }
                        }


                        @Override
                        public void onFailure(Call<DealerMaster> call, Throwable t) {
                            super.onFailure(call, t);
                            Log.d("API", "ServiceBookingRequest failure : " + t.getMessage());
                        }
                    });
        } catch (Exception e) {
            //As the API structure and params ma changes catch is added
            RELog.e(e);
            if (onServiceBookingListener != null)
                onServiceBookingListener.onServiceCenterFetchFailed("");
        }
    }

   public void getPickupAndDoorstepServiceResults(String dealerId, ServiceBookingListener onServiceBookingListener) {
       try {
           REApplication
                   .getInstance()
                   .getREServiceApiInstance()
                   .getServiceApi()
                   .getPickupandDoorstepServiceReslts(REUtils.getJwtToken(), REConstants.APP_ID,
                           new PickupandDropServiceSlotRequest(dealerId))
                   .enqueue(new BaseCallback<List<PickupandDoorstepServiceSlot>>() {
                       @Override
                       public void onResponse(Call<List<PickupandDoorstepServiceSlot>> call,
                                              Response<List<PickupandDoorstepServiceSlot>> response) {
                           super.onResponse(call, response);
                           Log.d("PickupDorrstep", "API :" + response.code()
                                   + "  response : " + new Gson().toJson(response.body()));
                           if (response.isSuccessful() && response.body() != null ) {
                             //  if (response.body().getStatus().equals(REConstants.API_SUCCESS_CODE)) {
                               REApplication.getInstance().setPickupandDoorstepServiceSlotResponse(response.body());
                                   onServiceBookingListener.ongetPickupAndDoorstepServiceSlotsSuccess();

                             /*  } else {
                                   onServiceBookingListener.ongetPickupAndDoorstepServiceSlotsFailure();
                               }*/

                           } else {
                               onServiceBookingListener.ongetPickupAndDoorstepServiceSlotsFailure();
                           }
                       }


                       @Override
                       public void onFailure(Call<List<PickupandDoorstepServiceSlot>> call, Throwable t) {
                           super.onFailure(call, t);
                           Log.d("API", "PickupDorrstep failure : " + t.getMessage());
                           onServiceBookingListener.ongetPickupAndDoorstepServiceSlotsFailure();
                       }
                   });
       } catch (Exception e) {
           //As the API structure and params ma changes catch is added
           RELog.e(e);
           if (onServiceBookingListener != null)
               onServiceBookingListener.ongetPickupAndDoorstepServiceSlotsFailure();
       }
   }
}
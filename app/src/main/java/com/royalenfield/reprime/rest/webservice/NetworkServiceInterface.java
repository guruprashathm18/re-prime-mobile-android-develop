package com.royalenfield.reprime.rest.webservice;

import com.royalenfield.reprime.models.request.proxy.firebase.FirebaseTokenRequest;
import com.royalenfield.reprime.models.request.proxy.service.SearchServiceCentreRequest;
import com.royalenfield.reprime.models.request.proxy.service.ServiceCenterRequest;
import com.royalenfield.reprime.models.request.proxy.service.cancelservice.ServiceCancelRequest;
import com.royalenfield.reprime.models.request.proxy.service.paymentgateway.PaymentGatewayRequest;
import com.royalenfield.reprime.models.request.proxy.service.reschedule.ServiceAppointmentRescheduleRequest;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingStatus;
import com.royalenfield.reprime.models.request.proxy.service.serviceestimate.ServiceEstimateRequest;
import com.royalenfield.reprime.models.request.proxy.service.serviceprogress.VehicleServiceProgressRequest;
import com.royalenfield.reprime.models.request.proxy.service.slot.PickupandDropServiceSlotRequest;
import com.royalenfield.reprime.models.request.proxy.service.slot.ServiceSlotRequest;
import com.royalenfield.reprime.models.request.proxy.service.vehicledetails.VehicleDetailsRequest;
import com.royalenfield.reprime.models.request.web.otap.OTAPFeedbackRequest;
import com.royalenfield.reprime.models.request.web.otap.OtapGetDeviceinfoApiRequest;
import com.royalenfield.reprime.models.request.web.otap.RemoveTripperDeviceApiRequest;
import com.royalenfield.reprime.models.request.web.otap.UpdateTripperDeviceApiRequest;
import com.royalenfield.reprime.models.request.web.tbtauthentication.TbtAuthRequest;
import com.royalenfield.reprime.models.response.firebase.DealerMaster;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.GetVehicleDetailsResponse;
import com.royalenfield.reprime.models.response.proxy.cancelserviceappointment.CancelResponse;
import com.royalenfield.reprime.models.response.proxy.paymentgateway.PaymentGatewayResponse;
import com.royalenfield.reprime.models.response.proxy.servicebooking.BookingResponse;
import com.royalenfield.reprime.models.response.proxy.serviceestimate.ServiceEstimateResponse;
import com.royalenfield.reprime.models.response.proxy.serviceslot.PickupandDoorstepServiceSlot;
import com.royalenfield.reprime.models.response.proxy.serviceslot.ServiceSlotResponse;
import com.royalenfield.reprime.models.response.proxy.servicestatus.ServiceStatusResponse;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.ServiceProgressResponse;
import com.royalenfield.reprime.models.response.web.firebasetoken.CustomTokenResponse;
import com.royalenfield.reprime.models.response.web.firebasetoken.FirebaseTokenResponse;
import com.royalenfield.reprime.models.response.web.otap.OTAPFeedbackResponse;
import com.royalenfield.reprime.models.response.web.otap.OtapGetDeviceinfoApiResopnse;
import com.royalenfield.reprime.models.response.web.otap.OtapSaveDeviceinfoApiRequest;
import com.royalenfield.reprime.models.response.web.otap.OtapSaveDeviceinfoApiResponse;
import com.royalenfield.reprime.models.response.web.otp.OtpResponse;
import com.royalenfield.reprime.models.response.web.tbtauthentication.TbtAuthResponse;
import com.royalenfield.reprime.ui.home.navigation.listener.SaveTripSummaryDetailsApiRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * With Retrofit 2, endpoints are defined inside of an interface using special retrofit annotations to
 * encode details about the parameters and request method. In addition, the return value is always a
 * parameterized Call<T> object such as Call<User>. If you do not need any type-specific response,
 * you can specify return value as simply Call<ResponseBody>.
 * <p>
 * For instance, the interface defines each endpoint in the following way:
 */

public interface NetworkServiceInterface {

    @Headers("Content-Type: application/json")
    @POST("notification/saveDeviceToken")
    Call<FirebaseTokenResponse> sendDeviceToken(@Header("app_id") int appId, @Body FirebaseTokenRequest request);

    @Headers("Content-Type: application/json")
    @POST("vehicle/GetVehicleDetails")
    Call<GetVehicleDetailsResponse> getVehicleDetails(@Header("Authorization") String authorization, @Body VehicleDetailsRequest request);

    //    ------------------------Service Booking-----------------------

    @Headers("Content-Type: application/json")
    @POST("appointment/searchSlotAppointment")
    Call<ServiceSlotResponse> getBookingSlots(@Header("Authorization") String authorization,
                                              @Header("app_id") int appId, @Body ServiceSlotRequest serviceSlotRequest);

    @Headers("Content-Type: application/json")
    @POST("appointment/serviceappointmentbooking")
    Call<BookingResponse> doServiceBooking(@Header("Authorization") String authorization, @Header("app_id") int appId,
                                           @Body ServiceBookingRequest serviceBookingRequest);

    @Headers("Content-Type: application/json")
    @POST("appointment/rescheduleserviceappointment")
    Call<BookingResponse>
    rescheduleServiceAppointment(@Header("Authorization") String authorization, @Header("app_id") int appId,
                                 @Body ServiceAppointmentRescheduleRequest serviceAppointmentRescheduleRequest);

    @Headers("Content-Type: application/json")
    @POST("appointment/cancelServiceAppointment")
    Call<CancelResponse>
    cancelServiceAppointment(@Header("Authorization") String authorization, @Header("app_id") int appId,
                             @Body ServiceCancelRequest serviceCancelRequest);

    //    ------------------------Service Appointment-----------------------

    @Headers("Content-Type: application/json")
    @POST("appointment/getVehicleServiceInProgressList")
    Call<ServiceProgressResponse> getServiceProgressList(@Header("Authorization") String authorization,
                                                         @Header("app_id") int appId,
                                                         @Body VehicleServiceProgressRequest
                                                                 vehicleServiceProgressRequest);

    @Headers("Content-Type: application/json")
    @POST("appointment/serviceEstimate")
    Call<ServiceEstimateResponse> getServiceEstimate(@Header("Authorization") String authorization,
                                                     @Header("app_id") int appId,
                                                     @Body ServiceEstimateRequest serviceEstimateRequest);

    @POST("appointment/serviceStatus")
    Call<ServiceStatusResponse>
    getServiceStatus(@Header("Authorization") String authorization, @Header("app_id") int appId,
                     @Body ServiceBookingStatus serviceBookingStatus);

    @Headers("Content-Type: application/json")
    @POST("appointment/searchNearByDealers")
    Call<DealerMaster> getServiceCenterList(@Header("Authorization") String authorization,
                                            @Header("app_id") int appId,
                                            @Body ServiceCenterRequest
                                                            serviceCenterRequeste);
    //    ------------------------Search Nearby Service Center and Text Search-----------------------
    @Headers("Content-Type: application/json")
    @POST("appointment/searchNearByDealers")
    Call<DealerMaster> getSearchServiceCenterList(@Header("Authorization") String authorization,
                                                  @Header("app_id") int appId,
                                                  @Body SearchServiceCentreRequest
                                                          searchServiceCentreRequest);

    @Headers("Content-Type: application/json")
    @POST("appointment/getPickupAndDoorstepServiceSlots")
    Call<List<PickupandDoorstepServiceSlot>> getPickupandDoorstepServiceReslts(@Header("Authorization") String authorization,
                                                                               @Header("app_id") int appId,
                                                                               @Body PickupandDropServiceSlotRequest
                                                          pickupandDropServiceSlotRequest);
    //    ------------------------Service Payment-----------------------

    @Headers("Content-Type: application/json")
    @POST("payment/generatepaymentmessage")
    Call<PaymentGatewayResponse>
    paymentGateWay(@Header("app_id") int appId, @Body PaymentGatewayRequest paymentGatewayRequest);


    @Headers("Content-Type: application/json")
    @POST("otap/feedback")
    Call<OTAPFeedbackResponse>
    otapFeedback(@Body OTAPFeedbackRequest otapFeedbackRequest);

    // New api to get the TBTDevice List from Cloud Database
    @Headers("Content-Type: application/json")
    @POST("utility/tbtutility/auth/getTBTDevices")
    Call<OtapGetDeviceinfoApiResopnse>
    otapGetDeviceListFromApi(@Body OtapGetDeviceinfoApiRequest otapGetDeviceinfoApiRequest);

    // New api to save the TBTDevice List to Cloud Database
    @Headers("Content-Type: application/json")
    @POST("utility/tbtutility/auth/saveTBTDevices")
    Call<OtapSaveDeviceinfoApiResponse>
    otapSaveDeviceListFromPreferenceToApi(@Body OtapSaveDeviceinfoApiRequest otapSaveDeviceinfoApiRequest);

    // New api to remove the TBTDevice List from Cloud Database
    @Headers("Content-Type: application/json")
    @POST("utility/tbtutility/auth/removeTBTDevice")
    Call<OtapSaveDeviceinfoApiResponse>
    removeTBTDeviceApi(@Body RemoveTripperDeviceApiRequest removeTripperDeviceApiRequest);

    // New api to update the TBTDevice In Cloud Database
    @Headers("Content-Type: application/json")
    @POST("utility/tbtutility/auth/updateTBTDevice")
    Call<OtapSaveDeviceinfoApiResponse>
    updateTBTDeviceApi(@Body UpdateTripperDeviceApiRequest updateTripperDeviceApiRequest);

    //    ------------------------TBT Authentication-----------------------
    @POST("utility/tbtutility/auth/getAuthenticationKey")
    Call<TbtAuthResponse> getTbtAuthKey(@Body TbtAuthRequest tbtAuthRequest);


    @GET("user-utility/firebase/token")
    Call<CustomTokenResponse> getFirebaseToken();
    //    ------------------------SAVE TRIP SUMMARY API-----------------------
    @POST("utility/tbtutility/auth/saveTrip")
    Call<OtapSaveDeviceinfoApiResponse> saveTripSummaryApi(@Body SaveTripSummaryDetailsApiRequest saveTripSummaryDetailsApiRequest);
}

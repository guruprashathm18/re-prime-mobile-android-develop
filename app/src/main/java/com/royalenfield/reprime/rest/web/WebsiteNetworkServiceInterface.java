package com.royalenfield.reprime.rest.web;

import com.google.gson.JsonObject;
import com.royalenfield.reprime.models.request.logs.ConnectedLogs;
import com.royalenfield.reprime.models.request.logs.Log;
import com.royalenfield.reprime.models.request.logs.LogRequest;
import com.royalenfield.reprime.models.request.web.changepassword.ChangePasswordRequest;
import com.royalenfield.reprime.models.request.web.forgotpassword.OtpRequest;
import com.royalenfield.reprime.models.request.web.generatetoken.GenerateTokenRequest;
import com.royalenfield.reprime.models.request.web.joinride.JoinRideRequest;
import com.royalenfield.reprime.models.request.web.login.LoginRequest;
import com.royalenfield.reprime.models.request.web.otp.OtpRequestModel;
import com.royalenfield.reprime.models.request.web.otp.OtpVerifyRequestModel;
import com.royalenfield.reprime.models.request.web.otp.SendOtpRequest;
import com.royalenfield.reprime.models.request.web.otp.SubmitOtpRequest;
import com.royalenfield.reprime.models.request.web.resetpassword.ResetPasswordRequest;
import com.royalenfield.reprime.models.request.web.signup.SignupRequest;
import com.royalenfield.reprime.models.request.web.signup.VerifyAccountRequest;
import com.royalenfield.reprime.models.request.web.updateprofile.UpdateProfileRequest;
import com.royalenfield.reprime.models.request.web.vehicledetails.UpdateVehicleDetailRequest;
import com.royalenfield.reprime.models.request.web.vehicledetails.VehicleDetailRequest;
import com.royalenfield.reprime.models.request.web.vehicleonboarding.VerifyVehicleRequest;
import com.royalenfield.reprime.models.response.web.UpdateProfile.ChangePassword;
import com.royalenfield.reprime.models.response.web.UpdateProfile.UpdateProfile;
import com.royalenfield.reprime.models.response.web.booking.BookingResponse;
import com.royalenfield.reprime.models.response.web.checkin.AddCheckInResponse;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.models.response.web.createride.CreateRideResponse;
import com.royalenfield.reprime.models.response.web.generatetoken.GenerateTokenResponse;
import com.royalenfield.reprime.models.response.web.joinride.JoinRideResponse;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.login.UpdatePersonalDetailResponse;
import com.royalenfield.reprime.models.response.web.navigation.ImageUploadResponse;
import com.royalenfield.reprime.models.response.web.otp.LoginOtpResponse;
import com.royalenfield.reprime.models.response.web.otp.OtpResponse;
import com.royalenfield.reprime.models.response.web.otp.ResetPasswordResponse;
import com.royalenfield.reprime.models.response.web.profile.GetUserResponse;
import com.royalenfield.reprime.models.response.web.profile.UploadProfilePic;
import com.royalenfield.reprime.models.response.web.publishride.RideResponse;
import com.royalenfield.reprime.models.response.web.qr.QrResponse;
import com.royalenfield.reprime.models.response.web.qr.RequestQR;
import com.royalenfield.reprime.models.response.web.signup.ConsentResponse;
import com.royalenfield.reprime.models.response.web.signup.RequestConsent;
import com.royalenfield.reprime.models.response.web.signup.VerifyAccountResponse;
import com.royalenfield.reprime.models.response.web.vehicledetails.UpdateVehicleResponse;
import com.royalenfield.reprime.models.response.web.vehicledetails.VehicleDetailResponse;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.UploadVehicleResponse;
import com.royalenfield.reprime.models.response.web.verifytoken.VerifyTokenResponse;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.CommonResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.ProvisionUpdateStatusRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingRequestModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingRequestModelRead;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel;
import com.royalenfield.reprime.ui.home.service.diy.interactor.AzureFileDownloadResponse;
import com.royalenfield.reprime.ui.userdatavalidation.otp.otpinteractor.SubmitOtpResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.UpdateAddressRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.UpdateAddressResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryListModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.SwapConfigRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.requestmodel.AddAddressRequestModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UpdateContactResponseModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel.UserContactDetailResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * With Retrofit 2, endpoints are defined inside of an interface using special retrofit annotations to
 * encode details about the parameters and request method. In addition, the return value is always a
 * parameterized Call<T> object such as Call<User>. If you do not need any type-specific response,
 * you can specify return value as simply Call<ResponseBody>.
 * <p>
 * For instance, the interface defines each endpoint in the following way:
 */

public interface WebsiteNetworkServiceInterface {

    @Headers("Content-Type: application/json")
    @POST("app/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    //------------------------------------------Request Access----------------------------------------------------------
	@POST("auth/otp-login")
	Call<LoginResponse> otpLogin(@Body OtpVerifyRequestModel credentials);

    @POST("auth/register")
    Call<LoginResponse> signUp(@Body SignupRequest body);
//

    @POST("auth/verify-user-account")
    Call<VerifyAccountResponse> verifyAccount(@Body VerifyAccountRequest verifyAccountRequest);

    @POST("auth/verify-user-account")
    @FormUrlEncoded
    Call<VerifyAccountResponse> verifyPhoneNumber(@Field("userId") String userId, @Field("otp") String otp, @Field("username") String userName);


    @POST("auth/generate-token")
    Call<GenerateTokenResponse> generateToken(@Body GenerateTokenRequest request);

    @GET("auth/verify-token")
    Call<VerifyTokenResponse> verifyToken();


    @GET("auth/logout")
    Call<OtpResponse> logout();

    @GET("user/forgot")
    Call<OtpResponse> sendOtp(@Query("username") String userName);

    @POST("user/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("/auth/send-otp")
    Call<OtpResponse> verifyOtp(@Body OtpRequest otpRequest);

    @GET("user/user-details")
    Call<GetUserResponse> getProfileData(@Header("Authorization") String authorization, @Query("userId") String userId);

    @POST("user/change-password")
    Call<ChangePassword> changePassword(@Header("Content-Type") String contentType,
                                        @Header("Authorization") String authorization,
                                        @Body ChangePasswordRequest changePasswordRequest);
/*
    @POST("reapi/getmarqueerideslist")
    Call<List<MarqueeRidesResponse>> getMarqueeRidesList(@Body MarqueeRideRequest dealersRequest);

    @POST("reapi/getpopularrideslist")
    Call<List<PopularRidesResponse>> getPopularRidesList(@Body PopularRideRequest popularRideRequest);
*/

    @POST("rides-events/create-user-ride")
    Call<CreateRideResponse> createRide(@Header("Authorization") String authorization, @Body RequestBody body);

    //------------------------------------------Profile data------------------------------------------------------------

    @POST("user/update-profile")
    Call<UpdateProfile> updateProfileData(@Header("Content-Type") String contentType,
                                          @Header("Authorization") String authorization,
                                          @Body UpdateProfileRequest updateProfileRequest);


    @POST("user/update-profile-image")
    @FormUrlEncoded
    Call<UploadProfilePic> uploadProfilePic(@Header("Authorization") String authorization, @Header("Content-Type") String contentType,
                                            @Field("userId") String userId, @Field("image") String image);

    //------------------------------------------Rides-------------------------------------------------------------------


    @POST("user/vehicle/verifyDetails")
    Call<AddVehicleResponse> verifyVehicleDetail(@Header("Authorization") String authorization, @Body VerifyVehicleRequest body);


    @GET("rides-events/fetch-checkin")
    Call<CheckInResponse> getCheckIns(@Query("rideId") String rideId);

    @POST("rides-events/publish-ride")
    @FormUrlEncoded
    Call<RideResponse> publishRide(@Header("Authorization") String authorization,
                                   @Field("rideId") String rideId);


    @FormUrlEncoded
    @POST("rides-events/start-ride")
    Call<RideResponse> startRide(@Header("Authorization") String authorization,
                                 @Field("rideId") String rideId);

    @FormUrlEncoded
    @POST("rides-events/end-ride")
    Call<RideResponse> endRide(@Header("Authorization") String authorization,
                               @Field("rideId") String rideId);

    @POST("rides-events/update-user-ride")
    Call<CreateRideResponse> modifyRide(@Header("Authorization") String authorization, @Body RequestBody body);

    @POST("rides-events/add-checkin")
    Call<AddCheckInResponse> addCheckIn(@Header("Authorization") String authorization, @Body RequestBody body);

    @POST("rides-events/join-ride")
    Call<JoinRideResponse> joinRide(@Header("Authorization") String authorization, @Body JoinRideRequest joinRideRequest);

    @Multipart
    @POST("user/vehicle/add")
    Call<UploadVehicleResponse> uploadVehicleDetail(@Header("Authorization") String authorization, @Part("json") RequestBody json, @Part MultipartBody.Part kycDoc);


    @POST("user/vehicle/getDetails")
    Call<VehicleDetailResponse> getVehicleDetails(@Header("app_id") int appId
            , @Header("Authorization") String authorization, @Body VehicleDetailRequest vehicleRequest);

    @POST("auth/otp-verification")
    Call<OtpResponse> requestOtp(@Body SendOtpRequest sendOtpRequest);

	@POST("app/sendLoginAndProfileUpdateOtp")
	Call<LoginOtpResponse> sendOtpRequest(@Body OtpRequestModel otpRequestModel);

    @POST("auth/submit-otp")
    Call<OtpResponse> submitOtp(@Body SubmitOtpRequest sendOtpRequest);

    @FormUrlEncoded
    @POST("auth/verify-otp")
    Call<OtpResponse> verifyOtp(@Field("phoneNo") String phoneNo, @Field("otp") String otp);

    @POST("user/vehicle/updateRegisNumber")
    Call<UpdateVehicleResponse> updateVehicleDetails(@Header("Authorization") String accessToken, @Header("app_id") int appId
            , @Body UpdateVehicleDetailRequest request);

    @POST("user/address-details")
    Call<UpdateProfile> addAddressDetail(@Header("Content-Type") String contentType,
                                         @Header("Authorization") String authorization,
                                         @Header("app_id") int appId,
                                         @Header("x-custom-language") String customLanguage,
                                         @Header("x-custom-country") String customCountry,
                                         @Body AddAddressRequestModel addAddressRequestModel);




    @POST("user/address-details")
    Call<UpdateAddressResponseModel> addAddressDetail(@Body UpdateAddressRequestModel updateAddressRequestModel);

    @PUT("user/address-details")
    Call<UpdateAddressResponseModel> updateAddress(@Body UpdateAddressRequestModel updateAddressRequestModel);

    @FormUrlEncoded
    @POST("user/contact-details")
    Call<UpdateContactResponseModel> addContact(@Field("callingCode") String callingCode
            , @Field("secondary") String secondaryPhoneNum
            , @Field("emergency") String emergencyPhoneNum);

    @FormUrlEncoded
    @PUT("user/contact-details")
    Call<UpdateContactResponseModel> updateContact(@Field("callingCode") String callingCode
            , @Field("secondary") String secondaryCode, @Field("emergency") String emergencyCode
            , @Field("email") String email);

    @PATCH("user/contact-details")
    Call<SubmitOtpResponseModel> swapPrimaryAndsecondary();

    @GET("user/contact-details")
    Call<UserContactDetailResponseModel> getUserContactDetail();

    @DELETE("user/contact-details")
    Call<DeleteUserContactResponse> deleteSecondaryPhoneNum();

    @POST("user/swap-approval")
    @FormUrlEncoded
    Call<SwapApprovalResponse> checkIfNumIsSwappable(@Field("number") String number, @Field("callingCode") String callingCode);

    @POST(".")
    Call<String> uploadLog(@Header("module_name") String name,@Header("app_name") String appName, @Body Log body, @QueryMap Map<String, String> url);


	@POST("saveConnectedErrorLogs")
	Call<String> UploadConnectedLog( @Body ConnectedLogs body);

	@Headers("Content-Type: application/json")
    @POST("authorize/getPairingKey")
    Call<PairingMotorcycleResponseModel> getPairingKey(@Body PairingMotorcycleRequestModel requestModel);

    @Headers("Content-Type: application/json")
    @POST("settings/read")
    Call<CommonResponseModel<SettingResponseModel>> settingsRead(@Body SettingRequestModelRead requestModel);

    @Headers("Content-Type: application/json")
    @POST("settings/update")
    Call<CommonResponseModel<String>> settingsUpdate(@Body SettingRequestModel requestModel);

    @POST("utility/uploadFilesToAzure")
    Call<ImageUploadResponse> uploadImages(@Header("Authorization") String authorization,  @Body RequestBody body);

    @PUT("user/rectify-details")
    @FormUrlEncoded
    Call<UpdatePersonalDetailResponse> updatePersonalDetail(
            @Field("fName") String fName,
            @Field("lName") String lName,
            @Field("number") String number,
            @Field("callingCode") String callingCode,
            @Field("email") String email,
            @Field("OTP") String otp);

    @POST("user/rectify-details")
    @FormUrlEncoded
    Call<UpdatePersonalDetailResponse> verifyPersonalDetail(
            @Field("number") String number,
            @Field("callingCode") String callingCode,
            @Field("email") String email);

    @GET
    Call<CountryListModel> getCountryJson(@Url String url);

    @POST("utility/downloadFilesFromAzure")
    Call<AzureFileDownloadResponse> downloadFilesFromAzure(@Header("app_id") String app_id, @Body RequestBody body);

    @GET("booking/fetchUserBookings")
    Call<BookingResponse> getBookingData(@Query("guid") String userId);

    @POST("customer/swapMobileNumberInConfig")
    Call<String> setSecondaryToPrimary(@Body SwapConfigRequestModel body);


    @Headers("Content-Type: application/json")
    @POST("provision/updateStatus")
    Call<CommonResponseModel<String>> provisionUpdateStatus(@Body ProvisionUpdateStatusRequestModel requestModel);

    @POST("user/consent")
    Call<ConsentResponse> takeUserConsentEU(@Body RequestConsent body);

    @GET("user/forget-me")
    Call<ConsentResponse> forgetMe();

    @POST("user/vehicle/validategmaqr")
    Call<QrResponse> getBarcodeData(@Body RequestQR code);
}

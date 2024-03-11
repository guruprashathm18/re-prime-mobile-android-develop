package com.royalenfield.reprime.ui.home.service.sharedpreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.VehicleServiceProgressListResponse;
import com.royalenfield.reprime.ui.home.service.specificissue.model.IssuesModel;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class REServiceSharedPreference {

    private static final String TAG = REServiceSharedPreference.class.getSimpleName();

    /**
     * Save appointment id into preference for the cancellation.
     */
    public static void saveBookedServiceCenterToPreferences(Activity activity, ServiceBookingRequest serviceBookingRequest,
                                                            String motorcycle) {
        Log.d(TAG, "saveAppointmentIDToPreferences");
        SharedPreferences.Editor editor = activity.getSharedPreferences(REConstants.MY_BOOKING_SERVICE_CENTER, MODE_PRIVATE).edit();
        editor.putString("branchId", serviceBookingRequest.getBranchID());
        editor.putString("appointment_date", serviceBookingRequest.getAppointmentDate());
        editor.putString("cust_remarks", serviceBookingRequest.getCustomerRemarks());
        editor.putString("pick_address", serviceBookingRequest.getPickUpAddress());
        editor.putString("pickup_time", serviceBookingRequest.getPickUpTime());
        editor.putString("motorcycle", motorcycle);
        editor.putString("regNum", serviceBookingRequest.getRegNo());
        editor.putString("job_card", "");
        editor.apply();
    }

    /**
     * Returns the List by adding the values fetched from shared preference
     *
     * @param activity : Activity
     * @return
     */
    public static List<VehicleServiceProgressListResponse> getBookedServiceCenterFromPreference(Activity activity) {

        Log.d(TAG, "saveAppointmentIDToPreferences");
        SharedPreferences sharedPreferences = activity.getSharedPreferences(REConstants.MY_BOOKING_SERVICE_CENTER, MODE_PRIVATE);

        List<VehicleServiceProgressListResponse> serviceAPPList = new ArrayList<>();

        String strModelName = sharedPreferences.getString("motorcycle", "");
        String branchId = sharedPreferences.getString("branchId", "");
        String appointmentDate = sharedPreferences.getString("appointment_date", "");
        String cust_remarks = sharedPreferences.getString("cust_remarks", "");
        String pick_address = sharedPreferences.getString("pick_address", "");
        String mJobCardStatus = sharedPreferences.getString("job_card", "");
        String mPickUpTime = sharedPreferences.getString("pickup_time", "");
        String registrationNumber = sharedPreferences.getString("regNum", "");

        VehicleServiceProgressListResponse serviceProgressListResponse = new VehicleServiceProgressListResponse();
        serviceProgressListResponse.setAppointmentNumber(activity.
                getSharedPreferences(REConstants.MY_APPOINTMET_PREFS_NAME, MODE_PRIVATE).getString(
                REConstants.PREF_APPOINTMENT_BOOKINGNO, ""));
        serviceProgressListResponse.setAppointmentStatus("NA");
        serviceProgressListResponse.setAppointmentDate(appointmentDate);
        serviceProgressListResponse.setDealerInfo(branchId);
        serviceProgressListResponse.setCustomerRemarks(cust_remarks);
        serviceProgressListResponse.setPickupAddres(pick_address);
        serviceProgressListResponse.setPaybleamount("NA");
        serviceProgressListResponse.setModelName(strModelName);
        serviceProgressListResponse.setRegistrationNumber(registrationNumber);
        serviceProgressListResponse.setJobcardStatus(mJobCardStatus);
        serviceProgressListResponse.setPickUpTime(mPickUpTime);
        serviceAPPList.add(serviceProgressListResponse);
        REApplication.getInstance().setServiceProgressListResponse(serviceAPPList);
        return serviceAPPList;
    }

    /**
     * Clears the local saved preference.
     */
    public static void clearBookingInfoPreferences(Context context) {
        Log.d(TAG, "Booking info pref cleared");
        SharedPreferences.Editor editor = context.getSharedPreferences(REConstants
                .MY_APPOINTMET_PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        SharedPreferences.Editor service_editor = context.getSharedPreferences(REConstants
                .MY_BOOKING_SERVICE_CENTER, MODE_PRIVATE).edit();
        service_editor.clear();
        service_editor.apply();
        SharedPreferences.Editor vehicle_editor = context.getSharedPreferences(REConstants
                .VEHICLE_DATA, MODE_PRIVATE).edit();
        vehicle_editor.clear();
        vehicle_editor.apply();

    }

    public static void saveStubbedVehicleData(Context context) {


        List<VehicleDataModel> vehicleDataList = new ArrayList<>();

        VehicleDataModel vehicleDataModel1 = new VehicleDataModel();
        vehicleDataModel1.setUserName(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getFirstName());
        vehicleDataModel1.setChaissisNo("ME3U5S5F0AF006310");
        vehicleDataModel1.setDateOfMfg("22/06/2010");
        vehicleDataModel1.setEngineNo("U5S5F0AF006310");
        vehicleDataModel1.setId("2898332");
        vehicleDataModel1.setPurchaseDate("21/06/2010");
        vehicleDataModel1.setMobileNo(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getPhone());
        vehicleDataModel1.setModelCode("800626");
        vehicleDataModel1.setModelName("Classic 500 Maroon");
        vehicleDataModel1.setRegistrationNo("97121aaa00");

        VehicleDataModel vehicleDataModel2 = new VehicleDataModel();
        vehicleDataModel2.setUserName(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getFirstName());
        vehicleDataModel2.setChaissisNo("ME3U5S5F0AF006310");
        vehicleDataModel2.setDateOfMfg("22/06/2010");
        vehicleDataModel2.setEngineNo("U5S5F0AF006310");
        vehicleDataModel2.setId("2898332");
        vehicleDataModel2.setPurchaseDate("21/06/2010");
        vehicleDataModel2.setMobileNo(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getPhone());
        vehicleDataModel2.setModelCode("800626");
        vehicleDataModel2.setModelName("Classic 500 Maroon");
        vehicleDataModel2.setRegistrationNo("97121aaa00");

        VehicleDataModel vehicleDataModel3 = new VehicleDataModel();
        vehicleDataModel3.setUserName(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getFirstName());
        vehicleDataModel3.setChaissisNo("ME3U5S5F0AF006310");
        vehicleDataModel3.setDateOfMfg("22/06/2010");
        vehicleDataModel3.setEngineNo("U5S5F0AF006310");
        vehicleDataModel3.setId("2898332");
        vehicleDataModel3.setPurchaseDate("21/06/2010");
        vehicleDataModel3.setMobileNo(REApplication.getInstance()
                .getUserLoginDetails().getData().getUser().getPhone());
        vehicleDataModel3.setModelCode("800626");
        vehicleDataModel3.setModelName("Classic 500 Maroon");
        vehicleDataModel3.setRegistrationNo("97121aaa00");

        vehicleDataList.add(vehicleDataModel1);
        vehicleDataList.add(vehicleDataModel2);
        vehicleDataList.add(vehicleDataModel3);

        SharedPreferences.Editor editor = context.getSharedPreferences(REConstants.VEHICLE_DATA, MODE_PRIVATE).edit();
        editor.putString("vehicle_data", new Gson().toJson(vehicleDataList));

        editor.apply();
    }

    public static void saveVehicleData(Context context, List<VehicleDataModel> vehicleDataList) {
        if (context != null) {
            SharedPreferences.Editor editor = context.getSharedPreferences(REConstants.VEHICLE_DATA, MODE_PRIVATE).edit();
            editor.putString("vehicle_data", new Gson().toJson(vehicleDataList));
            editor.apply();
        }
    }

    public static List<VehicleDataModel> getVehicleData(Context context) {
        if(context!=null) {
        String data;
        if (REApplication.CODE_STUB) {
            data = REUtils.loadJSONFromAsset(REApplication.getAppContext(), "vehicle_data.json");
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences(REConstants.VEHICLE_DATA, MODE_PRIVATE);
            data = sharedPreferences.getString("vehicle_data", null);
        }
        return new Gson().fromJson(data, new TypeToken<List<VehicleDataModel>>() {
            }.getType());
        }
        else
            return new ArrayList<>();
    }


    public static boolean isPopVisibleFirstTimeTheUser(Context context, String user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REConstants.FIRST_TIME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(user, true);
    }

    public static void setPopUpVisibleFirstTimeForUser(Context context, String user, boolean firstTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REConstants.FIRST_TIME, MODE_PRIVATE).edit();
        editor.putBoolean(user, firstTime);
        editor.apply();
    }

    public static boolean getVehicleOnboardingDialogFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REConstants.FIRST_TIME, MODE_PRIVATE);
        return sharedPreferences.getBoolean("VehicleOnBoardingDialogFlag", false);
    }

    public static void setVehicleOnboardingDialogFlag(Context context, boolean firstTime) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REConstants.FIRST_TIME, MODE_PRIVATE).edit();
        editor.putBoolean("VehicleOnBoardingDialogFlag", firstTime);
        editor.apply();
    }
    public static void setDeepLinkData(Context context, String deeplink_data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REConstants.DEEPLINK_DATA, MODE_PRIVATE).edit();
        editor.putString("deeplink_data", deeplink_data);
        editor.apply();
    }
    public static String getDeepLinkData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REConstants.DEEPLINK_DATA, MODE_PRIVATE);
        return sharedPreferences.getString("deeplink_data","");
    }

    /**
     * Save the specific issues selected for service.
     */
    public static void saveAllSelectedServiceIssues(Context context, Map<String, ArrayList<IssuesModel>> arrayListMap) {
        SharedPreferences.Editor editor = context.getSharedPreferences(REConstants.VEHICLE_ISSUE_PREF, MODE_PRIVATE).edit();
        editor.putString(REConstants.VEHICLE_ISSUE_DATA, new Gson().toJson(arrayListMap));
        editor.apply();
    }

    /**
     * Returns the saved specific issues for service.
     *
     * @return
     */
    public static Map<String, ArrayList<IssuesModel>> getAllSavedServiceIssues(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(REConstants.VEHICLE_ISSUE_PREF, MODE_PRIVATE);
        return new Gson().fromJson(sharedPreferences.getString(REConstants.VEHICLE_ISSUE_DATA, null),
                new TypeToken<Map<String, ArrayList<IssuesModel>>>() {
                }.getType());
    }
}

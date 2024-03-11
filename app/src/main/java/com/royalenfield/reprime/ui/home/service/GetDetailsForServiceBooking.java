package com.royalenfield.reprime.ui.home.service;

import android.content.Context;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.proxy.service.servicebooking.ServiceBookingRequest;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import com.royalenfield.reprime.utils.RELog;

public class GetDetailsForServiceBooking {

    public static ServiceBookingRequest serviceBooking(Context context, REUserModelStore mREUserModelStore, String mAppointmentDate,
                                                       DealerMasterResponse mSelectedDealerResponse, String mCustomerRemarks,
                                                       boolean isPickupSelected, String mRegistrationNo, String mChassisNo, String mSlotId,
                                                       String mPickupTime, String mEngineNumber, boolean isDoorStepSelected) {

        String phoneNo = "";
        String email = "";
        String fName = "";
        String lName = "";
        String gender = "";
        if (REUserModelStore.getInstance() != null) {
            phoneNo = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone();
            email = REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail();
            fName = REApplication.getInstance().getUserLoginDetails().getData().getUser().getFirstName();
            lName = REApplication.getInstance().getUserLoginDetails().getData().getUser().getLastName();
            gender = REUserModelStore.getInstance().getGender();
        }

        //TODO Gender has to be fecthed from Firestore userInfo which is currently not implemented
        String compId = "";
        String branchId = "";
        String city = "";
        String customerRemarks = "";
        String source = "2";
        String state = "";
        if (mSelectedDealerResponse != null) {
            compId = mSelectedDealerResponse.getBranchCode();
            branchId = mSelectedDealerResponse.getBranchCode();
            city = mSelectedDealerResponse.getCity();
            customerRemarks = mCustomerRemarks;
//            source = mSelectedDealerResponse.getDealerSource();
            state = mSelectedDealerResponse.getState();
        }
        String pickupAddress = "";
        String isPickupDrop = "No";
        String serviceBookingType = REConstants.SERVICE_BOOKING_TYPE_SELFDROP;
        if (isPickupSelected) {
            String add = null;
            try {
                String flat = REPreference.getInstance().getString(context, "flatNo");
                add = REPreference.getInstance().getString(context, "address");
                if (flat == null || flat.equals("")) {
                    pickupAddress = add;
                } else {
                    pickupAddress = "#" + flat + ", " + add;
                }
            } catch (PreferenceException e) {
                RELog.e(e);
            }
            isPickupDrop = "Yes";
            serviceBookingType = REConstants.SERVICE_BOOKING_TYPE_PICKUPANDDROP;
        }
        if (isDoorStepSelected){
            serviceBookingType =REConstants.SERVICE_BOOKING_TYPE_DOORSTEP;
            try {
                String add = REPreference.getInstance().getString(context, "address");
                pickupAddress = add;
                isPickupDrop = "No";
            } catch (PreferenceException e){

            }
        }
        String isdummyslots = REUtils.isDummySlotsEnabled();
        boolean isDummySlots = Boolean.parseBoolean(isdummyslots);

        return new ServiceBookingRequest(REUtils.trimCountryCodeFromMobile(phoneNo), mAppointmentDate, "", branchId, city, compId,
                customerRemarks, "", "", email, "", fName, mEngineNumber, lName,
                isPickupDrop, mRegistrationNo, "2", "2", state, "", mChassisNo, "",
                "", pickupAddress, "", "", mSlotId, gender, mPickupTime,
                "Yes", mSlotId,isDummySlots,serviceBookingType);

    }

}


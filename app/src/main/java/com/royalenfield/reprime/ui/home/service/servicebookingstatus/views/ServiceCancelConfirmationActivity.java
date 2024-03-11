package com.royalenfield.reprime.ui.home.service.servicebookingstatus.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.proxy.servicebooking.ServiceBookingResponse;
import com.royalenfield.reprime.models.response.proxy.vehicleserviceinprogresslist.VehicleServiceProgressListResponse;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.interactor.ServiceAppointmentCancelInteractor;
import com.royalenfield.reprime.ui.home.service.servicebookingstatus.presenter.ServiceAppointmentCancelPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class ServiceCancelConfirmationActivity extends REBaseActivity implements
        View.OnClickListener, ServiceCancelConfirmationView {
    private String mBookingNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicebooking_cancellation);
        initViews();
    }

    /**
     * Initialising views
     */
    private void initViews() {
        TextView mTextSuggestion = findViewById(R.id.textView_message);
        mTextSuggestion.setText(R.string.text_service_cancel_text);
        ImageView mClose = findViewById(R.id.imageView_close);
        mClose.setOnClickListener(this);
        Button mButtonNo = findViewById(R.id.button_no);
        mButtonNo.setOnClickListener(this);
        Button mButtonYes = findViewById(R.id.button_yes);
        mButtonYes.setOnClickListener(this);
    }

    /**
     * Cancel service appointment API call triggers from here
     */
    private void cancelServiceAppointment() {
        Bundle params=new Bundle();
        params.putString("eventCategory", "Motorcycles");
        params.putString("eventAction", "Cancel Booking");
        params.putString("eventLabel", "Yes Click");
       REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
        try {
            ServiceAppointmentCancelPresenter mServiceAppointmentCancelPresenter = new ServiceAppointmentCancelPresenter(
                    this, new ServiceAppointmentCancelInteractor());
            List<ServiceBookingResponse> mServiceBookingResponse = REApplication.getInstance().getServiceBookingResponse();
            //Checking the booking response in booking object....else taking from serviceInProgressAPI response
            //Booking object exists only if booking/rescheduling is done in the current session
            if (mServiceBookingResponse != null && mServiceBookingResponse.size() > 0) {
                mBookingNo = mServiceBookingResponse.get(0).getBookingno();
            } else {
                List<VehicleServiceProgressListResponse> mVehicleServiceInProgress = REApplication.getInstance().
                        getServiceProgressListResponse();
                if (mVehicleServiceInProgress != null && mVehicleServiceInProgress.size() > 0) {
                    mBookingNo = mVehicleServiceInProgress.get(0).getAppointmentNumber();
                }
            }
            String isdummyslots = REUtils.isDummySlotsEnabled();
            boolean isDummySlots = Boolean.parseBoolean(isdummyslots);
            mServiceAppointmentCancelPresenter.cancelServiceAppointment(mBookingNo,isDummySlots);
        } catch (Exception e) {
            RELog.e(e);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_close:
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
            case R.id.button_no:
                Bundle params=new Bundle();
                params.putString("eventCategory", "Motorcycles");
                params.putString("eventAction", "Cancel Booking");
                params.putString("eventLabel", "No Click");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
            case R.id.button_yes:
                //Does API call for cancelling scheduled service appointment
                cancelServiceAppointment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onSuccess() {
        hideLoading();
        Intent intent = new Intent();
        intent.putExtra(REConstants.SERVICE_BOOKING_CANCEL, true);
        setResult(REConstants.REQUESTCODE_BOOKINGCANCELLED, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
        showToastMessage(getApplicationContext().getResources().getString(R.string.service_booking_cancelled));
    }

    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Cancel Booking");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }
}

package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.royalenfield.bluetooth.utils.BLEConstants;
import com.royalenfield.bluetooth.utils.BLEDeviceManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.web.checkin.CheckInResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.rides.interactor.RidesInteractor;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesPresenter;
import com.royalenfield.reprime.ui.home.rides.views.RidesView;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

/**
 * This activity is used for ride confirmation
 */
public class ExitRideActivity extends REBaseActivity implements View.OnClickListener, RidesView {

    private RidesPresenter mRidesPresenter;
    private String mRideId;
    private String mType, mDisconnectType;
    private int mBleDevicePosition;
    Button mButtonYes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_save_confirm);
        getIntentData();
        initViews();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            mRideId = getIntent().getStringExtra(REConstants.RIDE_ID);
            mType = getIntent().getStringExtra(REConstants.SUCCESS_ACTIVITY);
            mDisconnectType = getIntent().getStringExtra(BLEConstants.BLE_USER_ACTION_TYPE);
            mBleDevicePosition = getIntent().getIntExtra(BLEConstants.BLE_DEVICE_POSITION,-1);
        }
    }

    /**
     * Initialising views
     */
    private void initViews() {
        TextView mTextMessage = findViewById(R.id.textView5);
        mRidesPresenter = new RidesPresenter(this, new RidesInteractor());
        ImageView mImageClose = findViewById(R.id.imageView_close);
        mImageClose.setOnClickListener(this);
        mButtonYes = findViewById(R.id.button_yes);
        mButtonYes.setOnClickListener(this);
        Button mButtonNo = findViewById(R.id.button_no);
        mButtonNo.setOnClickListener(this);
        if (mType != null && mType.equalsIgnoreCase(REConstants.BLE_ACTION_CONFIRMATION)) {
            if (mDisconnectType != null) {
                if (mDisconnectType.equals(BLEConstants.BLE_DEVICE_FORGET)) {
                    //mTextMessage.setText(R.string.text_ble_iOS_device_bricked);
                   // mButtonYes.setText("OK");
                   // mButtonNo.setVisibility(View.GONE);
                   if( BLEDeviceManager.getMyTBTList(this).get(mBleDevicePosition).getStatus()!=null && BLEDeviceManager.getMyTBTList(this).get(mBleDevicePosition).getStatus().equalsIgnoreCase("Y")) {
                      if (BLEDeviceManager.getMyTBTList(this).get(mBleDevicePosition).getAddress()!=null &&BLEDeviceManager.getMyTBTList(this).get(mBleDevicePosition).getAddress().length()>5 ) {
                          mTextMessage.setText(R.string.text_ble_android_device_bricked);
                          mButtonYes.setText("OK");
                          mButtonNo.setVisibility(View.GONE);
                      }else{
                          mTextMessage.setText(R.string.text_ble_iOS_device_bricked);
                          mButtonYes.setText("OK");
                          mButtonNo.setVisibility(View.GONE);
                      }
                   } else {
                       mTextMessage.setText(R.string.text_ble_device_forget);
                       mButtonYes.setText(R.string.yes_unapair);
                       Bundle bundle = new Bundle();
                       bundle.putString("screenName", "Tripper Unpair Devices");
                       REUtils.logGTMEvent(REConstants.ScreenViewManual, bundle);
                   }
                } else if (mDisconnectType.equals(BLEConstants.BLE_DEVICE_DISCONNECT)) {
                    mTextMessage.setText(R.string.text_ble_device_disconnect);
                    mButtonYes.setText(R.string.yes_disconnect);
                    Bundle bundle = new Bundle();
                    bundle.putString("screenName", "Tripper Disconnect Devices");
                    REUtils.logGTMEvent(REConstants.ScreenViewManual, bundle);
                }
            }
            mButtonNo.setText(R.string.no);
        } else {
            mButtonYes.setText(R.string.button_text_yes_share);
            mButtonNo.setText(R.string.button_text_no_share);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_no:
                if(mButtonYes.getText().toString().equals(getString(R.string.yes_unapair))){
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Tripper");
                    params.putString("eventAction", "Unpair Device");
                    params.putString("eventLabel", "No Clicked");
                    REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
                }
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
            case R.id.imageView_close:
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
            case R.id.button_yes:
                if (mType != null && mType.equalsIgnoreCase(REConstants.BLE_ACTION_CONFIRMATION)) {
                    if(mButtonYes.getText().toString().equals(getString(R.string.yes_disconnect))){
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Disconnect Device");
                        params.putString("eventLabel", "Yes Disconnect Clicked");
                        REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
                    }
                    if(mButtonYes.getText().toString().equals(getString(R.string.yes_unapair))){
                        Bundle params = new Bundle();
                        params.putString("eventCategory", "Tripper");
                        params.putString("eventAction", "Unpair Device");
                        params.putString("eventLabel", "Yes Unpair Clicked");
                        REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
                        try {
                            REPreference.getInstance().putBoolean(this, "isTripperUnpaired", true);
                        } catch (PreferenceException e) {
                            e.printStackTrace();
                        }
                    }
                    // Sending broadcast to update the text
                    finish();
                    Intent userResponseUpdate = new Intent(REConstants.BLE_USER_RESPONSE);
                    userResponseUpdate.putExtra("type", mDisconnectType);
                    if (!mButtonYes.getText().equals("OK"))
                    userResponseUpdate.putExtra("actionResponse", true);
                    else
                        userResponseUpdate.putExtra("actionResponse", false);
                    userResponseUpdate.putExtra(BLEConstants.BLE_DEVICE_POSITION, mBleDevicePosition);
                    LocalBroadcastManager.getInstance(REApplication.getAppContext()).sendBroadcast(userResponseUpdate);
                }
                if (mRideId != null && !mRideId.isEmpty()) {
                    mRidesPresenter.endRide(mRideId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetCheckInSuccess(CheckInResponse response) {

    }

    @Override
    public void onGetCheckInFailure(String errorMessage) {

    }

    @Override
    public void onPublishRideSuccess() {

    }

    @Override
    public void onPublishRideFailure(String errorMessage) {

    }

    @Override
    public void onUpdateRideStatusSuccess() {
        hideLoading();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        Intent mIntent = new Intent(getApplicationContext(), AccountCreationSuccessActivity.class);
        mIntent.putExtra(REConstants.SUCCESS_ACTIVITY, REConstants.ENDRIDE_TYPE);
        startActivity(mIntent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        finish();
    }

    @Override
    public void onUpdateRideStatusFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
    }
}

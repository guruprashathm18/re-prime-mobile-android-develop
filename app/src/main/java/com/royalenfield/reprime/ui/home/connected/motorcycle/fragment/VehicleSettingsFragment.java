package com.royalenfield.reprime.ui.home.connected.motorcycle.fragment;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.fragments.FindMyMotorcycleFragment;
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDResponseData;
import com.royalenfield.reprime.ui.home.connected.motorcycle.interactor.MotorcycleInteractor;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel;
import com.royalenfield.reprime.ui.home.connected.motorcycle.presenter.MotorcyclePresenter;
import com.royalenfield.reprime.ui.home.homescreen.fragments.MotorCycleFragment;
import com.royalenfield.reprime.ui.home.homescreen.view.IMotorcycleFragmentView;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class VehicleSettingsFragment extends REBaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IMotorcycleFragmentView {

    private static VehicleSettingsFragment vehicleSettingsFragment;
    private String modelName;
    private String registrationNumber;
    private TextView txtBikeModel;
    private TextView txtRegNo;
    private TextView txtSoftwareVersion;
    private TextView txtDeviceSerialNo;
    private TextView txtIMeiNO;
    private SettingResponseModel deviceSetting;
    private Switch switchToggleLocation;
    private Switch switchToggleNotification;
    private MotorcyclePresenter motorcyclePresenter;
    private Dialog settingDialog;
    private Fragment fragmentInstance;
    private TextView txtConfig;
    private TextView txtVehicle;
    public static VehicleSettingsFragment getInstance(Fragment fragmentInstance, String modelName, String registrationNumber, SettingResponseModel deviceSetting) {

        vehicleSettingsFragment = new VehicleSettingsFragment();
        vehicleSettingsFragment.modelName = modelName;
        vehicleSettingsFragment.registrationNumber = registrationNumber;
        vehicleSettingsFragment.fragmentInstance = fragmentInstance;
        vehicleSettingsFragment.deviceSetting = deviceSetting;
        return vehicleSettingsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vehicle_settings, container, false);
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Connected Vehicle Settings");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        initViews(rootView);
        motorcyclePresenter = new MotorcyclePresenter(this, new MotorcycleInteractor());
        return rootView;
    }

    private void initViews(View rootView) {
        TextView tvHeader = rootView.findViewById(R.id.tv_search_banner);
        tvHeader.setText(getString(R.string.motorcycle_settings));
        txtBikeModel = rootView.findViewById(R.id.txtBikeModel);
        txtRegNo = rootView.findViewById(R.id.txtRegNo);
        txtSoftwareVersion = rootView.findViewById(R.id.txtSoftwareVersion);
        txtDeviceSerialNo = rootView.findViewById(R.id.txtDeviceSerialNo);
        txtIMeiNO = rootView.findViewById(R.id.txtIMeiNO);
        txtConfig=rootView.findViewById(R.id.txt_config);
        txtVehicle=rootView.findViewById(R.id.txt_vehicle);
        switchToggleLocation = rootView.findViewById(R.id.switchToggleLocation);
        switchToggleNotification = rootView.findViewById(R.id.switchToggleNotification);

        setSettingsData();
        rootView.findViewById(R.id.back_image).setOnClickListener(this);
        switchToggleLocation.setOnCheckedChangeListener(this);
        switchToggleNotification.setOnCheckedChangeListener(this);
    }

    private void setSettingsData() {
        txtBikeModel.setText(modelName);
        txtRegNo.setText(registrationNumber);
        if (deviceSetting != null) {
            txtSoftwareVersion.setText(getString(R.string.vehicle_version_no, deviceSetting.getFirmwareVersion()));
            txtDeviceSerialNo.setText(deviceSetting.getDeviceSerialNumber());
            txtIMeiNO.setText(deviceSetting.getImei().trim());
            switchToggleNotification.setChecked(deviceSetting.getNotificationFlag());
            switchToggleLocation.setChecked(deviceSetting.getLocationAccessFlag());
            txtConfig.setText(deviceSetting.getConfigVersion()+"");
            txtVehicle.setText(deviceSetting.getVin().trim());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        hideREHeaderFromActivity(context, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (fragmentInstance instanceof FindMyMotorcycleFragment) {
            ((FindMyMotorcycleFragment) fragmentInstance).updateDeviceSettingData(deviceSetting);
            hideREHeaderFromActivity(getContext(), false);
        } else if (fragmentInstance instanceof MotorCycleFragment) {
            ((MotorCycleFragment) fragmentInstance).updateSettingsData(deviceSetting);
            hideREHeaderFromActivity(getContext(), true);
        }
    }

    private void hideREHeaderFromActivity(Context context, boolean hideShowValue) {
        Activity activity = (Activity) context;
        if (activity instanceof REHomeActivity) {
            ((REHomeActivity) activity).showHideREHeader(hideShowValue);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_image) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton.getId() == R.id.switchToggleLocation) {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Connected Module");
            params.putString("eventAction", "Vehicle Settings");
            if (!isChecked) {
                params.putString("eventLabel", "Location_"+"off");

                showAlertDialog(switchToggleLocation, getString(R.string.confirmation_turn_off_location));
            } else {
                params.putString("eventLabel", "Location_"+"on");
                updateSettings();
            }
            params.putString("modelName",modelName);
            REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
            //Toast.makeText(getContext(), "Location : " + isChecked, Toast.LENGTH_SHORT).show();
        } else if (compoundButton.getId() == R.id.switchToggleNotification) {
            //Toast.makeText(getContext(), "Notification : " + isChecked, Toast.LENGTH_SHORT).show();
            Bundle params = new Bundle();
            params.putString("eventCategory", "Connected Module");
            params.putString("eventAction", "Vehicle Settings");
            if (!isChecked) {
                params.putString("eventLabel", "Notification_"+"off");
                showAlertDialog(switchToggleNotification, getString(R.string.confirmation_turn_off_notification));
            } else {
                params.putString("eventLabel", "Notification_"+"on");
                updateSettings();
            }
            params.putString("modelName",modelName);
            REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);
        }
    }

    private void showAlertDialog(Switch switchToggle, String msg) {
        settingDialog = new Dialog(getActivity(), R.style.blurTheme); // Context, this, etc.
        settingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        settingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        settingDialog.setCancelable(false);
        settingDialog.setContentView(R.layout.layout_alert_dialog);
        settingDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        TextView message = settingDialog.findViewById(R.id.message);
        message.setText(msg);

        settingDialog.findViewById(R.id.no).setOnClickListener(view -> {
            switchToggle.setOnCheckedChangeListener(null);
            switchToggle.setChecked(true);
            switchToggle.setOnCheckedChangeListener(this);
            settingDialog.dismiss();
            Bundle params = new Bundle();
            params.putString("eventCategory", "Connected Module");
            params.putString("eventAction", "Vehicle Settings");
            params.putString("eventLabel", "Cancel");
            params.putString("modelName",modelName);
            REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);

        });

        settingDialog.findViewById(R.id.yes).setOnClickListener(view -> {
            updateSettings();
            settingDialog.dismiss();
            Bundle params = new Bundle();
            params.putString("eventCategory", "Connected Module");
            params.putString("eventAction", "Vehicle Settings");
            params.putString("eventLabel", "Turn Off");
            params.putString("modelName",modelName);
            REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params);

        });

        settingDialog.show();
    }

    private void updateSettings() {
        deviceSetting.setLocationAccessFlag(switchToggleLocation.isChecked());
        deviceSetting.setNotificationFlag(switchToggleNotification.isChecked());
        motorcyclePresenter.updateSettings(deviceSetting.getGuid(), deviceSetting.getImei()
                , switchToggleLocation.isChecked(), switchToggleNotification.isChecked());
    }

    public void settingsUpdated(String msg) {
        //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        if (settingDialog != null && settingDialog.isShowing()) {
            settingDialog.dismiss();
            settingDialog = null;
        }
    }

    @Override
    public void onProvisionUpdateStatusSuccess(String msg) {

    }

    @Override
    public void onProvisionUpdateStatusFailure(String msg) {
        hideLoading();
        if(msg==null)
            msg=getResources().getString(R.string.something_went_wrong);
        REUtils.showErrorDialog(getActivity(), msg);
    }

    @Override
    public void onDataSuccess(OBDResponseData payload) {

    }

    @Override
    public void onDataFailure(String errorMsg) {

    }

    @Override
    public void updateDeviceData(PairingMotorcycleResponseModel.GetDeviceData deviceData) {

    }

    @Override
    public void pairingDataFail(String msg) {
        REUtils.showErrorDialog(getActivity(),msg);
        hideLoading();
    }

    @Override
    public void updateSettingsData(SettingResponseModel settings) {

    }

    @Override
    public void checkConsentOfDevice() {

    }

    @Override
    public void consentSettingsUpdated(String msg) {
//        if (settingDialog != null && settingDialog.isShowing()) {
//            settingDialog.dismiss();
//            settingDialog = null;
//        }
    }
}

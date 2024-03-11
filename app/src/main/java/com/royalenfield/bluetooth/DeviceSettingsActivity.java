package com.royalenfield.bluetooth;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.bluetooth.adapters.DeviceSettingsAdapter;
import com.royalenfield.bluetooth.ble.BleManagerProvider;
import com.royalenfield.bluetooth.ble.DeviceInfo;
import com.royalenfield.bluetooth.ble.IBleManager;
import com.royalenfield.bluetooth.models.DeviceSettingsItems;
import com.royalenfield.bluetooth.otap.BlockSegmentUtils;
import com.royalenfield.bluetooth.otap.DownloadFirmwareFile;
import com.royalenfield.bluetooth.otap.RELogger;
import com.royalenfield.bluetooth.otap.interactor.OtapInteractor;
import com.royalenfield.bluetooth.otap.listener.OnOTAPCallback;
import com.royalenfield.bluetooth.otap.listener.OtapInstallationStatusListener;
import com.royalenfield.bluetooth.utils.BLEDeviceManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.otap.UpdateTripperDeviceApiRequest;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.fragment.OTAPUpdateFragment;
import com.royalenfield.reprime.ui.splash.activity.RESuccessSplashActivity;
import com.royalenfield.reprime.utils.REDialogUtils;
import com.royalenfield.reprime.utils.RELog;
import com.royalenfield.reprime.utils.REUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeviceSettingsActivity extends REBaseActivity implements OnOTAPCallback,
        OTAPUpdateFragment.ItemClickListener, TitleBarView.OnNavigationIconClickListener, DeviceSettingsAdapter.OnItemClickListener, OtapInstallationStatusListener {

    private static final int EXTERNAL_WRITE_PERMISSIONS = 10;
    private String deviceName;
    private String deviceIndex = "";
    private Integer tripperIndex = null;
    private List<DeviceInfo> deviceInfoArrayList;
    //private boolean connectionStatus = false;
    private boolean connectionStatus;
    TextView mDeviceName;
    ImageButton mImgConnectionStatus;
    private OTAPUpdateFragment otapUpdateFragment;
    private IBleManager manager = BleManagerProvider.INSTANCE.getInstance();
    private Map<String, Object> mOtapMapData;
    static boolean active = false;
    private static final int REQUEST_PERMISSION_SETTING = 14;
    private SharedPreferences sharedpreferences;
    BlockSegmentUtils blockSegmentUtils;
    //private ProgressDialog progressDialog;
    private RecyclerView mContentsRecyclerView;
    private Handler mDelayHandler = new Handler();
    private ProgressDialog mProgressLoading;
    private DownloadFirmwareFile mDownloadFirmwareFile;
    private RELogger reLogger;
    private File myStoredOTAPFile, myStoredInfoFile;
    private AlertDialog mOtapDialog = null;
    private AlertDialog mWaitDialog = null;
    private AlertDialog mAlertDialog = null;
    private AlertDialog mAlertRetryDialog = null;
    private AlertDialog mAlertPoPUPInfoDialog = null;
    private ProgressBar mProgressBar;
    private TextView mAlertMessageTXT, mPercentageTXT, mAlertOkTXT;
    private static final int NOTIF_ID = 1;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private SharedPreferences.Editor mOtapPrefEditor;
    OtapInteractor mOtapInteractor = new OtapInteractor();
    private boolean isOtapInstallationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        reLogger = new RELogger(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            DeviceSettingsActivity.this.setTurnScreenOn(true);
        } else {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        sharedpreferences = getApplicationContext().getSharedPreferences("RE_APP", Context.MODE_PRIVATE);
        mOtapPrefEditor = sharedpreferences.edit();
        getIntentData();
        deviceInfoArrayList = BLEDeviceManager.getMyTBTList(this);
        checkSoftwareVersion();
        initViews();
        checkConnectionStatus();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mconnectionBroadcastReceiver,
                new IntentFilter("updateconnection"));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mOtapProgressReceiver,
                new IntentFilter("otapprogress"));

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mOtapStatus,
                new IntentFilter("otapstatus"));
    }

    /**
     * Initializing views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tbt_device_settings);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().
                getString(R.string.title_device_settings));
        mDeviceName = findViewById(R.id.txtDeviceName);
        mImgConnectionStatus = findViewById(R.id.imgDeviceStatus);
        mContentsRecyclerView = findViewById(R.id.contents_recyclerview);
        mContentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(mContentsRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        mDeviceName.setText(deviceName);
        showLoading();
        mDelayHandler.postDelayed(mRunnable, 2000);
    }

    private Runnable mRunnable = this::addListItems;

    /**
     * Gets the intent data from device adapter
     */
    private void getIntentData() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            deviceName = intent.getStringExtra("devicename");
            deviceIndex = intent.getStringExtra("deviceindexinArraylist");
            connectionStatus = intent.getBooleanExtra("connectionstatus", false);
            mOtapMapData = (Map<String, Object>) intent.getSerializableExtra("otapmap");
            try {
                if (!deviceIndex.isEmpty())
                    tripperIndex = Integer.parseInt(deviceIndex);

            } catch (NumberFormatException e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case EXTERNAL_WRITE_PERMISSIONS:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted
                        checkFileAndShowUpdate();
                    } else {
                        // permission denied
                        //  checkFileStoragePermissions();
                    }
                    break;

            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    String permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkFileStoragePermissions() {
        int result, p;
        List<String> listPermissionsNeeded = new ArrayList<>();
        result = checkSelfPermission(permissions);
        if (result != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_WRITE_PERMISSIONS);
        } else {
            checkFileAndShowUpdate();
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_WRITE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void startDownloadingFiles() {
        startDownloadingSrecFile(mOtapMapData);
    }

    private void startDownloadingSrecFile(Map<String, Object> otapFirestoreData) {
        mDownloadFirmwareFile = new DownloadFirmwareFile(DeviceSettingsActivity.this, otapFirestoreData, this, connectionStatus);
        mDownloadFirmwareFile.setDownloadFileListener(() -> {
            startDownloadingFiles();
        });
//        mDownloadFirmwareFile.setStartInstallationListener(() -> {
//            showWaitDialog();
//        });
    }

    /**
     * Checking the device connection status
     */
    private void checkConnectionStatus() {
        if (connectionStatus && REApplication.getInstance().ismIsDeviceConnected()) {
            mImgConnectionStatus.setImageResource(R.drawable.ic_device_connected);
        } else {
            mImgConnectionStatus.setImageResource(R.drawable.ic_device_disconnected);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case EXTERNAL_WRITE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    displayNeverAskAgainDialog();
                }
                break;
        }
    }

    @Override
    public void onOTAPFirestoreSuccess(Map<String, Object> map) {
        mOtapMapData = map;
        RELog.e("NAV", "onOTAPFirestoreSuccess:" + map.get("Decription"));

    }

    @Override
    public void onOTAPFirestoreFailure(String message) {
        RELog.e("NAV", "onOTAPFirestoreFailure:");
        //initBle();
    }

    @Override
    public void onInstallClick() {
        displayInformationPoPUP();
    }

    private void displayInformationPoPUP() {
        try {
            TextView alert_message, alert_InstallInfo_OK, alert_Cancel;
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_alert_otap_info, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert retry dialog
            alert_message.setText(getResources().getString(R.string.text_otap_display_info));
            alert_InstallInfo_OK = alertLayout.findViewById(R.id.textView_alert_ok_button);
            alert_Cancel = alertLayout.findViewById(R.id.textView_alert_cancel_button);
            AlertDialog.Builder alert = new AlertDialog.Builder(DeviceSettingsActivity.this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            mAlertPoPUPInfoDialog = alert.create();
            mAlertPoPUPInfoDialog.show();
            //This is for ok click which dismisses the dialog
            alert_InstallInfo_OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertPoPUPInfoDialog.dismiss();
                    //start Otap download from Here.
                    onInstall();
                }
            });
            alert_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertPoPUPInfoDialog.dismiss();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void onInstall() {
        otapUpdateFragment.dismiss();
        showWaitDialog();
        if (mDownloadFirmwareFile != null && mDownloadFirmwareFile.isDownloading()) {
            mDownloadFirmwareFile.showDialog(DeviceSettingsActivity.this);
        } else {
            startDownloadingFiles();
        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Functionality Limited!!");
        builder.setMessage("We need background location permission necessary, Please permit the permission through "
                + "Settings screen.");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
        });
        //Abuilder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDownloadFirmwareFile != null && !mDownloadFirmwareFile.isDownloading()) {
            dismissWaitDialog();
        }
    }

    private boolean isSoftwareUpdateAvailable() {
        if (mOtapMapData != null && mOtapMapData.get("firmwareVersion") != null) {
            String aFireStoreVer = Objects.requireNonNull(mOtapMapData.get("firmwareVersion")).toString();//"07.06";
            String aDeviceVer = getCurrentVersion();//"10.09";
            String mVersionLog = "aDeviceVer:" + aDeviceVer + " && aFireStoreVer:" + aFireStoreVer;
            //Toast.makeText(getApplicationContext(), mVersionLog, Toast.LENGTH_SHORT).show();
            reLogger.appendLog(mVersionLog);
            //Log.e("DeviceVersionLog ",""+mVersionLog);
            if (!aFireStoreVer.isEmpty() && !aDeviceVer.isEmpty())
                return compareVersions(aFireStoreVer, aDeviceVer);
            else
                return false;
        } else
            return false;
    }

    private boolean isBaseVersionGreater() {
        if (mOtapMapData != null && mOtapMapData.get("firmwareBaseVersion") != null) {
            String aFireStoreBaseVer = Objects.requireNonNull(mOtapMapData.get("firmwareBaseVersion")).toString();//"07.06";
            String aDeviceVer = getCurrentVersion();//"10.09";
            //String aFireStoreBaseVer = "11.09";
            //String aDeviceVer = "10.09";
            String mVersionLog = "aDeviceVer:" + aDeviceVer + " && aFireStoreBaseVer:" + aFireStoreBaseVer;
            //Toast.makeText(getApplicationContext(), mVersionLog, Toast.LENGTH_SHORT).show();
            reLogger.appendLog(mVersionLog);
            if (!aFireStoreBaseVer.isEmpty() && !aDeviceVer.isEmpty())
                return compareBaseVersions(aFireStoreBaseVer, aDeviceVer);
            else
                return false;
        } else
            return false;
    }

    private String getCurrentVersion() {
        return sharedpreferences.getString("currentSoftwareVersion", "");
    }

    private String getTripperUniqueId() {
        return sharedpreferences.getString("currentTripperUniqueId", "");
    }

    private void checkSoftwareVersion() {
        if (connectionStatus && REApplication.getInstance().ismIsDeviceConnected()) {
            blockSegmentUtils = new BlockSegmentUtils(this);
            blockSegmentUtils.sendSoftwareVersionMessage();
        }
    }

    private void checkTripperUniqueId() {
        if (connectionStatus && REApplication.getInstance().ismIsDeviceConnected()) {
            blockSegmentUtils = new BlockSegmentUtils(this);
            //blockSegmentUtils.sendTripperUniqueIdMessage();
        }
    }

    /**
     *
     */
    private BroadcastReceiver mOtapProgressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mWaitDialog != null) {
                dismissWaitDialog();
            }
            if (!Objects.requireNonNull(mOtapMapData.get("firmwareVersion")).toString().isEmpty() && connectionStatus) {
                if (getCurrentVersion().equals(Objects.requireNonNull(mOtapMapData.get("firmwareVersion")).toString())) {
                    //Not OTAP data
                } else {
                    if ("otapprogress".equals(intent.getAction())) {
                        boolean isBluetoothConnected = intent.getBooleanExtra("status", false);
                        int total = intent.getIntExtra("total", -1);
                        int completed = intent.getIntExtra("completed", 1);
                        if (!isBluetoothConnected) {
                            if (mWaitDialog != null) {
                                dismissWaitDialog();
                            }
                            if (mOtapDialog != null) {
                                dismissOTAPProgressDialog();
                                resetListItems();
                            }
                            //showAlertDilogwithRetryOption
                            if (isOtapInstallationInProgress) {
                                showRetryAlert();
                                reLogger.appendLog("Show RetryAlert called inside isOtapInstallationInProgress -1");
                            }


                        } else {
                            try {
                                if (mWaitDialog != null) {
                                    dismissWaitDialog();
                                }
                                if (mOtapDialog == null) {
                                    showOTAPProgressDialog();
                                    mAlertMessageTXT.setText(getString(R.string.text_alert_please_wait));
                                }

                                showOTAPProgressDialog();
                                if (total == 0) {
                                    mAlertMessageTXT.setText(getString(R.string.text_alert_please_wait));
                                    //Start Foreground Service
                                    // startService();
                                } else {
                                    mAlertMessageTXT.setText(getString(R.string.text_otap_display_info_caution));
                                    double mResult = ((float) completed) / total;
                                    mResult = mResult * 100;
                                    String mProgress = new DecimalFormat("0.0").format(mResult);
                                    mPercentageTXT.setText(mProgress + "%");
                                    updateNotification(mProgress + "%");
                                    // mProgressBar.setProgress((int) Double.parseDouble(mProgress));
                                    mProgressBar.setProgress((int) mResult);
                                }
                                if (connectionStatus) {
                                    if (mOtapDialog != null)
                                        showOTAPProgressDialog();
                                } else {
                                    if (mOtapDialog != null)
                                        dismissOTAPProgressDialog();
                                    showRetryAlert();
                                    reLogger.appendLog("Show RetryAlert called-2");
                                }
                            } catch (Exception e) {
                                Log.e("showRetryAlert: ", e.getMessage());
                                if (mWaitDialog != null) {
                                    dismissWaitDialog();
                                }
                                e.printStackTrace();
                                showRetryAlert();

                                reLogger.appendLog("Show RetryAlert called inside catch block-3" + e.getMessage());
                            }
                        }
                    }
                }
            }

        }
    };


    /**
     *
     */
    private BroadcastReceiver mOtapStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("otapstatus".equals(intent.getAction())) {
                if (!Objects.equals(getIntent().getStringExtra("status"), "1"))
                    dismissWaitDialog();
                int status = intent.getIntExtra("status", -1);
                try {
                    if (status == 1) {
                        dismissOTAPProgressDialog();
                        // Otap Installation completetd. Reset the flag.
                        isOtapInstallationInProgress = false;
                        if (mAlertRetryDialog != null) {
                            mAlertRetryDialog.dismiss();
                        }
                        // Saving the latest updated file as current software version
                        if (mOtapMapData != null && mOtapMapData.get("firmwareVersion") != null) {
                            String deviceVersion = String.valueOf(mOtapMapData.get("firmwareVersion"));
                            if (!deviceVersion.isEmpty()) {
                                mOtapPrefEditor.putString("currentSoftwareVersion", deviceVersion);
                                mOtapPrefEditor.apply();
                                //Toast.makeText(context, "mOtapStatusReceiver:Storing deviceVersion:" + deviceVersion, Toast.LENGTH_SHORT).show();
                                reLogger.appendLog("mOtapStatusReceiver:Storing deviceVersion:" + deviceVersion);
                            }
                        }
                        //Reset Brick status
                        updateBrickStatus("N", "Success");

                        runOnUiThread(() -> {
                            Intent intentsuccess = RESuccessSplashActivity.getIntent(getApplicationContext(), null,
                                    getResources().getString(R.string.software_update_success));
                            startActivity(intentsuccess);
                            overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                        });
                        resetListItems();
                        mWaitDialog = null;
                        mOtapDialog = null;
                    } else if (status == 0) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Software Update Failed", Toast.LENGTH_SHORT).show());

                        runOnUiThread(() -> showRetryAlert());
                        reLogger.appendLog("Show RetryAlert called inside else if - status 0");
                    } else if (status == 2) {
                        // runOnUiThread(() -> showRetryAlert());
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Software Update Failed with 2 !!!", Toast.LENGTH_SHORT).show());
                        reLogger.appendLog("Show RetryAlert called inside else if - status 2");

                    }

                } catch (Exception e) {
                    RELog.e(e);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mconnectionBroadcastReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mOtapProgressReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mOtapStatus);
        mDelayHandler.removeCallbacks(mRunnable);
        // stopService();
    }

    /**
     *
     */
    private BroadcastReceiver mconnectionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("updateconnection".equals(intent.getAction()) && mImgConnectionStatus != null) {
                boolean b = intent.getBooleanExtra("connected", false);
                if (!b) {
                    mImgConnectionStatus.setImageResource(R.drawable.ic_device_disconnected);
                } else {
                    mImgConnectionStatus.setImageResource(R.drawable.ic_device_connected);
                }
            }
        }
    };

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    public void addListItems() {
        ArrayList<DeviceSettingsItems> mDeviceSettingsItemsArrayList = new ArrayList<>();
        DeviceSettingsItems aDeviceSettingsItems;
        aDeviceSettingsItems = new DeviceSettingsItems();
        aDeviceSettingsItems.setItemId(1);
        aDeviceSettingsItems.setItemName(getString(R.string.text_software_update));
        if (connectionStatus && REApplication.getInstance().ismIsDeviceConnected()) {
            //Update Tripper Device in Cloud database
            if (deviceInfoArrayList != null && deviceInfoArrayList.size() > 0 && tripperIndex != null) {
                UpdateTripperDeviceApiRequest updateTripperDeviceApiRequest = new UpdateTripperDeviceApiRequest(deviceInfoArrayList.get(tripperIndex).getSerialNumber(), deviceInfoArrayList.get(tripperIndex).getOSVersion(), deviceInfoArrayList.get(tripperIndex).getStatus(), "Active");
                OtapInteractor mzOtapInteractor = new OtapInteractor();
                mzOtapInteractor.updateTripperDeviceInCloud(updateTripperDeviceApiRequest);
            }
            if (isSoftwareUpdateAvailable())
                aDeviceSettingsItems.setSoftwareUpdate("1");
            else
                aDeviceSettingsItems.setSoftwareUpdate("");
        } else {
            aDeviceSettingsItems.setSoftwareUpdate("");
        }
        mDeviceSettingsItemsArrayList.add(aDeviceSettingsItems);
        aDeviceSettingsItems = new DeviceSettingsItems();
        aDeviceSettingsItems.setItemId(2);
        aDeviceSettingsItems.setItemName(getString(R.string.text_gdpr));
        mDeviceSettingsItemsArrayList.add(aDeviceSettingsItems);
        aDeviceSettingsItems = new DeviceSettingsItems();
        aDeviceSettingsItems.setItemId(3);
        aDeviceSettingsItems.setItemName(getString(R.string.text_user_manual));
        mDeviceSettingsItemsArrayList.add(aDeviceSettingsItems);
        DeviceSettingsAdapter mAdapter = new DeviceSettingsAdapter(getApplicationContext(),
                mDeviceSettingsItemsArrayList, this);
        mContentsRecyclerView.setAdapter(mAdapter);
        hideLoading();
    }

    private void softwareUpdateItemClick() {
        isOtapInstallationInProgress = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setTurnScreenOn(true);
        } else {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        if (REApplication.getInstance().isNavigationInProgress())
            REUtils.showErrorDialog(this, getString(R.string.text_otap_nav_progress_error));
        else if (connectionStatus && REApplication.getInstance().ismIsDeviceConnected()) {
            if (!isBaseVersionGreater()) {
                showSoftwareUpdate();
            } else {
//                        showAlertDialog(getString(R.string.text_alert_device_os));
                REUtils.showErrorDialog(this, getString(R.string.text_alert_device_os));
            }
        } else
            REUtils.showErrorDialog(this, getString(R.string.text_alert_disconnected));
    }

    @Override
    public void onItemClick(int itemId, String item) {
        switch (itemId) {
            case 1:
                softwareUpdateItemClick();
                break;
            default:
                break;
        }
    }


    private void showSoftwareUpdate() {
        if (mDownloadFirmwareFile != null && mDownloadFirmwareFile.isDownloading()) {
            showOTAPUpdateFragment(true);
        } else {
            checkFileAndShowUpdate();
        }
    }

    private void checkFileAndShowUpdate() {
        /*if (isFileAlreadyDownloaded())
            showOTAPUpdateFragment(false);
        else*/
        showOTAPUpdateFragment(true);
    }

    private void showOTAPUpdateFragment(boolean isDownloading) {
        otapUpdateFragment = OTAPUpdateFragment.newInstance(Objects.requireNonNull(mOtapMapData.get("description")).toString(), isDownloading);
        otapUpdateFragment.setListener(this);
        otapUpdateFragment.show(getSupportFragmentManager(), "");
    }


    public void showLoading() {
        hideLoading();
        mProgressLoading = REDialogUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressLoading != null && mProgressLoading.isShowing()) {
            mProgressLoading.cancel();
            mProgressLoading = null;
            REDialogUtils.removeHandlerCallbacks();
        }
    }

    private void resetListItems() {
        //Toast.makeText(getApplicationContext(), "resetListItems", Toast.LENGTH_SHORT).show();
        reLogger.appendLog("resetListItems");
        addListItems();
    }

    /**
     * Compare the versions from the device with firestore package...
     *
     * @param aFireStoreVer
     * @param aDeviceVer
     * @return
     */
    private boolean compareVersions(String aFireStoreVer, String aDeviceVer) {
        try {
            double aFireStoreVal = Double.parseDouble(aFireStoreVer);
            double aDevVal = Double.parseDouble(aDeviceVer);
            return Double.compare(aFireStoreVal, aDevVal) > 0;
        }catch (Exception e){
            return false;
        }
    }

    private boolean compareBaseVersions(String aFireStoreBaseVer, String aDeviceVer) {
        double aFireStoreBaseVal = Double.parseDouble(aFireStoreBaseVer);
        double aDevVal = Double.parseDouble(aDeviceVer);
        return Double.compare(aFireStoreBaseVal, aDevVal) > 0;
    }

    /**
     * Check whether the OTAP Package and info files are already downloaded.
     *
     * @return
     */
    private boolean isFileAlreadyDownloaded() {
        try {
            String mOTAPFileName, mInfoFileName;
            if (mOtapMapData.get("firmwareUrl") != null && !Objects.requireNonNull(mOtapMapData.get("firmwareUrl")).toString().isEmpty()) {
                URL url = new URL(Objects.requireNonNull(mOtapMapData.get("firmwareUrl")).toString());
                String[] srecFileName = Uri.fromFile(new File(Objects.requireNonNull(mOtapMapData.get("firmwareUrl")).toString())).getLastPathSegment().split("/");
                mOTAPFileName = srecFileName[srecFileName.length - 1];
                myStoredOTAPFile = new File(Objects.requireNonNull(getExternalFilesDir(null)).getPath(), mOTAPFileName);//Create Output file in Main File
            }
            if (mOtapMapData.get("firmwareMappingUrl") != null && !Objects.requireNonNull(mOtapMapData.get("firmwareMappingUrl")).toString().isEmpty()) {
                URL url = new URL(Objects.requireNonNull(mOtapMapData.get("firmwareMappingUrl")).toString());
                String[] aInfoFileName = Uri.fromFile(new File(Objects.requireNonNull(mOtapMapData.get("firmwareMappingUrl")).toString())).getLastPathSegment().split("/");
                mInfoFileName = aInfoFileName[aInfoFileName.length - 1];
                myStoredInfoFile = new File(Objects.requireNonNull(getExternalFilesDir(null)).getPath(), mInfoFileName);
            }
            if (Objects.requireNonNull(mOtapMapData.get("updateType")).toString().equalsIgnoreCase("full")) {
                return myStoredOTAPFile.exists();
            } else {
                return myStoredOTAPFile.exists() && myStoredInfoFile.exists();
            }
        } catch (MalformedURLException e) {
            RELog.e(e);
        }
        return false;
    }

    private void showRetryAlert() {
       /* if (isOtapInstallationInProgress){
            return;
        }*/
        isOtapInstallationInProgress = false;
        dismissWaitDialog();
        dismissOTAPProgressDialog();
        if (mAlertRetryDialog != null) {
            mAlertRetryDialog.dismiss();
        }
        try {
            TextView alert_message, alert_Retry, alert_Cancel;
            LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_alert_otap_retry, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert retry dialog
            alert_message.setText(getResources().getString(R.string.text_otap_retry));
            alert_Retry = alertLayout.findViewById(R.id.textView_alert_retrybutton);
            alert_Cancel = alertLayout.findViewById(R.id.textView_alert_cancel_button);
            AlertDialog.Builder alert = new AlertDialog.Builder(DeviceSettingsActivity.this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            mAlertRetryDialog = alert.create();
            mAlertRetryDialog.show();
            //This is for ok click which dismisses the dialog
            alert_Retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertRetryDialog.dismiss();
                    //add retry logic here
                    startRetryInstallation();
                }
            });
            alert_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOtapDialog = null;
                    mWaitDialog = null;
                    mAlertRetryDialog.dismiss();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void startRetryInstallation() {
        blockSegmentUtils = new BlockSegmentUtils(this);
        showWaitDialog();
        mOtapDialog = null;
        mWaitDialog = null;
        softwareUpdateItemClick();
    }

    private void showOTAPProgressDialog() {
        try {
            if (mOtapDialog != null)
                return;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View alertLayout = layoutInflater.inflate(R.layout.dialog_otap_progress_alert, null);
            mAlertMessageTXT = alertLayout.findViewById(R.id.textView_alert_message);
            mPercentageTXT = alertLayout.findViewById(R.id.textView_alert_percentage);
            mProgressBar = alertLayout.findViewById(R.id.textView_alert_progress);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(alertLayout);
            alert.setCancelable(false);
            mOtapDialog = alert.create();
            mProgressBar.setIndeterminate(false);
            mProgressBar.setProgress(0);//initially progress is 0
            mProgressBar.setMax(100);//sets the maximum value 100
            mOtapDialog.show();
            mOtapDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public void showWaitDialog() {
        try {
            if (mWaitDialog != null)
                return;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View alertLayout = layoutInflater.inflate(R.layout.dialog_otap_progress_wait_alert, null);
            mAlertMessageTXT = alertLayout.findViewById(R.id.textView_alert_message);
            mAlertMessageTXT.setText(getString(R.string.text_alert_please_wait));
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(alertLayout);
            alert.setCancelable(false);
            mWaitDialog = alert.create();
            mWaitDialog.show();
            mWaitDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public void showAlertDialog(String msg) {
        try {
            if (mAlertDialog != null)
                return;
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            mAlertMessageTXT = alertLayout.findViewById(R.id.textView_alert_message);
            mAlertOkTXT = alertLayout.findViewById(R.id.textView_alert_okbutton);
            mAlertMessageTXT.setText(msg);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setView(alertLayout);
            alert.setCancelable(true);
            mAlertDialog = alert.create();
            mAlertDialog.show();
            mAlertDialog.setCanceledOnTouchOutside(false);
            mAlertOkTXT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                    finish();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void dismissOTAPProgressDialog() {
        if (mOtapDialog != null)
            mOtapDialog.dismiss();
        //stopService();
    }

    public void dismissWaitDialog() {
        if (mWaitDialog != null)
            mWaitDialog.dismiss();
    }


    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Otap Software install in Progress:");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public void updateBrickStatus(String value, String otapStatus) {
        if (!deviceInfoArrayList.isEmpty() && tripperIndex != null) {

            deviceInfoArrayList.get(tripperIndex).setStatus(value);
            deviceInfoArrayList.get(tripperIndex).setOSVersion(getCurrentVersion());
            //runOnUiThread(() -> Toast.makeText(getApplicationContext(),"Brick value updated to   "+deviceInfoArrayList.get(index).getStatus(),Toast.LENGTH_LONG).show());
            //After changing the brick status, save the changes to Preference also
            BLEDeviceManager.updateMyTBTDevice(deviceInfoArrayList, this);
            //Update Tripper Device in Cloud database
            if (deviceInfoArrayList != null && deviceInfoArrayList.size() > 0 && tripperIndex != null) {
                UpdateTripperDeviceApiRequest updateTripperDeviceApiRequest = new UpdateTripperDeviceApiRequest(deviceInfoArrayList.get(tripperIndex).getSerialNumber(), deviceInfoArrayList.get(tripperIndex).getOSVersion(), deviceInfoArrayList.get(tripperIndex).getStatus(), "Active", otapStatus);
                OtapInteractor mzOtapInteractor = new OtapInteractor();
                mzOtapInteractor.updateTripperDeviceInCloud(updateTripperDeviceApiRequest);
            }
        }
    }

    @Override
    public void onInstallationStarted() {
        isOtapInstallationInProgress = true;
    }

    public class ForegroundService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            String input = intent.getStringExtra("inputExtra");
            createNotificationChannel();

            startForeground(1, getMyCustomNotification(input));
            //do heavy work on a background thread
            //stopSelf();
            return START_NOT_STICKY;
        }

        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "Foreground Service Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(serviceChannel);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public Notification getMyCustomNotification(String input) {
        Intent notificationIntent = new Intent(this, DeviceSettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("OTAP Installation")
                .setContentText(input)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .build();
    }

    /**
     * This is the method that can be called to update the Notification
     */
    public void updateNotification(String text) {
        Notification notification = getMyCustomNotification(text);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);
    }
}

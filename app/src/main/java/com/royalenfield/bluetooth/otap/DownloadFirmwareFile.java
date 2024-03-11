package com.royalenfield.bluetooth.otap;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.royalenfield.bluetooth.otap.listener.OtapInstallationStatusListener;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.REAlertActivity;
import com.royalenfield.reprime.utils.REUtils;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.royalenfield.bluetooth.otap.OtapProcessDisplayResponse.otapMessageQueue;

public class DownloadFirmwareFile {
    private static final String TAG = "Download Task";
    private Context context;
    private Dialog dialog;
    private ProgressBar progress_bar;
    private TextView textView_description;
    private TextView text_percentage;
    private TextView text_current_version;
    private Map<String, Object> otapData;
    //TODO PartialOTAP
    public static String otapSrecFilename = "", myInfoFileName = "", myUpdateType = "";
    private File myStoredOTAPFile, myStoredInfoFile;
    private DownloadManager systemDownloadManager;
    private long myOtapEnqueue, myInfoEnqueue;
    public static int totalBlocks;
    BlockSegmentUtils blockSegmentUtils;
    public boolean downloading = false;
    private String currentFileName = "";
    private DownloadFileListener mDownloadFileListener;
    private AlertDialog mWaitDialog = null;
    private ProgressBar mProgressBar;
    private TextView mAlertMessageTXT, mPercentageTXT;
    OtapInstallationStatusListener otapInstallationStatusListener;
    private boolean connectionStatus;
    private SharedPreferences sharedpreferences;
    //TODO PartialOTAP
    public DownloadFirmwareFile(Context context, Map<String, Object> otapData, OtapInstallationStatusListener otapInstallationStatusListener, boolean isTripperConnected) {
        this.context = context;
        this.otapData = otapData;
        this.otapInstallationStatusListener = otapInstallationStatusListener;
        this.connectionStatus = isTripperConnected;
        systemDownloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        blockSegmentUtils = new BlockSegmentUtils(context);
        try {
            //RE Display
            if (otapData.get("firmwareUrl") != null && !Objects.requireNonNull(otapData.get("firmwareUrl")).toString().isEmpty()) {
                URL url = new URL(Objects.requireNonNull(otapData.get("firmwareUrl")).toString());
                String[] srecFileName = Uri.fromFile(new File(Objects.requireNonNull(otapData.get("firmwareUrl")).toString())).getLastPathSegment().split("/");
                otapSrecFilename = srecFileName[srecFileName.length - 1];
            }

            if (otapData.get("updateType") != null && !Objects.requireNonNull(otapData.get("updateType")).toString().isEmpty()) {
                myUpdateType = Objects.requireNonNull(otapData.get("updateType")).toString();
            }

            if (!otapSrecFilename.isEmpty()) {
                myStoredOTAPFile = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getPath(), otapSrecFilename);//Create Output file in Main File
                if (myUpdateType.equalsIgnoreCase("full")) {
                    if (!isDownloading(context, myOtapEnqueue)) {
                        if (otapData.get("firmwareUrl") != null && !Objects.requireNonNull(otapData.get("firmwareUrl")).toString().isEmpty()) {
                            //Downloading the RE_OTAP_ver_partial.srec file
                            startDownload(Objects.requireNonNull(otapData.get("firmwareUrl")).toString(), otapSrecFilename);
                        }
                    }
                } else {
                    downloadFirmwareInfoFile();
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            downloadFirmwareFileForPartialOtap();
                        }
                    }, 3500);
                }
                if (isDownloading(context, myOtapEnqueue)) {
                    showDialog(context);
                }
            }

            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(myOtapEnqueue);

            Cursor cursor = systemDownloadManager.query(q);
            if (cursor != null && cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) != DownloadManager.STATUS_FAILED) {
                    RELog.d(TAG,"DownloadFirmwareFile: STATUS_RUNNING");
                    updateProgressBar(myOtapEnqueue, otapSrecFilename);
                } else {
                    RELog.d(TAG,"DownloadFirmwareFile: Download is nowhere in sight");
                    //Download failed , retry the download
                    Log.e("OTAPFILES","inside Full otap");
                    handleDownloadFailure();
                }
                cursor.close();
            }
        } catch (Exception e) {
            //Read exception if something went wrong
            RELog.e(e);
            myStoredOTAPFile = null;
            RELog.e(TAG,"Download Error Exception "+ e.getMessage());
            Toast.makeText(REApplication.getAppContext(), "ERRORRR    " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    //TODO PartialOTAP

    /**
     * download the firmware support address file.
     */
    public void downloadFirmwareInfoFile() {
        try {
            if (otapData.get("firmwareMappingUrl") != null && !Objects.requireNonNull(otapData.get("firmwareMappingUrl")).toString().isEmpty()) {
                URL url = new URL(Objects.requireNonNull(otapData.get("firmwareMappingUrl")).toString());
                String[] aInfoFileName = Uri.fromFile(new File(Objects.requireNonNull(otapData.get("firmwareMappingUrl")).toString())).getLastPathSegment().split("/");
                myInfoFileName = aInfoFileName[aInfoFileName.length - 1];
            }
            if (!myInfoFileName.isEmpty()) {
                myStoredInfoFile = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getPath(), myInfoFileName);
                if (!isDownloading(context, myInfoEnqueue)) {
                    if (otapData.get("firmwareMappingUrl") != null && !Objects.requireNonNull(otapData.get("firmwareMappingUrl")).toString().isEmpty()) {
                        //Downloading the srec_info.txt file
                        startDownload(Objects.requireNonNull(otapData.get("firmwareMappingUrl")).toString(), myInfoFileName);
                        Log.e("OTAPFILES","1");
                    }
                }
                 if (isDownloading(context, myInfoEnqueue)) {
                    showDialog(context);
                }
            }
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(myInfoEnqueue);
            Cursor cursor = systemDownloadManager.query(q);
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) != DownloadManager.STATUS_FAILED) {
                    RELog.d(TAG,"DownloadFirmwareInfoFile: STATUS_RUNNING");
                    updateProgressBar(myInfoEnqueue, myInfoFileName);
                } else {
                    RELog.d(TAG,"DownloadFirmwareInfoFile: Download is nowhere in sight");
                    //if the download fails ,retry the download
                    Log.e("OTAPFILES","inside Partial otap info file down");
                    handleDownloadFailure();
                }
                cursor.close();
            }
        } catch (Exception e) {
            //Read exception if something went wrong
            RELog.e(e);
            myStoredInfoFile = null;
            RELog.e(TAG,"Download Error Exception "+ e.getMessage());
        }
    }

    public void downloadFirmwareFileForPartialOtap() {
        Log.e("OTAPFILES","3");
        try {
            //RE Display
            if (otapData.get("firmwareUrl") != null && !Objects.requireNonNull(otapData.get("firmwareUrl")).toString().isEmpty()) {
                URL url = new URL(Objects.requireNonNull(otapData.get("firmwareUrl")).toString());
                String[] srecFileName = Uri.fromFile(new File(Objects.requireNonNull(otapData.get("firmwareUrl")).toString())).getLastPathSegment().split("/");
                otapSrecFilename = srecFileName[srecFileName.length - 1];
                Log.e("OTAPFILES","4");
            }

            if (otapData.get("updateType") != null && !Objects.requireNonNull(otapData.get("updateType")).toString().isEmpty()) {
                myUpdateType = Objects.requireNonNull(otapData.get("updateType")).toString();
            }

            if (!otapSrecFilename.isEmpty()) {
                Log.e("OTAPFILES","5");
                myStoredOTAPFile = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getPath(), otapSrecFilename);//Create Output file in Main File
                if (!isDownloading(context, myOtapEnqueue)) {
                    if (otapData.get("firmwareUrl") != null && !Objects.requireNonNull(otapData.get("firmwareUrl")).toString().isEmpty()) {
                        //Downloading the RE_OTAP_ver_partial.srec file
                        startDownload(Objects.requireNonNull(otapData.get("firmwareUrl")).toString(), otapSrecFilename);
                        Log.e("OTAPFILES","6");
                    }
                }
            }

            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(myOtapEnqueue);

            Cursor cursor = systemDownloadManager.query(q);
            if (cursor != null && cursor.moveToFirst()) {
                //cursor.moveToFirst();
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) != DownloadManager.STATUS_FAILED) {
                    RELog.d(TAG,"DownloadFirmwareFile: STATUS_RUNNING");
                    updateProgressBar(myOtapEnqueue, otapSrecFilename);
                } else {
                    RELog.d(TAG,"DownloadFirmwareFile: Download is nowhere in sight");
                    //Download failed , retry the download for partial otap firwarefile
                    Log.e("OTAPFILES","inside Partial otap srec file download"+ cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
                    handleDownloadFailure();
                }
                cursor.close();
            }
        } catch (Exception e) {
            //Read exception if something went wrong
            RELog.e(e);
            myStoredOTAPFile = null;
            RELog.e(TAG,"Download Error Exception "+ e.getMessage());
            Toast.makeText(REApplication.getAppContext(), "ERRORRR    " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateProgressBar(final long aEnqueueId, final String aFileNamenew) {
        Log.e("OTAPFILES1","filenm "+aFileNamenew);
        new Thread(() -> {
            downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(aEnqueueId);

                Cursor cursor = systemDownloadManager.query(q);
                if (cursor != null && cursor.moveToFirst()) {
                    //cursor.moveToFirst();
                    final int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    final int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                        if (bytesTotal != 0) {
                            if (isDownloadComplete(context, myOtapEnqueue)) {
                               //full otap and partial otap starts installation from here
                                startInstallation();
                            } else if(isDownloadComplete(context, myInfoEnqueue)){
                              //  downloadFirmwareFileForPartialOtap();
                            }
                        }
                    }

                    if (bytesTotal != 0) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);
                            text_percentage.setText(progress + "%");
                            progress_bar.setProgress(progress);
                        });
                    }
                    RELog.d(TAG,statusMessage(cursor, aFileNamenew));
                    cursor.close();
                }
            }
        }).start();
    }

    private boolean isDownloading(Context context, long downloadId) {
        return getStatus(context, downloadId) == DownloadManager.STATUS_RUNNING;
    }

    private boolean isDownloadComplete(Context context, long downloadId) {
        return getStatus(context, downloadId) == DownloadManager.STATUS_SUCCESSFUL;
    }

    private int getStatus(Context context, long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);// filter your download by download Id
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            //Log.i("handleData()", "Reason: " + c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));
            c.close();
            RELog.i("DOWNLOAD_STATUS",String.valueOf(status));
            return status;
        }
        RELog.i("AUTOMATION_DOWNLOAD","DEFAULT");
        return -1;
    }

    private void startDownload(String aFileURL, String aFileName) {
        Log.e("OTAPFILES","fileurl "+aFileURL+" filename "+aFileName);
        //before starting download check if the app is already having the file, then remove the file
        removeFile(aFileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(aFileURL));
        request.setDestinationInExternalFilesDir(context, "/", aFileName);
        if (aFileName.equals(otapSrecFilename)) {
            myOtapEnqueue = systemDownloadManager.enqueue(request);
        } else
            myInfoEnqueue = systemDownloadManager.enqueue(request);
        if (dialog == null) {
            showDialog(context);
        }
    }


    private String status = "";

    private String statusMessage(Cursor cursor, String aFileName) {
        File dir = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getPath(), aFileName);//Create Output file in Main File
        currentFileName = aFileName;
        Log.e("OTAPFILES","STATUSMESSAGE: "+"curentFileName "+currentFileName+ " status: "+cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
        switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                status = "Download failed!";
                Log.e("OTAPFILES","case DownloadManager.STATUS_FAILED:"+currentFileName);
                handleDownloadFailure();
                break;

            case DownloadManager.STATUS_PAUSED:
                status = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                status = "Download pending!";
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                RELog.d(TAG,"statusMessage: Already Exist");
                break;

            case DownloadManager.STATUS_RUNNING:
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                //status = "Download complete!";
                if (aFileName.equals(otapSrecFilename)) {
                    if (myUpdateType.equalsIgnoreCase("full")) {
                        status = "Download complete!";
                    }
                    else {
                        status = "Start Download Info File!";
                    }
                } else if (aFileName.equals(myInfoFileName)) {
                    Log.e("OTAPFILES","2");
                    status = "Download complete!";
                    //download srec file for partial otap
                   // downloadFirmwareFileForPartialOtap();
                }
                dialog.cancel();
                break;

            default:
                status = "Download is nowhere in sight";
                break;
        }

        return (status);
    }

    private void handleDownloadFailure() {
        try {
            RELog.e(TAG,"OTAP Download failed!");
            Log.e("OTAPFILES","info file: "+myInfoFileName+ " SrecFile: "+otapSrecFilename);
            if (!isDownloading(context, myOtapEnqueue) && systemDownloadManager != null) {
                systemDownloadManager.remove(myOtapEnqueue, myInfoEnqueue);
                downloading = false;
                if (dialog != null && dialog.isShowing())
                    dialog.cancel();
                //Start file download from beginning
                new Handler(Looper.getMainLooper()).post(() -> {
                    removeFile(currentFileName);
                    if (mDownloadFileListener != null)
                        mDownloadFileListener.startDownloading();
                });
            }
        } catch (Exception e) {
            RELog.e(TAG,e.getMessage());
        }
    }

    public void showDialog(final Context activity) {
        this.context = activity;
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
        dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_downloading_progress);

        TextView ble_tv_actionbar_title = dialog.findViewById(R.id.ble_tv_actionbar_title);
        ble_tv_actionbar_title.setText(R.string.software);
        ImageView imgClose = dialog.findViewById(R.id.iv_nav_ble);
        imgClose.setOnClickListener(v -> showConfirmationAlert());

        progress_bar = dialog.findViewById(R.id.progress_downloading);
        text_percentage = dialog.findViewById(R.id.text_percentage);
        text_current_version = dialog.findViewById(R.id.text_current_version);
        textView_description = dialog.findViewById(R.id.textView_description);
        if (otapData.get("firmwareVersion") != null && !Objects.requireNonNull(otapData.get("firmwareVersion")).toString().isEmpty()) {
            text_current_version.setText("OS " + otapData.get("firmwareVersion").toString());
        }
        if (otapData.get("description") != null && !Objects.requireNonNull(otapData.get("description")).toString().isEmpty()) {
            textView_description.setText(Objects.requireNonNull(otapData.get("description")).toString() + "\n\n" + context.getString(R.string.text_otap_internet_msg));
        }
        progress_bar.setIndeterminate(false);
        progress_bar.setMax(100);
        dialog.show();

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            /**
             * Called when a key is dispatched to a dialog. This allows listeners to
             * get a chance to respond before the dialog.
             *
             * @param dialog  the dialog the key has been dispatched to
             * @param keyCode the code for the physical key that was pressed
             * @param event   the KeyEvent object containing full information about
             *                the event
             * @return {@code true} if the listener has consumed the event,
             * {@code false} otherwise
             */
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    showConfirmationAlert();
                }
                return true;
            }
        });
    }

    private void startInstallation() {
        otapInstallationStatusListener.onInstallationStarted();
        if (connectionStatus) {
            RELog.e(TAG,"OTAP Install started!");
            // Change the TBT Device brick logic status here, ie change status = 1
            //otapInstallationStatusListener.updateBrickStatus("Y");
            sharedpreferences = context.getSharedPreferences("RE_APP", Context.MODE_PRIVATE);
            String brickStatus  = "N";
            if (!getCurrentVersion().isEmpty()) {
                if (compareVersions("12.10", getCurrentVersion())){
                    brickStatus = "Y";
                }
            }
            otapInstallationStatusListener.updateBrickStatus(brickStatus,"Failed");
            otapMessageQueue.clear();
//        totalBlocks = 0;
            blockSegmentUtils.sendReflashImageNotification();
        } else {
            REUtils.showErrorDialog(context, context.getString(R.string.text_otap_nav_progress_error));
        }
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void showConfirmationAlert() {
        Intent intent = REAlertActivity.getIntent(context, context.getResources().getString(R.string.title_alert_cancel_downloading),
                new REAlertActivity.OnItemClickListener() {
                    @Override
                    public void onPositiveButton() {
                        try {
                            downloading = false;
                            ((Activity) context).runOnUiThread(() -> {
                                dialog.cancel();
                                systemDownloadManager.remove(myOtapEnqueue, myInfoEnqueue);
                                removeFile(currentFileName);
                            });
                        } catch (Exception e) {
                            RELog.e(e);
                        }
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        }
    }

    public void removeFile(String aFileName) {
        try {
            File dir = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getPath(), aFileName);
            dir.delete();
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public void setDownloadFileListener(DownloadFileListener listener) {
        mDownloadFileListener = listener;
    }

    public interface DownloadFileListener {
        void startDownloading();
    }
    private String getCurrentVersion() {
        String value = "";
        if (sharedpreferences!=null) {
            value = sharedpreferences.getString("currentSoftwareVersion", "");
        }
        return value;
    }
    private boolean compareVersions(String aFireStoreVer, String aDeviceVer) {
        double aFireStoreVal = Double.parseDouble(aFireStoreVer);
        double aDevVal = Double.parseDouble(aDeviceVer);
        return Double.compare(aFireStoreVal, aDevVal) > 0 ;
    }
}

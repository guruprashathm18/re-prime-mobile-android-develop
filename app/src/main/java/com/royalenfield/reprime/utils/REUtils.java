package com.royalenfield.reprime.utils;

import static com.royalenfield.reprime.application.REApplication.getAppContext;
import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;
import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.generateLogs;
import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.getEnvironment;
import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.getNetworkSpeed;
import static com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback.uploadLogs;
import static com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils.PC_FIREBASE_COUNTRY_KEY;
import static com.royalenfield.reprime.utils.REConstants.COUNTRY_URL_KEY;
import static com.royalenfield.reprime.utils.REConstants.JWT_TOKEN_KEY;
import static com.royalenfield.reprime.utils.REConstants.KEY_SETTINGS_GTM;
import static com.royalenfield.reprime.utils.REConstants.MOBILE_NO_KEY;
import static com.royalenfield.reprime.utils.REConstants.REFRESH_TOKEN_KEY;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_CHECK_CAMERA;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_CHECK_CAMERA_PROFILE;
import static com.royalenfield.reprime.utils.REConstants.REQUEST_CHECK_GALLERY;
import static com.royalenfield.reprime.utils.REConstants.USERDATA;
import static com.royalenfield.reprime.utils.REConstants.USERID_KEY;
import static com.royalenfield.reprime.utils.REFirestoreConstants.CONFIGURATION_REALTIME_DB;
import static com.royalenfield.reprime.utils.REFirestoreConstants.REALTIME_DB;
import static com.royalenfield.reprime.utils.REFirestoreConstants.VARIANT_REALTIME_DB;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.auth0.android.jwt.JWT;
import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.Duration;
import com.bosch.softtec.components.core.models.LatLng;
import com.bosch.softtec.components.core.models.NetworkTimeoutConfiguration;
import com.bosch.softtec.components.midgard.core.directions.models.Polyline;
import com.bosch.softtec.components.midgard.core.directions.models.RouteTrace;
import com.bosch.softtec.components.midgard.core.search.models.SearchError;
import com.bosch.softtec.components.nephele.AuthorizationConfiguration;
import com.bosch.softtec.components.nephele.CloudException;
import com.bosch.softtec.components.nephele.CloudManager;
import com.bosch.softtec.components.nephele.JsonWebToken;
import com.bosch.softtec.components.nephele.OAuthConfiguration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.royalenfield.configuration.CloudConfigurationProvider;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleDataMappingCallback;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.request.logs.Body;
import com.royalenfield.reprime.models.request.logs.ConnectedLogs;
import com.royalenfield.reprime.models.request.logs.ConnectedRequest;
import com.royalenfield.reprime.models.request.logs.Message;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.remoteconfig.AcmConfigurationKeys;
import com.royalenfield.reprime.models.response.remoteconfig.AppSettings;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.models.response.remoteconfig.ConnectedKeys;
import com.royalenfield.reprime.models.response.remoteconfig.DiyVideos;
import com.royalenfield.reprime.models.response.remoteconfig.Feature;
import com.royalenfield.reprime.models.response.remoteconfig.FeatureList;
import com.royalenfield.reprime.models.response.remoteconfig.FirebaseURLs;
import com.royalenfield.reprime.models.response.remoteconfig.GlobalValidations;
import com.royalenfield.reprime.models.response.remoteconfig.LoggerBaseUrls;
import com.royalenfield.reprime.models.response.remoteconfig.MobileappbaseURLs;
import com.royalenfield.reprime.models.response.remoteconfig.NavigationKeys;
import com.royalenfield.reprime.models.response.remoteconfig.REGoogleKeys;
import com.royalenfield.reprime.models.response.remoteconfig.REKeys;
import com.royalenfield.reprime.models.response.remoteconfig.RidesKeys;
import com.royalenfield.reprime.models.response.web.login.LoginResponse;
import com.royalenfield.reprime.models.response.web.verifytoken.VerifyTokenResponse;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.home.homescreen.listener.OnFirestoreResponseListener;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.home.service.specificissue.model.IssuesModel;
import com.royalenfield.reprime.ui.onboarding.editprofile.interactor.EditProfileInteractor;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.splash.presenter.FirebaseAuthListner;
import com.royalenfield.reprime.ui.triplisting.view.TripListingFragment;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryListModel;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.CountryModel;
import com.royalenfield.workmanager.ProvisioningWorker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.royalenfield.reprime.utils.REConstants.REQUEST_CHECK_CAMERA_PROFILE;
//import com.bosch.softtec.components.midgard.core.common.models.LatLng;
//import com.bosch.softtec.components.midgard.core.directions.models.Polyline;
//import com.bosch.softtec.components.midgard.core.directions.models.RouteTrace;

/**
 * @author BOP1KOR on 11/19/2018.
 */

public class REUtils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_ENGINE_NUMBER_REGEX =
            Pattern.compile("[^A-Za-z0-9]");

    public static final int REQUEST_MULTIPLE_PERMISSIONS_ID = 1;
    private static String DEVICE_TOKEN_REF = "DeviceTokenPref";
    public static final String USER_LOGGED_IN = "UserLoggedIn";
    public static final String NAVIGATION_CALLING_ACTIVITY = "navigationActivity";
    private static final String TAG = REUtils.class.getSimpleName();
    public static NewsResponse mNewsResponse;
    public static EventsResponse mEventsResponse;
    public static final String FULL_DATE_FORMAT = "dd\'th\' MMM yyyy, hh:mm a";
    private static SharedPreferences mNotifPreference = REApplication.getAppContext().
            getSharedPreferences(REConstants.DEVICE_TOKEN_PREF, 0); // 0 - for private mode
    private static SharedPreferences.Editor mNotifeditor = mNotifPreference.edit();
    public static String modelName = null;

    public static boolean CHECK_VEHICLE_PENDING = false;
	public static boolean CHECK_VEHICLE_SYNCING_FAILED =false;
	public static boolean CHECK_VEHICLE_SYNCING_INPROGRESS =true;
	public static boolean CHECK_FIREBASE_AUTH_INPROGRESS =true;
    public static boolean UPDATE_USER_PERSONAL_DETAIL = true;
    public static boolean refreshVehicle = false;
    public static CountDownTimer countDownTimer;

    public static String UPDATED_MOBILE;
    public static String UPDATED_COUNTRY_CODE = null;
    public static String UPDATED_FIRST_NAME;
    public static String UPDATED_LAST_NAME;
    public static String UPDATED_EMAIL;
    public static String tbtAuthHeaders = null;
    static Random r = new Random();
    public static String imageFilePath;
    public static String IV = "ausXIGkv32uFtASSlFuvwA==";

    /**
     * Checks if the given string is a valid email id or not.
     *
     * @param emailStr given email id as input
     * @return returns true if email id is valid else false.
     */
    public static boolean isValidEmailId(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /**
     * Checks if the given string is valid mobile number or not.
     *
     * @param mobileNumber mobile number as input.
     * @return true if valid else false.
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        int min = REUtils.getGlobalValidations().getMinPhoneNum();
        int max = REUtils.getGlobalValidations().getPhoneNumber();
        if (REApplication.getInstance().validMobile != null) {
            min = REApplication.getInstance().validMobile.getMin();
            max = REApplication.getInstance().validMobile.getMax();
        }
        return (mobileNumber.length() >= min && mobileNumber.length() <= max);
    }

    /**
     * Checks is the string contains only number or character
     *
     * @param mobileNumber string.
     * @return true if contains only number else false.
     */
    public static boolean isMobileNumber(String mobileNumber) {
        return mobileNumber.matches("[0-9]+");
    }
//    public static void setDataToUserInfo(LoginResponse response) {
//        if (response != null && response.getData() != null) {
//            // Storing the data to preference
//            try {
//
//                REPreference.getInstance().putString(REApplication.getAppContext(),USERDATA,new Gson().toJson(response));
//            } catch (PreferenceException e) {
//                Timber.e(e);
//            }
//        }
//    }

    //TODO Later this dialog needs to implements as per MVP pattern......temporary
    public static void showErrorDialog(final Context context, String message) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public static void showErrorPerMission(final Context context, String message) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    startAppInfoScreen(context);
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    /**
     * This dialog is used to add custom title & message
     *
     * @param context
     * @param title
     * @param message
     */
    public static void showErrorDialogCustomTitle(final Context context, String title, String message) {
        try {
            TextView alert_message, alert_ok, alert_title;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_title = alertLayout.findViewById(R.id.textView_alert_title);
            alert_title.setText(title);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    /**
     * Provides app version code.
     *
     * @param context the context.
     * @return version name.
     */
    public static String getVersionName(Context context) {
        String version = "0.0";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (BuildConfig.FLAVOR.equals("dev")) {
                version = "(DEV) " + pInfo.versionName;
            } else if (BuildConfig.FLAVOR.equals("uat")) {
                version = "(UAT) " + pInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            RELog.e(e);
        }
        return version;
    }

    /**
     * Retrieves the address list form the lat long
     * GeoCoding.
     *
     * @param context   the context.
     * @param latitude  latitude.
     * @param longitude longitude.
     */
    public static String getCurrentAddressFromLocation(Context context, double latitude, double longitude) {
        String currentAddress = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = "";
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            }
            currentAddress = address;
        } catch (IOException e) {
            RELog.e(e);
        }
        return currentAddress;
    }

    /**
     * Returns current location address list......
     *
     * @param context   the context.
     * @param latitude  latitude
     * @param longitude longitude
     * @return List of the address
     */
    public static List<Address> getCurrentAddressList(Context context, double latitude, double longitude) {
        List<Address> addressList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            RELog.e(e);
        }
        if (addressList.size() > 0) {
            printAddress(addressList.get(0));
        } else {
            Log.e("printAddress: ", "no address found empty array");
        }
        return addressList;
    }

    public static void printAddress(Address address) {
        Log.e("printAddress: ", address.toString());
    }


    /**
     * Checks the permission and request for permissions
     * Need to call from activity and should handle inside activity with onRequestPermissionsResult callback
     *
     * @param activity THe activity
     * @return true of permissions are granted
     */
    public static boolean checkAndRequestPermissions(Activity activity) {
        int coarseLocation = ContextCompat.checkSelfPermission(REApplication.getAppContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocation = ContextCompat.checkSelfPermission(REApplication.getAppContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
//        int call_phone = ContextCompat.checkSelfPermission(REApplication.getAppContext(), Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (fineLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
      /*  if (call_phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }*/
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_MULTIPLE_PERMISSIONS_ID);
            return false;
        }
        return true;
    }

    /**
     * To Check is device has Google play services enabled or not.
     *
     * @param context the activity context.
     * @return true if available else false.
     */
    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(context, "Please Install google play services to use this feature", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * Method converts string array into comma separated values
     *
     * @param strArray : raw string to be converted
     * @return string with comma separated values
     */
    public static String convertArrayToString(String[] strArray) {
        StringBuilder builder = new StringBuilder();
        if (strArray != null) {
            for (int i = 0; i <= strArray.length - 1; i++) {
                if (i == strArray.length - 1) {
                    builder.append("" + strArray[i] + "");
                } else {
                    builder.append("" + strArray[i] + "" + ",");
                }
            }
        }
        return builder.toString();
    }

    /**
     * @param bm : bitmap
     * @return encoded base64 value
     */
    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return "data:image/png;base64," + encImage;
    }


    /**
     * This is for getting date for tomorrow and day after tomorrow
     *
     * @param strDate :tomorow or dayaftertomorrow
     * @return : String
     */
    public static String getCurrentDate(String strDate, String formatString) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (strDate.equalsIgnoreCase(REConstants.DATE_DAY_AFTER_TOMORROW)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (strDate.equalsIgnoreCase(REConstants.NEXT_DATE_DAY_AFTER_TOMORROW)) {
            calendar.add(Calendar.DAY_OF_YEAR, 2);
        }
        return sdf.format(calendar.getTime());
    }


    /**
     * Getting current date
     */
    public static String getFormattedDate(Context context, String daySelection) {
        SimpleDateFormat sdf = new SimpleDateFormat("d  MMM yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        sdf.format(calendar.getTime());
        String finalDate = "";
        String[] monthName = context.getResources().getStringArray(R.array.month_name_arrays);
        String[] strDays = context.getResources().getStringArray(R.array.week_day_name);
        // add one day to the date/calendar
        if (daySelection.equalsIgnoreCase(REConstants.DATE_TOMORROW)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            finalDate = String.format(context.getResources().getString(R.string.time_date_name), context.getResources().getString(R.string.text_label_format2) +
                    calendar.get(Calendar.DAY_OF_MONTH) + REUtils.getOrdinalFor(calendar.get(Calendar.DAY_OF_MONTH)) + " "
                    + monthName[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
        } else if (daySelection.equalsIgnoreCase(REConstants.DATE_DAY_AFTER_TOMORROW)) {
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            finalDate = String.format(context.getResources().getString(R.string.time_date_name), strDays[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "\n" +
                    calendar.get(Calendar.DAY_OF_MONTH) + REUtils.getOrdinalFor(calendar.get(Calendar.DAY_OF_MONTH)) + " "
                    + monthName[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
        } else if (daySelection.equalsIgnoreCase(REConstants.NEXT_DATE_DAY_AFTER_TOMORROW)) {
            calendar.add(Calendar.DAY_OF_YEAR, 3);
            finalDate = String.format(context.getResources().getString(R.string.time_date_name), strDays[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "\n" +
                    calendar.get(Calendar.DAY_OF_MONTH) + REUtils.getOrdinalFor(calendar.get(Calendar.DAY_OF_MONTH)) + " "
                    + monthName[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
        }
        return finalDate;
    }

    /**
     * GEts the URI for the fiven bitmap
     *
     * @param inContext : Context
     * @param inImage   : Bitmap
     * @return : Uri
     */
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "reimage" + timeStamp, null);
        RELog.e("Jaya", "Path = " + path);
        if (path == null && isExternalStorageWritable()) {
            path = saveImage(inImage);
        }
        return Uri.parse(path);
    }

    private static String saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "RE_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static Uri getCaptureImageOutputUri(Context context) {
        File getImage = context.getExternalCacheDir();
        File file = new File(getImage.getPath(), "pickImageResult.jpeg");
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    public static Uri getPickImageResultUri(@NonNull Context context, @Nullable Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera || data.getData() == null ? getCaptureImageOutputUri(context) : data.getData();
    }

    /**
     * Decoding bitmap to byte array
     *
     * @param resizedBitmap : Bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap resizedBitmap) {
        if (resizedBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    public static void requestPermissionForCamera(Activity activity, boolean isProfile, boolean b) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCamera = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA);
            int permissionStorage = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionReadStorage = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE);


            int readMedia = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_MEDIA_IMAGES);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (readMedia != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
                }
                if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            } else {
                if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        isProfile ? REQUEST_CHECK_CAMERA_PROFILE : REQUEST_CHECK_CAMERA);
            } else {
                // Runtime permission not required.
                launchCamera(activity, isProfile);
            }
        } else {
            // Runtime permission not required.
            launchCamera(activity, isProfile);
        }
    }

    public static void launchCamera(Activity activity, boolean isProfile) {
        try {
            if (isProfile) {
                Intent pictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (pictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    //Create a file to store the image
                    File photoFile = null;
                    try {
                        photoFile = createImageFile(activity);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(activity),
                                BuildConfig.APPLICATION_ID + ".provider", photoFile);
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                photoURI);
                        activity.startActivityForResult(pictureIntent,
                                isProfile ? REQUEST_CHECK_CAMERA_PROFILE : REQUEST_CHECK_CAMERA);
                    }
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activity.startActivityForResult(intent, isProfile ? REQUEST_CHECK_CAMERA_PROFILE : REQUEST_CHECK_CAMERA);
                }

            } else {

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, R.string.camerapermission, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activity.startActivityForResult(intent, isProfile ? REQUEST_CHECK_CAMERA_PROFILE : REQUEST_CHECK_CAMERA);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * This method is to request the permission to access phone's gallery.
     */
    public static void requestPermissionForGallery(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readWritePermission = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            int mediaPermission=0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                 mediaPermission = ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_MEDIA_IMAGES);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if (readWritePermission != PackageManager.PERMISSION_GRANTED && mediaPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_MEDIA_IMAGES},
                            REQUEST_CHECK_GALLERY);
                }else {
                    launchGallery(activity);
                }
            }else{
                if (readWritePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CHECK_GALLERY);
                } else {
                    launchGallery(activity);
                }
            }

        } else {
            // Runtime permission not required.
            launchGallery(activity);
        }

    }
    public static void launchGallery(Activity activity) {
        //Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
       /* if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, R.string.fileaccesspermission, Toast.LENGTH_SHORT).show();
        } else {*/
            Intent pickPhoto = new Intent(Intent.ACTION_PICK);
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickPhoto.setType("image/*");
            activity.startActivityForResult(pickPhoto, REQUEST_CHECK_GALLERY);
       // }
    }

    private static File createImageFile(Context context) throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    /**
     * Take time stamp string as input and return date month and year string some specific format.
     *
     * @param date Date time stamp.
     * @return Date format value.
     */
    public static String getDateObject(Context context, String date) {
        try {
            String dateSource = date.substring(0, 10);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date readDate = format.parse(dateSource);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(readDate.getTime());
            Log.d(TAG, "Year: " + cal.get(Calendar.YEAR));
            Log.d(TAG, "Month: " + cal.get(Calendar.MONTH));
            Log.d(TAG, "Day: " + cal.get(Calendar.DAY_OF_MONTH));
            // Log.e("Date", " :" + cal.get(Calendar.DAY_OF_MONTH) + "th" + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.YEAR));
            String[] monthName = context.getResources().getStringArray(R.array.month_name_arrays);
            return cal.get(Calendar.DAY_OF_MONTH) + getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
        } catch (Exception e) {
            // Log.e("getDateObject", "Exception");
        }
        return "NA";
    }

    /**
     * Take time stamp string as input and return date month and year string some specific format.
     * Created for modify ride screen
     *
     * @param date Date time stamp.
     * @return Date format value.
     */
    public static String getDateFormatted(Context context, String date) {
        try {
            String dateSource = date.substring(0, 10);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date readDate = format.parse(dateSource);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(readDate.getTime());
            Log.d(TAG, "Year: " + cal.get(Calendar.YEAR));
            Log.d(TAG, "Month: " + cal.get(Calendar.MONTH));
            Log.d(TAG, "Day: " + cal.get(Calendar.DAY_OF_MONTH));
            // Log.e("Date", " :" + cal.get(Calendar.DAY_OF_MONTH) + "th" + cal.get(Calendar.MONTH) + " " + cal.get(Calendar.YEAR));
            String[] monthName = context.getResources().getStringArray(R.array.month_name_arrays);
            String[] strDays = context.getResources().getStringArray(R.array.week_day_name);
            return String.format(context.getResources().getString(R.string.time_date_name), strDays[cal.get(Calendar.DAY_OF_WEEK) - 1] + "\n" + cal.get(Calendar.DAY_OF_MONTH) + getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
        } catch (Exception e) {
            // Log.e("getDateObject", "Exception");
        }
        return "NA";
    }

    /**
     * Takes the time stamp string as input and returns the only date in some specific format.
     *
     * @param context the context
     * @param date    date
     * @return date in a format with ordinal.
     */
    public static String getStartDateObject(Context context, String date) {
        try {
            String dateSource = date.substring(0, 10);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date readDate = format.parse(dateSource);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(readDate.getTime());
            Log.d(TAG, "Year: " + cal.get(Calendar.YEAR));
            Log.d(TAG, "Month: " + cal.get(Calendar.MONTH));
            Log.d(TAG, "Day: " + cal.get(Calendar.DAY_OF_MONTH));
            // Log.e("Date", " :" + cal.get(Calendar.DAY_OF_MONTH) + "th" + cal.get(Calendar.MONTH) + "'" + cal.get(Calendar.YEAR));
            return cal.get(Calendar.DAY_OF_MONTH) + getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            // Log.e("getDateObject", "Exception");
        }
        return "NA";
    }

    /**
     * Take time stamp string as input and return date month and year string some specific format.
     *
     * @param date Date time stamp.
     * @return Date format value.
     */
    public static String getOrdinalDateObject(Context context, String date, boolean isTimeRequired) {    //2018-09-10T00:00:00
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date journeyDate = new java.sql.Date(df.parse(date).getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(journeyDate.getTime());
            String[] monthName = context.getResources().getStringArray(R.array.month_name_arrays);
            String formattedDate = cal.get(Calendar.DAY_OF_MONTH) + getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " +
                    monthName[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
            if (!isTimeRequired) {
                return formattedDate;
            } else {
                return formattedDate + " " + timeFormat.format(df.parse(date));
            }
        } catch (Exception e) {
            RELog.e(e);
        }
        return "---";
    }


    public static String getDistanceInUnits(double mDistance) {
        String distance = null;
        if (mDistance < 1000) {
            distance = String.format("%.1f", mDistance);
            distance = distance + " m";
        } else if (mDistance >= 1000) {
            mDistance = mDistance / 1000;
            distance = String.format("%.1f", mDistance);
            distance = distance + " km";
        }
        try {
            if (REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.KEY_COMMA_OR_POINT,
                    REConstants.KEY_POINT).equalsIgnoreCase(REConstants.KEY_COMMA)) {
                if (distance != null)
                    distance = distance.replace(".", ",");
            }
        } catch (PreferenceException e) {
            //  e.printStackTrace();
        }
        return distance;
    }

    public static String formatDistanceInMilesUnit(Distance mDistance) {
        String distance = null;
        Double feets = mDistance.toFeet();
        if (feets > 500) {
            distance = String.format("%.1f", mDistance.toMiles());
            distance = distance + " mi";
        } else {
            distance = String.format("%.1f", mDistance.toFeet());
            distance = distance + " ft";
        }
        try {
            if (REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.KEY_COMMA_OR_POINT,
                    REConstants.KEY_POINT).equalsIgnoreCase(REConstants.KEY_COMMA)) {
                distance = distance.replace(".", ",");
            }
        } catch (PreferenceException e) {
            //  e.printStackTrace();
        }
        return distance;
    }


    /**
     * Get Real path from URI
     *
     * @param contentURI: Uri
     * @return: String
     */
    public static String getRealPathFromURI(Uri contentURI, Activity activity) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null,
                null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Bitmap getBitmap(Context context, Uri imageUri) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri, filePath, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range")
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        // Do something with the bitmap
        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close();
        return bitmap;
    }



    /**
     * Take time stamp string as input and return date month and year string some specific format.
     *
     * @param date Date time stamp.
     * @return Date format value.
     */
    public static String getOrdinalDateObjectForHistoryDate(Context context, String date) {    //2018-09-10T00:00:00
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
            Date journeyDate = new java.sql.Date(df.parse(date).getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(journeyDate.getTime());
            Log.d(TAG, "Year: " + cal.get(Calendar.YEAR));
            Log.d(TAG, "Month: " + cal.get(Calendar.MONTH));
            Log.d(TAG, "Day: " + cal.get(Calendar.DAY_OF_MONTH));
            // Log.e("Date", " :" + cal.get(Calendar.DAY_OF_MONTH) + "th" + cal.get(Calendar.MONTH) + "'" + cal.get(Calendar.YEAR));
            String[] monthName = context.getResources().getStringArray(R.array.month_name_arrays);
            return cal.get(Calendar.DAY_OF_MONTH) + getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
        } catch (Exception e) {
            //  Log.e("getDateObject", "Exception");
        }
        return "---";
    }

    public static String getYearFromTimeStamp(String date) {
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date journeyDate = new java.sql.Date(df.parse(date).getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(journeyDate.getTime());
            Log.d(TAG, "Year: " + cal.get(Calendar.YEAR));
            Log.d(TAG, "Month: " + cal.get(Calendar.MONTH));
            Log.d(TAG, "Day: " + cal.get(Calendar.DAY_OF_MONTH));
            // Log.e("Date", " :" + cal.get(Calendar.DAY_OF_MONTH) + "th" + cal.get(Calendar.MONTH) + "'" + cal.get(Calendar.YEAR));
            Log.d(TAG, "Year : " + cal.get(Calendar.YEAR));
            return String.valueOf(cal.get(Calendar.YEAR));
        } catch (Exception e) {
            //  Log.e("getYearFromTimeStamp", "Exception");
        }
        return "NA";
    }

    public static String getManufacturingYear(String date) {        //2018-09-10T00:00:00
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);
            Date journeyDate = new java.sql.Date(df.parse(date).getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(journeyDate.getTime());
            return String.valueOf(cal.get(Calendar.YEAR));
        } catch (Exception e) {
            //  Log.e("getManufacturingYear", "Exception");
        }
        return "NA";
    }


    public static String getOrdinalFor(final int value) {
        Log.d(TAG, "" + value);
        if (value >= 11 && value <= 13) {
            return "th";
        }
        switch (value % 10) {
            case 1:
                Log.d(TAG, "st");
                return "st";
            case 2:
                Log.d(TAG, "nd");
                return "nd";
            case 3:
                Log.d(TAG, "rd");
                return "rd";
            default:
                Log.d(TAG, "th");
                return "th";
        }
    }
//    /**
//     * Provides the last serviced date from the service history.
//     *
//     * @param context the activity context.
//     * @return last service date in format.
//     */
//    public static String getLastServicedDate(Context context) {
//        List<ServiceHistoryResponse> serviceHistoryResponseList = REApplication.getInstance().getServiceHistoryResponse();
//        if (serviceHistoryResponseList != null && serviceHistoryResponseList.size() > 0) {
//            return getOrdinalDateObject(context, serviceHistoryResponseList.get(serviceHistoryResponseList.size() - 1).getInvoiceDate());
//        }
//        return "NA";
//    }

    /**
     * Dialog for choosing options to select image
     *
     * @param context  : Context
     * @param activity : Activity
     */
    public static void selectImage(Context context, Activity activity) {
        try {
            final CharSequence[] items = {context.getResources().getString(R.string.text_take_photo),
                    context.getResources().getString(R.string.text_choose_gallery),
                    context.getResources().getString(R.string.text_cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.text_add_photo));
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals(context.getResources().getString(R.string.text_take_photo))) {
                    REUtils.requestPermissionForCamera(activity, false, true);
                } else if (items[item].equals(context.getResources().getString(R.string.text_choose_gallery))) {
                    REUtils.requestPermissionForGallery(activity);
                } else if (items[item].equals(context.getResources().getString(R.string.text_cancel))) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    public static void selectImageProfile(Context context, Activity activity) {
        try {
            final CharSequence[] items = {context.getResources().getString(R.string.text_take_photo),
                    context.getResources().getString(R.string.text_choose_gallery),
                    context.getResources().getString(R.string.text_cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.text_add_photo));
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals(context.getResources().getString(R.string.text_take_photo))) {
                    REUtils.requestPermissionForCamera(activity, true, true);
                } else if (items[item].equals(context.getResources().getString(R.string.text_choose_gallery))) {
                    REUtils.requestPermissionForGallery(activity);
                } else if (items[item].equals(context.getResources().getString(R.string.text_cancel))) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * USed in Map fragment for calculating distance in rides
     * Calculates the distance from all the legs in route
     *
     * @param distanceValues : String[]
     * @return : String
     */
    public static String getDistance(String[] distanceValues) {
        String totalDis = "";
        float distance = 0;
        String[] splitArray;
        if (distanceValues != null && distanceValues.length > 1) {
            for (String distanceItem : distanceValues) {
                String replacedString = distanceItem.replaceAll("[-+^:,]", "");
                splitArray = replacedString.split(" ");
                distance = distance + Float.valueOf(splitArray[0]);
            }
        } else if (distanceValues != null) {
            String replacedString = distanceValues[0].replaceAll("[-+^:,]", "");
            splitArray = replacedString.split(" ");
            distance = Float.valueOf(splitArray[0]);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        float twoDigitsF = Float.valueOf(decimalFormat.format(distance));
        totalDis = String.valueOf(twoDigitsF);
        return totalDis;
    }


    /**
     * Rounding of the amount
     *
     * @param amount : amount
     * @return : Amount in string
     */
    public static String getRoundoffAmount(String amount) {
        double amount_roundoff = Math.round(Double.parseDouble(amount) * 100.0) / 100.0;
        return String.valueOf(amount_roundoff);
    }
    /**
     * Gets the LatLng Bound from the the given routeTrace from the Bila library
     *
     * @param routeTrace {@link RouteTrace}
     * @return {@link LatLngBounds}
     */
//    public static LatLngBounds getLatLngBounds(RouteTrace routeTrace) {
//        /**create for loop/manual to add LatLng's to the LatLngBounds.Builder*/
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        Polyline polyline = routeTrace.getPolyline();
//        //A Polyline is a series of connected line segments. Polylines are useful to represent routes, paths, or other connections between locations on the map.
//        Iterable iterable = polyline.getPoints();
//        for (Object points : iterable) {
//            com.bosch.softtec.components.midgard.core.common.models.LatLng it = (com.bosch.softtec.components.midgard.core.common.models.LatLng) points;
//            com.google.android.gms.maps.model.LatLng latLng = createLatLng(it);
//            builder.include(latLng);
//        }
//        /**create the bounds from latlng Builder to set into map camera*/
//        LatLngBounds bounds = builder.build();
//        return bounds;
//    }
    /**
     * Creates the LatLng {@link com.google.android.gms.maps.model.LatLng} from Bila LatLng object
     *
     * @param latLng {@link LatLng}
     * @return {@link com.google.android.gms.maps.model.LatLng} google LatLng object.
     */
//    public static final com.google.android.gms.maps.model.LatLng createLatLng(com.bosch.softtec.components.midgard.core.common.models.LatLng latLng) {
//        return new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
//    }

    /**
     * Provides the all selected service issues.
     *
     * @return list of selected issues.
     */
    public static ArrayList<String> getSelectedIssues(boolean isSaved, Context context) {
        Map<String, ArrayList<IssuesModel>> map;
        if (isSaved)
            map = REServiceSharedPreference.getAllSavedServiceIssues(context);
        else
            map = REApplication.getInstance().getAllSelectedServiceIssues();
        ArrayList<String> issuesSelected = new ArrayList<>();
        for (Map.Entry<String, ArrayList<IssuesModel>> entry : map.entrySet()) {
            ArrayList<IssuesModel> values = entry.getValue();
            for (IssuesModel issuesModel : values) {
                if (issuesModel.getSelected()) {
                    issuesSelected.add(issuesModel.getIssueName());
                }
            }
        }
        return issuesSelected;
    }

    public static ArrayList<IssuesModel> getAllServiceIssues(String strKeyStore) {
        Map<String, ArrayList<IssuesModel>> map = REApplication.getInstance().getAllSelectedServiceIssues();
        if (map != null) {
            for (Map.Entry<String, ArrayList<IssuesModel>> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key.equalsIgnoreCase(strKeyStore)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Returns the address as input and returns Address object from the location.
     * Reverse GeoCoding.
     *
     * @param context    Activity context.
     * @param oldAddress old address.
     * @return return address {@link Address}
     */
    public static Address setLocationFromUserEnteredAddress(Context context, String oldAddress) {
        //  Log.e("oldAddress:", oldAddress);
        List<Address> addresses = null;
        Address address;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(oldAddress, 1);
        } catch (IOException e) {
            RELog.e(e);
        }
        if (addresses != null && addresses.size() > 0) {
            address = addresses.get(0);
            return address;
        } else {
            Toast.makeText(context, "Unable to fetch location on map",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }

    /**
     * Navigates to login page by removing modelstore and preferences
     */
    public static void navigateToLogin() {
        try {
            //Clearing the modelstore and preferences
            REApplication.getInstance().clearAllModelStore();
            REPreference.getInstance().removeAll(REApplication.getAppContext());
            Intent intent = new Intent(REApplication.getAppContext(), UserOnboardingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            REApplication.getAppContext().startActivity(intent);
        } catch (PreferenceException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    /**
     * Navigates to login page by removing modelstore and preferences
     */
    public static void navigateToSplash() {
        try {
			REBaseActivity.isUserActive=true;
            //Clearing the modelstore and preferences
            REApplication.getInstance().clearAllModelStore();
            REPreference.getInstance().removeAll(REApplication.getAppContext());
            REServiceSharedPreference.clearBookingInfoPreferences(REApplication.getAppContext());
            Intent intent = new Intent(REApplication.getAppContext(), UserOnboardingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            REApplication.getAppContext().startActivity(intent);
        } catch (PreferenceException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    /**
     * Used in ride details for formatting string
     *
     * @param placeName : String
     * @return String
     */
    public static String splitPlaceName(String placeName) {
        String truncatedPlaceName = "";
        if (placeName != null) {
            if (placeName.contains(",")) {
                String[] PlaceNameArray = placeName.split(",");
                truncatedPlaceName = PlaceNameArray[0];
            } else {
                truncatedPlaceName = placeName;
            }
        }
        return truncatedPlaceName;
    }

    /**
     * Check the network connection and returns true/false
     *
     * @return boolean
     */
    public static boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) REApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (connectivityManager != null) netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static String getTimeSlots(String startTimeSlot) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        String time = "";
        try {
            Date date = inputFormat.parse(startTimeSlot);
            time = outputFormat.format(date);
        } catch (ParseException e) {
        }
        return time;
    }

    /**
     * This method is used to share the text using android intent action send
     *
     * @param activity : Activity
     * @param url      : String
     */
    public static void shareRide(Activity activity, String url) {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        i.putExtra(android.content.Intent.EXTRA_TEXT, REUtils.getMobileappbaseURLs().getLightweightPageURL() + url);
        activity.startActivity(Intent.createChooser(i, "Share via"));
    }


    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    /**
     * Load json from the asset fi
     */
    public static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            while (is.read(buffer) > 0) {

            }
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            RELog.e(ex);
            return null;
        }
        return json;
    }

    /**
     * This method created creates the circular image border
     *
     * @param bitmap      bitmap
     * @param borderWidth width of the border.
     * @return Bitmap
     */
    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap, int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;
        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2f, paint);
        return canvasBitmap;
    }

    /**
     * Common function for loading Glide
     *
     * @param context   : Context
     * @param imageURL  : String
     * @param imageView : ImageView
     */
    public static void loadImageWithGlide(Context context, String imageURL, ImageView imageView) {
        //TODO Need to handle out of memory exception
        try {
            RequestBuilder<Bitmap> requestBuilder = Glide.with(context)
                    .asBitmap()
                    .load(imageURL);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.no_image_found)
                    .priority(Priority.HIGH);
            requestBuilder
                    .apply(options)
                    .into(imageView);
        } catch (OutOfMemoryError e) {
            RELog.e(e.getMessage(), e.getLocalizedMessage());
        }
    }

    /*
     * This method is to set the rider joined details.
     */
    public static void setRidersJoinedInfo(Context context, ProfileRidesResponse profileRideResponse,
                                           RECircularImageView mCircularImageView1, RECircularImageView mCircularImageView2,
                                           RECircularImageView mCircularImageView3, TextView mTvRideMemberNames, Guideline mImageGuideline) {
        int noOfRidersJoined = profileRideResponse.getRidersJoined().size();
        switch (noOfRidersJoined) {
            case 1:
                mCircularImageView2.setVisibility(View.GONE);
                mCircularImageView3.setVisibility(View.GONE);
                REUtils.loadImageWithGlide(context, REUtils.getMobileappbaseURLs().getAssetsURL() +
                        profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL), mCircularImageView1);
                mTvRideMemberNames.setText(profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) + " " +
                        profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME));
                setGuidelinePercentage(mImageGuideline, 0.135f);
                break;
            case 2:
                mCircularImageView3.setVisibility(View.GONE);
                REUtils.loadImageWithGlide(context, REUtils.getMobileappbaseURLs().getAssetsURL() +
                        profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL), mCircularImageView1);
                REUtils.loadImageWithGlide(context, REUtils.getMobileappbaseURLs().getAssetsURL() +
                        profileRideResponse.getRidersJoined().get(1).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL), mCircularImageView2);
                mTvRideMemberNames.setText(profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) + " " +
                        profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME) + " & " +
                        profileRideResponse.getRidersJoined().get(1).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) + " " +
                        profileRideResponse.getRidersJoined().get(1).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME));
                setGuidelinePercentage(mImageGuideline, 0.20f);
                break;
            default:
                REUtils.loadImageWithGlide(context, REUtils.getMobileappbaseURLs().getAssetsURL() +
                        profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL), mCircularImageView1);
                REUtils.loadImageWithGlide(context, REUtils.getMobileappbaseURLs().getAssetsURL() +
                        profileRideResponse.getRidersJoined().get(1).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL), mCircularImageView2);
                REUtils.loadImageWithGlide(context, REUtils.getMobileappbaseURLs().getAssetsURL() +
                        profileRideResponse.getRidersJoined().get(2).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_PROFILE_URL), mCircularImageView3);
                mTvRideMemberNames.setText(profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) + " " +
                        profileRideResponse.getRidersJoined().get(0).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME) + " , " +
                        profileRideResponse.getRidersJoined().get(1).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_FNAME) + " " +
                        profileRideResponse.getRidersJoined().get(1).getUser().get(REFirestoreConstants.RIDERS_JOINED_USER_LNAME) + " & " + (noOfRidersJoined - 2) + " Others");
                break;
        }
    }

    private static void setGuidelinePercentage(Guideline mImageGuideline, float val) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mImageGuideline.getLayoutParams();
        params.guidePercent = val;
        mImageGuideline.setLayoutParams(params);
    }
    /**
     * Returns the last visited service center dealer id from the service history response list.
     *
     * @param serviceHistoryResponseList {@link ServiceHistoryResponse>}
     * @return dealer Id.
     */
 /*   public static String getLastVisitedServiceCenterDealerId(List<ServiceHistoryResponse> serviceHistoryResponseList) {
        ArrayList<Date> listOfDates = new ArrayList<>();
        for (int i = 0; i < serviceHistoryResponseList.size(); i++) {
            String dtStart = String.valueOf(serviceHistoryResponseList.get(i).getLastUpdatedOn()); //TODO Lat updated date needs to be changed instead of getInvoiced date.
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
            try {
                Date date = format.parse(dtStart);
                listOfDates.add(date);
            } catch (java.text.ParseException e) {
                RELog.e(e);
            }
        }
        Date minDate = Collections.max(listOfDates);
        int index = listOfDates.indexOf(minDate);
        return serviceHistoryResponseList.get(index).getDealerId();
    }*/

    /**
     * @param context  the context.
     * @param icon     icon.
     * @param newColor color.
     * @return This is used to change the color of drawable
     */
    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        try {
            Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
            mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
            return mDrawable;
        } catch (Exception e) {
            RELog.e(e);
        }
        return null;
    }

    public static double distanceFromCurrentLocationInKms(double initialLat, double initialLong,
                                                          double finalLat, double finalLong) {
        int R = 6371; // km (Earth radius)
        double dLat = toRadians(finalLat - initialLat);
        double dLon = toRadians(finalLong - initialLong);
        initialLat = toRadians(initialLat);
        finalLat = toRadians(finalLat);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private static double toRadians(double deg) {
        return deg * (Math.PI / 180);
    }

    public static boolean checkIfTokenValid() {
        if (REUtils.isUserLoggedIn()) {
            try {
                JWT jwt = new JWT(REApplication.getInstance().getUserTokenDetails().trim().split("\\s+")[1]);
                //  Log.e("EXPIRY TOKEN>>>", jwt.getExpiresAt() + "");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, Integer.parseInt(REUtils.getREKeys().getJWTExpiryThreshold()));
                Date currentTime = cal.getTime();
                // Log.e("EXPIRY Current>>>>>", currentTime + "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                long currentDate = setTimeToMidnight(cal.getTime());
                long expiryDate = setTimeToMidnight(jwt.getExpiresAt());
                if (expiryDate - currentDate <= 0) {
                    return false;
                } else
                    return true;
//                Log.e("EXPIRY Current>>>>>", (expiryTime-current) + "");
//             //else
//               //  return false;
//                long diff = jwt.getExpiresAt().getTime() - currentTime.getTime();
//                long seconds = diff / 1000;
//                long minutes = seconds / 60;
//                long hours = minutes / 60;
//                long days = hours / 24;
//                Log.e("EXPIRY DIFF>>>>>", days + "," + hours + "," + minutes + "," + seconds + "");
//
//                if (hours < 2) {
//                    return false;
//                }
//
//                return true;
            } catch (Exception e) {
                RELog.e(e);
            }
        }
        return false;
    }

    public static boolean checkIfTokenGeneratedValid() {
        if (REUtils.isUserLoggedIn()) {
            try {
                JWT jwt = new JWT(REApplication.getInstance().getUserTokenDetails().trim().split("\\s+")[1]);
                Log.e("EXPIRY TOKEN>>>", jwt.getExpiresAt() + "");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,6);
                Date currentTime = cal.getTime();
                Log.e("EXPIRY Current>>>>>", currentTime + "");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                long currentDate = setTimeToMidnight(cal.getTime());
                long expiryDate = setTimeToMidnight(jwt.getExpiresAt());
                if (expiryDate <currentDate) {
                    return false;
                } else
                    return true;


//
            } catch (Exception e) {
                RELog.e(e);
            }
        }
        return false;
    }

    public static long setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static void showBlurDialog(FragmentActivity activity
            , String title, String message, OnBlurDialogPositiveButtonClickListener listener) {

        Dialog dialog = new Dialog(activity, R.style.blurTheme); // Context, this, etc.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.blur_dialog_view);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView messageView = dialog.findViewById(R.id.message);
        messageView.setText(message);

        TextView titleView = dialog.findViewById(R.id.title);
        titleView.setText(title);

        dialog.findViewById(R.id.no).setOnClickListener(view -> {
            listener.onNegetiveClicked();
            dialog.dismiss();
        });

        dialog.findViewById(R.id.yes).setOnClickListener(view -> {
            listener.onPositiveClicked();
            dialog.dismiss();
        });

        dialog.show();
    }

    public static void logGTMEvent(String keyMotorcyclesGtm, Bundle params) {
        String flavour = "";
        if (BuildConfig.FLAVOR.contains("dev")) {
            flavour = "REAPPDEV";
        } else if (BuildConfig.FLAVOR.contains("uat")) {
            flavour = "REAPPUAT";
        } else if (BuildConfig.FLAVOR.contains("production")) {
            flavour = "REAPPPROD";
        }
        params.putString("countryCode", REApplication.getInstance().Country.equalsIgnoreCase(REConstants.DEFAULT_HEADER) ? "" : REApplication.getInstance().Country.toLowerCase());
        params.putString("regionCode", BuildConfig.APPLICATION_ID);

        params.putString("tvc_environment", flavour);
        params.putString("appIdentificationValue", "Mobile app");

        if (REApplication.getInstance().Country.equalsIgnoreCase(REConstants.COUNTRY_INDIA)) {
            mFirebaseAnalytics.logEvent(keyMotorcyclesGtm, params);
        }

         Log.e( "logGTMEvent: ", params.toString());
        //  mFirebaseAnalytics.logEvent(keyMotorcyclesGtm, params);
        RELog.e(TAG + ":logGTMEvent:", params.toString());
    }

    public static Bundle bundleFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return new Bundle();
        }

        Bundle result = new Bundle();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);

                if (value instanceof String) {
                    result.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    result.putInt(key, (Integer) value);
                } else if (value instanceof Double) {
                    result.putDouble(key, (Double) value);
                } else {
                    Log.w(TAG, "Value for key " + key + " not one of [String, Integer, Double]");
                }
            }
        } catch (JSONException e) {
            Log.w(TAG, "Failed to parse JSON, returning empty Bundle.", e);
            return new Bundle();
        }

        return result;
        // [END_EXCLUDE]
    }

    public static void setUserModel() {
        if (REUtils.isUserLoggedIn()) {
            if (REUserModelStore.getInstance().getUserId() == null || REUserModelStore.getInstance().getUserId().equalsIgnoreCase("")) {
                try {
                    LoginResponse response = new Gson().fromJson(REPreference.getInstance().getString(REApplication.getAppContext(), USERDATA), LoginResponse.class);
                    if (response != null && response.getData() != null) {
                        REUserModelStore.getInstance().setRefreshToken(response.getData().getRefreshToken());
                        REUserModelStore.getInstance().setUserId(response.getData().getUser().getUserId());
                        REUserModelStore.getInstance().setPhoneNo(response.getData().getUser().getPhone());
                        REUserModelStore.getInstance().setCallingCde(response.getData().getUser().getCallingCode());
                        REUserModelStore.getInstance().setFname(response.getData().getUser().getFirstName());
                        REUserModelStore.getInstance().setLname(response.getData().getUser().getLastName());
//                        REUserModelStore.getInstance().setEmail(response.getData().getUser().getEmail());
//                        REUserModelStore.getInstance().setProfileUrl(response.getData().getUser().getProfilePicture());
                    }
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
            }
            FirestoreManager.getInstance().getAllRidesInfoFromFirestore();

        }
    }

    public static List<String> getConnectedBike(Context context) {
        List<VehicleDataModel> list= REServiceSharedPreference.getVehicleData(context);
        List<String> connectdBike=new ArrayList<>();
        for(VehicleDataModel data:list){
            if(data.getIsConnected()){
                connectdBike.add(data.getChaissisNo());
            }
        }
        return  connectdBike;
    }


    public void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            //  Log.e(TAG, "printHashKey()", e);
        }
    }

    /**
     * Gets the current calendar instance in d MMM YYYY format
     *
     * @return Calendar
     */
    public static Calendar getCurrentDateInDMM() {
        SimpleDateFormat sdf = new SimpleDateFormat("d  MMM'' yyyy");
        Calendar calendar = Calendar.getInstance();
        sdf.format(calendar.getTime());
        return calendar;
    }

    /**
     * Checks whether network is available or not.
     *
     * @param context The context.
     * @return TRUE, if connected to a network, FALSE otherwise.
     */
    public static boolean isNetworkAvailable(Context context) {

        if (context != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                RELog.e(e);
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the error message based on code received by interactor
     *
     * @param code : Response code from API response
     * @return : String
     */
    public static String getErrorMessageFromCode(int code) {
        String mErrorMessage;
        switch (code) {
            case 404:
            case 401:
                mErrorMessage = REApplication.getAppContext().getResources().getString(R.string.wrong_login_details);
                break;
            case 0:
                mErrorMessage = REApplication.getAppContext().getResources().getString(R.string.system_down);
                break;
            case 400:
                mErrorMessage = REApplication.getAppContext().getResources().getString(R.string.no_tries_exceed);
                break;
            default:
                mErrorMessage = REApplication.getAppContext().getResources().getString(R.string.system_down);
                break;
        }
        return mErrorMessage;
    }

    /**
     * Gets the error message based on code received by interactor
     *
     * @param code : Response code from API response
     * @return : String
     */
    public static String getErrorMessageFromCodeForSignup(int code) {
        String mErrorMessage;
        switch (code) {
            case 409:
            case 204:
                mErrorMessage = REApplication.getAppContext().getResources().getString(R.string.signup_issue_details);
                break;

            default:
                mErrorMessage = REApplication.getAppContext().getResources().getString(R.string.system_down);
                break;
        }
        return mErrorMessage;
    }

    public static String bikeNameInSpinner(String modelName) {
        String truncatedModelName = "";
        if (modelName != null) {
            if (modelName.contains(" ")) {
                String[] modelNameArray = modelName.split("\\s+");
                truncatedModelName = modelNameArray[0] + " " + modelNameArray[1];
            } else {
                truncatedModelName = modelName;
            }
        }
        return truncatedModelName;
    }

    public static void saveFcmRegistrationTokenIntoPref(String token) {
        mNotifeditor.putString(REConstants.DEVICE_TOKEN, token);
        mNotifeditor.apply();
    }

    public static String getFcmRegistrationTokenFromPref(Context context) throws PreferenceException {
        return mNotifPreference.getString(REConstants.DEVICE_TOKEN, "");
    }

    public static void setFCMTokenSent(boolean isSent) {
        mNotifeditor.putBoolean(REConstants.DEVICE_TOKEN_SENT, isSent);
        mNotifeditor.apply();
    }

    public static boolean isFCMTokenSent(Context context) {
        return mNotifPreference.getBoolean(REConstants.DEVICE_TOKEN_SENT, false);
    }

    public static boolean isUserLoggedIn() {
        boolean isUserLogged = false;
        try {
            if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId().length() > 0 && REApplication.getInstance().getUserTokenDetails() != null) {
                isUserLogged = true;
                if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser().getLocality() != null)
                    REApplication.getInstance().Country = REApplication.getInstance().getUserLoginDetails().getData().getUser().getLocality().getCountry().toUpperCase();
                else
                    REApplication.getInstance().Country = REConstants.COUNTRY_INDIA;

            }
        } catch (Exception e) {

        }
        return isUserLogged;
    }

    public static boolean isAppInstalled(String packageName) {
        Intent mIntent = REApplication.getAppContext().getPackageManager().getLaunchIntentForPackage(packageName);
        return mIntent != null;
    }

    public static NewsResponse getmNewsResponse() {
        return mNewsResponse;
    }

    public static EventsResponse getmEventsResponse() {
        return mEventsResponse;
    }

    /**
     * Gets the current location from preference
     *
     * @return : Location
     */
    public static Location getLocationFromModel() {
        Location mCurrentLocation = new Location("dest");
        mCurrentLocation.setLongitude(REUserModelStore.getInstance().getLongitude());
        mCurrentLocation.setLatitude(REUserModelStore.getInstance().getLatitude());
        return mCurrentLocation;
    }

    /**
     * @param response : VerifyTokenResponse
     */
    public static void setDataFromVerifyTokenToUserInfo(VerifyTokenResponse response) {
        if (response != null && response.getData() != null) {
            REUserModelStore.getInstance().setUserId(response.getData().getId());
            REUserModelStore.getInstance().setFname(response.getData().getFName());
            REUserModelStore.getInstance().setLname(response.getData().getLName());
            REUserModelStore.getInstance().setGender(response.getData().getGender());
            REUserModelStore.getInstance().setPhoneNo("+" + response.getData().getContactDetails().getMobile().getCallingCode() + "" + response.getData().getContactDetails().getMobile().getNumber());
            REUserModelStore.getInstance().setProfileUrl(response.getData().getProfilePicture());
            REUserModelStore.getInstance().setEmail(response.getData().getContactDetails().getEmail());
            REUserModelStore.getInstance().setDob(response.getData().getDob());
            initCloudManager();
        }
    }

    public static boolean validateLoginResponse(LoginResponse response) {
        String userId = response.getData().getUser().getUserId();
        String jwtToken = response.getData().getJwtAccessToken();
        String refreshToken = response.getData().getRefreshToken();
        String mobileNo = response.getData().getUser().getPhone();
        return userId != null && !userId.isEmpty() && jwtToken != null && !jwtToken.isEmpty() &&
                refreshToken != null && !refreshToken.isEmpty() && mobileNo != null && !mobileNo.isEmpty();
    }

    /**
     * Sets the data to user modelstore and stores UserId, JWT & Refresh token in preference
     *
     * @param response : LoginResponse
     */
    public static void setDataToUserInfo(LoginResponse response) {
        if (response != null && response.getData() != null) {
            FirebaseCrashlytics.getInstance().setUserId(response.getData().getUser().getUserId());
            REUserModelStore.getInstance().setRefreshToken(response.getData().getRefreshToken());
            REUserModelStore.getInstance().setUserId(response.getData().getUser().getUserId());
            REUserModelStore.getInstance().setPhoneNo(response.getData().getUser().getPhone());
            REUserModelStore.getInstance().setCallingCde(response.getData().getUser().getCallingCode());
            REUserModelStore.getInstance().setFname(response.getData().getUser().getFirstName());
            REUserModelStore.getInstance().setLname(response.getData().getUser().getLastName());
            REUserModelStore.getInstance().setEmail(response.getData().getUser().getEmail());
            REUserModelStore.getInstance().setProfileUrl(response.getData().getUser().getProfilePicture());
            // Storing the data to preference
            try {
                if (response.getData().getUser().getLocality() != null && response.getData().getUser().getLocality().getCountry() != null) {
                    REApplication.getInstance().Country = response.getData().getUser().getLocality().getCountry().toUpperCase();
                    REApplication.getInstance().featureCountry = REUtils.getFeatureMap().get(REApplication.getInstance().Country);
                }
                if (REApplication.getInstance().featureCountry == null) {
                    REApplication.getInstance().featureCountry = REUtils.getFeatures().getDefaultFeatures();
                }

                REPreference.getInstance().putString(REApplication.getAppContext(), USERID_KEY,
                        response.getData().getUser().getUserId());
                REPreference.getInstance().putString(REApplication.getAppContext(), MOBILE_NO_KEY,
                        response.getData().getUser().getPhone());
                REPreference.getInstance().putString(REApplication.getAppContext(), REFRESH_TOKEN_KEY,
                        response.getData().getRefreshToken());
                REApplication.getInstance().JWTToken = response.getData().getJwtAccessToken();
                REApplication.getInstance().getEncryptedSharedPreference(REApplication.getAppContext()).edit()
                        .putString(JWT_TOKEN_KEY, response.getData().getJwtAccessToken())
                        .apply();
                response.getData().setJwtAccessToken(null);
                REPreference.getInstance().putString(REApplication.getAppContext(), USERDATA, new Gson().toJson(response));
                if(!checkIfTokenGeneratedValid()){
                    logJWTPrint(REApplication.getInstance().getUserTokenDetails(),response.getData().getUser().getUserId());
                }
            } catch (PreferenceException | GeneralSecurityException | IOException e) {
                RELog.e(e);
            }
            initCloudManager();
        }
    }

    public static void initCloudManager() {
        try {
            CloudManager cloudManager = CloudManager.Companion.createInstance(CloudConfigurationProvider.INSTANCE.getCloudConfiguration());

            // TODO use corresponding configuration based on the used server (e.g. NoAuthConfig for NoAuth-Server
            //  and OAuthConfig for productive server)
            final AuthorizationConfiguration authorizationConfiguration;
            if (REUtils.getJwtToken() != null) {
                JsonWebToken jsonWebToken = new JsonWebToken(REUtils.getJwtToken());
                authorizationConfiguration = new OAuthConfiguration(jsonWebToken.getToken());
                cloudManager.setAuthorizationConfiguration(authorizationConfiguration);
            }


//            final String userName = REUserModelStore.getModelStore().getUserId();
//            final String scope = "admin/read,admin/write"; // TODO set appropriate scope
//            authorizationConfiguration = new NoAuthConfiguration(userName, scope);
//            cloudManager.setAuthorizationConfiguration(authorizationConfiguration);

            Duration duration = new Duration(50, TimeUnit.SECONDS);
            NetworkTimeoutConfiguration networkTimeoutConfiguration = new NetworkTimeoutConfiguration(duration, duration, duration, duration);
            cloudManager.setNetworkTimeoutConfiguration(networkTimeoutConfiguration);

            REApplication.getInstance().setCloudManager(cloudManager);
        } catch (CloudException e) {
            // Log.e("test", "Cloud Exception = " + e.getHttpErrorMessage());
        }
    }


    public static void getFirebaseReactUrl(Context context, String variantCode, ImageView bikeImage, TextView model, boolean fullScreenImage, OnFirestoreResponseListener listener) {
        if (variantCode != null) {
            REApplication.mFireStoreInstance.collection(REALTIME_DB).document(VARIANT_REALTIME_DB).collection(PC_FIREBASE_COUNTRY_KEY).document(variantCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String img = document.getString("imageUrl");
                            String name = document.getString("displayName");
                            if (model != null) {
                                model.setText(name);
                            }
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            int width = 1000;
                            if (context != null && ((Activity) context).getWindowManager() != null && ((Activity) context).getWindowManager().getDefaultDisplay() != null) {
                                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                width = displayMetrics.widthPixels;
                            }
                            if (bikeImage != null) {
                                if (fullScreenImage) {
                                    int finalWidth = width;
                                    RequestListener<Drawable> listener = new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable drawable, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            bikeImage.setAdjustViewBounds(true);
                                            bikeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                            bikeImage.setImageDrawable(drawable);

                                            return true;
                                        }
                                    };
                                    RequestBuilder<Drawable> requestBuilder = Glide.with(getAppContext())
                                            .load(img)
                                            .listener(listener);
                                    RequestOptions options1 = new RequestOptions()
                                            .override(bikeImage.getLayoutParams().width, (int) (bikeImage.getLayoutParams().width * .75));
                                    requestBuilder.apply(options1)
                                            .into(bikeImage);
                                } else {
                                    RequestBuilder<Drawable> requestBuilder = Glide.with(getAppContext())
                                            .load(img);
                                    RequestOptions options = new RequestOptions()
                                            .fitCenter().dontAnimate()
                                            .placeholder(R.drawable.placeholder_re)
                                            .error(R.drawable.no_image_found)
                                            .priority(Priority.HIGH);
                                    requestBuilder
                                            .apply(options)
                                            .into(bikeImage);
                                }
                            }
                            if (listener != null) {
                                listener.onSuccess(document);
                            }


                        } else {
                            generateLogs(null, CONFIGURATION_REALTIME_DB, "Document not exist " + variantCode);

                        }
                    } else {
                        generateLogs(null, CONFIGURATION_REALTIME_DB, task.getException() != null ? task.getException().getMessage() : "");

                    }
                }
            });
        }
    }

    public void setScaleImage(ImageView view) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();
        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float xScale = ((float) 4) / width;
        float yScale = ((float) 4) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        view.setImageDrawable(result);

//		LayoutParams params = view.getLayoutParams();
//		params.width = width;
//		params.height = height;
//		view.setLayoutParams(params);
    }

    //    public static void getModelNameFromModelCode(OnFirestoreVehicleDataMappingCallback callback, List<VehicleDataModel> vehicleModel, final int i) {
//        String modelCode = vehicleModel.get(i).getModelCode();
//        if (modelCode != null) {
//            DatabaseReference myRef = FirebaseDatabase.getInstance(PCUtils.PC_FIREBASE_DEV_VARIANT_URL)
//                    .getReference();
//            myRef.child("variant").child(PCUtils.PC_FIREBASE_COUNTRY_KEY).child(modelCode).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() != null) {
//                        String modelCode = dataSnapshot.child("id").getValue(String.class);
//                        String displayName = dataSnapshot.child("displayName").getValue(String.class);
//                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
//                        String ownerOwner = dataSnapshot.child("ownerManual").getValue(String.class);
//                        boolean variantConnected = false;
//                        variantConnected = vehicleModel.get(i).getIsConnected();
//                        Map<String, String> map = vehicleModel.get(i).getMapVehicleData();
//                        map.put(modelCode, displayName);
//                        vehicleModel.get(i).setMapVehicleData(map);
//                        if (vehicleModel.get(i).getModelCode().equals(modelCode)) {
//                            vehicleModel.get(i).setModelName(displayName);
//                            vehicleModel.get(i).setImageUrl(imageUrl);
//                            vehicleModel.get(i).setDisplayName(displayName);
//                            vehicleModel.get(i).setOwnerManual(ownerOwner);
//                            vehicleModel.get(i).setIsConnected(variantConnected);
//                        }
//
//                    } else {
//                        generateLogs(null, PCUtils.PC_FIREBASE_DEV_VARIANT_URL, "Model code " + vehicleModel.get(i).getModelCode() + " is missing in variant master");
//                    }
//                    if (callback != null) {
//                        callback.onVehicleMappingSuccess(vehicleModel, i);
//                    }
////                    REServiceSharedPreference.saveVehicleData(context, vehicleModel);
//                }
//
//                @Override
//                public void onCancelled(@NotNull DatabaseError error) {
//                    generateLogs(null, PCUtils.PC_FIREBASE_DEV_VARIANT_URL, error.getMessage());
//                    // Failed to read value
//                }
//            });
//        }
//
//
//    }
    public static void getModelNameFromModelCode(OnFirestoreVehicleDataMappingCallback callback, List<VehicleDataModel> vehicleModel, final int i) {
        String modelCode = vehicleModel.get(i).getModelCode();
        if (modelCode != null) {
            REApplication.mFireStoreInstance.collection(REALTIME_DB).document(VARIANT_REALTIME_DB).collection(PC_FIREBASE_COUNTRY_KEY).document(modelCode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String modelCode = document.getString("id");
                            String displayName = document.getString("displayName");
                            String imageUrl = document.getString("imageUrl");
                            String ownerOwner = document.getString("ownerManual");
                            boolean variantConnected = false;
                            variantConnected = vehicleModel.get(i).getIsConnected();
                            Map<String, String> map = vehicleModel.get(i).getMapVehicleData();
                            map.put(modelCode, displayName);
                            vehicleModel.get(i).setMapVehicleData(map);
                            if (vehicleModel.get(i).getModelCode().equals(modelCode)) {
                                vehicleModel.get(i).setModelName(displayName);
                                vehicleModel.get(i).setImageUrl(imageUrl);
                                vehicleModel.get(i).setDisplayName(displayName);
                                vehicleModel.get(i).setOwnerManual(ownerOwner);
                                vehicleModel.get(i).setIsConnected(variantConnected);
                            }
                            if (callback != null) {
                                callback.onVehicleMappingSuccess(vehicleModel, i);
                            }
                        } else {
                            generateLogs(null, CONFIGURATION_REALTIME_DB, "Document not exist " + modelCode);

                        }
                    } else {
                        generateLogs(null, CONFIGURATION_REALTIME_DB, task.getException() != null ? task.getException().getMessage() : "");

                    }
                }
            });
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static String getDate(String dateFormat, long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        //String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        String date = new SimpleDateFormat(dateFormat, Locale.getDefault())
                .format(cal.getTime());
        return date;
    }

    public static void getProfileDetailsFromServer(OnEditProfileFinishedListener editProfileFinishedListener) {
        LoginResponse loginResponse = REApplication.getInstance().getUserLoginDetails();
        if (loginResponse != null && loginResponse.getData() != null && loginResponse.getData().getUser() != null) {
            Log.d("API", "Website : getProfileDetails API called");
            new EditProfileInteractor().getProfileDetails(loginResponse.getData().getUser().getUserId(), editProfileFinishedListener);
        }
    }

    /**
     * Trims the mobile number by removing the country code
     *
     * @param mobile: String
     * @return
     */
    public static String trimCountryCodeFromMobile(String mobile) {
        if (mobile != null) {
            return mobile;
        }
        return null;
    }


    //    public static void createFirebaseUser(FirebaseAuthListner firebaseAuthListner) {
//        if (REApplication.getInstance().mFirebaseAuth != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser() != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser().isAnonymous()) {
//            FirebaseUser user = REApplication.getInstance().mFirebaseAuth.getCurrentUser();
//            AuthCredential credential = EmailAuthProvider.getCredential(REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail(), REApplication.getInstance().getKeys());
//            user.linkWithCredential(credential)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "convertAnonymustoRegisterUser:success");
//                            firebaseAuthListner.onFirebaseAuthSuccess();
//                        } else {
//
//                            try {
//                                throw task.getException();
//                            }
//                            // if user enters wrong email.
//                            catch (FirebaseAuthUserCollisionException existEmail) {
//                                Log.d(TAG, "onComplete: exist_email");
//                                signInWithFirebase(firebaseAuthListner);
//                            } catch (Exception e) {
//                                logPrint(task.getException().toString(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
//                                firebaseAuthListner.onFirebaseAuthFailure();
//                                Log.d(TAG, "onComplete: " + e.getMessage());
//                            }
//
//
//                            Log.d(TAG, task.getException().toString());
//                        }
//
//                    });
//        } else {
//
//            if (REApplication.getInstance().mFirebaseAuth != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser() != null) {
//                firebaseAuthListner.onFirebaseAuthSuccess();
//            } else {
//                createNewUserOnFirebase(firebaseAuthListner);
//            }
//        }
//    }
    public static void logPrint(String error, String user) {
        com.royalenfield.reprime.models.request.logs.Log log = new com.royalenfield.reprime.models.request.logs.Log();

        log.setEnvironment("Android");
        log.setIp(REUtils.getIP(REApplication.getAppContext()));
        log.setAppId(getEnvironment());
        log.setTimestamp(dateFormater());
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
            log.setUser(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        else
            log.setUser("Guest");

        Message message = new Message();
        StringBuilder type = new StringBuilder("");
        Body body = new Body();
        type.append("/" + BuildConfig.VERSION_NAME + "/" + BuildConfig.VERSION_CODE + "/");
        log.setMessageType("error");
        type.append("Firebase/" + "AUTH/" + "Android");
        log.setMessageType("error");
        message.setTitle("Firebase error");
        body.setMessage(user);
        body.setResponse(error);
        message.setBody(body);
        log.setTitle("Firebase error");
        log.setMessage(message);
        uploadLogs(log, type.toString(), log.getAppId());
    }

//    private static void createNewUserOnFirebase(FirebaseAuthListner firebaseAuthListner) {
//        REApplication.getInstance().mFirebaseAuth.createUserWithEmailAndPassword(REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail(),  REApplication.getInstance().getKeys())
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = REApplication.getInstance().mFirebaseAuth.getCurrentUser();
//                            firebaseAuthListner.onFirebaseAuthSuccess();
//
//                        }
//                        if (!task.isSuccessful()) {
//                            try {
//                                throw task.getException();
//                            }
//                            // if user enters wrong email.
//                            catch (FirebaseAuthWeakPasswordException weakPassword) {
//                                logPrint(task.getException().toString(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
//
//                                firebaseAuthListner.onFirebaseAuthFailure();
//                                Log.d(TAG, "onComplete: weak_password");
//
//                            }
//                            // if user enters wrong password.
//                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
//                                logPrint(task.getException().toString(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
//
//                                Log.d(TAG, "onComplete: malformed_email");
//                                firebaseAuthListner.onFirebaseAuthFailure();
//
//                            } catch (FirebaseAuthUserCollisionException existEmail) {
//                                Log.d(TAG, "onComplete: exist_email");
//
//                                signInWithFirebase(firebaseAuthListner);
//                            } catch (Exception e) {
//                                logPrint(task.getException().toString(),REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
//                                firebaseAuthListner.onFirebaseAuthFailure();
//                                Log.d(TAG, "onComplete: " + e.getMessage());
//                            }
//                        }
//
//                    }
//                });
//
//    }
    public static void logJWTPrint(String jwt,String user){
        com.royalenfield.reprime.models.request.logs.Log log = new com.royalenfield.reprime.models.request.logs.Log();

        log.setEnvironment("Android");
        log.setAppId("reprime");
        log.setTimestamp(dateFormater());
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
            log.setUser(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        else
            log.setUser("Guest");

        Message message = new Message();

        String type = "";
        Body body = new Body();
        log.setMessageType("error");
        type = "INVALID_JWT_RECEIVED/" + "Android";
        log.setMessageType("error");
        message.setTitle("JWT error");
        body.setMessage(user);
        body.setResponse(jwt);
        message.setBody(body);
        log.setTitle("JWT error");
        log.setMessage(message);
        uploadLogs(log, type,log.getAppId());
    }


	public static void logConnected(String url,String imei,String vin)
	{

        com.royalenfield.reprime.models.request.logs.Log log = new com.royalenfield.reprime.models.request.logs.Log();
        log.setEnvironment("Android");

        log.setAppId(getEnvironment());
        log.setTimestamp(dateFormater());
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
            log.setUser(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        else
            log.setUser("Guest");

        Message message = new Message();

		StringBuilder type =new StringBuilder("");
		Body body = new Body();
		type.append( "/"+BuildConfig.VERSION_NAME+"/"+BuildConfig.VERSION_CODE+"/Connected/");
		// type = "Misc";
		String baseurl = url;
		String   type1 = baseurl.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)", "");
		type1=type1.split("\\?")[0];
		if (type1.endsWith("/")) {
			type.append(type1.substring(0, type1.length() - 1));
		}
		else
			type.append(type1);
		body.setResponse("timeout");
		body.setUrl(url);
		body.setTimeDifference("60 seconds");
		body.setRequest(log.getUser()+","+imei+","+vin);

		message.setBody(body);
		log.setMessageType("error");
		log.setClassname("");
		message.setTitle("Websocket error");
		log.setTitle("Websocket error");
		type.append( "/Android");

		log.setApp_version(BuildConfig.VERSION_NAME+"("+BuildConfig.VERSION_CODE+")");
		log.setMessage(message);
		log.setBrand(Build.MANUFACTURER+" - "+Build.BRAND);
		log.setModel(Build.MODEL);
		log.setOs(Build.VERSION.SDK_INT+" - "+Build.VERSION.RELEASE);
		int[] speed=getNetworkSpeed();
		if(speed!=null&&speed.length>1)
			log.setNetworkStrength("Download Speed(KBPS)- "+speed[0]+" Upload Speed(KBPS)- "+speed[1]);
		Log.e("RES", new Gson().toJson(log));
		Log.e("TYPE>>>>>>>>>>>>>>>>>", type.toString());
		uploadLogs(log, type.toString(),log.getAppId());
	}



    public static void signInWithFirebaseCustomToken(String token, FirebaseAuthListner firebaseAuthListner) {
        if (REApplication.getInstance().mFirebaseAuth != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser() != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())) {
            firebaseAuthListner.onFirebaseAuthSuccess();

        } else {
            REApplication.getInstance().mFirebaseAuth.signInWithCustomToken(token)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = REApplication.getInstance().mFirebaseAuth.getCurrentUser();
                                Log.d(TAG, "signInWithEmail:success" + user.getUid() + "");
                                firebaseAuthListner.onFirebaseAuthSuccess();
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    logPrint(task.getException().toString(), REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail());
                                    firebaseAuthListner.onFirebaseAuthFailure(task.getException());
                                    Log.d(TAG, "onComplete: " + e.getMessage());
                                }
                                Log.d(TAG, task.getException().toString());
                            }
                        }
                    });

        }
    }


    public static void annonymasSignInWithFirebase(Activity activity, FirebaseAuthListner firebaseAuthListner) {
        if (REApplication.getInstance().mFirebaseAuth != null && REApplication.getInstance().mFirebaseAuth.getCurrentUser() != null) {
            firebaseAuthListner.onFirebaseAuthSuccess();
            //      firebaseAuthListner.onFirebaseAuthFailure(new Exception());
        } else {
            //  firebaseAuthListner.onFirebaseAuthFailure(new Exception());
            REApplication.getInstance().mFirebaseAuth.signInAnonymously()
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnnonymous:success");
                                FirebaseUser user = REApplication.getInstance().mFirebaseAuth.getCurrentUser();
                                firebaseAuthListner.onFirebaseAuthSuccess();
                            } else {
                                String user = "GUEST";
                                if (REApplication.getInstance().getUserLoginDetails() != null) {
                                    user = REApplication.getInstance().getUserLoginDetails().getData().getUser().getEmail();
                                }
                                logPrint(task.getException().toString(), user);

                                // If sign in fails, display a message to the user.
                                // Log.e(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(activity, "Authentication failed."+task.getException(),
//                                    Toast.LENGTH_SHORT).show();

                                firebaseAuthListner.onFirebaseAuthFailure(task.getException());
                            }
                        }
                    });
        }
    }


//    public static void getFirebaseRemoteConfig(Activity activity, FirebaseConfigListner firebaseConfigListner) {
//        REApplication.mFirebaseRemoteConfig.fetchAndActivate()
//                .addOnCompleteListener(activity, task -> {
//                    if (task.isSuccessful()) {
//                        boolean updated = task.getResult();
//                        Log.d(TAG, "Config params updated: " + updated);
//                        firebaseConfigListner.onRemoteConfigSucess();
//                    } else {
//                        FirebaseCrashlytics.getInstance().recordException(task.getException());
//                        try {
//                            throw task.getException();
//                        } catch (FirebaseRemoteConfigFetchThrottledException throttledException) {
//                            firebaseConfigListner.onRemoteConfigSucess();
//                        } catch (Exception e) {
//                            firebaseConfigListner.onRemoteConfigFailure();
//                        }
//                    }
//                });
//    }

    public static MobileappbaseURLs getMobileappbaseURLs() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getMobile_base_urls(), MobileappbaseURLs.class);
    }

    public static ConnectedKeys getConnectedFeatureKeys() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getConnected_keys(), ConnectedKeys.class);
    }

    public static GlobalValidations getGlobalValidations() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getGlobal_validations(), GlobalValidations.class);
    }

    public static AppSettings getAppSettings() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getApp_settings(), AppSettings.class);
    }


    public static FirebaseURLs getFirebaseURLs() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getFirebase_urls(), FirebaseURLs.class);
    }

    public static REKeys getREKeys() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getRe_keys(), REKeys.class);
    }

    public static REGoogleKeys getREGoogleKeys() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getGoogle_keys(), REGoogleKeys.class);
    }

    public static LoggerBaseUrls getLoggerappbaseURLs() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getLogger_service(), LoggerBaseUrls.class);
    }

    public static ConfigFeatures getConfigFeatures() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getConfigurable_features(), ConfigFeatures.class);
    }

    public static RidesKeys getRidesKeys() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getRides_keys(), RidesKeys.class);
    }

    public static NavigationKeys getNavigationKeys() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getNavigation_keys(), NavigationKeys.class);
    }

    public static FeatureList getFeatures() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getFeature_status(), FeatureList.class);
    }

    public static Map<String, Feature> getFeatureMap() {
        Map<String, Feature> retMap = new Gson().fromJson(
                REApplication.getInstance().getRemoteConfigData().getFeature_status(), new TypeToken<HashMap<String, Feature>>() {
                }.getType()
        );
        return retMap;
    }

    public static Integer getFlexibleAppUpdateTime() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getApp_update_interval(), Integer.class);
    }

    public static DiyVideos getDiyVideoList(String mVideosData) {
        return REApplication.mGson.fromJson(mVideosData, DiyVideos.class);
    }

    public static String isDummySlotsEnabled() {
        return REApplication.getInstance().getRemoteConfigData().getIsDummySlotsEnabled();
    }

    public static String servicebookingCutOffTime() {
        return REApplication.getInstance().getRemoteConfigData().getServicebookingCutOffTime();
    }
    public static AcmConfigurationKeys getAcmConfiguration() {
        return REApplication.mGson.fromJson(REApplication.getInstance().getRemoteConfigData().getAcm_configuration(), AcmConfigurationKeys.class);
    }

    public static String getTwoWheelerCountries() {
        return REApplication.getInstance().getRemoteConfigData().getTwoWheelerCountries();
    }

    public static String searchServiceCentreLWPURL() {
        return REApplication.getInstance().getRemoteConfigData().getServiceCentersLWPURL();
    }

//    public static void updateAPPIfNeeded(Activity activity, AppUpdateListner appUpdateListner) {
//        try {
//            int remoteConfigVC = Integer.valueOf(REApplication.mFirebaseRemoteConfig.getString(REConstants.RE_PRIME_APP_MINIMUM_ANDROID_VERSION));
//            int versionCode = BuildConfig.VERSION_CODE;
//            if (versionCode >= remoteConfigVC) {
//                appUpdateListner.onAppUpdateNoRequired();
//            } else {
//                appUpdateListner.onAppUpdateNoRequired();
//            }
//        } catch (Exception e) {
//            RELog.e(e);
//        }
//    }

    public static void sendUserToPlayStore(Activity activity) {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private static void startAppInfoScreen(Context mContext) {
        try {

            //   ActivityManager mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);

            // mActivityManager.killBackgroundProcesses(mContext.getPackageName());


        } catch (ActivityNotFoundException e) {
            RELog.e(e);
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            mContext.startActivity(intent);
        }
    }

    public static void showAppUpdateDialog(final Activity context, String message) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendUserToPlayStore(context);
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }


    public interface OnDialogButtonClickListener {
        void onOkCLick();
    }

    public interface OnDialogButtonsClickListener {
        void onOkCLick();

        void onCancelClick();
    }


    public interface OnTimerFinishedListener {
        void onFinished(int type);
    }

    public interface OnDialogClickListener {
        void onOkCLick();

        void onCancelCLick();
    }

    public static void showConfirmationDialog(final Activity activity, String title, String msg, OnDialogButtonClickListener onDeleteConfirmationListener) {
        final Dialog dialog = new Dialog(activity, R.style.blurTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_config_confimation_dialog);
        ((TextView) dialog.findViewById(R.id.title)).setText(title);
        ((TextView) dialog.findViewById(R.id.message)).setText(msg);
        dialog.findViewById(R.id.yes).setOnClickListener(v -> {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Settings");
            params.putString("eventAction", "Logout Confirmation");
            params.putString("eventLabel", "Yes clicked");
            REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
            dialog.dismiss();
            onDeleteConfirmationListener.onOkCLick();
        });
        dialog.findViewById(R.id.no).setOnClickListener(v -> {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Settings");
            params.putString("eventAction", "Logout Confirmation");
            params.putString("eventLabel", "No clicked");
            REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void showOnlyOfflineServicDialog(final Activity activity, String title, String msg, OnDialogButtonClickListener onOfflineContactDealerListener) {
        final Dialog dialog = new Dialog(activity, R.style.blurTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_alert_offlineservice);
        ((TextView) dialog.findViewById(R.id.tv_alertservice_offlne_title)).setText(title);
        ((TextView) dialog.findViewById(R.id.tv_alertservice_offlne_msg)).setText(msg);
        dialog.findViewById(R.id.imageViewCall).setOnClickListener(v -> {
            dialog.dismiss();
            onOfflineContactDealerListener.onOkCLick();
        });
        dialog.findViewById(R.id.textView_alert_okbutton).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static void showConnectionLostDialog(final Activity activity, OnDialogClickListener listener) {
        final Dialog dialog = new Dialog(activity, R.style.blurTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_alert_dialog);
        ((TextView) dialog.findViewById(R.id.title)).setText(activity.getString(R.string.customalert_title1));
        ((TextView) dialog.findViewById(R.id.message)).setText(activity.getString(R.string.sorry_please_try_again1));
        GradientDrawable cardView = (GradientDrawable) dialog.findViewById(R.id.card_view).getBackground();
        cardView.setStroke(1, ContextCompat.getColor(activity, R.color.orangey_red));
        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        yes.setText(activity.getString(R.string.tap_retry));
        no.setText(activity.getString(R.string.text_cancel));
        yes.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onOkCLick();
        });
        no.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onCancelCLick();
        });
        dialog.show();
    }


    /**
     * Gets the LatLng Bound from the the given routeTrace from the Bila library
     *
     * @param routeTrace {@link RouteTrace}
     * @return {@link LatLngBounds}
     */
    public static LatLngBounds getLatLngBounds(RouteTrace routeTrace) {
        /**create for loop/manual to add LatLng's to the LatLngBounds.Builder*/
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Polyline polyline = routeTrace.getPolyline();
        //A Polyline is a series of connected line segments. Polylines are useful to represent routes, paths,
        // or other connections between locations on the map.
        Iterable iterable = polyline.getPoints();
        for (Object points : iterable) {
            LatLng it = (LatLng) points;
            com.google.android.gms.maps.model.LatLng latLng = createLatLng(it);
            builder.include(latLng);
        }
        /**create the bounds from latlng Builder to set into map camera*/
        return builder.build();
    }


    /**
     * Creates the LatLng {@link com.google.android.gms.maps.model.LatLng} from Bila LatLng object
     *
     * @param latLng {@link LatLng}
     * @return {@link com.google.android.gms.maps.model.LatLng} google LatLng object.
     */
    private static com.google.android.gms.maps.model.LatLng createLatLng(LatLng latLng) {
        return new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(), latLng.getLongitude());
    }

    public static String getDurationInUnits(double mDuration) {
        String duration = null;
        if (mDuration < 60) {
            duration = String.format("%.1f", mDuration);
            duration = duration + " mins";
        } else if (mDuration >= 60) {
            mDuration = mDuration / 60;
            duration = String.format("%.1f", mDuration);
            duration = duration + " Hrs";
        }
        try {
            if (REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.KEY_COMMA_OR_POINT,
                    REConstants.KEY_POINT).equalsIgnoreCase(REConstants.KEY_COMMA)) {
                if (duration != null)
                    duration = duration.replace(".", ",");
            }
        } catch (PreferenceException e) {
            //  e.printStackTrace();
        }
        return duration;
    }


    public static void showErrorDialog(final Context context, String message, OnDialogButtonClickListener listener) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onOkCLick();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }

	public static void showErrorDialogWithTitle(final Context context, String title,String message, OnDialogButtonClickListener listener) {
		try {
			TextView alert_message, alert_ok;
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//Custom layout initialization
			View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
			alert_message = alertLayout.findViewById(R.id.textView_alert_message);
			TextView txtAlert=alertLayout.findViewById(R.id.textView_alert_title);
			//setting message for alert dialog
			alert_message.setText(message);
			txtAlert.setText(title);
			alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			// this is set the view from XML inside AlertDialog
			alert.setView(alertLayout);
			// allows cancel of AlertDialog on click of back button and outside touch
			alert.setCancelable(false);
			final AlertDialog dialog = alert.create();
			dialog.show();
			//This is for ok click which dismisses the dialog
			alert_ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (listener != null)
						listener.onOkCLick();
				}
			});
		} catch (Exception e) {
			RELog.e(e);
		}
	}


	public static void showErrorDialogWithTwoButtons(final Context context, boolean showCancel, String message, String btnPositive, String btnNegetive, OnDialogButtonsClickListener listener) {
        try {
            TextView alert_message, alert_ok, alert_cancel;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            alert_cancel = alertLayout.findViewById(R.id.textView_alert_cancel_button);
            if (showCancel)
                alert_cancel.setVisibility(View.VISIBLE);
            alert_ok.setText(btnPositive);
            alert_cancel.setText(btnNegetive);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onOkCLick();
                }
            });
            alert_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onCancelClick();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }



    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || CommonUtils.isRooted(REApplication.getAppContext());
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            if ((line = in.readLine()) != null)
                return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public static void showErrorDialogVO(final Context context, String message) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    //REApplication.getInstance().setComingFromVehicleOnboarding(true);
                    //startActivity(new Intent(UploadDocumentActivity.this, REHomeActivity.class));
                    //finish();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    public static String dateFormater() {
        Calendar calendar = Calendar.getInstance();
        //   TimeZone tz = TimeZone.getDefault();
        // calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss zzz", Locale.getDefault());
        java.util.Date currenTimeZone = new java.util.Date((long) calendar.getTimeInMillis());
        return sdf.format(currenTimeZone);
    }

    /**
     * Common method which returns JWT token
     *
     * @return : String
     */
    public static String getJwtToken() {
        String jwtToken = null;
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            jwtToken = REApplication.getInstance().getUserTokenDetails();
        }
        return jwtToken;
    }


    /**
     * Common method which returns TBT Athentication Headers
     *
     * @return : String
     */
    public static String getTBTAuthHeaders() {
        String authKey_azure = null;
        //String devToken = mNotifPreference.getString(REConstants.DEVICE_TOKEN, "");
        //String guidPhone = getGuid() + getPhoneNumber();
        try {
            authKey_azure = REPreference.getInstance().getString(REApplication.getAppContext(), "Azure_Key");
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        if (authKey_azure != null) {
            tbtAuthHeaders = getJwtToken() + "####" + authKey_azure + "####" + getUserId() + "####" + "true";
        } else {
            tbtAuthHeaders = "";
        }
        return tbtAuthHeaders;
    }

    /**
     * Common method which returns User ID
     *
     * @return : String
     */
    public static String getUserId() {
        String userId = null;
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            userId = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
        }
        return userId;
    }

    /**
     * Common method which returns User Phone
     *
     * @return : String
     */
    public static String getPhoneNumber() {
        String phone = "";
        if (REApplication.getInstance().getUserLoginDetails() != null) {
            phone = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone();
        }
        return phone;
    }


    /**
     * Common method which returns User GUID
     *
     * @return : String
     */
    public static String getGuid() {
        String guid = "";
        if (REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId() != null) {
            guid = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
        }
        return guid;
    }

    /**
     * Prevent any view from clicking multiple times by the user
     *
     * @param view
     */
    public static void preventMultipleClick(final View view, int delayMillis) {
        view.setEnabled(false);
        view.postDelayed(() -> view.setEnabled(true), delayMillis);
    }

    /**
     * Change the color of the drawable in advised way.
     *
     * @param mContext
     * @param resId
     * @param tintColor
     */
    public static void modifyDrawableColor(Context mContext, int resId, int tintColor) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, resId);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, tintColor);
    }

    public static String getItemFromStringIndex(String aString, String regex, int aIndex) {
        String[] aList = aString.split(regex);
        return aList[aIndex];
    }

    public static String getNavigationError(SearchError searchError) {
        if (searchError != null && searchError.name().equals(REConstants.TBT_API_UNAUTHORIZED)) {
            return REApplication.getAppContext().getString(R.string.error_nav_unauthorized);
        } else {
            return REApplication.getAppContext().getString(R.string.sorry_please_try_again);
        }
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }


        return r.nextInt((max - min) + 1) + min;
    }

    public static void showToastMsg(Context mContext, String msg) {
        //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getFormattedTime(String millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(millis) * 1000);
        return formatter.format(cal.getTimeInMillis());

    }

    public static String getMonth(String millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(millis) * 1000);
        return formatter.format(cal.getTimeInMillis()).toUpperCase();
    }

    public static String getDate(String millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(millis) * 1000);
        return formatter.format(cal.getTimeInMillis());
    }

    public static String getTimeInHr(Long time) {


        return String.valueOf((int) (time/ 3600));
    }

    public static String getTimeInMin(Long time) {
        return String.valueOf((int) (time % 3600) / 60);
    }
    public static void countDownTimer(Fragment fragment,int type, OnTimerFinishedListener finishedListener) {
        long timer = 60000;/* milliSeconds*/
        countDownTimer = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
                Log.e("TICK",millisUntilFinished+"");
            }

            public void onFinish() {
                //mTextField.setText("done!");
                if (fragment != null && fragment.isVisible()) {
                    if (((REBaseFragment) fragment).isLoaderShowing()) {
                        ((REBaseFragment) fragment).hideLoading();
                        finishedListener.onFinished(type);
                    }
                }
                countDownTimer=null;
            }
        }.start();
    }

//    public static void countDownTimer(Fragment fragment, long milis1) {
//      long  milis = 15000;
//        countDownTimer = new CountDownTimer(milis, 1000) {
//            public void onTick(long millisUntilFinished) {
//                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//                //here you can have your logic to set text to edittext
//            }
//
//            public void onFinish() {
//                //mTextField.setText("done!");
//                if (fragment != null && fragment.isVisible()) {
//                    //((REBaseFragment) fragment).hideLoading();
//
//                    if (fragment instanceof HealthAlertFragment) {
//                        ((HealthAlertFragment) fragment).hideLoading();
//                        ((HealthAlertFragment) fragment).showConnectionLostDialog();
//                    } else if (fragment instanceof TripListingFragment) {
//                        ((TripListingFragment) fragment).hideLoading();
//                        ((TripListingFragment) fragment).showConnectionLostDialog();
//                    }
//                }
//            }
//
//        }.start();
//    }

    public static String parseDateTo_ddMMMyyyy(String time, String inputpattern) {
        String inputPattern = inputpattern;
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            RELog.e(e);
        }
        return str;
    }

    public static void showMessageDialog(final Context context, String message, OnDialogButtonsClickListener onClickListener) {
        try {
            TextView alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onClickListener.onOkCLick();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }
    }
    public static void getCountryData() {
        List<CountryModel> data = null;
        try {
            Gson g = new Gson();
            final CountryListModel modelData = g.fromJson(REUtils.loadJSONFromAsset(REApplication.getAppContext(), "country_list.json"), CountryListModel.class);

            data = REApplication.getInstance().getCountryList();
            if (data == null || data.size() == 0 || data.size() != modelData.getCountry().size()) {

                //  Log.e("Local Country", "Local country set");
                REApplication.getInstance().setCountryList(modelData.getCountry());
                REPreference.getInstance().putString(REApplication.getAppContext(), COUNTRY_URL_KEY, null);
            }

            FirestoreManager.getInstance().getCountryDataFromAzure();
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    public static boolean isValidPassword(String passStr) {
        if (passStr != null && passStr.length() >= 6)
            return true;
        else
            return false;
    }


    public static String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        RELog.d("", "ANDROID_ID : " + androidId);
        return androidId;
    }

    public static String getManufacturerName() {
        return android.os.Build.MANUFACTURER;
    }

    public static void showForgetConfirmationDialog(final Activity activity, String title, String msg, OnDialogButtonClickListener onDeleteConfirmationListener) {
        final Dialog dialog = new Dialog(activity, R.style.blurTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_config_confimation_dialog);
        ((TextView) dialog.findViewById(R.id.title)).setText(title);
        ((TextView) dialog.findViewById(R.id.message)).setText(msg);
        Button btnYes = dialog.findViewById(R.id.yes);
        Button btnNo = dialog.findViewById(R.id.no);
        btnYes.setText(activity.getResources().getString(R.string.confirm));
        btnNo.setText(activity.getResources().getString(R.string.cancel));
        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            onDeleteConfirmationListener.onOkCLick();
        });
        btnNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public static String removeLeadingZeroes(String str) {
        String strPattern = "^0+(?!$)";
        str = str.replaceAll(strPattern, "");
        return str;
    }


    public static String getDeviceModel() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }


    public static void showPopupDialog(Context context, String message,OnDialogButtonClickListener listener) {
        TextView messageView;
        try{
           LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.info_popup, null);
        messageView = alertLayout.findViewById(R.id.message);
            //setting message for alert dialog
        messageView.setText(message);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);

            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            final AlertDialog dialog = alert.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.show();
            //This is for ok click which dismisses the dialog
            dialog.findViewById(R.id.img_cross).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onOkCLick();
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            RELog.e(e);
        }


//        Dialog dialog = new Dialog(activity, R.style.blurTheme); // Context, this, etc.
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.info_popup);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//        dialog.getWindow().setGravity(Gravity.CENTER);
//        TextView messageView = dialog.findViewById(R.id.message);
//        messageView.setText(message);
//        messageView.setMovementMethod(new ScrollingMovementMethod());
//        dialog.findViewById(R.id.img_cross).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
    }


//    public static void showPopupDialog(Activity activity,String msg){
//        try {
//            PopupWindow pw;
//            //We need to get the instance of the LayoutInflater, use the context of this activity
//            LayoutInflater inflater = (LayoutInflater) activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            //Inflate the view from a predefined XML layout
//            View layout = inflater.inflate(R.layout.info_popup, null);
//
//            // create a 300px width and 470px height PopupWindow
//            pw = new PopupWindow(layout,  ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//            pw.setFocusable(true);
//            setPopupWindowTouchModal(pw,false);
//            ViewGroup root = (ViewGroup)activity. getWindow().getDecorView().getRootView();
//            TextView txtMsg=layout.findViewById(R.id.message);
//            txtMsg.setText(msg);
//            txtMsg.setMovementMethod(new ScrollingMovementMethod());
//            layout.findViewById(R.id.img_cross).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pw.dismiss();
//                }
//            });
//            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    clearDim(root);
//                }
//            });
//            // display the popup in the center
//            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
//
//            applyDim(root, 0.6f);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        }
    public static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal)
    {
        if (null == popupWindow)
        {
            return;
        }
        Method method;
        try
        {
            method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }

    public static void startWorker(String data){
        Data workerData = new Data.Builder()
                .putString(ProvisioningWorker.TASK_DESC,data)
                .build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // you can add as many constraints as you want
                .build();
        final OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(ProvisioningWorker.class)
                        .setInputData(workerData)
                        .setConstraints(constraints)
                        .build();
        WorkManager.getInstance().enqueue(workRequest);
    }
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, processSecretKey(secret));
            return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, processSecretKey(secret));
            return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKeySpec processSecretKey(String mySecretKey) {
        MessageDigest sha = null;
        try {
            byte[] key = mySecretKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(java.util.Base64.getDecoder().decode(initVector));
            SecretKeySpec skeySpec = new SecretKeySpec(java.util.Base64.getDecoder().decode(key), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return java.util.Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(java.util.Base64.getDecoder().decode(initVector));
            SecretKeySpec skeySpec = new SecretKeySpec(java.util.Base64.getDecoder().decode(key), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }



    public static boolean isMilesUnitSelected() {
        try {
            String distanceUnit = REPreference.getInstance().getString(
                    REApplication.getAppContext(), REConstants.KEY_KM_OR_MILES,
                    REUtils.getAppSettings().getDistanceUnit());
            return distanceUnit.equalsIgnoreCase(REConstants.KEY_MILES);
        } catch (PreferenceException e) {
            //  e.printStackTrace();
            return false;
        }
    }

    public static boolean isPreferenceComma() {
        boolean value = false;
        try {
            if (REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.KEY_COMMA_OR_POINT,
                    REConstants.KEY_POINT).equalsIgnoreCase(REConstants.KEY_COMMA)) {
                value = true;
            }
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void gotoSignin(Context context) {
        REUtils.CHECK_VEHICLE_PENDING = false;
		REUtils.CHECK_VEHICLE_SYNCING_FAILED = false;
        FirestoreManager.getInstance().removeFirestoreEvents();
        try {
            REPreference.getInstance().removeAll(context.getApplicationContext());
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        REUtils.setFCMTokenSent(false);
        REUtils.navigateToSplash();
    }


    public static void sendResponseLog(String flow, String error, double total) {
        com.royalenfield.reprime.models.request.logs.Log log = new com.royalenfield.reprime.models.request.logs.Log();

        log.setEnvironment("Android");
        log.setIp(REUtils.getIP(REApplication.getAppContext()));
        log.setAppId(getEnvironment());
        log.setTimestamp(dateFormater());
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData() != null)
            log.setUser(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        else
            log.setUser("Guest");

        Message message = new Message();
        StringBuilder type = new StringBuilder("");
        Body body = new Body();
        type.append("/" + BuildConfig.VERSION_NAME + "/" + BuildConfig.VERSION_CODE + "/");
        type.append("UserJourney/" + flow + "/" + total + "/" + "Android");
        log.setMessageType("total time");
        body.setResponse(error);
        message.setBody(body);
        log.setMessage(message);
        uploadLogs(log, type.toString(), log.getAppId());
    }

    public static String getIP(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

public static String getRegion() {
	if (BuildConfig.FLAVOR.contains("Apac"))
		return "APAC";
	else if (BuildConfig.FLAVOR.contains("Rena"))
		return "RENA";
	else if (BuildConfig.FLAVOR.contains("Latm"))
		return "LATM";
	else if (BuildConfig.FLAVOR.contains("EU"))
		return "EU";
else
	return "IN";
}


	public static boolean isInternationalRegion(){
		if (BuildConfig.FLAVOR.contains("Apac")||BuildConfig.FLAVOR.contains("Rena")||BuildConfig.FLAVOR.contains("Latm")||BuildConfig.FLAVOR.contains("EU"))
		return true;
		else
			return false;
	}
}


package com.royalenfield.reprime.ui.wonderlust.interfaces;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.userdetails.UserProfileToReact;
import com.royalenfield.reprime.models.response.common.GetJWTModel;
import com.royalenfield.reprime.models.response.common.GetLatLongModel;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.utils.REUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.royalenfield.reprime.utils.RELog;

public class CommonWebAppInterface {
    private AppCompatActivity mContext;
    private WebView mWebView;
    private boolean flag = true;
    private WebviewListener webviewListener;
    /**
     * Instantiate the interface and set the context
     */
    public CommonWebAppInterface(AppCompatActivity c, WebView webView, WebviewListener webviewListener) {
        mContext = c;
        mWebView = webView;
        this.webviewListener = webviewListener;
    }

    @JavascriptInterface
    public void  sendJWTToken(){
        String JWT ;
        if (REUtils.isUserLoggedIn()) {
            JWT = REApplication.getInstance().getUserTokenDetails();
        } else {
            JWT = "";
        }
        mWebView.post(() -> mWebView.loadUrl("javascript:recieveJWTToken('" + JWT + "')"));
    }
    @JavascriptInterface
    public void  sendUserProfile(){
        if(REUtils.isUserLoggedIn()&&REUserModelStore.getInstance()!=null&&REUserModelStore.getInstance().getProfileData()!=null) {
            ProfileData profileData = REUserModelStore.getInstance().getProfileData();
            UserProfileToReact userProfile = new UserProfileToReact();
            userProfile.setFirstName(profileData.getfName());
            userProfile.setLastName(profileData.getlName());
            userProfile.setGender(profileData.getGender());
            userProfile.setDob(profileData.getDob());
            userProfile.setEmail(profileData.getContactDetails().getEmail());
            userProfile.setNumber(profileData.getContactDetails().getMobile().getPrimary().getNumber());
            userProfile.setCallingCode(profileData.getContactDetails().getMobile().getPrimary().getCallingCode());
            userProfile.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
            if(profileData.getAddressInfo().size()>0) {
                userProfile.setAddress1(profileData.getAddressInfo().get(0).getAddressInfo().getAddress1());
                userProfile.setAddress2(profileData.getAddressInfo().get(0).getAddressInfo().getAddress2());
                userProfile.setAddress3(profileData.getAddressInfo().get(0).getAddressInfo().getAddress3());
                userProfile.setCity(profileData.getAddressInfo().get(0).getAddressInfo().getCity());
                userProfile.setRegion(profileData.getAddressInfo().get(0).getAddressInfo().getRegion());
                userProfile.setRegionCode(profileData.getAddressInfo().get(0).getAddressInfo().getRegionCode());
                userProfile.setCountry(profileData.getAddressInfo().get(0).getAddressInfo().getCountry());
                userProfile.setCountryCode(profileData.getAddressInfo().get(0).getAddressInfo().getCountryCode());
                userProfile.setZip(profileData.getAddressInfo().get(0).getAddressInfo().getZip());
            }

            String stringUserProfile = null;
            ObjectMapper Obj = new ObjectMapper();
            try {
                stringUserProfile = Obj.writerWithDefaultPrettyPrinter().writeValueAsString(userProfile);
            } catch (IOException e) {
                RELog.e(e);
            }

            String finalStringUserProfile = stringUserProfile;
            mWebView.post(() -> {
                mWebView.loadUrl("javascript:recieveUserProfile('" + finalStringUserProfile + "')");
            });
        }
    }


    @JavascriptInterface
    public void logAnalyticsData(String type, String bundle) {
        REUtils.logGTMEvent(type, REUtils.bundleFromJson(bundle));
    }

    @JavascriptInterface
    public void requestUserLogin() {
        mWebView.post(() -> {
           if (REUtils.isUserLoggedIn()) {
                String phoneNumber =  REApplication.getInstance().getUserTokenDetails();
               mWebView.post(() -> mWebView.loadUrl("javascript:recieveJWTToken('" + phoneNumber + "')"));
            } else {
                showLoginActivity();
            }
        });
    }

    @JavascriptInterface
    public void sendLocationData() {
        GetLatLongModel getLatLongModel = new GetLatLongModel();
        getLatLongModel.setLat(REUserModelStore.getInstance().getLatitude());
        getLatLongModel.setLng(REUserModelStore.getInstance().getLongitude());

        // Creating Object of ObjectMapper define in Jakson Api
        ObjectMapper Obj = new ObjectMapper();
        try {
           String letlon= Obj.writerWithDefaultPrettyPrinter().writeValueAsString(getLatLongModel);
            mWebView.post(() -> mWebView.loadUrl("javascript:recieveLocationData('" +letlon + "')"));
        } catch (IOException e) {
            RELog.e(e);
        }
    }

    @JavascriptInterface
    public void dismissView() {
        if (flag) {
            flag = false;
            webviewListener.finishWebView("");
        }
    }

    @JavascriptInterface
    public void openLinkInBrowser(String url){
        if(mContext!=null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(browserIntent);
        }
    }









    @JavascriptInterface
    public void addToAppStorage(String key, String value) throws PreferenceException {
        REPreference.getInstance().putString(mContext, key, value);
    }

    @JavascriptInterface
    public void removeFromAppStorage(String key) {
        try { REPreference.getInstance().remove(mContext,key); } catch (PreferenceException e) { RELog.e(e); }

    }

    @JavascriptInterface
    public String getFromAppStorage(String key) {

        try {
            return REPreference.getInstance().getString(mContext, key);
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        return null;
    }


    @JavascriptInterface
    public void shareURLFromApp(String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, url);
        mContext.startActivity(Intent.createChooser(share, "Share Configuration!"));
    }



    /**
     * Show a toast from the web page
     */

    @JavascriptInterface
    public void notiftyToChangeOrientationTo(String type) {
        webviewListener.chnageOrientation(type);
    }

    @JavascriptInterface
    public void sendAppVersion() {
        mWebView.post(() -> mWebView.loadUrl("javascript:receiveAppVersion('" + BuildConfig.VERSION_NAME + "')"));
    }

    private void showLoginActivity() {
        webviewListener.showLoginActivity();
    }

    @JavascriptInterface
    public void navigateToScreen(String id) {
        webviewListener.navigateToScreen(id);
    }
    @JavascriptInterface
    public void showHideKeyboard(boolean showKeybaord) {
        webviewListener.showHideKeyboard(showKeybaord);
    }
}

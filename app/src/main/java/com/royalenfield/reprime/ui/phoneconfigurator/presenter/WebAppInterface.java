package com.royalenfield.reprime.ui.phoneconfigurator.presenter;

import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.userdetails.UserProfileToReact;
import com.royalenfield.reprime.models.response.common.GetJWTModel;
import com.royalenfield.reprime.models.response.common.GetLatLongModel;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.profile.ProfileData;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.phoneconfigurator.listener.ShowLoginListener;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;

import com.royalenfield.reprime.utils.RELog;


public class WebAppInterface {
    private AppCompatActivity mContext;
    private WebView mWebView;
    private boolean flag = true;
    private ShowLoginListener showLoginListener;
    private String variantId;

    /**
     * Instantiate the interface and set the context
     */
    public WebAppInterface(AppCompatActivity c, WebView webView, ShowLoginListener showLoginListener) {
        mContext = c;
        mWebView = webView;
        this.showLoginListener = showLoginListener;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void notifyLoadCompletion() {
        String JWT = null;
        if (REUtils.isUserLoggedIn()) {
            JWT = REApplication.getInstance().getUserTokenDetails();
        } else {
            JWT = "";
        }
        GetJWTModel getJWTModel = new GetJWTModel();
        getJWTModel.setJwtAccessToken(JWT);
        String stringJWT = null;
        ObjectMapper Obj = new ObjectMapper();
        try {
            stringJWT = Obj.writerWithDefaultPrettyPrinter().writeValueAsString(getJWTModel);
        } catch (IOException e) {
            RELog.e(e);
        }

        String finalStringJWT = stringJWT;
        mWebView.post(() -> {
            mWebView.clearCache(true);//Here you call the methond in other thread
            mWebView.loadUrl("javascript:sendBikeDetailsToConfigurator('" + finalStringJWT + "')");
        });
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
    public String getLocationFromDevice() {
        GetLatLongModel getLatLongModel = new GetLatLongModel();
        getLatLongModel.setLat(REUserModelStore.getInstance().getLatitude());
        getLatLongModel.setLng(REUserModelStore.getInstance().getLongitude());

        // Creating Object of ObjectMapper define in Jakson Api
        ObjectMapper Obj = new ObjectMapper();
        try {
            return Obj.writerWithDefaultPrettyPrinter().writeValueAsString(getLatLongModel);
        } catch (IOException e) {
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


    @JavascriptInterface
    public void isUserHaveRegisteredPhoneNumber() {
        mWebView.post(() -> {
            mWebView.clearCache(true);//Here you call the methond in other thread
            if (REUtils.isUserLoggedIn()) {
                String phoneNumber = REApplication.getInstance().getUserLoginDetails().getData().getUser().getPhone() + "|" + REApplication.getInstance().getUserTokenDetails();
                mWebView.loadUrl("javascript:fetchUserDetailsFromApp('" + phoneNumber + "')");
            } else {
                showLoginActivity();
            }
        });
    }

    @JavascriptInterface
    public void notiftyUserToSendToSavedScreen() {
        showLoginListener.gotoSavedConfigScreen();
//
//        PCUtils.showDialog(mContext);
//        new Handler().postDelayed(() -> {
//            //Do something after 2s
//        }, 2000);
    }


    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void dismissConfiguratorView(String error) {
        if (flag) {
            flag = false;
            showLoginListener.finishWebView(error);
        }
    }

    @JavascriptInterface
    public void notiftyToChangeOrientationTo(String type) {
        showLoginListener.chnageOrientation(type);
    }
//    @JavascriptInterface
//    public boolean isREPrimeBetaApp() {
//       return true;
//    }

    @JavascriptInterface
    public void logAnalyticsData(String type, String bundle) {
        REUtils.logGTMEvent(type, REUtils.bundleFromJson(bundle));
    }

    @JavascriptInterface
    public void notifyDeviceToOpenLink(String url){
        if(mContext!=null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(browserIntent);
        }
    }

    @JavascriptInterface
    public void getUserProfileFromNative() {
        ProfileData profileData = REUserModelStore.getInstance().getProfileData();
        UserProfileToReact userProfile=new UserProfileToReact();
        userProfile.setFirstName(profileData.getfName());
        userProfile.setLastName(profileData.getlName());
        userProfile.setGender(profileData.getGender());
        userProfile.setDob(profileData.getDob());
        userProfile.setEmail(profileData.getContactDetails().getEmail());
        userProfile.setNumber(profileData.getContactDetails().getMobile().getPrimary().getNumber());
        userProfile.setCallingCode(profileData.getContactDetails().getMobile().getPrimary().getCallingCode());
        userProfile.setGuid(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
        if(profileData.getAddressInfo()!=null&&profileData.getAddressInfo().size()>0) {
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
            mWebView.loadUrl("javascript:sendUserProfileToReact('" + finalStringUserProfile + "')");
        });

    }

    private void showLoginActivity() {
        showLoginListener.showLoginActivity();
    }
}

package com.royalenfield.reprime.ui.home.rides.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.*;

public class RidesLightWeightWebActivity extends REBaseActivity implements
        TitleBarView.OnNavigationIconClickListener {
    private String mTitle, mURL;
    public ValueCallback<Uri[]> uploadMessage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_register);
        getIntentData();
        initViews();
    }

    private void getData(String strRideType, int iPosition) {
        switch (strRideType) {
            case RIDE_TYPE_POPULAR:
                List<PopularRidesResponse> mPopularRidesResponse = RERidesModelStore.getInstance().getPopularRidesResponse();
                mTitle = mPopularRidesResponse.get(iPosition).getTitle();
                mURL = REUtils.getMobileappbaseURLs().getLightweightPageURL() +
                        mPopularRidesResponse.get(iPosition).getLightWeightPageUrl() + "?deviceType=android";
                break;
            case RIDE_TYPE_MARQUEE:
                List<MarqueeRidesResponse> mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
                mTitle = mMarqueeRidesResponse.get(iPosition).getTitle();
                if(REUtils.getMobileappbaseURLs().getLightweightPageURL()!=null && mMarqueeRidesResponse.get(iPosition).getLightWeightPageUrl() !=null) {
                    mURL = REUtils.getMobileappbaseURLs().getLightweightPageURL() +
                            mMarqueeRidesResponse.get(iPosition).getLightWeightPageUrl() + "?deviceType=android";
                } else {
                    mURL = "";
                }
                break;
            case OUR_WORLD_EVENTS:
                List<EventsResponse> mEventsResponse = RERidesModelStore.getInstance().getOurWorldEventsResponse();
                mTitle = mEventsResponse.get(iPosition).getTitle();
                mURL = REUtils.getMobileappbaseURLs().getLightweightPageURL() +
                        mEventsResponse.get(iPosition).getLightWeightPageUrl() + "?deviceType=android";
                break;
            case NEWS_OURWORLD:
                NewsResponse mNewsResponse = REUtils.getmNewsResponse();
                mTitle = REApplication.getAppContext().getResources().getString(R.string.news_title);
                mURL = REUtils.getMobileappbaseURLs().getLightweightPageURL() +
                        mNewsResponse.getLightWeightPageUrl();
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            String strRidesType = getIntent().getExtras().getString(RIDE_TYPE);
            int iPosition = getIntent().getExtras().getInt(RIDES_LIST_POSITION);
            if (strRidesType != null) {
                getData(strRidesType, iPosition);
            }
        }
    }

    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.popular_ride_header);
        mTitleBarView.bindData(this, R.drawable.icon_close, mTitle);

        final WebView webView = findViewById(R.id.webView_popular_details);
        RECustomJavascriptInterface reJavaScriptInterface = new RECustomJavascriptInterface(this);

        webView.addJavascriptInterface(reJavaScriptInterface, "CustomPrimeAppHandler");
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        if (!mURL.isEmpty())
        webView.loadUrl(mURL);
        webView.setWebChromeClient(new WebChromeClient() {

            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                try {
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(null);
                        uploadMessage = null;
                    }

                    try {
                        selectImage(RidesLightWeightWebActivity.this, RidesLightWeightWebActivity.this);
                        uploadMessage = filePathCallback;
                        return true;
                    } catch (ActivityNotFoundException e) {
                        uploadMessage = null;
                        Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (Exception e) {
                    uploadMessage = null;
                    return false;
                }
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    public  void selectImage(Context context, Activity activity) {
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
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(null);
                        uploadMessage = null;
                    }
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } catch (NullPointerException e) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                uploadMessage = null;
            }
            else if (requestCode == REConstants.REQUEST_CHECK_CAMERA && data != null && data.getExtras() != null) {
    //
    //        // Check that the response is a good one
                Uri[] results = null;
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = getImageUri(getApplicationContext(), photo);
                if (resultCode == Activity.RESULT_OK) {
    //            if (data == null) {
    //                // If there is not data, then we may have taken a photo
    //                if (mCameraPhotoPath != null) {
    //                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
    //                }
    //            } else {
    //                String dataString = data.getDataString();
    //                if (dataString != null) {
    //                    results = new Uri[]{Uri.parse(dataString)};
    //                }
    //            }
                    results=new Uri[]{imageUri};
                    if (uploadMessage == null)
                        return;
                    uploadMessage.onReceiveValue(results);
                    uploadMessage = null;
                }


                uploadMessage = null;
            }
            else {
                uploadMessage = null;
                uploadMessage.onReceiveValue(null);
                Toast.makeText(getApplicationContext(), "Failed to Upload File", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            if (uploadMessage != null)
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Image", null);
        return Uri.parse(path);
    }

    public class RECustomJavascriptInterface {
        Context mContext;

        RECustomJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onResponse(String toast) {
            Toast.makeText(mContext, "" + toast, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.slide_down);
    }
}

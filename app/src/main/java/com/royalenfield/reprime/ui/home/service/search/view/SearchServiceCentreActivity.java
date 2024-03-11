package com.royalenfield.reprime.ui.home.service.search.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.royalenfield.firebase.realTimeDatabase.FirebaseManager;
import com.royalenfield.firebase.realTimeDatabase.OnFirebaseDealerResponseCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.service.search.SearchActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class SearchServiceCentreActivity extends REBaseActivity implements
        TitleBarView.OnNavigationIconClickListener, OnFirebaseDealerResponseCallback {
    private String mTitle, mURL;
    ArrayList<DealerMasterResponse> filteredArrayList = new ArrayList<>();
    private DealerMasterResponse mDealerMasterResponse;
    private static final int SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_service_centre);
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Search Service Center");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        initViews();
    }

    private void initViews() {
        mURL = REUtils.searchServiceCentreLWPURL()+"?deviceType=android";
        TitleBarView mTitleBarView = findViewById(R.id.search_topbar);
        mTitleBarView.bindData(this, R.drawable.back_arrow, "SERVICE CENTRES");

        final WebView webView = findViewById(R.id.webView_searchservicecentre);
        SearchServiceCentreActivity.RECustomJavascriptInterface reJavaScriptInterface = new SearchServiceCentreActivity.RECustomJavascriptInterface(this);

        webView.addJavascriptInterface(reJavaScriptInterface, "CustomPrimeAppHandler");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.loadUrl(mURL);

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

    @Override
    public void onFirebaseDealersListSuccess(List<DealerMasterResponse> dealersResponseArrayList) {
        hideLoading();

    }

    @Override
    public void onFirebaseDealerListFailure(String message) {
     hideLoading();
    }

    @Override
    public void onFirebaseDealerDetailSuccess(DealerMasterResponse dealersDetailResponse) {

        hideLoading();
        if (filteredArrayList != null) {
            filteredArrayList.clear();
            filteredArrayList.add(dealersDetailResponse);

            Intent intent = new Intent();
            intent.putExtra("sc_index", "" + 0);
            intent.putParcelableArrayListExtra(REConstants.DEALERS_FILTERED_LIST_EXTRA, filteredArrayList);
            setResult(3, intent);
            finish();//finishing activity
        }
    }

    @Override
    public void onFirebaseDealerDetailFailure(String message) {
        hideLoading();
    }

    public class RECustomJavascriptInterface {
        Context mContext;

        RECustomJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onResponse(String toast) {
            sendData(toast);
        }

    }

    private void sendData(String toast) {
        try {
            JSONObject jsonObject = new JSONObject(toast);
            if (jsonObject.getString("dealerSource").equalsIgnoreCase(REConstants.DEALER_SOURCE_OFFLINE)) {
                String phone  = jsonObject.getString("phone");
                //event1
                Bundle params = new Bundle();
                params.putString("eventCategory", "Search Service Center");
                params.putString("eventAction", "Call Service Center click");
                params.putString("eventLabel", jsonObject.getString("dealerName"));
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, params);
                //event2
                params = new Bundle();
                params.putString("eventCategory", "Search Service Center");
                params.putString("eventAction", "Call Service Center click");
                params.putString("eventLabel","Service Center Offline");
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, params);
                showOfflineServiceDialog(phone);
            } else {
                Bundle params = new Bundle();
                params.putString("eventCategory", "Search Service Center");
                params.putString("eventAction", "Choose Service Center click");
                params.putString("eventLabel", jsonObject.getString("dealerName"));
                REUtils.logGTMEvent(REConstants.KEY_SERVICE_GTM, params);
                String branchId = jsonObject.getString("branchCode");
                if (branchId != null && !branchId.isEmpty()) {
                    showLoading();
                    //FirebaseManager.getInstance().prepareSingleDealerData(branchId, this);
                    FirebaseManager.getInstance().fetchDealerResponseFromFirebase(branchId, this);
                }
               /* mDealerMasterResponse = new DealerMasterResponse();
                mDealerMasterResponse.setDealerId(jsonObject.getString("dealerId"));
                mDealerMasterResponse.setPhone(jsonObject.getString("phone"));
                mDealerMasterResponse.setLongitude(Double.parseDouble(jsonObject.getString("longitude")));
                mDealerMasterResponse.setLatitude(Double.parseDouble(jsonObject.getString("latitude")));
                // mDealerMasterResponse.setLatlng(jsonObject.getString("latlng"));
                mDealerMasterResponse.setEmailId(jsonObject.getString("emailId"));
                mDealerMasterResponse.setDealerName(jsonObject.getString("dealerName"));
                mDealerMasterResponse.setCity(jsonObject.getString("city"));
                mDealerMasterResponse.setBranchName(jsonObject.getString("branchName"));
                mDealerMasterResponse.setAddressLine1(jsonObject.getString("addressLine1"));
                mDealerMasterResponse.setAddressLine2(jsonObject.getString("addressLine2"));
                // mDealerMasterResponse.setAddress(jsonObject.getString("address"));
                mDealerMasterResponse.setBranchCode(jsonObject.getString("branchCode"));
                // mDealerMasterResponse.setDistance(jsonObject.getString("distance"));
                mDealerMasterResponse.setDealerSource(jsonObject.getString("dealerSource"));
                mDealerMasterResponse.setIsService(jsonObject.getBoolean("isService"));
                filteredArrayList.add(mDealerMasterResponse);*/


            }

        } catch (JSONException e) {
            RELog.e(e);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    private void showOfflineServiceDialog(String mServiceCenterNo) {
        REUtils.showOnlyOfflineServicDialog(this, getString(R.string.text_servicecenter_offline_title), getString(R.string.text_servicecenter_offline_msg), new REUtils.OnDialogButtonClickListener() {
            @Override
            public void onOkCLick() {
                if (mServiceCenterNo != null && !mServiceCenterNo.equals("")) {
                    if ( getApplicationContext()!= null) {
                        checkAndRequestCallPermissions(getApplicationContext(),
                                SearchServiceCentreActivity.this, mServiceCenterNo,
                                SERVICE_CENTER_CALL_PERMISSIONS_REQUESTS, SearchServiceCentreActivity.this);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Contact number unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

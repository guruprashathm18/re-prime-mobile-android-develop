package com.royalenfield.reprime.ui.home.homescreen.view;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import com.android.assignment.util.view.BarcodeScannerActivity;
import com.google.rpc.Help;
import com.radaee.pdf.Page;
import com.radaee.util.RadaeePDFManager;
import com.radaee.util.RadaeePluginCallback;
import com.royalenfield.appIndexing.REAppIndexScreen;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.home.service.diy.activity.DoItYourSelfActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.Objects;

import static com.radaee.util.RadaeePDFManager.CPU_BASED_LAYOUT;
import static com.radaee.util.RadaeePDFManager.sHideAnnotButton;
import static com.radaee.util.RadaeePDFManager.sHideOutlineButton;
import static com.radaee.util.RadaeePDFManager.sHideSelectButton;
import static com.radaee.util.RadaeePDFManager.sHideShowBookmarksButton;
import static com.radaee.util.RadaeePDFManager.sHideViewModeButton;
import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SETTINGS_GTM;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;

public class HelpAndSupportActivity extends REBaseActivity implements View.OnClickListener {

    private Intent intent;
private   LinearLayout llOwner;
    RadaeePDFManager mPDFManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help_support);
         mPDFManager = new RadaeePDFManager(new RadaeePluginCallback.PDFReaderListener() {
             @Override
             public void willShowReader() {

             }

             @Override
             public void didShowReader() {

             }

             @Override
             public void willCloseReader() {

             }

             @Override
             public void didCloseReader() {

             }

             @Override
             public void didChangePage(int pageno) {

             }

             @Override
             public void didSearchTerm(String query, boolean found) {
                 Bundle params=new Bundle();
                 params.putString("eventCategory", "Owners Manual");
                 params.putString("eventAction", "Search");
                 params.putString("eventLabel", query);
                 REUtils.logGTMEvent(REConstants.KEY_OWNER_MANUAL_GTM, params);
             }

             @Override
             public void onBlankTapped(int pageno) {

             }

             @Override
             public void onAnnotTapped(Page.Annotation annot) {

             }

             @Override
             public void onDoubleTapped(int pageno, float x, float y) {

             }

             @Override
             public void onLongPressed(int pageno, float x, float y) {

             }

             @Override
             public void didOpenSearch() {
                 Bundle params=new Bundle();
                 params.putString("eventCategory", "Owners Manual");
                 params.putString("eventAction", "Search clicked");
                 REUtils.logGTMEvent(REConstants.KEY_OWNER_MANUAL_GTM, params);
                // Log.e("Search","open serach");
             }

             @Override
             public void didCloseSearch() {
                 Bundle params=new Bundle();
                 params.putString("eventCategory", "Owners Manual");
                 params.putString("eventAction", "Cancel search clicked");
                 REUtils.logGTMEvent(REConstants.KEY_OWNER_MANUAL_GTM, params);
             }
         });
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Support screen");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        initViews();

    }

    private void initViews() {
        TextView mTvHeading = findViewById(R.id.tv_actionbar_title);
        mTvHeading.setText(getString(R.string.support).toUpperCase());
        ImageView ivNavigation = findViewById(R.id.iv_navigation);
        ivNavigation.setImageResource(R.drawable.back_arrow);
        LinearLayout rSACalling = findViewById(R.id.ll_rsa);
        rSACalling.setOnClickListener(this);
        ImageButton mImageRSA = findViewById(R.id.image_rsa);
        mImageRSA.setOnClickListener(this);
        ivNavigation.setOnClickListener(this);
        findViewById(R.id.llDIYGuides).setOnClickListener(this);
        findViewById(R.id.llExtendedWarranty).setOnClickListener(this);
        findViewById(R.id.llCustomerCare).setOnClickListener(this);
       LinearLayout llSpare= findViewById(R.id.llSpareParts);
        llSpare.setVisibility(View.GONE);
       llSpare.setOnClickListener(this);
        findViewById(R.id.iv_title_profile_header_logo).setOnClickListener(this);
        TextView txtOwner=findViewById(R.id.txt_owner);
         llOwner=findViewById(R.id.ll_owner);
        llOwner.setOnClickListener(this);
        if(getIntent().getStringExtra("URL")!=null&&!getIntent().getStringExtra("URL").isEmpty()){
            llOwner.setEnabled(true);
            llOwner.setClickable(true);
            txtOwner.setAlpha(1f);
            if( getIntent().hasExtra("TYPE")&&getIntent().getSerializableExtra("TYPE")!=null&&getIntent().getSerializableExtra("TYPE").equals(REAppIndexScreen.OWNERS_MANUAL)){
                llOwner.performClick();
            }
        }
        else{
            llOwner.setEnabled(false);
            llOwner.setClickable(false);
            txtOwner.setAlpha(0.5f);

        }
        if(REApplication.getInstance().featureCountry !=null&&REApplication.getInstance().featureCountry.getShowSparesGenuinityCheck()!=null&&REApplication.getInstance().featureCountry.getShowSparesGenuinityCheck().equalsIgnoreCase(FEATURE_ENABLED)){
            llSpare.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_navigation) {
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        } else if (view.getId() == R.id.llDIYGuides) {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Settings");
            params.putString("eventAction", "DIY Videos clicked");
            REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
            callDIYMethod();
        } else if (view.getId() == R.id.llCustomerCare) {
            Bundle  params = new Bundle();
            params.putString("eventCategory", "Support");
            params.putString("eventAction", "Contact Us clicked");
            REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params);
            startExplicitActivity(CustomerCareActivity.class);
        }
        else if (view.getId() == R.id.llSpareParts) {
            Bundle  params = new Bundle();
            params.putString("eventCategory", "Support");
            params.putString("eventAction", "Spare Genuinity Check clicked");
            REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params);
            startExplicitActivity(BarcodeScannerActivity.class);
        }
        else if (view.getId() == R.id.ll_rsa || view.getId() == R.id.image_rsa) {
            Bundle  params = new Bundle();
            params.putString("eventCategory", "Support");
            params.putString("eventAction", "Roadside Assistance clicked");
            REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params);
            intent = new Intent(HelpAndSupportActivity.this, REWebViewActivity.class);
            intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, getResources().getString(R.string.road_side_assistance).toUpperCase());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        } else if (view.getId() == R.id.llExtendedWarranty) {
            Bundle  params = new Bundle();
            params.putString("eventCategory", "Support");
            params.putString("eventAction", "Ride Sure clicked");
            REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params);
            intent = new Intent(HelpAndSupportActivity.this, REWebViewActivity.class);
            intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, getResources().getString(R.string.text_extended_warranty).toUpperCase());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        } else if (view.getId() == R.id.iv_title_profile_header_logo) {
            REApplication.getInstance().setComingFromVehicleOnboarding(true);
            Intent intent = new Intent(this, REHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if (view.getId() == R.id.ll_owner) {
        //    String uriFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + URLUtil.guessFileName(getIntent().getStringExtra("URL"), null, null);
            Bundle  params = new Bundle();
            params.putString("eventCategory", "Support");
            params.putString("eventAction", "Owners Manual clicked");
            REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params);
            File file = new
                    File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),URLUtil.guessFileName(getIntent().getStringExtra("URL"), null, null));


         //  File file = new File(uriFile);
            Uri uriFileName = Uri.fromFile(file);
            String uriStr = uriFileName.toString();
          //  Toast.makeText(this,file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            if (Files.exists(file.toPath())||file.toPath().toFile().exists()) {
                new Handler().postDelayed(() -> {
                    Bundle paramsScr = new Bundle();
                    paramsScr.putString("screenname", "Owners Manual screen");
                    REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
                    mPDFManager.show(HelpAndSupportActivity.this, uriStr, "");
                    llOwner.setEnabled(true);
                },200);

            } else {
                downLoadFile(getIntent().getStringExtra("URL"));
                new Handler().postDelayed(() -> {
                    showLoading();
                    String http_link =getIntent().getStringExtra("URL");
                    Bundle paramsScr = new Bundle();
                    paramsScr.putString("screenname", "Owners Manual screen");
                    REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
                    mPDFManager.show(HelpAndSupportActivity.this, http_link, "");
                    llOwner.setEnabled(true);
                },200);
            }


            llOwner.setEnabled(false);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RadaeePDFManager.sHideRedoButton=true;
        RadaeePDFManager.sHideUndoButton=true;
        RadaeePDFManager.sHideOutlineButton=true;
        RadaeePDFManager.sHideMoreButton=true;
        RadaeePDFManager.sHideViewModeButton=true;
        RadaeePDFManager.sHideAddBookmarkButton=true;
        RadaeePDFManager.sHideRedoButton=true;
        sHideShowBookmarksButton=true;
        sHideViewModeButton=true;
        sHideOutlineButton=true;
        sHideSelectButton=true;
        sHideAnnotButton=true;
        mPDFManager.activateLicense(HelpAndSupportActivity.this,0,"Royal Enfield","dnait@royalenfield.com","B8W9KR-QBFJBT-XA4XYF-X3WJY8-8C61KE-HLB6QV");

    }

    private void callDIYMethod() {
        if (REUtils.isAppInstalled(REConstants.YOUTUBE_PACKAGENAME)) {
            startExplicitActivity(DoItYourSelfActivity.class);
        } else {
            REUtils.showErrorDialog(getApplicationContext(),
                    getApplicationContext().getResources().getString(R.string.diy_youtube_install_error));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideLoading();
    }

    /**
     * Starts the Create Ride activity.
     */
    private void startExplicitActivity(Class activity) {
        startActivity(new Intent(getApplicationContext(), activity));
        overridePendingTransition(R.anim.slide_in_right, 0);
    }
    @SuppressLint("InlinedApi")
    @Override
    protected void onDestroy() {
        com.radaee.pdf.Global.RemoveTmp();
        super.onDestroy();
    }

    private void downLoadFile(String url) {
//
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));

        request.setDescription("Downloading File...");
        request.setTitle(URLUtil.guessFileName(url,null,null));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(this,
                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                        url,null,null
                )
        );
        DownloadManager dm =
                (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(request);
        // webView.loadUrl(CommonWebAppInterface.getBase64StringFromBlobUrl(url));







    }

}

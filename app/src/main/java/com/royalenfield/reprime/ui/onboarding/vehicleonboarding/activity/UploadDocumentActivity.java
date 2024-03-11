package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.activity;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.web.vehicleonboarding.AddVehicleResponse;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.interactor.UploadDocInteractor;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter.UploadDocPresenter;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.utils.REAddVehicleUtils;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.views.UploadDocumentView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.InputStream;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.ui.onboarding.vehicleonboarding.utils.REAddVehicleUtils.getPath;


public class UploadDocumentActivity extends REBaseActivity implements View.OnClickListener, UploadDocumentView, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "UploadDocumentActivity";
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 5341;
    private static final int PICK_FILE_REQUEST_CODE = 5342;
    private static final long FILE_SIZE_LIMIT = 3000000;
    private ImageView ownshipDocLeftImg, ownshipDocRightImg, ownshipImg;
    private TextView ownshipDocTxt, errorOwnDocTxt;
    private Button submit, cancel;
    private RelativeLayout ownershipRL, kycRL;
    private UploadDocPresenter uploadDocPresenter;
    private boolean ownshipDocFlag = false;
    private File ownerShipUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        initViews();

        requestReadPermission();
        uploadDocPresenter = new UploadDocPresenter(this, new UploadDocInteractor());
    }

    private void initViews() {
        ownshipDocLeftImg = findViewById(R.id.owner_doc_left_img);
        ownshipDocRightImg = findViewById(R.id.ownship_doc_right_img);
        ownshipDocRightImg.setBackgroundResource(R.drawable.upload);
        ownshipImg = findViewById(R.id.ownership_img);

        ownshipDocTxt = findViewById(R.id.owner_doc_txt);
        errorOwnDocTxt = findViewById(R.id.error_owndoc);
        submit = findViewById(R.id.upload_doc_submit);
        submit.setOnClickListener(this);
        cancel = findViewById(R.id.upload_doc_cancel);
        cancel.setOnClickListener(this);
        ownershipRL = findViewById(R.id.ownership_rl);
        ownershipRL.setOnClickListener(this);
        kycRL = findViewById(R.id.kyc_rl);
        kycRL.setOnClickListener(this);
        disableSubmit();
    }

    private void enableSubmit() {
        if ( ownshipDocFlag) {
            submit.setEnabled(true);
            submit.setClickable(true);
            submit.setTextColor(getResources().getColor(R.color.white));
            submit.setBackgroundResource(R.drawable.enable_button_bg);
        } else {
            disableSubmit();
        }

    }

    private void disableSubmit() {
        submit.setTextColor(getResources().getColor(R.color.add_vehicle_screen_label_color));
        submit.setBackgroundResource(R.drawable.disable_button_bg);
        submit.setEnabled(false);
        submit.setClickable(false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        REApplication.getInstance().setComingFromVehicleOnboarding(true);
        startActivity(new Intent(this, REHomeActivity.class));
    }

    private boolean requestReadPermission() {
		String readImagePermission =  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ?Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

		if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(readImagePermission)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{readImagePermission}, MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        } else { //permission is automatically granted on sdk<23 upon installation

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // openFilePicker();

        } else {
            Toast.makeText(this, "Go to settings to enable permissions", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                REApplication.getInstance().setComingFromVehicleOnboarding(true);
                startActivity(new Intent(this, REHomeActivity.class));
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ownership_rl: {
                if(ownerShipUri!=null){
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Vehicle Onboarding");
                    params.putString("eventAction", "Edit Document");
                    params.putString("eventLabel", "RC document ");
                   REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                }else{
                    Bundle params = new Bundle();
                    params.putString("eventCategory", "Vehicle Onboarding");
                    params.putString("eventAction", "Upload Document");
                    params.putString("eventLabel", "RC document click");
                   REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
                }

                if(requestReadPermission()){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
                }else {
                    Toast.makeText(this, "Go to settings to enable permissions", Toast.LENGTH_LONG).show();
                }

            }

            break;

            case R.id.upload_doc_submit:

                Bundle params1 = new Bundle();
                params1.putString("eventCategory", "Vehicle Onboarding");
                params1.putString("eventAction", "Upload Document");
                params1.putString("eventLabel", "Submit");
                REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params1);
                showLoading();
                Gson gson = new Gson();
                AddVehicleResponse addVehicleResponse = gson.fromJson(getIntent().getStringExtra("addVehicleResponseJson"), AddVehicleResponse.class);
                uploadDocPresenter.submitDocs(ownerShipUri, addVehicleResponse);

                //for stub
//                Intent intent = new Intent(this, OnboardingResultActivity.class);
//                intent.putExtra("sender", "UploadDocumentActivity"); // getText() SHOULD NOT be static!!!
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
//                finish();

                break;
            case R.id.upload_doc_cancel:
                Bundle params2 = new Bundle();
                params2.putString("eventCategory", "Vehicle Onboarding");
                params2.putString("eventAction", "Upload Document");
                params2.putString("eventLabel", "Cancel");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params2);
                REApplication.getInstance().setComingFromVehicleOnboarding(true);
                startActivity(new Intent(this, REHomeActivity.class));
                finish();
                break;

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onOwnerShipFile(data);
            }
        }
    }

    private void onOwnerShipFile(Intent data) {
        try {
            Uri uri = data.getData();
            String mimeType = getContentResolver().getType(uri);
            String filename;
            long fileSize;
            String path;
            if (mimeType == null) {
                path = getPath(this, uri);
                if (path == null) {
                    filename = FilenameUtils.getName(uri.toString());
                } else {
                    File file = new File(path);
                    filename = file.getName();
                }
            } else {
                Uri returnUri = data.getData();
                Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                fileSize = returnCursor.getLong(sizeIndex);
                uploadDocPresenter.validateOwnershipDoc(filename, fileSize, mimeType, uri);

                //get real path
                //String id = DocumentsContract.getDocumentId(uri);
                String id = REAddVehicleUtils.RC_DOCUMENT+getFileExt(filename);
                InputStream inputStream = getContentResolver().openInputStream(uri);
                ownerShipUri = new File(getCacheDir().getAbsolutePath()+"/"+id);
                REAddVehicleUtils.writeFile(inputStream, ownerShipUri);
                //ownerShipUri=new File(getPath(this, uri));
            }

        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private String getFileExt(String url){
        String ext="";
        if(url.lastIndexOf(".") != -1) {
            ext = url.substring(url.lastIndexOf(".")+1);
        }
        return "."+ext;
    }


    @Override
    public void onSuccessOwnDoc(Uri uri, String fileName, boolean flag) {
        ownshipDocFlag = true;
        errorOwnDocTxt.setVisibility(View.GONE);
        ownshipDocLeftImg.setVisibility(View.VISIBLE);
        ownshipDocRightImg.setBackgroundResource(R.drawable.group_4);
        if (flag) {
            ownshipImg.setVisibility(View.VISIBLE);
            Picasso.get().load(uri.toString()).into(ownshipImg);
        } else {
            //nothing
            ownshipImg.setVisibility(View.GONE);
        }
        ownshipDocTxt.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(this, R.font.montserrat_bold));
        ownshipDocTxt.setTextColor(getResources().getColor(R.color.white));
        ownshipDocTxt.setTextSize(18);
        ownshipDocTxt.setText(fileName);
        enableSubmit();


    }


    @Override
    public void onOwnershipFileSizeError() {
        ownshipDocFlag = false;
        disableSubmit();
        ownshipDocLeftImg.setVisibility(View.GONE);
        errorOwnDocTxt.setVisibility(View.VISIBLE);
        ownshipDocRightImg.setBackgroundResource(R.drawable.upload);
        errorOwnDocTxt.setText(getResources().getString(R.string.max_onboard_filesize));
        ownshipImg.setVisibility(View.GONE);
        ownshipDocTxt.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(this, R.font.montserrat_regular));
        ownshipDocTxt.setTextColor(getResources().getColor(R.color.add_vehicle_screen_label_color));
        ownshipDocTxt.setTextSize(16);
        ownshipDocTxt.setText(getResources().getString(R.string.ownership_document));

    }

    @Override
    public void onOwnershipFileFormatError() {
        ownshipDocFlag = false;
        disableSubmit();
        errorOwnDocTxt.setVisibility(View.VISIBLE);
        ownshipDocLeftImg.setVisibility(View.GONE);
        ownshipDocRightImg.setBackgroundResource(R.drawable.upload);
        errorOwnDocTxt.setText(getResources().getString(R.string.error_on_board_file_type));
        ownshipImg.setVisibility(View.GONE);
        ownshipDocTxt.setTypeface(androidx.core.content.res.ResourcesCompat.getFont(this, R.font.montserrat_regular));
        ownshipDocTxt.setTextColor(getResources().getColor(R.color.add_vehicle_screen_label_color));
        ownshipDocTxt.setTextSize(16);
        ownshipDocTxt.setText(getResources().getString(R.string.ownership_document));
    }


    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialogVO(this,errorMessage);
    }

    @Override
    public void onSuccessResponse() {
        hideLoading();
        Intent intent = new Intent(this, OnboardingResultActivity.class);
        intent.putExtra("sender", "UploadDocumentActivity"); // getText() SHOULD NOT be static!!!
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        finish();
    }




    @Override
    public void onFailureResponse(String message) {
        hideLoading();
        if(message==null||message.length()==0)
        REUtils.showErrorDialogVO(this,getResources().getString(R.string.upload_error));
        else
            REUtils.showErrorDialogVO(this,message);

    }


}

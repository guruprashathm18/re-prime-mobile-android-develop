package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.rides.AddCheckInRequest;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.presenter.CheckInRidePresenter;
import com.royalenfield.reprime.ui.riderprofile.activity.RideImageCropActivity;
import com.royalenfield.reprime.ui.riderprofile.interactor.AddCheckInInteractor;
import com.royalenfield.reprime.ui.riderprofile.interactor.ICheckINRideCreator;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.FileInputStream;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;


public class AddCheckInActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, ICheckINRideCreator.MainView {

    private static final int REQUEST_CODE_IMAGE_CROP = 1095;
    private ImageView mIvCheckInImage;
    private FrameLayout mIvFrameHotelImage;
    private ImageView mCameraAddImage;
    private TextView mTvUploadPhoto;
    private RadioGroup mRgPlaceCategory;
    private EditText etCheckInDesc;
    private Button mBtAddCheckInButton;
    private String filePath, mRideId, placeName, strCheckInCategory, strAddress;
    private CheckInRidePresenter mCheckInPresenter;
    private ImageView mEditCheckInImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check_in);
        initViews();
        mCheckInPresenter = new CheckInRidePresenter(this, new AddCheckInInteractor());
    }

    private void initViews() {
        Intent intent = getIntent();
        placeName = intent.getStringExtra("CheckInPlaceName");
        strAddress = intent.getStringExtra("CheckInAddress");
        mRideId = intent.getStringExtra(REConstants.SEARCH_ACTIVITY_RIDE_ID);

        TitleBarView mTitleBarView = findViewById(R.id.tb_add_check_in);
        mTitleBarView.bindData(this, R.drawable.back_arrow, "");

        mCameraAddImage = findViewById(R.id.camera_image);
        mCameraAddImage.setOnClickListener(this);
        etCheckInDesc = findViewById(R.id.edit_text_add_description);
        mIvCheckInImage = findViewById(R.id.check_in_image);
        mIvFrameHotelImage = findViewById(R.id.frame_hotel_image);
        mRgPlaceCategory = findViewById(R.id.rd_place_category);
        mRgPlaceCategory.setOnCheckedChangeListener(this);
        mBtAddCheckInButton = findViewById(R.id.button_add_check_in);
        mBtAddCheckInButton.setOnClickListener(this);
        mTvUploadPhoto = findViewById(R.id.tv_upload_photo);
        TextView mTvPlaceName = findViewById(R.id.tv_place_name);
        mTvPlaceName.setText(placeName);
        mEditCheckInImage = findViewById(R.id.ivEditIcon);
        mEditCheckInImage.setOnClickListener(this);
        //set default radiobutton value
        mRgPlaceCategory.check(R.id.rd_sightseeing);
        getSelectedRadioGroupValue();
        //radio group selected listener value
        mRgPlaceCategory.setOnCheckedChangeListener((group, checkedId) -> {
            getSelectedRadioGroupValue();
        });
    }

    private void getSelectedRadioGroupValue() {
        int selectedId = mRgPlaceCategory.getCheckedRadioButtonId();

        // find the radio button by returned id
        RadioButton rdButtonSelected = findViewById(selectedId);
        strCheckInCategory = rdButtonSelected.getText().toString();
    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_image:
            case R.id.ivEditIcon:
                REUtils.selectImage(AddCheckInActivity.this, AddCheckInActivity.this);
                break;
            case R.id.button_add_check_in:
                AddCheckInRequest addCheckInRequest = new AddCheckInRequest();
                addCheckInRequest.setRideId(mRideId);
                addCheckInRequest.setCheckInPlaceName(placeName);
                addCheckInRequest.setCheckInDescription(etCheckInDesc.getText().toString());
                addCheckInRequest.setCheckInCategory(strCheckInCategory);
                addCheckInRequest.setAddress(strAddress);
                if (mRideId != null && mRideId.length() > 0) {
                    mCheckInPresenter.checkInRideDetail(addCheckInRequest, filePath);
                } else {
                    Toast.makeText(this, "Ride Id not present", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mRgPlaceCategory.getCheckedRadioButtonId() != -1) {
            //one of the radio button is checked
            //enable the check_in button
            mBtAddCheckInButton.setEnabled(true);
            mBtAddCheckInButton.setBackgroundResource(R.drawable.button_border_enable);
            mBtAddCheckInButton.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                Uri imageUri = data.getData();
                filePath = REUtils.getRealPathFromURI(imageUri, AddCheckInActivity.this);
                Intent cropIntent = new Intent(getApplicationContext(), RideImageCropActivity.class);
                cropIntent.putExtra("imageuri", imageUri);
                cropIntent.putExtra("filepath", filePath);
                cropIntent.putExtra("imagemode", "gallery");
                startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
                // onCaptureImageResult(BitmapFactory.decodeFile(filePath));
            } else if (requestCode == REConstants.REQUEST_CHECK_CAMERA && data != null && data.getExtras() != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (thumbnail != null) {
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = REUtils.getImageUri(getApplicationContext(), thumbnail);
                    filePath = REUtils.getRealPathFromURI(tempUri, AddCheckInActivity.this);
                    Intent cropIntent = new Intent(getApplicationContext(), RideImageCropActivity.class);
                    cropIntent.putExtra("imageuri", tempUri);
                    cropIntent.putExtra("filepath", filePath);
                    cropIntent.putExtra("thumbnail", thumbnail);
                    cropIntent.putExtra("imagemode", "camera");
                    startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
                    //onCaptureImageResult(thumbnail);
                }
            } else if (requestCode == REQUEST_CODE_IMAGE_CROP && data != null) {
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = null;
                    String filename = data.getStringExtra("BitmapImage");
                    filePath = data.getStringExtra("filepathcropped");

                    try {
                        FileInputStream is = this.openFileInput(filename);
                        bmp = BitmapFactory.decodeStream(is);
                        is.close();
                        if (bmp != null)
                            onCaptureImageResult(bmp);
                    } catch (Exception e) {
                        RELog.e(e);
                    }
                }
                if (resultCode == RESULT_CANCELED) {
                    if (!Objects.requireNonNull(data.getStringExtra("filepathcropped")).isEmpty()) {
                        filePath = data.getStringExtra("filepathcropped");
                    } else {
                        filePath = "";
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            RELog.e(e.getMessage());
        } catch (NullPointerException e) {
            RELog.e(e.getMessage());
        } catch (Exception e) {
            RELog.e(e.getMessage());
        }
    }


    /**
     * @param thumbnail : Bitmap
     */
    private void onCaptureImageResult(Bitmap thumbnail) {
        if (thumbnail != null) {
            mIvCheckInImage.setImageBitmap(thumbnail);
            mIvFrameHotelImage.setForeground(getDrawable(R.drawable.foreground_no_gradient));
            mCameraAddImage.setVisibility(View.INVISIBLE);
            mTvUploadPhoto.setVisibility(View.INVISIBLE);
            mEditCheckInImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCheckInRideSuccess() {
        try {
            finish();
            startActivity(new Intent(getApplicationContext(), InRideActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public void onResponseFailure(String throwable) {
        Toast.makeText(this, "" + throwable, Toast.LENGTH_LONG).show();
    }
}

package com.royalenfield.reprime.ui.home.rides.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.*;

import androidx.core.content.res.ResourcesCompat;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.request.web.rides.CreateRideRequest;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.custom.FragmentFrameHolder;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.CreateRideTitleInfo;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.CustomRideCategory;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.RideCategory;
import com.royalenfield.reprime.ui.home.rides.fragment.createride.TerrainFragment;
import com.royalenfield.reprime.ui.home.rides.interactor.CreateRideInteractor;
import com.royalenfield.reprime.ui.home.rides.presenter.CreateRidePresenter;
import com.royalenfield.reprime.ui.home.rides.views.ICreateRideView;
import com.royalenfield.reprime.ui.riderprofile.activity.RideImageCropActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REUtils;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.TEXT_SPLASH_DESC_SUCCESS;

/*
 * It contains the details of the ride to be shared with other people.
 * */

public class ShareYourRideActivity extends REBaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, ICreateRideView, CustomRideCategory.OnFragmentInteractionListener,
        TerrainFragment.OnFragmentInteractionListener, RideCategory.OnFragmentInteractionListener,
        CreateRideTitleInfo.OnFragmentInteractionListener {

    private static final int REQUEST_CODE_IMAGE_CROP = 1095;
    private static final int TERMS_CONDITIONS = 10;
    private String rideEndDate;
    private ImageView mCameraAddImage, mShareRideImage, mEditRideImage;
    private FrameLayout mFrameShareRideImage;
    private TextView mTvUploadRidePhoto, mRideRoute, mRideDate, mRideEndDate, mRideStartTime, mRideEndTime, mRideDuration;
    private ArrayList<WayPointsData> mGetRideRoute = new ArrayList<>();
    private String mGetRideDate = "";
    private String mGetRideEndDate = "";
    private String mGetRideStartTime = "";
    private String mGetRideEndTime = "";
    private EditText etHashTags;
    private int mGetRideDuration = 0;
    private String mStrDistance;
    private Button mNextButton;
    private String mRideType, mTerrainType, mDifficultyLevel, mRideCategory, mRideTitle, mRideDesc;
    private CreateRidePresenter mCreateRidePresenter;
    private CreateRideRequest mCreateRideRequest;
    private String mImageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_your_ride);
        initViews();
        mCreateRidePresenter = new CreateRidePresenter(this, new CreateRideInteractor());
    }

    @Override
    protected void onDestroy() {
        mCreateRidePresenter.onDestroy();
        super.onDestroy();
    }

    /*
     * Initialising the views
     */

    public void initViews() {
        try {
            Intent intent = getIntent();
            mGetRideRoute = intent.getParcelableArrayListExtra("RideRoute");
            mGetRideDate = intent.getStringExtra("StartDate");
            mGetRideEndDate = intent.getStringExtra("EndDate");
            mGetRideStartTime = intent.getStringExtra("StartTime");
            mGetRideEndTime = intent.getStringExtra("EndTime");
            mGetRideDuration = intent.getIntExtra("Duration", 1);
            mStrDistance = intent.getStringExtra("distance");
            mRideTitle = intent.getStringExtra("ride_title");
            mRideDesc = intent.getStringExtra("ride_desc");
            mCreateRideRequest = (CreateRideRequest) intent.getSerializableExtra("ride_data");
            mImageFilePath = intent.getStringExtra("ride_image");
            TitleBarView mTitleBarView = findViewById(R.id.tb_share_your_ride);
            mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.label_create_ride));
            ImageView mNavCloseBtn = findViewById(R.id.iv_navigation);
            mNavCloseBtn.setOnClickListener(this);

            mCameraAddImage = findViewById(R.id.camera_add_image);
            mCameraAddImage.setOnClickListener(this);
            mEditRideImage = findViewById(R.id.ivEditIcon);
            mEditRideImage.setOnClickListener(this);
            mShareRideImage = findViewById(R.id.share_ride_image);
            mFrameShareRideImage = findViewById(R.id.frame_share_ride_image);
            mTvUploadRidePhoto = findViewById(R.id.tv_upload_ride_photo);
            mNextButton = findViewById(R.id.button_next);
            mNextButton.setOnClickListener(this);

            etHashTags = findViewById(R.id.et_ride_tags);
            etHashTags.setText(intent.getStringExtra("ride_hastags"));
            mRideRoute = findViewById(R.id.ride_route);
            mRideDate = findViewById(R.id.ride_date);
            mRideEndDate = findViewById(R.id.ride_end_date);
            mRideStartTime = findViewById(R.id.ride_start_time);
            mRideEndTime = findViewById(R.id.ride_end_time);

            mRideDuration = findViewById(R.id.ride_duration);
            if (mImageFilePath != null && mImageFilePath.length() > 0) {
                onCaptureImageResult(BitmapFactory.decodeFile(mImageFilePath));
            }
            loadRideInfoFragment();
            loadLevelDifficultyFragment();
            loadRideTypeCategoryFragment();
            loadTerrainCategoryFragment();
            loadRideCategoryFragment();
            setRideDetails();
            disableCreateRideSubmit();
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private void loadRideInfoFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_ride_info);
        CreateRideTitleInfo rideHeader;
        if (mCreateRideRequest != null) {
            rideHeader = CreateRideTitleInfo.newInstance(mCreateRideRequest.getRideName(),
                    mCreateRideRequest.getRideDetails());
        } else {
            rideHeader = CreateRideTitleInfo.newInstance();
        }
        mDealerRidesFrame.loadFragment(this, rideHeader, null);
    }

    private void loadLevelDifficultyFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_level_of_difficulty);
        //Initializing DealerRidesList
        String frgTitle = getResources().getString(R.string.level_of_difficulty);
        CustomRideCategory rideCategory = CustomRideCategory.newInstance("level_of_difficulty",
                frgTitle, "Easy", "Medium", "Hard");
        mDealerRidesFrame.loadFragment(this, rideCategory, null);
        if (mCreateRideRequest != null) {
            rideCategory.categoryIsLike(mCreateRideRequest.getDifficulty());
        }
    }

    private void loadRideTypeCategoryFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_ride_type);
        //Initializing DealerRidesList
        String frgTitle = getResources().getString(R.string.ride_type);
        CustomRideCategory rideCategory = CustomRideCategory.newInstance("ride_type", frgTitle, "Solo", "Private", "Public");
        mDealerRidesFrame.loadFragment(this, rideCategory, null);
        if (mCreateRideRequest != null) {
            rideCategory.typeIsLike(mCreateRideRequest.getRideType());
        }
    }

    private void loadTerrainCategoryFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_type_of_terrain);
        //Initializing DealerRidesList
        TerrainFragment terrainFragment = TerrainFragment.newInstance();
        mDealerRidesFrame.loadFragment(this, terrainFragment, null);
        String selectedCategory;
        if (mCreateRideRequest != null && mCreateRideRequest.getTerrainType() != null) {
            if (mCreateRideRequest.getTerrainType().contains("-")) {
                selectedCategory = mCreateRideRequest.getTerrainType().replace("-", "");
            } else {
                selectedCategory = mCreateRideRequest.getTerrainType();
            }
            terrainFragment.categoryIsLike(selectedCategory);
        }
    }

    private void loadRideCategoryFragment() {
        //Views
        FragmentFrameHolder mDealerRidesFrame = findViewById(R.id.layout_ride_category);
        //Initializing RideCategory
        RideCategory rideCategory = RideCategory.newInstance(mCreateRideRequest != null ?
                mCreateRideRequest.getRideCategory() : "");
        mDealerRidesFrame.loadFragment(this, rideCategory, null);
    }

    /*
     * Setting the ride details.
     */

    public void setRideDetails() {
        Typeface regular = ResourcesCompat.getFont(this, R.font.montserrat_light);
        Typeface bold = ResourcesCompat.getFont(this, R.font.montserrat_semibold);
        String nextArrow = " > ";
        SpannableStringBuilder route = new SpannableStringBuilder();


        String source = mGetRideRoute.get(0).getPlaceName();
        System.out.println("route1: " + source);
        SpannableStringBuilder spanSource = new SpannableStringBuilder(source);
        //if waypoint is selected make the source to be bold else normal
        if (mGetRideRoute.size() > 2) {
            spanSource.setSpan((new RECustomTyperFaceSpan(bold)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spanSource.setSpan((new RECustomTyperFaceSpan(regular)), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanSource.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        route.append(spanSource);

        for (int iSize = 1; iSize < mGetRideRoute.size() - 1; iSize++) {
            String middle = mGetRideRoute.get(iSize).getPlaceName();
            SpannableStringBuilder spanMiddle = new SpannableStringBuilder(nextArrow + middle);
            spanMiddle.setSpan((new RECustomTyperFaceSpan(regular)), 0, spanMiddle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanMiddle.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spanMiddle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            route.append(spanMiddle);
        }

        String destination = mGetRideRoute.get(mGetRideRoute.size() - 1).getPlaceName();
        SpannableStringBuilder spanDestination = new SpannableStringBuilder(nextArrow + destination);
        spanDestination.setSpan((new RECustomTyperFaceSpan(regular)), 0, nextArrow.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //if waypoint is selected make the destination to be bold else normal
        if (mGetRideRoute.size() > 2) {
            spanDestination.setSpan((new RECustomTyperFaceSpan(bold)), nextArrow.length(), nextArrow.length() + destination.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spanDestination.setSpan((new RECustomTyperFaceSpan(regular)), nextArrow.length(), nextArrow.length() + destination.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanDestination.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), nextArrow.length(), nextArrow.length() + destination.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        route.append(spanDestination);
        mRideRoute.setText(route);
        setStartAndEndDate();
        mRideStartTime.setVisibility(View.GONE);
        mRideStartTime.setText(mGetRideStartTime);
        mRideEndTime.setVisibility(View.GONE);
        mRideEndTime.setText(mGetRideEndTime);

        if (mGetRideDuration <= 1) {
            mRideDuration.setText(mGetRideDuration + " " + getResources().getString(R.string.text_day));
        } else {
            mRideDuration.setText(mGetRideDuration + " " + getResources().getString(R.string.text_days));
        }
    }

    private void setStartAndEndDate() {
        int rideDurationForDate = mGetRideDuration + 1;
        if (mGetRideDate != null && !mGetRideDate.isEmpty() && mGetRideEndDate != null && !mGetRideEndDate.isEmpty()) {
            String[] monthName = getApplicationContext().getResources().getStringArray(R.array.month_name_arrays);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date journeyDate = new java.sql.Date(sdf.parse(mGetRideDate).getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(journeyDate);
                String mRideStartDate = String.format(getResources().getString(R.string.time_date_name), cal.get(Calendar.DAY_OF_MONTH) +
                        REUtils.getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));

                cal.add(Calendar.DAY_OF_MONTH, rideDurationForDate - 1);
                Date journeyEndDate = new java.sql.Date(sdf.parse(mGetRideEndDate).getTime());
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(journeyEndDate);
                String endDate = String.format(getResources().getString(R.string.time_date_name), cal1.get(Calendar.DAY_OF_MONTH) +
                        REUtils.getOrdinalFor(cal1.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal1.get(Calendar.MONTH)] + " " + cal1.get(Calendar.YEAR));

//                rideEndDate = sdf.format(cal.getTime());
//
//                String endDate = String.format(getResources().getString(R.string.time_date_name), cal.get(Calendar.DAY_OF_MONTH) +
//                        REUtils.getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));
//                if (rideDurationForDate > 1) {
//                    mRideDate.setText(mRideStartDate + " - " + endDate);
//                } else {
                mRideDate.setText(mRideStartDate + " " + mGetRideStartTime);
                mRideEndDate.setText(endDate + " " + mGetRideEndTime);
//                }
            } catch (Exception e) {
                RELog.e(e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_navigation:
                sendCreateRideData();
                break;

            case R.id.camera_add_image:
            case R.id.ivEditIcon:
                REUtils.selectImage(ShareYourRideActivity.this, ShareYourRideActivity.this);
                break;

            case R.id.button_next:
                if (!checkMandatoryFields()) {
                    createRideRequest();
                    mCreateRidePresenter.createRide(mCreateRideRequest, mImageFilePath);
                }
                break;
        }
    }

    private void removeIntentData() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ride_data", new CreateRideRequest());
        resultIntent.putExtra("ride_hastags", "");
        resultIntent.putExtra("ride_image", "");
        resultIntent.putExtra("ride_title", "");
        resultIntent.putExtra("ride_desc", "");
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    private boolean checkMandatoryFields() {
        boolean isPresent = isEmpty(mRideTitle);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride Title", Toast.LENGTH_LONG).show();
            return isPresent;
        }

        isPresent = isEmpty(mRideDesc);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride Description", Toast.LENGTH_LONG).show();
            return isPresent;
        }
        isPresent = isEmpty(mRideCategory);
        if (isPresent) {
            Toast.makeText(this, "Please Select Ride Category", Toast.LENGTH_LONG).show();
            return isPresent;
        }
        isPresent = isEmpty(etHashTags.getText().toString());
        if (isPresent) {
            Toast.makeText(this, "Please select hash tags", Toast.LENGTH_LONG).show();
        } else {
            String[] hashTagArray = etHashTags.getText().toString().trim().split(",");
            List<String> hashTags = Arrays.asList(hashTagArray);
            Pattern p = Pattern.compile("[^a-zA-Z]");
            for (String strhashTag : hashTags) {
                if (strhashTag.startsWith("#") && strhashTag.length() > 1 && !p.matcher(strhashTag.substring(1)).find()) {
                    isPresent = false;
                } else {
                    isPresent = true;
                    break;
                }
            }
            if (isPresent) {
                Toast.makeText(this, "Please Enter Hash Tags Starts with # followed by string", Toast.LENGTH_LONG).show();
            }
        }
        return isPresent;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().isEmpty();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                Uri imageUri = data.getData();
                mImageFilePath = REUtils.getRealPathFromURI(imageUri, ShareYourRideActivity.this);
                Intent cropIntent = new Intent(getApplicationContext(), RideImageCropActivity.class);
                cropIntent.putExtra("imageuri", imageUri);
                cropIntent.putExtra("filepath", mImageFilePath);
                cropIntent.putExtra("imagemode", "gallery");
                startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
                //onCaptureImageResult(BitmapFactory.decodeFile(mImageFilePath));
            } else if (requestCode == REConstants.REQUEST_CHECK_CAMERA && data != null && data.getExtras() != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (thumbnail != null) {
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = REUtils.getImageUri(getApplicationContext(), thumbnail);
                    mImageFilePath = REUtils.getRealPathFromURI(tempUri, ShareYourRideActivity.this);
                    Intent cropIntent = new Intent(getApplicationContext(), RideImageCropActivity.class);
                    cropIntent.putExtra("imageuri", tempUri);
                    cropIntent.putExtra("filepath", mImageFilePath);
                    cropIntent.putExtra("thumbnail", thumbnail);
                    cropIntent.putExtra("imagemode", "camera");
                    startActivityForResult(cropIntent, REQUEST_CODE_IMAGE_CROP);
                    // onCaptureImageResult(thumbnail);
                }
            } else if (requestCode == REQUEST_CODE_IMAGE_CROP && data != null) {
                if (resultCode == RESULT_OK) {
                    Bitmap bmp = null;
                    String filename = data.getStringExtra("BitmapImage");
                    mImageFilePath = data.getStringExtra("filepathcropped");

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
                        mImageFilePath = data.getStringExtra("filepathcropped");
                    } else {
                        mImageFilePath = "";
                    }
                }
            } else if (requestCode == TERMS_CONDITIONS && data != null) {
                if (resultCode == RESULT_OK && data.getExtras() != null) {
                    String value = data.getExtras().getString("terms_cons");
                    if (value != null && value.equalsIgnoreCase("accepted")) {
                        enableCreateRideSubmit();
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
            mShareRideImage.setImageBitmap(thumbnail);
            mFrameShareRideImage.setForeground(getDrawable(R.drawable.foreground_no_gradient));
            mCameraAddImage.setVisibility(View.INVISIBLE);
            mTvUploadRidePhoto.setVisibility(View.INVISIBLE);
            mEditRideImage.setVisibility(View.VISIBLE);
            /*//Convert to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            mImageByteArray = stream.toByteArray();*/
        }
    }

    private void createRideRequest() {

        mCreateRideRequest = new CreateRideRequest();
        String userId = REUtils.getUserId();

        double slat = mGetRideRoute.get(0).getLatitude();
        double slon = mGetRideRoute.get(0).getLongitude();
        ArrayList<Double> startPointCoordinates = new ArrayList<>();
        startPointCoordinates.add(slat);
        startPointCoordinates.add(slon);
        double elat = mGetRideRoute.get(mGetRideRoute.size() - 1).getLatitude();
        double elon = mGetRideRoute.get(mGetRideRoute.size() - 1).getLongitude();
        ArrayList<Double> endPointCoordinates = new ArrayList<>();
        endPointCoordinates.add(elat);
        endPointCoordinates.add(elon);

        //Creating temporary way points
        ArrayList<WayPointsData> wayPointArrayList = new ArrayList<>();
        if (mGetRideRoute.size() > 2) {
            for (int iCount = 1; iCount <= mGetRideRoute.size() - 2; iCount++) {
                WayPointsData waypoint = new WayPointsData(mGetRideRoute.get(iCount).getLatitude(),
                        mGetRideRoute.get(iCount).getLongitude(), mGetRideRoute.get(iCount).getPlaceName());
                wayPointArrayList.add(waypoint);
            }
        }

        //Hash tags
        String[] hashTagArray = etHashTags.getText().toString().split(",");
        List<String> hashTags = Arrays.asList(hashTagArray);

        //Creating RideRequest
        mCreateRideRequest.setStartPoint(mGetRideRoute.get(0).getPlaceName());
        mCreateRideRequest.setStartPointCoordinates(startPointCoordinates);
        mCreateRideRequest.setEndPoint(mGetRideRoute.get(mGetRideRoute.size() - 1).getPlaceName());
        mCreateRideRequest.setEndPointCoordinates(endPointCoordinates);
        mCreateRideRequest.setCreatedBy(userId);
        mCreateRideRequest.setRideName(mRideTitle);
        mCreateRideRequest.setDifficulty(mDifficultyLevel);
        mCreateRideRequest.setStartDate(mGetRideDate);
        mCreateRideRequest.setEndDate(mGetRideEndDate);
        mCreateRideRequest.setDurationInDays(String.valueOf(mGetRideDuration));
        if (mStrDistance != null && !mStrDistance.isEmpty() && !mStrDistance.equals("null")) {
            mCreateRideRequest.setTotalDistance(String.valueOf(Math.round(Float.valueOf(mStrDistance))));
        }
        mCreateRideRequest.setRideDetails(mRideDesc);
        mCreateRideRequest.setTerrainType(mTerrainType);
        mCreateRideRequest.setStartTime(mGetRideStartTime);
        mCreateRideRequest.setEndTime(mGetRideEndTime);

        mCreateRideRequest.setRideStatus("UPCOMING");
        mCreateRideRequest.setRideCategory(mRideCategory);
        mCreateRideRequest.setRideType(mRideType);
        mCreateRideRequest.setWaypoints(wayPointArrayList);
        mCreateRideRequest.setHashTags(hashTags);


    }

    private void disableCreateRideSubmit() {
        mNextButton.setEnabled(false);
        mNextButton.setBackgroundResource(R.drawable.button_border_disable);
        mNextButton.setTextColor(getResources().getColor(R.color.white_50));
    }

    private void enableCreateRideSubmit() {
        mNextButton.setBackgroundResource(R.drawable.button_border_enable);
        mNextButton.setTextColor(getResources().getColor(R.color.white));
        mNextButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        sendCreateRideData();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCreateRideSuccess() {
        hideLoading();
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        overridePendingTransition(0, R.anim.slide_out_right);
        Intent rideStatus = new Intent(this, JoinRideStatusActivity.class);
        rideStatus.putExtra(TEXT_SPLASH_DESC_SUCCESS, getResources().getString(R.string.label_ride_info));
        rideStatus.putExtra("msg", "home");
        startActivity(rideStatus);
    }

    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    public void onFragmentInteraction(String strType, String uri) {
        if (strType.equalsIgnoreCase("level_of_difficulty")) {
            if (uri.equalsIgnoreCase("Hard")) {
                mDifficultyLevel = "HARD";
            } else {
                mDifficultyLevel = uri.toUpperCase();
            }
        } else {
            mRideType = uri.toUpperCase();
        }
    }

    @Override
    public void onTerrainFragmentInteraction(String uri) {
        mTerrainType = uri.toUpperCase();
        if (mTerrainType.contains(" ")) {
            mTerrainType = mTerrainType.replace(" ", "-");
        }
    }

    @Override
    public void onFragmentRideCategory(String uri) {
        mRideCategory = uri.toUpperCase();
    }

    public void clickTermsAndConditions(View view) {
        boolean isEmpty = checkMandatoryFields();
        if (!isEmpty) {
            Intent termsIntent = new Intent(this, RideTermsAndConditions.class);
            startActivityForResult(termsIntent, TERMS_CONDITIONS);
            overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
        }
    }

    @Override
    public void onCreateRideHeaderDetails(String title, String desc) {
        mRideTitle = title;
        mRideDesc = desc;
    }

    public void onModifyCreateRide(View view) {
        sendCreateRideData();
    }

    private void sendCreateRideData() {
        try {
            hideKeyboard();
            createRideRequest();
            if (mCreateRideRequest != null) {
                mCreateRideRequest.setWaypoints(null);
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ride_data", mCreateRideRequest);
            resultIntent.putExtra("ride_hastags", etHashTags.getText().toString());
            resultIntent.putExtra("ride_image", mImageFilePath);
            resultIntent.putExtra("ride_title", mRideTitle);
            resultIntent.putExtra("ride_desc", mRideDesc);
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        } catch (IllegalStateException e) {
            RELog.e(e);
        } catch (RuntimeException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }
}

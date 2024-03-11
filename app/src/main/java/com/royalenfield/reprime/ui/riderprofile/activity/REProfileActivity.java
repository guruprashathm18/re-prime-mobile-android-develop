package com.royalenfield.reprime.ui.riderprofile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.royalenfield.firebase.fireStore.OnFirestoreVehicleDataMappingCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.web.booking.BookingResponse;
import com.royalenfield.reprime.ui.home.homescreen.adapter.HomePagerAdapter;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.riderprofile.fragment.BookingFragment;
import com.royalenfield.reprime.ui.riderprofile.fragment.MotorCycleProfileFragment;
import com.royalenfield.reprime.ui.riderprofile.fragment.REProfileFragment;
import com.royalenfield.reprime.ui.riderprofile.interactor.BookingIntrector;
import com.royalenfield.reprime.ui.riderprofile.presenter.BookingPresenter;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;
import com.royalenfield.reprime.ui.riderprofile.views.BookingView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.INPUT_PROFILE_ACTIVITY;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;

public class REProfileActivity extends REBaseActivity implements View.OnClickListener, MotorCycleProfileFragment.OnMotorcycleFragmentInteractionListener, OnFirestoreVehicleDataMappingCallback, BookingView {
    private final String TAG = REProfileActivity.class.getSimpleName();
    private ImageView[] ivDots;
    private LinearLayout mTabIndicatorLinear;
    private TextView tvStartTab, tvMiddleTab, tvEndTab;
    private RiderProfileViewPager mViewPager;
    private HomePagerAdapter mPagerAdapter;
    private ArrayList<String> mTabFeatureList = new ArrayList<>();
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private ImageView mREHeader;
    public boolean navigateFromDashboard = false;
    public BookingResponse bookingResponse;
    public BookingResponse bookingGMAResponse;
    public String bookingError;
    public String bookingGMAError;
    private static WeakReference<REProfileActivity> wrActivity = null;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, REProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprofile_home);

        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Profile screen");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        bookingError = getResources().getString(R.string.no_booking);
        bookingGMAError = getResources().getString(R.string.no_booking);
        wrActivity = new WeakReference<REProfileActivity>(this);
        navigateFromDashboard = getIntent().hasExtra("NAVIGATE_FROM");
        //  showLoading();
        //  BookingPresenter   intrector = new BookingPresenter(this, new BookingIntrector());
        // intrector.getBookingData(REUserModelStore.getInstance().getUserId());
        onFirebaseHTMLPageUrls();
        if(REUtils.getFeatures().getDefaultFeatures()!=null&&REUtils.getFeatures().getDefaultFeatures().getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED))
            mapVehileData();
        else
            initView();
    }

    private void mapVehileData() {
        List<VehicleDataModel> list = REServiceSharedPreference.getVehicleData(this);
        if (list == null || list.size() <= 0) {
            initView();
        } else {
            if (list.get(0).getDisplayName() == null) {
                showLoading();
                REUtils.getModelNameFromModelCode(this, list, 0);
            } else {
                initView();
            }

        }
    }

    public void initView() {
        showLoading();
        try {
            new Handler().postDelayed(() -> {
				BookingPresenter intrector = new BookingPresenter(REProfileActivity.this, new BookingIntrector());
				if (REApplication.getInstance().getUserLoginDetails() != null){
					intrector.getBookingData(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
				intrector.getGMABookingData(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
			}
            }, 200);
        }
        catch (Exception e){
            hideLoading();
          //  e.printStackTrace();
        }
        createTabList();
        initializeViews();
        setupRiderProfileViewPager();
    }

    @Override
    public void onVehicleMappingSuccess(List<VehicleDataModel> vehicleModel, int position) {
        if (vehicleModel.size() - 1 == position) {
            REServiceSharedPreference.saveVehicleData(this, vehicleModel);
            hideLoading();
            initView();
        } else {
            position = position + 1;
            REUtils.getModelNameFromModelCode(this, vehicleModel, position);
        }
    }

    /**
     * Manually Creates the FeatureTab list for the profile screen.
     */
    private void createTabList() {
        mTabFeatureList.add("Profile");
        mTabFeatureList.add("Notifications");
        mTabFeatureList.add("Testing");
    }

    /**
     * Initializes the view ids.
     */
    private void initializeViews() {
        ImageView mCloseProfileIcon = findViewById(R.id.iv_close_profile_icon);
        mCloseProfileIcon.setOnClickListener(this);
        ImageView mSettingsIcon = findViewById(R.id.iv_settings_icon);
        mSettingsIcon.setOnClickListener(this);
        mREHeader = findViewById(R.id.iv_title_profile_header_logo);
        mREHeader.setOnClickListener(this);


    }

    /**
     * Set-Up the Profile View Pager.
     */
    private void setupRiderProfileViewPager() {

        initViewPagerAdapter();
    }

    /**
     * Initialize the View Pager Adapter.
     */
    private void initViewPagerAdapter() {


        final Activity activity = wrActivity.get();
        if (activity != null && !activity.isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.view_pager_profile_home, new REProfileFragment());
            transaction.commitAllowingStateLoss();
        }

    }


    /**
     * Add the Fragments to the The fragment list which all will be added to view pager.
     *
     * @return
     */
    private ArrayList<Fragment> getFragments() {
        mFragmentList.add(new REProfileFragment());
        return mFragmentList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_settings_icon:
                Bundle  params = new Bundle();
                params.putString("eventCategory", "User Profile");
                params.putString("eventAction", "Settings clicked");
                REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, INPUT_PROFILE_ACTIVITY);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;
            case R.id.iv_close_profile_icon:
                if (REApplication.getInstance().isComingFromVehicleOnboarding()) {
                    startActivity(new Intent(this, REHomeActivity.class));
                } else {
                    onBackPressed();
                }

                overridePendingTransition(0, R.anim.slide_down);
                break;
            case R.id.iv_title_profile_header_logo:

                break;
        }
    }


    @Override
    public void onMotorcycleFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_down);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                //Setting address when coming back from AddLocationActivity
                if (requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                    Uri imageUri = data.getData();
                    //  String path = REUtils.getRealPathFromURI(imageUri, REProfileActivity.this);
                    Bitmap bmp = handleSamplingAndRotationBitmap(this, imageUri);
                    if (bmp.equals("")) {
                        Bitmap bitmap = REUtils.getBitmap(this, imageUri);
                        ((REProfileFragment) getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)).setProfileImage(bitmap);

                    }
                    ((REProfileFragment) getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)).setProfileImage(bmp);

                    //  onCaptureImageResult(decodeImage(path));
                } else if (requestCode == REConstants.REQUEST_CHECK_CAMERA_PROFILE) {
                    ((REProfileFragment) getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)).setProfileImageSRC(REUtils.imageFilePath);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REConstants.REQUEST_CHECK_GALLERY:
                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted
//                    REUtils.launchGallery(REProfileActivity.this);
//                } else {
//                    // permission denied
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//                    permissionHandling( Manifest.permission.READ_MEDIA_IMAGES,"", "", "", false);
//                else
//						permissionHandling(Manifest.permission.WRITE_EXTERNAL_STORAGE, "", "", "", false);

				//}
                break;
            case REConstants.REQUEST_CHECK_CAMERA_PROFILE:
                // If request is cancelled, the result arrays are empty.
				String readImagePermission =(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)? Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.WRITE_EXTERNAL_STORAGE;

				Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(readImagePermission, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(permissions[i], grantResults[i]);
                    }
                }


				if (perms != null && perms.size() > 0 && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
						perms.get(readImagePermission) == PackageManager.PERMISSION_GRANTED) {
					// permission was granted
					REUtils.launchCamera(REProfileActivity.this, true);
				} else {
					permissionHandling(Manifest.permission.CAMERA, readImagePermission, "", "", false);
				}
        }
    }

    /**
     * This method is responsible for solving the rotation issue if exist. Also scale the images to
     * 1024x1024 resolution
     *
     * @param context       The current context
     * @param selectedImage The Image URI
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        height = width * 2 / 3;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final float height = options.outHeight;
        final float width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2f;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public void bookingSuccess(BookingResponse response) {
        hideLoading();
		bookingError=getString(R.string.no_booking);
        bookingResponse = response;
        if(getSupportFragmentManager()!=null&&  getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)!=null)
        ((REProfileFragment) getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)).realTmeListener();
    }

    @Override
    public void bookingFailure(String error) {
        hideLoading();
        bookingError = error;
    }

    @Override
    public void bookingGMASuccess(BookingResponse response) {
        hideLoading();
		bookingGMAError=getString(R.string.no_booking);
        bookingGMAResponse = response;
        if(getSupportFragmentManager()!=null&&  getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)!=null)
            ((REProfileFragment) getSupportFragmentManager().findFragmentById(R.id.view_pager_profile_home)).realTmeListener();

    }

    @Override
    public void bookingGMAFailure(String error) {
        hideLoading();
        bookingGMAError = error;
    }
}

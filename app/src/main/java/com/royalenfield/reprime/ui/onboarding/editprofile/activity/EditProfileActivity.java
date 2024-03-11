package com.royalenfield.reprime.ui.onboarding.editprofile.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.onboarding.address.activity.AddAddressActivity;
import com.royalenfield.reprime.ui.onboarding.address.activity.ModifyAddressActivity;
import com.royalenfield.reprime.ui.onboarding.editprofile.interactor.EditProfileInteractor;
import com.royalenfield.reprime.ui.onboarding.editprofile.presenter.EditProfilePresenter;
import com.royalenfield.reprime.ui.onboarding.editprofile.views.EditProfileView;
import com.royalenfield.reprime.ui.riderprofile.interactor.RiderProfileUploadInteractor;
import com.royalenfield.reprime.ui.riderprofile.presenter.RiderProfileUploadPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.UploadProfilePicView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.Calendar;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REUtils.bitmapToByteArray;
import static com.royalenfield.reprime.utils.REUtils.showErrorDialog;

/*
 * Edit profile activity only displays the views, and sends data and events to the presenter
 * */

public class EditProfileActivity extends REBaseActivity implements EditProfileView,
        View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, UploadProfilePicView {

    private static final int IMAGE_UPDATE = 101;
    private static final int NAME_AND_EMAIL_UPDATE = 105;
    private int RequestCode = 1;
    private int REQUEST_CODE_PICKUP = 2;
    private TextView mTvInlineEmailError, mTvInlinePhoneError, mTvFirstNameError, mTvLastnameError, mTvDoBError,
            mTvCityError, mTvPasswordError;
    private EditText mEtEmailId, mEtPhone, mEtFirstName, mEtLastName, mEtDob, mEtCity, mEtRiderDescription, mEtPassword;
    private ImageView imageView_profile;
    //EditProfileView Presenter
    private EditProfilePresenter mEditProfilePresenter;
    private LinearLayout parentLinearLayout;
    private TextView mTvAddNewAddress;
    private Bitmap resizedBitmap;
    private boolean mProfilePicUpdated = false;
    private String flat, add;

    public static Intent getStartIntent(Context context, byte[] profileImageArray) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        intent.putExtra(REConstants.KEY_PROFILE_IMAGE, profileImageArray);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
        loadRiderImage();
        setUserDetails();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flat = intent.getStringExtra(REConstants.KEY_FLAT_NO);
        add = intent.getStringExtra(REConstants.KEY_ADDRESS);
        checkFlatAndAddress(flat, add);
    }

    //initializing the views
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.tb_edit_profile);
        mTitleBarView.bindData(this, R.drawable.ic_back_arrow,
                getApplicationContext().getResources().getString(R.string.text_edit_profile_title));

        mTitleBarView.ivNavButton.setOnClickListener(this);
        Button saveProfile_button = findViewById(R.id.button_saveprofile);
        saveProfile_button.setOnClickListener(this);

        imageView_profile = findViewById(R.id.imageView_profile);
        mTvInlineEmailError = findViewById(R.id.email_error_message);
        mTvInlinePhoneError = findViewById(R.id.phone_error_message);
        mTvFirstNameError = findViewById(R.id.first_name_error_message);
        mTvLastnameError = findViewById(R.id.last_name_error_message);
        mTvDoBError = findViewById(R.id.dob_error_message);
        mTvCityError = findViewById(R.id.city_error_message);
        mTvPasswordError = findViewById(R.id.password_error_message);

        mEtEmailId = findViewById(R.id.email);
        mEtEmailId.addTextChangedListener(this);
        mEtPhone = findViewById(R.id.ph_number);
        mEtPhone.addTextChangedListener(this);
        //mEtPhone.setOnEditorActionListener( this );
        mEtFirstName = findViewById(R.id.first_name);
        mEtFirstName.addTextChangedListener(this);
        mEtLastName = findViewById(R.id.last_name);
        mEtLastName.addTextChangedListener(this);
        mEtDob = findViewById(R.id.dob);
        mEtDob.addTextChangedListener(this);
        mEtCity = findViewById(R.id.city);
        mEtCity.addTextChangedListener(this);
        mEtRiderDescription = findViewById(R.id.rider_description);
        mEtRiderDescription.addTextChangedListener(this);
        mEtPassword = findViewById(R.id.edit_password);
        mEtPassword.addTextChangedListener(this);
        //mSDob=mEtDob.getText().toString();
        ImageView mIvCalendar = findViewById(R.id.calendar_image);
        mIvCalendar.setOnClickListener(this);
        mEditProfilePresenter = new EditProfilePresenter(this, new EditProfileInteractor());
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        mTvAddNewAddress = findViewById(R.id.add_new_address);
        mTvAddNewAddress.setOnClickListener(this);
        TextView mTvEditProfilePhoto = findViewById(R.id.tv_edit_profile_photo);
        mTvEditProfilePhoto.setOnClickListener(this);

        //Getting address from Shared preference
        String ADDRESS = null;
        try {
            ADDRESS = REPreference.getInstance().getString(getApplicationContext(), REConstants.PREF_ADDRESS_KEY);
            flat = REPreference.getInstance().getString(getApplicationContext(), REConstants.KEY_FLAT_NO);
            add = REPreference.getInstance().getString(getApplicationContext(),REConstants.KEY_ADDRESS);
        } catch (PreferenceException e) {
            RELog.e(e);
        }
        if (ADDRESS != null && !ADDRESS.equals("")) {
            mTvAddNewAddress.setVisibility(View.GONE);
            checkFlatAndAddress(flat, add);
        } else {
            mTvAddNewAddress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loads the rider profile image
     */
    private void loadRiderImage() {
        Bitmap mProfileBitmap = null;
        // Getting byte array from intent data and decoding to bitmap
        byte[] byteArray = getIntent().getByteArrayExtra(REConstants.KEY_PROFILE_IMAGE);
        if (byteArray != null) {
            mProfileBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        if (mProfileBitmap != null) {
            imageView_profile.setImageBitmap(mProfileBitmap);
        } else {
            imageView_profile.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_profile_noimage));
        }
    }

    /**
     * Setting user details to editText fields
     */
    private void setUserDetails() {
        if (REUserModelStore.getInstance() != null) {
            mEtFirstName.setText(REUserModelStore.getInstance().getFname());
            mEtLastName.setText(REUserModelStore.getInstance().getLname());
            mEtEmailId.setText(REUserModelStore.getInstance().getEmail());
            mEtPhone.setText(REUserModelStore.getInstance().getPhoneNo());
            mEtDob.setText(REUserModelStore.getInstance().getDob());
            mEtCity.setText(REUserModelStore.getInstance().getCity());
            if (REUserModelStore.getInstance().getAboutMe() != null && !REUserModelStore.getInstance().getAboutMe().isEmpty()) {
                mEtRiderDescription.setVisibility(View.VISIBLE);
                mEtRiderDescription.setText(REUserModelStore.getInstance().getAboutMe());
            }
        }
    }

    /**
     * This is for displaying datepicker dialog
     */
    private void showDatePickerDialog() {
        //current date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) ->
                mEtDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, mYear, mMonth, mDay);
        dpDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpDialog.show();
    }

    //TODO dynamically add the address

    /**
     * This is to add address dynamically
     *
     * @param address : String
     */
    private void addAddress(String address) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView address_number, modifyAddress;
        final View rowView = inflater.inflate(R.layout.layout_add_address, null);
        modifyAddress = rowView.findViewById(R.id.modify_address_text);
        modifyAddress.setOnClickListener(this);
        address_number = rowView.findViewById(R.id.address_number);
        address_number.setText(getResources().getString(R.string.address1));
        TextView pickup_address = rowView.findViewById(R.id.pickup_address);
        pickup_address.setText(address);
        parentLinearLayout.removeAllViews();
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        mEditProfilePresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onFieldsEmpty() {
        mTvInlineEmailError.setVisibility(View.VISIBLE);
        mTvInlineEmailError.setText(R.string.enter_email);
        mTvInlinePhoneError.setVisibility(View.VISIBLE);
        mTvInlinePhoneError.setText(R.string.enter_ph_number);
        mTvFirstNameError.setVisibility(View.VISIBLE);
        mTvFirstNameError.setText(R.string.first_name_error);
        mTvLastnameError.setVisibility(View.VISIBLE);
        mTvLastnameError.setText(R.string.last_name_error);
        mTvDoBError.setVisibility(View.VISIBLE);
        mTvDoBError.setText(R.string.dob_error);
        mTvCityError.setVisibility(View.VISIBLE);
        mTvCityError.setText(R.string.city_error);
        mTvPasswordError.setVisibility(View.VISIBLE);
        mTvPasswordError.setText(R.string.password_error);
    }

    @Override
    public void onEmailEmptyError() {
        mTvInlineEmailError.setVisibility(View.VISIBLE);
        mTvInlineEmailError.setText(R.string.enter_email);
    }

    @Override
    public void onPhoneEmptyError() {
        mTvInlinePhoneError.setVisibility(View.VISIBLE);
        mTvInlinePhoneError.setText(R.string.enter_ph_number);
    }

    @Override
    public void onFirstNameEmptyError() {
        mTvFirstNameError.setVisibility(View.VISIBLE);
        mTvFirstNameError.setText(R.string.first_name_error);
    }

    @Override
    public void onLastNameEmptyError() {
        mTvLastnameError.setVisibility(View.VISIBLE);
        mTvLastnameError.setText(R.string.last_name_error);
    }

    @Override
    public void onDobEmptyError() {
        mTvDoBError.setVisibility(View.VISIBLE);
        mTvDoBError.setText(R.string.dob_error);
    }

    @Override
    public void onCityEmptyError() {
        mTvCityError.setVisibility(View.VISIBLE);
        mTvCityError.setText(R.string.city_error);
    }

    @Override
    public void onPasswordEmptyError() {
        mTvPasswordError.setVisibility(View.VISIBLE);
        mTvPasswordError.setText(R.string.password_error);
    }

    @Override
    public void onUpdateSuccess() {
        hideLoading();
        //Update ProfileData model store.
        mEditProfilePresenter.updateLoginUserDataModelStore(mEtFirstName.getText().toString(), mEtLastName.getText().toString()
                , mEtEmailId.getText().toString(), mEtDob.getText().toString(), mEtPhone.getText().toString(),
                mEtCity.getText().toString(), mEtRiderDescription.getText().toString());
        updateNameAndEmail();
        showToastMessage("Profile updated successfully");
        if (mProfilePicUpdated) {
            updateImageChange();
        }
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onUploadPicSuccess(String message) {
        mProfilePicUpdated = true;
        imageView_profile.setImageBitmap(resizedBitmap);
        hideLoading();
    }

    @Override
    public void onUploadPicFailure(String message) {
        mProfilePicUpdated = false;
        hideLoading();
        showErrorDialog(this, message);
    }

    @Override
    public void onUpdateFailure(String errorMessage) {
        hideLoading();
        showErrorDialog(this, errorMessage);
    }

    @Override
    public void hideEmailError() {
        mTvInlineEmailError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hidePhoneError() {
        mTvInlinePhoneError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideFirstNameError() {
        mTvFirstNameError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLastNameError() {
        mTvLastnameError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideDoBError() {
        mTvDoBError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideCityError() {
        mTvCityError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hidePasswordError() {
        //TODO need to uncomment
        mTvPasswordError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void invalidEmail() {
        mTvInlineEmailError.setText(getResources().getString(R.string.valid_email));
        mTvInlineEmailError.setVisibility(View.VISIBLE);
    }

    @Override
    public void invalidPhone() {
        mTvInlinePhoneError.setText(getResources().getString(R.string.valid_phone_number));
        mTvInlinePhoneError.setVisibility(View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mEditProfilePresenter.checkErrorViewVisibility(mEtEmailId.getText().toString(),
                mEtPhone.getText().toString(), mEtFirstName.getText().toString(),
                mEtLastName.getText().toString(), mEtDob.getText().toString(),
                mEtCity.getText().toString(), mEtRiderDescription.getText().toString(), mEtPassword.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard(this);
            handled = true;
        }
        return handled;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calendar_image:
                try {
                    showDatePickerDialog();
                } catch (Exception e) {
                    RELog.e(e);
                }
                break;
            case R.id.button_saveprofile:
                mTvInlineEmailError.setVisibility(View.INVISIBLE);
                mTvInlinePhoneError.setVisibility(View.INVISIBLE);
                mEditProfilePresenter.validateEditProfile(mEtFirstName.getText().toString(),
                        mEtLastName.getText().toString(), mEtEmailId.getText().toString(),
                        mEtPhone.getText().toString(), mEtDob.getText().toString(),
                        mEtCity.getText().toString(), mEtRiderDescription.getText().toString(),
                        mEtPassword.getText().toString());
                if (mProfilePicUpdated) {
                    updateImageChange();
                }
                break;
            case R.id.iv_navigation:
                if (mProfilePicUpdated) {
                    updateImageChange();
                }
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
                break;
            case R.id.add_new_address:
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                intent.putExtra(REConstants.KEY_IS_EDIT_PROFILE, false);
                startActivityForResult(intent, RequestCode);
                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                break;
            case R.id.modify_address_text:
                Intent intent1 = new Intent(this, ModifyAddressActivity.class);
                intent1.putExtra(REConstants.KEY_FLAT_NO, flat);
                intent1.putExtra(REConstants.KEY_ADDRESS, add);
                intent1.putExtra(REConstants.KEY_IS_EDIT_PROFILE, false);
                startActivityForResult(intent1, REQUEST_CODE_PICKUP);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;
            case R.id.tv_edit_profile_photo:
                REUtils.selectImage(EditProfileActivity.this, EditProfileActivity.this);
                break;
            default:
                break;
        }
    }

    /**
     * @param requestCode : requestcode
     * @param resultCode  : resultcode
     * @param data        This gets called when we come back from AddAddressActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //Setting address when coming back from AddLocationActivity
            if (requestCode == RequestCode && data != null) {
                mTvAddNewAddress.setVisibility(View.GONE);
                flat = data.getStringExtra(REConstants.KEY_FLAT_NO);
                add = data.getStringExtra(REConstants.KEY_ADDRESS);
                checkFlatAndAddress(flat, add);
            } else if (requestCode == REQUEST_CODE_PICKUP && data != null) {
                flat = data.getStringExtra(REConstants.KEY_FLAT_NO);
                add = data.getStringExtra(REConstants.KEY_ADDRESS);
                checkFlatAndAddress(flat, add);
            } else if (resultCode == RESULT_OK && requestCode == REConstants.REQUEST_CHECK_GALLERY && data != null) {
                Uri imageUri = data.getData();
                String path = REUtils.getRealPathFromURI(imageUri, EditProfileActivity.this);
                onCaptureImageResult(decodeImage(path));
            } else if (requestCode == REConstants.REQUEST_CHECK_CAMERA && data != null && data.getExtras() != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                onCaptureImageResult(thumbnail);
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
     * Resizes image and sends to server
     *
     * @param thumbnail : Bitmap
     */
    private void onCaptureImageResult(Bitmap thumbnail) {
        if (thumbnail != null) {
            resizedBitmap = Bitmap.createScaledBitmap(thumbnail, 300, 300, false);
            if (resizedBitmap != null) {
                showLoading();
                String encodedImage = REUtils.encodeImage(resizedBitmap);
                RiderProfileUploadPresenter mRiderProfileUploadPresenter = new
                        RiderProfileUploadPresenter(this, new RiderProfileUploadInteractor());
                mRiderProfileUploadPresenter.uploadRiderProfileImage(encodedImage);
            }
        }
    }

    /**
     * Decoding bitmap using bitmap options
     *
     * @param path : path of selected image
     * @return :Bitmap
     */
    private Bitmap decodeImage(final String path) {
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        // Calculate inSampleSize
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions, 300, 300);
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, bmOptions);
    }

    /**
     * @param options   : BitmapFactory.Options
     * @param reqWidth  : int
     * @param reqHeight : int
     * @return
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * Updating image in profile screen
     */
    private void updateImageChange() {
        Intent intent = new Intent();
        //Convert to byte array and send via intent
        intent.putExtra(REConstants.KEY_PROFILE_IMAGE, bitmapToByteArray(resizedBitmap));
        setResult(IMAGE_UPDATE, intent);
    }


    /**
     * Updating name and email in profile screen
     */
    private void updateNameAndEmail() {
        Intent intent = new Intent();
        intent.putExtra(REConstants.KEY_NAME, mEtFirstName.getText().toString() + " " + mEtLastName.getText().toString());
        intent.putExtra(REConstants.KEY_ABOUT_ME, mEtRiderDescription.getText().toString());
        setResult(NAME_AND_EMAIL_UPDATE, intent);
    }

    @Override
    public void onBackPressed() {
        if (mProfilePicUpdated) {
            updateImageChange();
        }
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    private void checkFlatAndAddress(String flat, String add) {
        if (flat == null || flat.equals("")) {
            addAddress(add);
        } else {
            addAddress("#" + flat + ", " + add);
        }
    }

}
package com.royalenfield.reprime.ui.onboarding.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnFirestoreUserSettingCallback;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleDataMappingCallback;
import com.royalenfield.firebase.fireStore.OnFirestoreVehicleListCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.vehicledetails.UserSettingFirestoreModel;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.ui.forgot.activity.ForgotPasswordActivity;
import com.royalenfield.reprime.ui.home.homescreen.interactor.HomeActivityInteractor;
import com.royalenfield.reprime.ui.home.homescreen.listener.FirebaseCustomTokenListener;
import com.royalenfield.reprime.ui.home.homescreen.presenter.HomeActivityPresenter;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.LogResponse;
import com.royalenfield.reprime.ui.onboarding.login.interactor.LoginInteractor;
import com.royalenfield.reprime.ui.onboarding.login.presenter.LoginPresenter;
import com.royalenfield.reprime.ui.onboarding.login.views.LoginBannerLayout;
import com.royalenfield.reprime.ui.onboarding.login.views.LoginView;
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils;
import com.royalenfield.reprime.ui.splash.presenter.FirebaseAuthListner;
import com.royalenfield.reprime.ui.userdatavalidation.popup.models.Pincode;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REUtils.getRandomNumberInRange;

/**
 * LoginActivity ONLY knows how to display views and sending events and data to the presenter
 * LoginActivity doesn't know anything about the model (LoginInteractor)
 * The only changes to the LoginActivity to allow for asynchronous behavior was to add a Progressbar
 */
public class LoginActivity extends REBaseActivity implements View.OnClickListener, LoginView,
        TextWatcher, TextView.OnEditorActionListener, OnFirestoreUserSettingCallback, OnFirestoreVehicleListCallback, OnFirestoreVehicleDataMappingCallback, FirebaseCustomTokenListener {
    public static final String SCREEN = "screen";
    public static final String ACTION = "action";
    private static final String TAG = LoginActivity.class.getSimpleName();

    public static final int CODE_SPLASH_SCREEN_ACTIVITY = 1;
    public static final int CODE_PROFILE_ACTIVITY = 2;
    public static final int CODE_NOTIFICATION_ACTIVITY = 3;
    public static final int CODE_CREATE_RIDE_ACTIVITY = 4;
    public static final int CODE_ON_BOARDING_DIALOG = 5;
    public static final int CODE_SERVICING_ACTIVITY = 6;
    public static final int CODE_PC_ACTIVITY = 7;
    public static final int CODE_NAVIGATION_SCREEN = 8;
    int callingActivity;
private long startTimer;
    //Views
    private TextView mTvInlinePasswordError;
    private TextView mTvInlineUsernameError;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private TextView mReInfoSignUpLink;
private TextView mTvLoginInfo;
    //presenter for the login
    private LoginPresenter mLoginPresenter;
    private String DEVICE_TOKEN_REF = "DeviceTokenRef";
    private Bundle bundle;
    private HomeActivityPresenter homeActivityPresenter;
    private Bundle params = new Bundle();
    private int failureCount=0;
	private Long startTime;

	/**
     * Provides the activity intent instance.
     *
     * @param context starting activity.
     * @return {@link LoginActivity} instance.
     */
    public static Intent getStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        REApplication.getInstance().setCountryHeaderDefault();
        //initialize views
        initViews();
        //instantiate presenter.
        homeActivityPresenter = new HomeActivityPresenter( new HomeActivityInteractor(),this);

        mLoginPresenter = new LoginPresenter(this, new LoginInteractor());
        callingActivity = getIntent().getIntExtra(PCUtils.PC_CALLING_ACTIVITY, 0);

    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Initialize all the views for the login screen.
     */
    private void initViews() {
        Pincode validMobile=new Pincode();
        validMobile.setMin(REUtils.getGlobalValidations().getMinPhoneNum());
        validMobile.setMax(REUtils.getGlobalValidations().getPhoneNumber());
        REApplication.getInstance().validMobile=validMobile;
        mEtUsername = findViewById(R.id.et_username);
        mEtUsername.addTextChangedListener(this);
        mEtPassword = findViewById(R.id.et_password);
        mEtPassword.addTextChangedListener(this);
        mEtPassword.setOnEditorActionListener(this);
  /*      mFbLoginButton = findViewById(R.id.fb_login_btn);
        mFbLoginButton.setOnClickListener(this);*/
        mTvInlinePasswordError = findViewById(R.id.tv_inline_password_error);
        mTvInlineUsernameError = findViewById(R.id.tv_inline_username_error);
        Button login_button = findViewById(R.id.btn_login_button);
        login_button.setOnClickListener(this);
        LoginBannerLayout layout = findViewById(R.id.layout_login_top_bar);
        layout.bindData(getString(R.string.login_header), R.drawable.royalenfield_linear_single);
        TextView mTvForgotPassword = findViewById(R.id.tv_forgot_password);
        mTvForgotPassword.setOnClickListener(this);
        mReInfoSignUpLink = findViewById(R.id.tv_re_info_signup);
        mTvLoginInfo=findViewById(R.id.tv_re_info);
        TextView mTvVersionName = findViewById(R.id.tv_version_name);
        mTvVersionName.setVisibility(View.GONE);
        mTvVersionName.setText("V : " + REUtils.getVersionName(this));
        //For UAT build Release we don't want this function, so we are disabling this functionality
        reSignUpInfoLinkClick();
        findViewById(R.id.iv_back).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_back).setOnClickListener(this);
        if(REApplication.getInstance().Country.equalsIgnoreCase(REConstants.COUNTRY_INDIA))
            mTvLoginInfo.setVisibility(View.GONE);
    }


    /**
     * Handles the click event sign up info link.
     */
    private void reSignUpInfoLinkClick() {
        SpannableString spannableString = new SpannableString(getResources().getString(R.string.text_do_not_have_an_re_account_sign_up));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                params.putString("eventCategory", "Login");
                params.putString("eventAction", "Signup clicked");
               REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);

                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }
        };
//        spannableString.setSpan(clickableSpan, 27, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        mReInfoSignUpLink.setText(spannableString);
//        mReInfoSignUpLink.setMovementMethod(LinkMovementMethod.getInstance());
//        mReInfoSignUpLink.setHighlightColor(Color.TRANSPARENT);
        String signUp = spannableString.toString();
        String lastWord = signUp.substring(signUp.lastIndexOf(" ") + 1);
        spannableString.setSpan(clickableSpan, signUp.length() - lastWord.length(),
                signUp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mReInfoSignUpLink.setText(spannableString);
        mReInfoSignUpLink.setMovementMethod(LinkMovementMethod.getInstance());
        mReInfoSignUpLink.setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * Clears username password fields.
     */
    public void clearUserNamePassword() {
        mEtUsername.setText("");
        mEtPassword.setText("");
    }

    /**
     * Create the booking no key using the logged in user userId.
     */
    private void updateBookingNoKey() {
        if (REApplication.getInstance().getUserLoginDetails() != null && REApplication.getInstance().getUserLoginDetails().getData().getUser() != null) {
            Log.d(TAG, "booking no key : " + REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId());
            REConstants.PREF_APPOINTMENT_BOOKINGNO = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
            //REConstants.PREF_APPOINTMENT_BOOKINGNO = REUserModelStore.getInstance().getUserId();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.fb_login_btn:
            Log.d(TAG, "facebook login clicked");
                signInWithFacebook();
                fbLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList(REConstants.PROFILE_PERMISSION_VALUE,
                        REConstants.USER_EMAIL_PERMISSION_VALUE));t
                break;*/
            case R.id.tv_forgot_password:
                Log.d(TAG, "forgot password clicked");
                params.putString("eventCategory", "Login");
                params.putString("eventAction", "Forgot Password clicked");
               REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);

                clearUserNamePassword();
                Intent intent = ForgotPasswordActivity.getStartIntent(this);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                break;
            case R.id.btn_login_button:
                Log.d(TAG, "RE login clicked");
                Log.e(TAG,"BUTTON CLICK");
                //Clear model store.
                hideKeyboard();
                REApplication.getInstance().clearAllModelStore();
                //Clear preference.
                try {
                    REPreference.getInstance().removeAll(getApplicationContext());
                } catch (PreferenceException e) {
                    RELog.e(e);
                }
                mTvInlineUsernameError.setVisibility(View.INVISIBLE);
                mTvInlinePasswordError.setVisibility(View.INVISIBLE);
				startTime=System.currentTimeMillis();
				Log.e("RE_LOGGER","Login Api call start");
                mLoginPresenter.validateLogin(REUtils.removeLeadingZeroes(mEtUsername.getText().toString().trim()), mEtPassword.getText().toString());
                break;
            case R.id.iv_back:
                onBackPressed();
                overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    @Override
    public void onUsernamePasswordEmpty() {
        mTvInlineUsernameError.setVisibility(View.VISIBLE);
        mTvInlineUsernameError.setText(R.string.text_error_enter_username);
        mTvInlinePasswordError.setVisibility(View.VISIBLE);
        mTvInlinePasswordError.setText(R.string.text_error_enter_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onUsernameEmptyError() {
        mTvInlineUsernameError.setVisibility(View.VISIBLE);
        mTvInlineUsernameError.setText(R.string.text_error_enter_username);
    }

    @Override
    public void onPasswordEmptyError() {
        mTvInlinePasswordError.setVisibility(View.VISIBLE);
        mTvInlinePasswordError.setText(R.string.text_error_enter_password);
    }

    @Override
    public void onLoginSuccess() {
		Log.e("RE_LOGGER","Login Api call end");

		REUtils.setFCMTokenSent(false);
     //   FirestoreManager.getInstance().readPersonalDetailUpdateFirestore(this);
        showLoading();
        //cloud function call
        Log.e(TAG,"CREATEFIREBASE START");
        homeActivityPresenter.getFirebaseCustomToken();
        params.putString("eventCategory", "Login");
        params.putString("eventAction", "Login clicked");
        params.putString("Userid",  REUserModelStore.getInstance().getUserId());
       REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);

      //  hideLoading();
    }

    @Override
    public void onAccountPending() {


    }

    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Login Alert");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        REUtils.showErrorDialog(this, errorMessage, () -> {
            params = new Bundle();
            params.putString("eventCategory", "Login");
            params.putString("eventAction", "Alert Ok click");
           REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle params = new Bundle();
        params.putString("screenname", "Login");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, params);
    }

    @Override
    public void hideUserNameError() {
        mTvInlineUsernameError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hidePasswordError() {
        mTvInlinePasswordError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void invalidUserId() {
        mTvInlineUsernameError.setText(getResources().getString(R.string.text_error_enter_valid_user_id));
        mTvInlineUsernameError.setVisibility(View.VISIBLE);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mLoginPresenter.checkErrorViewVisibility(mEtUsername.getText().toString().trim(), mEtPassword.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard(this);
            //TODO will check if below code is needed or not
            //mLoginPresenter.validateLogin(mEtUsername.getText().toString(), mEtPassword.getText().toString());
            handled = true;
        }
        return handled;
    }

    private void PCTransitionShowDialog() {
        PCUtils.showDialog(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 2s
                //dialogFragment.dismiss();
                finish();
            }
        }, 2000);
    }

    private int getCallbackScreen() {
        if (bundle != null) {
            int screenName = bundle.getInt(LoginActivity.SCREEN);
            if (screenName == LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY) {
                return LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY;
            }
        }
        return 0;
    }


    @Override
    public void onFirestoreUserSettingSuccess() {
		Log.e("RE_LOGGER","read user setting firebase call end");

		updateBookingNoKey();
        hideLoading();
        getVehicleDetails();

    }


    private void getVehicleDetails() {
        showLoading();
       // homeActivityPresenter.getVehicleDetails();
    }
    public void gotoSetResult(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        if (callingActivity == LoginActivity.CODE_PC_ACTIVITY&&!getIntent().hasExtra(PCUtils.PC_BOOK_NOW)) {
            PCTransitionShowDialog();
        } else {
            finish();
        }
    }
    @Override
    public void onFirestoreUserSettingFailure(String message) {
        hideLoading();
        UserSettingFirestoreModel userSettingFirestoreModel = new UserSettingFirestoreModel();
        userSettingFirestoreModel.setGetVehicleDetailsFromFirestore(true);
        userSettingFirestoreModel.setShowUserValidationPopup(false);
        userSettingFirestoreModel.setShowVehicleOnboardingPopup(false);
        REApplication.getInstance().setUserSettingFirestoreModel(userSettingFirestoreModel);
        updateBookingNoKey();
        getVehicleDetails();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFirestoreVehicleDetailsSuccess(List<VehicleDataModel> list) {
		Log.e("RE_LOGGER","get vehicle details call end");
		REApplication.getInstance().flow="login";

		REServiceSharedPreference.saveVehicleData(this, list);
        if (list.size() <= 0) {
            hideLoading();
            gotoSetResult();
        } else {
            Log.e(TAG,"VEHICLE_SUCCESS");
            Log.e("MAPPING","SUCCESSLOgin");
            REServiceSharedPreference.saveVehicleData(this, list);
            Log.e(TAG,"GET SERVICE HISTORY");
            FirestoreManager.getInstance().getServiceHistory();
            hideLoading();
            gotoSetResult();
          //  REUtils.getModelNameFromModelCode(this, list, 0);

        }
    }

    @Override
    public void onVehicleMappingSuccess(List<VehicleDataModel> listVehicleModel, int position) {
        if (listVehicleModel.size()-1 == position) {
            Log.e("MAPPING","SUCCESSLOgin");
            REServiceSharedPreference.saveVehicleData(this, listVehicleModel);
            FirestoreManager.getInstance().getServiceHistory();
            hideLoading();
            gotoSetResult();
        } else {
            position=position+1;
            REUtils.getModelNameFromModelCode(this, listVehicleModel, position);
        }
    }


    @Override
    public void onFirestoreVehicleDetailsFailure(String message) {
        hideLoading();
        REUtils.showErrorDialog(this, getResources().getString(R.string.vehicle_failure_signin), new REUtils.OnDialogButtonClickListener() {
            @Override
            public void onOkCLick() {
                finish();
            }
        });
    }

    /**
     * Push an "openScreen" event with the given screen name. Tags that match that event will fire.
     */
    public static void pushOpenScreenEvent(FirebaseAnalytics mFirebaseAnalytics, String screenName) {

    }



    @Override
    public void onFirebaseCustomTokenSuccess(String token,String reqId) {
        doFirebaseAuthLgin(token);
    }

    private void doFirebaseAuthLgin(String token) {
		long startTime = System.currentTimeMillis();
		Log.e("RE_LOGGER","Firebase auth start");
        REUtils.signInWithFirebaseCustomToken(token + "", new FirebaseAuthListner() {
            @Override
            public void onFirebaseAuthSuccess() {
				Log.e("RE_LOGGER","Firebase auth end");
					hideLoading();
                if(REUtils.getFeatures().getDefaultFeatures()!=null&&REUtils.getFeatures().getDefaultFeatures().getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED)) {
				startTimer=System.currentTimeMillis();
					Log.e("RE_LOGGER","read user setting firebase call start");
				//	FirestoreManager.getInstance().readUserSettingToFirestore(LoginActivity.this);
				}
                else
                    gotoSetResult();
            }

            @Override
            public void onFirebaseAuthFailure(Exception e) {

                failureCount++;
                int randNum = getRandomNumberInRange(1, 3);
                if (failureCount <= 1 && (e instanceof FirebaseNetworkException || e instanceof FirebaseTooManyRequestsException)) {
                    new Handler().postDelayed(() ->doFirebaseAuthLgin(token), randNum * 1000);
                }
                else {
                    hideLoading();
                    gotoSetResult();
                }
            }
        });

    }


    @Override
    public void onFirebaseCustomFailure(String error) {
        hideLoading();
        gotoSetResult();
        }

	@Override
	public void onFirebaseAuthSuccess() {

	}
}

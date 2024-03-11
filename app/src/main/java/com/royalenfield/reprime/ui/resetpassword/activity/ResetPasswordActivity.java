package com.royalenfield.reprime.ui.resetpassword.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.forgot.interactor.ForgotPasswordInteractor;
import com.royalenfield.reprime.ui.forgot.interactor.IForgotPasswordInteractor;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.resetpassword.interactor.ResetPasswordInteractor;
import com.royalenfield.reprime.ui.resetpassword.presenter.IResetPasswordPresenter;
import com.royalenfield.reprime.ui.resetpassword.presenter.ResetPasswordPresenter;
import com.royalenfield.reprime.ui.resetpassword.views.ResetPasswordView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

/**
 * @author BOP1KOR on 12/05/2018.
 * <p>
 * Reset password screen to create the new password by confirming the OTP.
 */
public class ResetPasswordActivity extends REBaseActivity implements View.OnClickListener, ResetPasswordView, TextWatcher, OnSendOtpResponseListener {
    private static final String TAG = ResetPasswordActivity.class.getSimpleName();

    //Presenter
    private IResetPasswordPresenter mResetPasswordPresenter;
    private String mUserId;

    private EditText mEditNewPassword;
    private EditText mEditConfirmPassword;
    private EditText mEditOtp;
    private TextView mTvOtpInlineErrorView;
    private TextView mTvNewPwdInlineErrorView;
    private TextView mTvConfirmPwdInlineErrorView;
    private IForgotPasswordInteractor mForgotPasswordInteractor;
    @BindView(R.id.text_resend_otp_link)
    LinearLayout mResendLayout;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    private TextView mResendText;
    private CountDownTimer mCountDownTimer;
    public static Intent getStartIntent(Context context) {
        return new Intent(context, ResetPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        getIntentData();

        //initialize all the views
        initViews();
        mResendText = mResendLayout.findViewById(R.id.txv_resend);
        //initialize presenter.
        mResetPasswordPresenter = new ResetPasswordPresenter(this, new ResetPasswordInteractor());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Forgot Password OTP");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    @OnClick(R.id.text_resend_otp_link)
    public void onResendClicked() {

        if (mResendText.getText().equals(getString(R.string.resend_otp))) {
            requestOtp();
            startTimer();
        }
    }


    private void requestOtp() {
        mForgotPasswordInteractor = new ForgotPasswordInteractor();
        showLoading();
        mForgotPasswordInteractor.sendOtp(mUserId, ResetPasswordActivity.this);
    }
    private void startTimer() {
        startCountDownTimer(60000, 1000);
    }

    private void startCountDownTimer(int total, int interval) {
        mCountDownTimer = new CountDownTimer(total, interval) {

            @Override
            public void onTick(long l) {
                mResendText.setText("Resend in " + ((int) (l / 1000)) + " secs");
                mResendText.setTextColor(getResources().getColor(R.color.white_50));
            }

            @Override
            public void onFinish() {
                mResendText.setText(getString(R.string.resend_otp));
                mResendText.setTextColor(getResources().getColor(R.color.white));
            }
        };

        mCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserId = bundle.getString("USER_ID");
        }
    }

    private void initViews() {
        ImageView mBackArrowButton = findViewById(R.id.iv_back_arrow);
        mBackArrowButton.setOnClickListener(this);

        Button mButtonSubmit = findViewById(R.id.button_submit_reset_password);
        mButtonSubmit.setOnClickListener(this);

        mEditNewPassword = findViewById(R.id.edit_enter_new_password);
        mEditConfirmPassword = findViewById(R.id.edit_enter_confirm_password);
        mEditOtp = findViewById(R.id.edit_enter_otp);

        //Inline error views.
        mTvOtpInlineErrorView = findViewById(R.id.tv_otp_inline_error);
        mTvNewPwdInlineErrorView = findViewById(R.id.tv_new_pwd_inline_error);
        mTvConfirmPwdInlineErrorView = findViewById(R.id.tv_confirm_pwd_inline_error);

        //Add text change listeners for all the edit fields.
        mEditOtp.addTextChangedListener(this);
        mEditNewPassword.addTextChangedListener(this);
        mEditConfirmPassword.addTextChangedListener(this);
        if(REUtils.getFeatures().getDefaultFeatures().getShowOTPEmailText()!=null&&REUtils.getFeatures().getDefaultFeatures().getShowOTPEmailText().equalsIgnoreCase(FEATURE_DISABLED))
            tvInfo.setVisibility(View.GONE);
        else
            tvInfo.setVisibility(View.VISIBLE);

        //resend otp
//        TextView mTextResendOtpLink = findViewById(R.id.text_resend_otp_link);
//        resendOtpClick(mTextResendOtpLink);

        startTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_arrow:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.button_submit_reset_password:
                mResetPasswordPresenter
                        .resetPassword(mUserId, mEditNewPassword.getText().toString(),
                                mEditConfirmPassword.getText().toString(),
                                mEditOtp.getText().toString());
                Log.d("API", "resetPassword() API called");

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onResetSuccess() {
        Log.d("API", "resetPassword() API success");
        Bundle params=new Bundle();
        params.putString("eventCategory", "Forgot Password");
        params.putString("eventAction", "Submit click");
       REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);

        showToastMessage("Password updated successfully!");
        setResult(RESULT_OK);
        onBackPressed();
//        Intent loginIntent = new Intent(this, LoginActivity.class);
//        startActivityForResult(loginIntent, LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
      //  startActivity(intent);
       // finish();
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onResetFailure(String errorMessage) {
        Log.d("API", "resetPassword() API failure");
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    public void onAllEmptyFields() {
        mTvOtpInlineErrorView.setVisibility(View.VISIBLE);
        mTvNewPwdInlineErrorView.setVisibility(View.VISIBLE);
        mTvConfirmPwdInlineErrorView.setVisibility(View.VISIBLE);
        mTvConfirmPwdInlineErrorView.setText(getResources().getString(R.string.text_enter_confirm_password));
    }

    @Override
    public void onOtpEmpty() {
        mTvOtpInlineErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNewPasswordEmpty() {
        mTvNewPwdInlineErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfirmPasswordEmpty() {
        mTvConfirmPwdInlineErrorView.setVisibility(View.VISIBLE);
        mTvConfirmPwdInlineErrorView.setText(getResources().getString(R.string.text_enter_confirm_password));
    }

    @Override
    public void onPasswordConfirmationError() {
        mTvConfirmPwdInlineErrorView.setVisibility(View.VISIBLE);
        mTvConfirmPwdInlineErrorView.setText(R.string.text_password_mismatch);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onInlineErrorVisibility();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * On Text changes updates the inline errors visibility state....
     */
    public void onInlineErrorVisibility() {
        if (mEditOtp.getText().toString().length() >= 1) {
            mTvOtpInlineErrorView.setVisibility(View.INVISIBLE);
        }
        if (mEditNewPassword.getText().toString().length() >= 1) {
            mTvNewPwdInlineErrorView.setVisibility(View.INVISIBLE);
        }
        if (mEditConfirmPassword.getText().toString().length() >= 1) {
            mTvConfirmPwdInlineErrorView.setVisibility(View.INVISIBLE);
        }
    }

    private void resendOtpClick(TextView view) {
        try {
            Typeface typeface_montserrat_regular = ResourcesCompat.getFont(getApplicationContext(),
                    R.font.montserrat_regular);
            SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                    getApplicationContext().getString(R.string.text_otp_not_received_link));
            spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_regular), 0,
                    getApplicationContext().getString(R.string.text_otp_not_received_link).length(), 0);
            spanTxt.append(getApplicationContext().getString(R.string.text_otp_resend_link));
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //TODO send otp api call goes
                    mForgotPasswordInteractor = new ForgotPasswordInteractor();
                    showLoading();
                    mForgotPasswordInteractor.sendOtp(mUserId, ResetPasswordActivity.this);
                    Log.d("API", "sendOtp() API called");
                }
            }, spanTxt.length() - getApplicationContext().getString(R.string.text_otp_resend_link)
                    .length(), spanTxt.length(), 0);
            Typeface typeface_montserrat_semibold = ResourcesCompat.getFont(getApplicationContext(),
                    R.font.montserrat_semibold);
            spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold),
                    spanTxt.length() - getApplicationContext().getString(R.string.text_otp_resend_link)
                            .length(), spanTxt.length(), 0);
            view.setMovementMethod(LinkMovementMethod.getInstance());
            view.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            view.setHighlightColor(Color.TRANSPARENT);
            view.setText(spanTxt, TextView.BufferType.SPANNABLE);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    @Override
    public void onSuccess() {
        Log.d(TAG, "Resend OTP success.");
        showToastMessage(getResources().getString(R.string.text_message_otp_sent_success));
        hideLoading();
    }

    @Override
    public void onFailure(String errorMessage) {
        Log.d(TAG, "Resend OTP failed.");
        REUtils.showErrorDialog(this, errorMessage);
        hideLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.CODE_SPLASH_SCREEN_ACTIVITY) {
                REApplication.getInstance().setLoggedInUser(true);
                Intent intent=new Intent(this, REHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        }
        else{
            setResult(2);
            onBackPressed();
        }
    }

    @Override
    public void onNewPasswordInvalid() {
        mTvNewPwdInlineErrorView.setVisibility(View.VISIBLE);
        mTvNewPwdInlineErrorView.setText(getResources().getString(R.string.invalid_password_length));
    }

    @Override
    public void onConfirmPasswordInvalid() {
        mTvConfirmPwdInlineErrorView.setVisibility(View.VISIBLE);
        mTvConfirmPwdInlineErrorView.setText(getResources().getString(R.string.invalid_password_length));
    }


}



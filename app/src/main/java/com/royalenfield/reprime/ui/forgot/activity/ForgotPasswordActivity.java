package com.royalenfield.reprime.ui.forgot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.forgot.interactor.ForgotPasswordInteractor;
import com.royalenfield.reprime.ui.forgot.presenter.ForgotPasswordPresenter;
import com.royalenfield.reprime.ui.forgot.presenter.IForgotPasswordPresenter;
import com.royalenfield.reprime.ui.forgot.views.ForgotPasswordView;
import com.royalenfield.reprime.ui.resetpassword.activity.ResetPasswordActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

/**
 * @author BOP1KOR on 12/05/2018.
 * <p>
 * Forgot passwword screen for the user to generate the OTP using the RE user id..
 */
public class ForgotPasswordActivity extends REBaseActivity implements ForgotPasswordView, View.OnClickListener, TextWatcher {
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();


    //Presenter
    private IForgotPasswordPresenter mForgotPasswordPresenter;

    //Views
    private EditText mEditUserId;
    private TitleBarView mTitleBarView;
    private TextView mTextInlineError;
    private TextView mTvInfo;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //initialize views.
        initViews();

        //instantiate presenter.
        mForgotPasswordPresenter = new ForgotPasswordPresenter(this, new ForgotPasswordInteractor());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Forgot Password");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTitleBarView.bindData(this, R.drawable.icon_close, getResources().getString(R.string.text_forgot_password));

    }

    @Override
    protected void onDestroy() {
        mForgotPasswordPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * Initializes all the views.
     */
    private void initViews() {
        ImageView mNavCloseButton = findViewById(R.id.iv_navigation);
        mNavCloseButton.setOnClickListener(this);
        mTitleBarView = findViewById(R.id.tb_forgot_password);
        mEditUserId = findViewById(R.id.editText_forgot_pwd_user_id);
        mEditUserId.addTextChangedListener(this);
        Button mSubmitButton = findViewById(R.id.button_forgot_pwd_submit);
        mSubmitButton.setOnClickListener(this);
        mTvInfo=findViewById(R.id.tv_re_info);
        if(REApplication.getInstance().Country.equalsIgnoreCase(REConstants.COUNTRY_INDIA))
            mTvInfo.setVisibility(View.GONE);
        mTextInlineError = findViewById(R.id.text_email_phone_inline_error);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_forgot_pwd_submit:
                mTextInlineError.setVisibility(View.INVISIBLE);
                mForgotPasswordPresenter.sendOtp(REUtils.removeLeadingZeroes(mEditUserId.getText().toString()));
                Log.d("API", "sendOtp() API called");
                break;
            case R.id.iv_navigation:
                hideKeyboard(this);
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onOtpSendSuccess() {
        Bundle params=new Bundle();
        params.putString("eventCategory", "Forgot Password ");
        params.putString("eventAction", "Submit email/phone click");
       REUtils.logGTMEvent(REConstants.KEY_LOGIN_GTM, params);


        Log.d("API", "sendOtp() API success");
        showToastMessage(getResources().getString(R.string.text_message_otp_sent_success));
        Intent intent = ResetPasswordActivity.getStartIntent(this);
        intent.putExtra("USER_ID", REUtils.removeLeadingZeroes(mEditUserId.getText().toString()));
        startActivityForResult(intent,2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onOtpSendFailure(String errorMessage) {
        Log.d("API", "sendOtp() API failure");
        REUtils.showErrorDialog(this, errorMessage);
    }

    @Override
    public void onEmailOrPhoneNumberEmpty() {
        mTextInlineError.setVisibility(View.VISIBLE);
        mTextInlineError.setText(R.string.text_error_enter_username);

    }

    @Override
    public void onInvalidPhoneNumber() {
        mTextInlineError.setVisibility(View.VISIBLE);
        mTextInlineError.setText(getResources().getString(R.string.text_error_enter_valid_user_id));
    }

    @Override
    public void onInvalidEmailId() {
        mTextInlineError.setVisibility(View.VISIBLE);
        mTextInlineError.setText(getResources().getString(R.string.text_error_enter_valid_user_id));
    }

    @Override
    public void hideInlineError() {
        mTextInlineError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mForgotPasswordPresenter.onInlineErrorVisibility(mEditUserId.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onBackPressed();
    }
}

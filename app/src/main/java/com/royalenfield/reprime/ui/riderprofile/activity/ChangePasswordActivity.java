package com.royalenfield.reprime.ui.riderprofile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.riderprofile.interactor.ChangePasswordInteractor;
import com.royalenfield.reprime.ui.riderprofile.presenter.ChangePasswordPresenter;
import com.royalenfield.reprime.ui.riderprofile.views.ChangePasswordView;
import com.royalenfield.reprime.ui.splash.activity.RESuccessSplashActivity;
import com.royalenfield.reprime.utils.REUtils;

import static com.royalenfield.reprime.utils.REConstants.KEY_ACCOUNT_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;
import static com.royalenfield.reprime.utils.REConstants.KEY_SETTINGS_GTM;

/**
 * This activity is to change the password
 */
public class ChangePasswordActivity extends REBaseActivity implements TitleBarView.OnNavigationIconClickListener,
        View.OnClickListener, ChangePasswordView, TextWatcher {

    private ChangePasswordPresenter mChangePasswordPresenter;
    private EditText mEtconfirmPassword, mEtNewPassword,mEtPreviousPassword;
    private String mConfirmPassword, mNewPassword,mPreviousPassword;
    private Button mSubmitButton;
    private TextView tvNewPWDInline, tvConfirmPWDInline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Change Password screen");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
        mChangePasswordPresenter = new ChangePasswordPresenter(this, new ChangePasswordInteractor());
        initializeViews();
    }

    /**
     * Initializes all the views for the UI....
     */
    private void initializeViews() {
        TitleBarView mTitleBarView = findViewById(R.id.titleBarView_changepassword);
        mTitleBarView.bindData(this, R.drawable.back_arrow, getResources().getString(R.string.text_title_changepassword).toUpperCase());
        mSubmitButton = findViewById(R.id.button_submit);
        mSubmitButton.setEnabled(false);
        mSubmitButton.setOnClickListener(this);
        mEtNewPassword = findViewById(R.id.et_newpassword);
        mEtNewPassword.addTextChangedListener(this);
        mEtconfirmPassword = findViewById(R.id.et_confirm_newpassword);
        mEtconfirmPassword.addTextChangedListener(this);
        mEtPreviousPassword=findViewById(R.id.et_previous_password);
        mEtPreviousPassword.addTextChangedListener(this);
        tvNewPWDInline=findViewById(R.id.tv_new_pwd_inline_error);
        tvConfirmPWDInline=findViewById(R.id.tv_confirm_pwd_inline_error);
    }

    /**
     * get text from Edittexts
     */
    private void getTexts() {
        mConfirmPassword = mEtconfirmPassword.getText().toString();
        mNewPassword = mEtNewPassword.getText().toString();
        mPreviousPassword=mEtPreviousPassword.getText().toString();
    }

    /**
     * Checks whether the edittext are filled or not
     */
    private void checkFields() {
        tvNewPWDInline.setVisibility(View.INVISIBLE);
        tvConfirmPWDInline.setVisibility(View.INVISIBLE);
        if (!mNewPassword.isEmpty() && !mPreviousPassword.isEmpty() && !mConfirmPassword.isEmpty()) {
            mSubmitButton.setBackground(getApplicationContext().getDrawable(R.drawable.button_border_enable));
            mSubmitButton.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
            mSubmitButton.setEnabled(true);
        } else {
            mSubmitButton.setBackground(getApplicationContext().getDrawable(R.drawable.button_border_disable));
            mSubmitButton.setTextColor(getApplicationContext().getResources().getColor(R.color.white_50));
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onNavigationIconClick() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Settings");
        params.putString("eventAction", "Change Password");
        params.putString("eventLabel", "Close clicked");
        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_submit) {
//            showLoading();

            mChangePasswordPresenter.validateFields(mPreviousPassword, mNewPassword,mConfirmPassword);
            Log.d("API", "onChangePassword() API called");

        }
    }

    @Override
    public void onPaswordsMismatch() {
        showPermissionsErrorDialog("Enter new password and Confirm password fields don't match", false);
//        Toast.makeText(getApplicationContext(), "Enter new password and Confirm password fields don't match", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDuplicatePassword() {
        showPermissionsErrorDialog("Old password and New password cannot be the same", false);
//        Toast.makeText(getApplicationContext(), "Old password and New password cannot be the same", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChangePasswordSuccess() {
        Bundle params = new Bundle();
        params.putString("eventCategory", "Settings");
        params.putString("eventAction", "Change Password");
        params.putString("eventLabel", "Submit clicked");
        REUtils.logGTMEvent(KEY_SETTINGS_GTM, params);
        Log.d("API", "onChangePassword() API success");
        Intent intent = RESuccessSplashActivity.getIntent(getApplicationContext(), null,
                getResources().getString(R.string.changepassword_success));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onChangePasswordFailure() {
        Log.d("API", "onChangePassword() API failure");
        showPermissionsErrorDialog("Invalid old password", false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getTexts();
        checkFields();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onNewPasswordInvalid() {
        tvNewPWDInline.setVisibility(View.VISIBLE);
        tvNewPWDInline.setText(getResources().getString(R.string.invalid_password_length));
    }

    @Override
    public void onConfirmPasswordInvalid() {
        tvConfirmPWDInline.setVisibility(View.VISIBLE);
        tvConfirmPWDInline.setText(getResources().getString(R.string.invalid_password_length));
    }
}

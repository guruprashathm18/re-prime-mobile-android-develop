package com.royalenfield.reprime.ui.home.connected.motorcycle.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.firebase.fireStore.OnAuthorizationCallback;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.PairingMotorcycleResponseModel;
import com.royalenfield.reprime.ui.home.homescreen.fragments.MotorCycleFragment;
import com.royalenfield.reprime.ui.onboarding.AccountCreationSuccessActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.riderprofile.activity.REWebViewActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpanAndColor;
import com.royalenfield.reprime.utils.REUtils;

import java.util.Objects;

import static com.royalenfield.reprime.utils.REConstants.SETTING_ACTIVITY_INPUT_TYPE;

public class PairingMotorcycleDialog extends BottomSheetDialogFragment
        implements View.OnClickListener, OnOtpCompletionListener, OnAuthorizationCallback {

    private final MotorCycleFragment motorCycleFragment;
    private final VehicleDataModel vehicleModel;
    private OtpView otpView;
    private PairingMotorcycleResponseModel.GetDeviceData deviceData;
    private TextView txtConnect;
    private TextView txtIncorrectCode;
    private ImageView mImagePasswordEye;
    private boolean isPasswordShow = false;
    private TextView mResendText;
    public PairingMotorcycleDialog(MotorCycleFragment motorCycleFragment, VehicleDataModel vehicleModel, PairingMotorcycleResponseModel.GetDeviceData deviceData) {
        this.vehicleModel = vehicleModel;
        this.deviceData = deviceData;
        this.motorCycleFragment = motorCycleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.dialog_pairing_motorcycles, container, false);
        initViews(mView);
        return mView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogStyle);

        dialog.setContentView(R.layout.dialog_pairing_motorcycles);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                //FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                //BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });


        return dialog;
    }

    private void initViews(View mView) {
        mView.findViewById(R.id.txtDeviceName);
        TextView txtBikeName = mView.findViewById(R.id.txtBikeName);
        txtBikeName.setText(vehicleModel.getRegistrationNo());
        otpView = mView.findViewById(R.id.otp_view);
        otpView.requestFocus();

        txtConnect = mView.findViewById(R.id.txtConnect);
        txtIncorrectCode = mView.findViewById(R.id.txtIncorrectCode);
        txtConnect.setOnClickListener(this);
        mResendText = mView.findViewById(R.id.txv_resnd);
        mResendText.setOnClickListener(this);
        setResendText();
        mView.findViewById(R.id.imgCross).setOnClickListener(this);

        mImagePasswordEye = mView.findViewById(R.id.image_password_eye);
        mImagePasswordEye.setOnClickListener(this);
        otpView.setOtpCompletionListener(this);
        setOtpViewClear();
        otpView.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                setOtpViewClear();
            }
            return false;
        });

    }


    private void setResendText() {
        Typeface mTypefaceMontserratRegular = ResourcesCompat.getFont(getContext(),
                R.font.montserrat_regular);
        Typeface typeface_montserrat_semibold = ResourcesCompat.getFont(getContext(),
                R.font.montserrat_bold);

        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getContext().getString(R.string.txt_resend));
        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), 0,
                getContext().getString(R.string.txt_resend).length(), 0);

        spanTxt.append(" ");
        spanTxt.append(getContext().getString(R.string.str_resend));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
//                Intent intent = new Intent(getContext(), REWebViewActivity.class);
//                intent.putExtra(SETTING_ACTIVITY_INPUT_TYPE, getResources().getString(R.string.term_of_condition));
//                startActivity(intent);
//                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
                setOtpViewClear();
                motorCycleFragment.getPairingKeyAndShowDialog();

            }
        }, spanTxt.length() - getContext().getString(R.string.str_resend)
                .length(), spanTxt.length(), 0);
        spanTxt.setSpan(new RECustomTyperFaceSpanAndColor(typeface_montserrat_semibold, Color.BLACK), spanTxt.length()
                - getContext().getString(R.string.str_resend)
                .length(), spanTxt.length(), 0);




        //  spanTxt.append(getContext().getString(R.string.text_and));
//
//        spanTxt.setSpan(new RECustomTyperFaceSpan(mTypefaceMontserratRegular), spanTxt.length() -
//                getContext().getString(R.string.text_and)
//                        .length(), spanTxt.length(), 0);


//        spanTxt.append(getContext().getString(R.string.text_privacy_policies));
//        spanTxt.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                Intent intent = new Intent(getContext(), PrivacyPolicyAndTermsActivity.class);
//                intent.putExtra("activity_type", "Privacy Policy");
//                startActivity(intent);
//                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
//            }
//        }, spanTxt.length() - getContext().getString(R.string.text_privacy_policies)
//                .length(), spanTxt.length(), 0);
//
//        spanTxt.setSpan(new RECustomTyperFaceSpan(typeface_montserrat_semibold), spanTxt.length() -
//                getContext().getString(R.string.text_privacy_policies)
//                        .length(), spanTxt.length(), 0);

        mResendText.setMovementMethod(LinkMovementMethod.getInstance());
        mResendText.setTextColor(getContext().getResources().getColor(R.color.black));
        mResendText.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    private void setOtpViewClear() {
        txtConnect.setEnabled(false);
        txtIncorrectCode.setText("");
        txtConnect.getBackground().setAlpha(128);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtConnect) {
            if (validateOtp()) {
                connectBike();
            } else {
                //Toast.makeText(getActivity(), getString(R.string.alert_pairing_key_incorrect), Toast.LENGTH_SHORT).show();
                txtIncorrectCode.setText(getString(R.string.please_enter_correct_code));
            }
        }

        else if (view.getId() == R.id.imgCross) {
            motorCycleFragment.setAuthorizedVehicle(false);
            dismiss();
        }
        else if(view.getId()==R.id.image_password_eye){
            if (!isPasswordShow) {
                otpView.setInputType(InputType.TYPE_CLASS_TEXT);
                mImagePasswordEye.setImageDrawable(getContext().
                        getDrawable(R.drawable.ic_show_text));
                otpView.invalidate();
                isPasswordShow = true;
            } else {
                otpView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mImagePasswordEye.setImageDrawable(getContext().
                        getDrawable(R.drawable.ic_hide_text));
                otpView.invalidate();
                isPasswordShow = false;
            }

        }

    }

    private void connectBike() {
        FirestoreManager.createConnectedInfoToFirestore(vehicleModel.getChaissisNo(), deviceData.deviceImei, false, this);
        //motorCycleFragment.fetchConnectedStatus();
        //Toast.makeText(getActivity(), getString(R.string.alert_pairing_key_success), Toast.LENGTH_SHORT).show();

    }

    private boolean validateOtp() {
        if (otpView.getText().length() != 6) {
            return false;
        } else return otpView.getText().toString().equals(deviceData.userKey);

    }

    @Override
    public void onOtpCompleted(String otp) {
        txtConnect.setEnabled(true);
        txtConnect.getBackground().setAlpha(255);
    }

    @Override
    public void onAuthorizeSuccess(String deviceIMei, boolean value) {
        motorCycleFragment.provisionUpdateStatus();
    }

    @Override
    public void onAuthorizeFailure(String message) {

    }


}

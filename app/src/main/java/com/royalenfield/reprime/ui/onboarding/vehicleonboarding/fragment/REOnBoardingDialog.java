package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.activity.AddVehicleActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.royalenfield.reprime.application.REApplication.mFirebaseAnalytics;
import static com.royalenfield.reprime.utils.REConstants.KEY_SCREEN_GTM;

public class REOnBoardingDialog extends BottomSheetDialogFragment implements View.OnClickListener {


    private static REOnBoardingDialog reOnBoardingDialog;
    private Button addVehicleYes, addVehicleNo;
    private ImageView cancel;
    private REHomeActivity reHomeActivity;


    public static REOnBoardingDialog newInstance(REHomeActivity reHomeActivity) {
        if (reOnBoardingDialog == null) {
            reOnBoardingDialog = new REOnBoardingDialog();
        }
        reOnBoardingDialog.reHomeActivity = reHomeActivity;
        return reOnBoardingDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container,
                false);
        addVehicleYes = view.findViewById(R.id.add_vehicle_yes);
        Button addVehicleNo = view.findViewById(R.id.add_vehicle_no);
        cancel = view.findViewById(R.id.cancel);
        addVehicleYes.setOnClickListener(this);
        addVehicleNo.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        Window window = getDialog().getWindow();
//        WindowManager.LayoutParams windowParams = window.getAttributes();
//        windowParams.dimAmount = 0.0f;
//        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(windowParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        onBackPressed();
        Bundle paramsScr = new Bundle();
        paramsScr.putString("screenname", "Own a Motorcycle");
        REUtils.logGTMEvent(KEY_SCREEN_GTM, paramsScr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                Bundle params = new Bundle();
                params.putString("eventCategory", "Own a motorcycle");
                params.putString("eventAction", "close");
				REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params);
				if(reHomeActivity!=null)
					reHomeActivity.openCommunityTab();
				this.dismiss();
				break;
            case R.id.add_vehicle_no:
                Bundle params1 = new Bundle();
                params1.putString("eventCategory", "Own a motorcycle");
                params1.putString("eventAction", "No");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params1);
                if(reHomeActivity!=null)
                reHomeActivity.openCommunityTab();
                this.dismiss();
                break;
            case R.id.add_vehicle_yes:
                Bundle params2 = new Bundle();
                params2.putString("eventCategory", "Own a motorcycle");
                params2.putString("eventAction", "Yes");
               REUtils.logGTMEvent(REConstants.KEY_MOTORCYCLES_GTM, params2);
                if (REApplication.getInstance().getUserLoginDetails() != null) {
                    launchAddVehicleActivity();
                } else {
                    startActivityForResult(new Intent(getContext(), UserOnboardingActivity.class), LoginActivity.CODE_ON_BOARDING_DIALOG);
                }
                break;
            default:
                break;
        }
    }

    private void launchAddVehicleActivity() {
        this.dismiss();
        startActivity(new Intent(getActivity(), AddVehicleActivity.class));
       // getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_ON_BOARDING_DIALOG) {
            launchAddVehicleActivity();
        }
        else if (resultCode == RESULT_CANCELED && requestCode == LoginActivity.CODE_ON_BOARDING_DIALOG){
            if(reHomeActivity!=null)
            reHomeActivity.openCommunityTab();
            this.dismiss();
        }
    }

    private void onBackPressed() {
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                // To dismiss the fragment when the back-button is pressed.
                reHomeActivity.openCommunityTab();
                dismiss();
                return true;
            }
            // Otherwise, do nothing else
            else return false;
        });
    }
}
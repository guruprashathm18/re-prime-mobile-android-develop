package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.utils;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.onboarding.vehicleonboarding.fragment.REOnBoardingDialog;

public class REAddVehicleDialogUtil {


    public static REOnBoardingDialog showOnBoardingDialog(Context context, FragmentManager fragmentManager) {
        REOnBoardingDialog reOnBoardingDialog = REOnBoardingDialog.newInstance(((REHomeActivity) context));
        reOnBoardingDialog.setCancelable(false);
        return reOnBoardingDialog;
    }
}

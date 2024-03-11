package com.royalenfield.reprime.ui.phoneconfigurator.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.phoneconfigurator.fragment.PCTransitionDialogFragment;
import com.royalenfield.reprime.utils.REUtils;

import java.text.NumberFormat;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class PCUtils {

    public static final String PC_FIREBASE_COUNTRY_KEY = "IN";
    public static final String PC_CALLING_ACTIVITY = "callingActivity";
    public static final String NAVIGATION_CALLING_ACTIVITY = "navigationActivity";
    public static final String PC_REACT_FLAG = "react_flag";
    public static final String PC_CONFIG_NOW = "ConfigNow";
    public static final String PC_BOOK_NOW = "BookNow";
    public static final String PC_VARIANT_ID = "variant_ID";
    public static final String PC_SAVED_CONFIG = "SavedConfig";
    public static final String PC_OPEN_MYO = "OpenMyo";
    public static final String PC_OPEN_WONDERLUST = "OpenWdls";
    public static final String PC_OPEN_SERVICE = "OpenSerice";
    public static final String PC_BIKE_ID = "bike_ID";
    public static final String PC_CONFIG_ID = "config_ID";
    public static final String PC_PLATFORM = "?App=reprime&platform=android";
    public static final String PC_STATE_CODE = "statecode";
    public static final String PC_VARIANT_MODEL = "pc_model";
    public static final String PC_BALANCE_PAMENT = "Balance_Payment";
    public static final String URL_PAYMENT = "Payment_URL";
    public static final String PC_CHESSIS_NO = "chessis_no";
    public static final String PC_OPEN_ORDER_TRACKING = "OpenOrderTracking";
    public static final String PC_OPEN_FINANCE= "OpenFinance";
    public static final String ORDER_ID ="order_id" ;
    // public static String WEB_VIEW_URL = "";
    private static Handler mHandler;
    private static Runnable mRunnable;
    private static int DELAY = 1000;

    public static void showErrorDialog(final Activity activity, String message) {
        final Dialog dialog = new Dialog(activity, R.style.blurTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.webview_cancel_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.message);
        text.setText(message);
        Button dialogButton = (Button) dialog.findViewById(R.id.okay);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
            }
        });
        dialog.show();
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        // Loading icons array
        final int[] loaderDrawables = {R.drawable.ic_loader_round_logo,
                R.drawable.ic_loader_bike,
                R.drawable.ic_loader_gun, R.drawable.ic_loader_service, R.drawable.ic_loader_navigation};
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.LoaderDialogThemeStatusBar);
        progressDialog.show();
        // Setting customView
        progressDialog.setContentView(R.layout.pc_layout_loading);
        final ImageView mLoaderImage = progressDialog.findViewById(R.id.imageView_loader);
        final ImageView[] ivDots = new ImageView[loaderDrawables.length];
        // Linear layout for adding dot drawables
        LinearLayout linearDots = progressDialog.findViewById(R.id.linear_dots);
        for (int j = 0; j < loaderDrawables.length; j++) {
            ivDots[j] = new ImageView(context);
            ivDots[j].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.loading_circulardot_normal));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.
                    LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins((int) context.getResources().getDimension(R.dimen.linearlayout_dots_margin),
                    0, (int) context.getResources().getDimension(R.dimen.linearlayout_dots_margin), 0);
            // Adding dot drawables to linear layout
            linearDots.addView(ivDots[j], params);
        }
        mHandler = new Handler();
        // Runnable for changing drawables
        mRunnable = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                for (int k = 0; k < loaderDrawables.length; k++) {
                    ivDots[k].setImageResource(R.drawable.loading_circulardot_normal);
                }
                if (i == loaderDrawables.length - 1) {
                    i = 0;
                } else {
                    i++;
                }
                ivDots[i].setImageResource(R.drawable.circular_dot_selected);
                mLoaderImage.setImageResource(loaderDrawables[i]);
                mHandler.postDelayed(mRunnable, DELAY);
            }
        };
        mHandler.postDelayed(mRunnable, DELAY);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }


    public static PCTransitionDialogFragment showDialog(AppCompatActivity activity) {
        PCTransitionDialogFragment pcTransitionDialogFragment = PCTransitionDialogFragment.newInstance();
        if (pcTransitionDialogFragment.getDialog() != null && pcTransitionDialogFragment.getDialog().isShowing()) {
            //do nothing
        } else {
            pcTransitionDialogFragment.setCancelable(false);
            pcTransitionDialogFragment.show(activity.getSupportFragmentManager(), "Dialog");
        }
        return pcTransitionDialogFragment;
    }


    public static String currencyFormat(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        //format.setMinimumFractionDigits(0);
        return format.format(amount);
    }



}

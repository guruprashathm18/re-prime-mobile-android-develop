package com.royalenfield.reprime.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author BOP1KOR on 12/21/2018.
 */

public class REDialogUtils {

    private static Runnable mRunnable;
    private static Handler mHandler;
    private static int DELAY = 1000; // Delay in ms

    private REDialogUtils() {
    }

    /**
     * To show loading indication dialog.
     *
     * @param context the context.
     * @return progress dialog instance.
     */
    public static ProgressDialog showLoadingDialog(Context context) {
        if (context != null) {
            ProgressDialog progressDialog = new ProgressDialog(context, R.style.LoaderDialogThemeStatusBar);
            if (!((Activity) context).isFinishing()) {
                // Loading icons array
                final int[] loaderDrawables = {R.drawable.ic_loader_round_logo,
                        R.drawable.ic_loader_bike,
                        R.drawable.ic_loader_gun, R.drawable.ic_loader_services, R.drawable.ic_loader_navigate};
                progressDialog.show();
                // Setting customView
                progressDialog.setContentView(R.layout.layout_loading);
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
            }
            return progressDialog;
        }
        return null;
    }

    /**
     * To remove the handler callbacks
     */
    public static void removeHandlerCallbacks() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * Dialog for showing No internet
     *
     * @param context : Context
     * @return : Dialog
     */
    public static Dialog showNoInternetDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.NoInternetDialogTheme);
        dialog.setContentView(R.layout.activity_done_successfully);
        dialog.setCancelable(false);
        ImageView imgtick = dialog.findViewById(R.id.imageView6);
        ImageView imgbike = dialog.findViewById(R.id.image_bike);
        ImageView imgroad = dialog.findViewById(R.id.image_road);
        TextView mTitle = dialog.findViewById(R.id.textview_message);
        mTitle.setText(context.getResources().getString(R.string.text_nointernet));
        try {
            boolean value = REPreference.getInstance().getBoolean(context,"pip");
            imgtick.setVisibility(value ? GONE : VISIBLE);
            imgbike.setVisibility(value ? GONE : VISIBLE);
            imgroad.setVisibility(value ? GONE : VISIBLE);
                mTitle.setTextSize(value ? 8: 24);
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
        return dialog;
    }

}

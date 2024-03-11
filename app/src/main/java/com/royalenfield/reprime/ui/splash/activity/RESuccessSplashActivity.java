package com.royalenfield.reprime.ui.splash.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.home.service.listener.IServiceStatusListener;
import com.royalenfield.reprime.ui.riderprofile.activity.SettingsActivity;

public class RESuccessSplashActivity extends REBaseActivity {

    private Handler handler = new Handler();
    private Runnable mRunnable;
    static IServiceStatusListener mServiceStatusListener = null;
    private String mTitle;

    public static Intent getIntent(Context context, IServiceStatusListener serviceStatusListener, String title) {
        mServiceStatusListener = serviceStatusListener;
        Intent intent = new Intent(context, RESuccessSplashActivity.class);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_successfully);
        initViews();
        screenTimeout();
    }

    /**
     * Initialising views
     */
    private void initViews() {
        TextView mMessage = findViewById(R.id.textview_message);
        mTitle = getIntent().getStringExtra("title");
        //setting Message
        mMessage.setText(mTitle);
        if (mTitle.equalsIgnoreCase("EDIT BCT")){
            mMessage.setText(getResources().getString(R.string.bct_edit_success_msg));
            mMessage.setTextSize(18);
        }
    }

    /**
     * Screen to show account creation success.
     */
    private void screenTimeout() {
        int SPLASH_TIMEOUT = 6000;
        mRunnable = new Runnable() {
            public void run() {
                if (handler != null) {
                    handler.removeCallbacks(mRunnable);
                }
                finish();
                handleBackPressAndTimeout();
            }
        };
        handler.postDelayed(mRunnable, SPLASH_TIMEOUT);
    }

    /**
     * Handles the back press flow and splash time out flow
     */
    private void handleBackPressAndTimeout() {
        if (mTitle.equals(getApplicationContext().getResources().getString(R.string.text_message_servicebooked)) ||
                mTitle.equals(getApplicationContext().getResources().getString(R.string.text_message_servicerescheduled))) {
            mServiceStatusListener.openServiceStatusFragment();
        } else if (mTitle.equals(getApplicationContext().getResources().getString(R.string.software_update_success))) {
            //Otap Success.
        } else if (mTitle.equals("EDIT BCT")) {
            //do nothing
        } else {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        handleBackPressAndTimeout();
    }
}

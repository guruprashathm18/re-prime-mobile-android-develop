package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.TEXT_SPLASH_DESC_SUCCESS;

/**
 * @author BOP1KOR on 3/29/2019.
 * <p>
 * Responsible for showing the join ride adding status progress.
 */
public class JoinRideStatusActivity extends REBaseActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private Handler mHandler = new Handler();
    private int mProgressBarStatus = 0;
    private int mProgressBarText = 3;
    private String strMsg;

    public JoinRideStatusActivity() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_ride_status);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.tv_second_countdown_timer);
        TextView tvRideDesc = findViewById(R.id.tv_adding_to_rides);

        if (getIntent().getExtras() != null) {
            String strDesc = getIntent().getExtras().getString(TEXT_SPLASH_DESC_SUCCESS);
            strMsg = getIntent().getExtras().getString("msg", "");
            tvRideDesc.setText(strDesc);
        }

        updateStatus();
    }


    public void updateStatus() {
        new Thread(() -> {
            while (mProgressBarStatus < 100) {
                mProgressBarStatus += 1;
                if (mProgressBarStatus == 25 || mProgressBarStatus == 50 || mProgressBarStatus == 75
                        || mProgressBarStatus == 100) {
                    if (mProgressBarText != 0)
                        mProgressBarText--;
                }
                mHandler.post(() -> {
                    progressBar.setProgress(mProgressBarStatus);
                    textView.setText(mProgressBarText + "\nsecs");
                    if (mProgressBarStatus == 100) {
                        finish();
                        gotoActivity();
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    RELog.e(e);
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    /**
     * Opens the DealerRideDetailsActivity by setting type as unjoin ride
     */
    private void gotoActivity() {
        try {
            if (strMsg.equalsIgnoreCase("home")) {
//                Intent intent = new Intent(getApplicationContext(), REHomeActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            } else if (strMsg.equalsIgnoreCase("modify")) {
                Intent intent = new Intent(getApplicationContext(), REProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            } else {
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

}

package com.royalenfield.reprime.ui.home.navigation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bosch.softtec.components.core.conversion.LengthUnit;
import com.bosch.softtec.components.core.conversion.SpeedUnit;
import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.core.models.Speed;
import com.royalenfield.bluetooth.otap.RELogger;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.preference.PreferenceException;
import com.royalenfield.reprime.preference.REPreference;
import com.royalenfield.reprime.utils.REUtils;
import com.royalenfield.reprime.utils.REConstants;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.BCT_DISTANCE;
import static com.royalenfield.reprime.utils.REConstants.BCT_DURATION;
import static com.royalenfield.reprime.utils.REConstants.BCT_SPEED;

/**
 * This activity is used to show navigation summary
 */
public class NavigationSummaryActivity extends REBaseActivity implements View.OnClickListener {

    private static final String TAG = NavigationSummaryActivity.class.getSimpleName();
    private RELogger reLogger;
    private TextView tvTitle,tvRouteSummaryText, avgSpeed,timeTaken, totalDistance,totalDistanceDisclaimer;
    private ImageView closeIMG, staticImage1, staticImage2, staticImage3;
    boolean isMilesUnitSelected =  false;
    private String journeySpeed="0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_summary);
        reLogger = new RELogger(this);
        initViews();


    }

    @Override
    protected void onResume() {
        super.onResume();
        isMilesUnitSelected = REUtils.isMilesUnitSelected();

        try {
            boolean value = REPreference.getInstance().getBoolean(this,"pip");
            handleUIForPiPMode(value);
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the views.
     */
    private void initViews() {
        REApplication.getInstance().setNavigationInProgress(false);
        isMilesUnitSelected = REUtils.isMilesUnitSelected();

        tvTitle = findViewById(R.id.nav_summary_title);
         avgSpeed = findViewById(R.id.avg_speed);
         timeTaken = findViewById(R.id.time_duration);
         totalDistance = findViewById(R.id.total_distance);
        tvRouteSummaryText = findViewById(R.id.route_summary);
         totalDistanceDisclaimer = findViewById(R.id.total_distance_disclaimer_TXT);
         closeIMG = findViewById(R.id.nav_summary_close_IMG);
        closeIMG.setVisibility(View.VISIBLE);
        closeIMG.setOnClickListener(this);
        staticImage1 = findViewById(R.id.image_bike);
        staticImage2 = findViewById(R.id.image_road);
        staticImage3 = findViewById(R.id.imageView8);
        String strTitle = null;
        String strDistance ="";
        String strDuration="";

        try {
            Intent intent = getIntent();
            if (intent != null) {
                strTitle = intent.getStringExtra("action");
                if (strTitle != null) {
                    if (strTitle.equalsIgnoreCase("NoSave")) {
                        tvTitle.setVisibility(View.GONE);
                    } else if (strTitle.equalsIgnoreCase("Save")) {
                        tvTitle.setText(R.string.nav_route_save);
                    } else if (strTitle.equalsIgnoreCase("Share")) {
                        tvTitle.setText(R.string.nav_route_share);
                    } else if (strTitle.equalsIgnoreCase("nav_end")) {
                        tvTitle.setText(R.string.nav_end_msg);
                    }
                }
                Double str_Duration = intent.getDoubleExtra(BCT_DURATION,0.0);
                strDuration = REUtils.getDurationInUnits(str_Duration);
                if (strDuration !=null) {
                    if (isPreferenceComma()) {
                        strDuration = strDuration.replace(".", ",");
                    }
                    timeTaken.setText(String.format(getResources().getString(R.string.time_reach), strDuration));
                } else{
                    timeTaken.setText(getResources().getString(R.string.time_reach_null));
                }
                //String strDistance = intent.getStringExtra(BCT_DISTANCE);
                Double mDistance = intent.getDoubleExtra(BCT_DISTANCE,0.0);

                if (isMilesUnitSelected){
                    strDistance = REUtils.formatDistanceInMilesUnit(new Distance(mDistance, LengthUnit.METERS));
                }
                else{
                    strDistance = REUtils.getDistanceInUnits(mDistance);
                }
                if (isPreferenceComma()){
                    strDistance = strDistance.replace(".",",");
                }
                totalDistance.setText(String.format(getResources().getString(R.string.total_distance), strDistance));
                double speed = intent.getDoubleExtra(BCT_SPEED, 0.0);
                String strSpeed = "";
                if(isMilesUnitSelected){
                    Speed mph = new Speed(speed, SpeedUnit.KILOMETERS_PER_HOUR);
                    Double speedValue = mph.toMilesPerHour();
                    if (speedValue!=null){
                        journeySpeed=speedValue+" mph";
                    }
                    strSpeed = String.format(getResources().getString(R.string.speed_msg_miles), String.valueOf(Math.round(speedValue)));
                }else{
                    strSpeed = String.format(getResources().getString(R.string.speed_msg), String.valueOf(Math.round(speed)));
                    journeySpeed=speed+" kmph";
                }


                if (isPreferenceComma()) {
                    avgSpeed.setText(strSpeed.replace(".",","));
                } else {
                    avgSpeed.setText(strSpeed);
                }
            }

        } catch (Exception e) {
            RELog.e(TAG,"initViews:" + e.getMessage() + ": cause:" + e.getCause());
            reLogger.appendLog("initViews:" + e.getMessage() + ": cause:" + e.getCause());
            avgSpeed.setText(getResources().getString(R.string.speed_msg_null));
            timeTaken.setText(getResources().getString(R.string.time_reach_null));
            totalDistance.setText(getResources().getString(R.string.total_distance_null));
            closeIMG.setVisibility(View.VISIBLE);
            closeIMG.setOnClickListener(this);
        }

        Bundle aParams = new Bundle();
        aParams.putString("screenName", "Tripper Summary Screen");
        aParams.putString("Journey_time", strDuration);
        aParams.putString("Journey_kms", strDistance);
        aParams.putString("Journey_speed", journeySpeed+"");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, aParams);

        assert strTitle != null;
        Log.e("title",strTitle);
        if (!strTitle.equalsIgnoreCase("nav_end") ||!strTitle.equalsIgnoreCase("Save") || !strTitle.equalsIgnoreCase("Share")) {
           // startSplashTimer();
        }
        if (strTitle.equalsIgnoreCase("Share"))
            startSplashTimer();
    }

    private void startSplashTimer() {
        Handler handler = new Handler();
        handler.postDelayed(this::dismissScreen, 3000);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.nav_summary_close_IMG) {
                dismissScreen();
            }
        } catch (Exception e) {
            RELog.e(e);
            dismissScreen();
        }
    }

    private void dismissScreen() {
        try {
            finish();
        } catch (Exception e) {
            RELog.e(e);
        }
    }
    private boolean isPreferenceComma(){
        boolean value = false;
        try {
            if (REPreference.getInstance().getString(REApplication.getAppContext(), REConstants.KEY_COMMA_OR_POINT,
                    REConstants.KEY_POINT).equalsIgnoreCase(REConstants.KEY_COMMA)){
                value = true;
            }
        } catch (PreferenceException e) {
            e.printStackTrace();
        }
        return value;
    }
    public void handleUIForPiPMode(boolean value){
        closeIMG.setVisibility(value ? View.GONE:View.VISIBLE);
        staticImage1.setVisibility(value ? View.GONE:View.VISIBLE);
        staticImage2.setVisibility(value ? View.GONE:View.VISIBLE);
        staticImage3.setVisibility(value ? View.GONE:View.VISIBLE);
        totalDistanceDisclaimer.setVisibility(value ? View.GONE:View.VISIBLE);
        tvTitle.setTextSize(value ? 8:24);
        tvRouteSummaryText.setTextSize(value ? 7:20);
        avgSpeed.setTextSize(value ? 5: 18);
        timeTaken.setTextSize(value ? 5:18);
        totalDistance.setTextSize(value ? 5:18);
    }
}



package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity;
import com.royalenfield.reprime.ui.home.rides.interactor.CreateRideInteractor;
import com.royalenfield.reprime.ui.home.rides.presenter.CreateRidePresenter;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.ui.home.rides.views.ICreateRideView;
import com.royalenfield.reprime.utils.REUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.royalenfield.reprime.utils.RELog;

/**
 * This activity is used for ride confirmation
 */
public class RideSaveShareConfirmationActivity extends REBaseActivity implements View.OnClickListener, ICreateRideView {

    ImageView mImageClose;
    Button mButtonYes, mButtonNo;
    String mRideStartDate;
    private ArrayList<WayPointsData> mGetRideRoute;
    private String mGetRideDate;
    private String mGetRideStartTime;
    private int mGetRideDuration;
    private CreateRidePresenter mCreateRidePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_save_confirm);
        initViews();
        mCreateRidePresenter = new CreateRidePresenter(this, new CreateRideInteractor());
    }

    /**
     * Initialising views
     */
    private void initViews() {

        Intent intent = getIntent();
        mGetRideRoute = intent.getParcelableArrayListExtra("RideRoute");
        mGetRideDate = intent.getStringExtra("StartDate");
        mGetRideStartTime = intent.getStringExtra("StartTime");
        mGetRideDuration = intent.getIntExtra("Duration", 1);

        mImageClose = findViewById(R.id.imageView_close);
        mImageClose.setOnClickListener(this);
        mButtonYes = findViewById(R.id.button_yes);
        mButtonYes.setOnClickListener(this);
        mButtonNo = findViewById(R.id.button_no);
        mButtonNo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_close:
                finish();
                overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
                break;
            case R.id.button_yes:
                finish();
                Intent shareRideIntent = new Intent(getApplicationContext(), ShareYourRideActivity.class);
                shareRideIntent.putParcelableArrayListExtra("RideRoute", mGetRideRoute);
                shareRideIntent.putExtra("StartDate", mGetRideDate);
                shareRideIntent.putExtra("StartTime", mGetRideStartTime);
                shareRideIntent.putExtra("Duration", mGetRideDuration);
                startActivity(shareRideIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);
                break;
            case R.id.button_no:
                double slat = mGetRideRoute.get(0).getLatitude();
                double slon = mGetRideRoute.get(0).getLongitude();
                ArrayList<Double> startPointCoordinates = new ArrayList<>();
                startPointCoordinates.add(slat);
                startPointCoordinates.add(slon);
                double elat = mGetRideRoute.get(mGetRideRoute.size() - 1).getLatitude();
                double elon = mGetRideRoute.get(mGetRideRoute.size() - 1).getLongitude();
                ArrayList<Double> endPointCoordinates = new ArrayList<>();
                endPointCoordinates.add(elat);
                endPointCoordinates.add(elon);
                String userId = REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId();
                String rideDuration;
                if (mGetRideDuration <= 1) {
                    rideDuration = mGetRideDuration + " day";
                } else {
                    rideDuration = mGetRideDuration + " days";
                }
                /*mCreateRidePresenter.createRide("500", mGetRideRoute, null, null,
                        userId, startPointCoordinates, endPointCoordinates,
                        null, null, mGetRideRoute.get(mGetRideRoute.size() - 1).getPlaceName(), mGetRideRoute.get(0).getPlaceName(),
                        null, mGetRideStartTime, rideDuration, getRideEndDate(), mRideStartDate,
                        null, "Public");*/
                break;
            default:
                break;
        }
    }

    private String getRideEndDate() {
        String dd, mm, yyyy;
        Calendar cal = Calendar.getInstance();
        String[] monthName = getApplicationContext().getResources().getStringArray(R.array.month_name_arrays);
        SimpleDateFormat spf = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date newDate = spf.parse(mGetRideDate);
            spf = new SimpleDateFormat("dd/MM/yyyy");
            mRideStartDate = spf.format(newDate);
            dd = mRideStartDate.substring(0, 2);
            mm = mRideStartDate.substring(3, 5);
            yyyy = mRideStartDate.substring(6, 10);
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
            cal.set(Calendar.MONTH, Integer.parseInt(mm) - 1);
            cal.set(Calendar.YEAR, Integer.parseInt(yyyy));
            mRideStartDate = String.format(getResources().getString(R.string.time_date_name), cal.get(Calendar.DAY_OF_MONTH) +
                    REUtils.getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + "' " + cal.get(Calendar.YEAR));
            cal.add(Calendar.DAY_OF_MONTH, mGetRideDuration);
            return String.format(getResources().getString(R.string.time_date_name), cal.get(Calendar.DAY_OF_MONTH) +
                    REUtils.getOrdinalFor(cal.get(Calendar.DAY_OF_MONTH)) + " " + monthName[cal.get(Calendar.MONTH)] + "' " + cal.get(Calendar.YEAR));
        } catch (Exception e) {
            RELog.e(e);
        }
        return null;
    }

    @Override
    public void onCreateRideSuccess() {
        hideLoading();
        Toast.makeText(getApplicationContext(), R.string.message_ride_created, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), REHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onFailure(String errorMessage) {
        hideLoading();
        REUtils.showErrorDialog(this, errorMessage);
    }
}

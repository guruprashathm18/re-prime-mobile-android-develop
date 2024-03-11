package com.royalenfield.reprime.ui.home.rides.fragment.dealer;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.configuration.ConfigurationPrefManager;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.activity.CreateRideActivity;
import com.royalenfield.reprime.ui.home.rides.activity.DealerRideDetailsActivity;
import com.royalenfield.reprime.ui.home.rides.adapter.DealerRidesListAdapter;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesDistanceCalcAsyncTask;
import com.royalenfield.reprime.ui.home.rides.custom.RecyclerViewItemOffsetDecoration;
import com.royalenfield.reprime.ui.home.rides.views.DealerRidesListView;
import com.royalenfield.reprime.ui.home.rides.views.RidesHomeView;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.royalenfield.reprime.utils.REFirestoreConstants.DEALER_UPCOMING_RIDES;

/**
 * @author BOP1KOR on 3/26/2019.
 * <p>
 * A simple {@link Fragment} subclass.
 * This Fragment Class display the list of the Dealer rides in the horizontal view.
 */
public class DealerRidesHomeFragment extends REBaseFragment implements DealerRidesListAdapter.OnItemClickListener,DealerRidesListAdapter.OnLoadMoreClickListener,
        RidesHomeView, RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> {
    private static final String TAG = DealerRidesHomeFragment.class.getSimpleName();

    //Views....
    private View mRootView;
    private TextView mErrorDealerText;
    private DealerRidesListView mDealerRidesListView;
    private final List<DealerUpcomingRidesResponse> mDealerRideList= new ArrayList<>();
    private int clickedPosition=-1;
    Boolean isLoadMoreNeeded=false;
    Boolean isGeoqueryEnabled = false;
    private static final int REQUEST_PERMISSION_SETTING = 14;


    public DealerRidesHomeFragment() {
        // Required empty public constructor
    }

//    public static DealerRidesHomeFragment newInstance() {
//        return new DealerRidesHomeFragment();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_dealer_rides, container, false);
        mErrorDealerText = mRootView.findViewById(R.id.tv_error_dealer_ride);
        mErrorDealerText.setVisibility(View.VISIBLE);
        mErrorDealerText.setText(R.string.text_loading);
        mDealerRidesListView = mRootView.findViewById(R.id.recycler_view_dealer_rides);
        mDealerRidesListView.addOnItemTouchListener(new RERecyclerTouchListener());
        mDealerRidesListView.addItemDecoration(new RecyclerViewItemOffsetDecoration(18,
                RecyclerViewItemOffsetDecoration.HORIZONTAL));
        getDealerUpcomingRides();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Fetches the dealer data from firestore
     */
    private void getDealerUpcomingRides() {
        double lat=REUserModelStore.getInstance().getLatitude();
        double lon=REUserModelStore.getInstance().getLongitude();

        if (REUtils.getFeatures().getDefaultFeatures().getIs_geo_query_enabled().equalsIgnoreCase(REConstants.FEATURE_ENABLED)){
            isGeoqueryEnabled = true;
        }
        // apply the new geoquery logic here
        if (isGeoqueryEnabled && isLocationEnabled(getContext()) && isGpsEnabled(getContext()) && lat!=0.0 && lon!=0.0) {
           // FirestoreManager.getInstance().getDealerUpcomingRidesGeoQuery(this,0.0,0.0);
            FirestoreManager.getInstance().getDealerUpcomingRidesGeoQuery(this,lat,lon);
            isLoadMoreNeeded=false;
        } else {
//            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                displayNeverAskAgainDialog();
//            }
            FirestoreManager.getInstance().getDealerUpcomingRides(this);
            isLoadMoreNeeded=true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirestoreManager.getInstance().removeUserRidesListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FirestoreManager.getInstance().removeUserRidesListener();
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(getResources().getString(R.string.customalert_title));
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion >= Build.VERSION_CODES.Q) {
            builder.setMessage(getResources().getString(R.string.text_bg_location_android_10));
        } else {
            builder.setMessage(getResources().getString(R.string.text_bg_location_android_9));
        }
        builder.setCancelable(true);

        builder.setPositiveButton(getResources().getString(R.string.customalert_ok), (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(getResources().getString(R.string.text_package),
                    getContext().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
        });

        builder.setNegativeButton("Cancel",((dialog, which) -> {
            FirestoreManager.getInstance().getDealerUpcomingRides(this);
            isLoadMoreNeeded=true;
        }));
        builder.show();
    }

    /**
     * Sets the dealer ride list view with the data.
     */
    private void setDealerRideListView() {
        try {
            if (mRootView != null && mDealerRideList != null && mDealerRideList.size() > 0) {
                mErrorDealerText.setVisibility(View.GONE);
                mDealerRidesListView.setVisibility(View.VISIBLE);
//                mDealerRidesListView.setHasFixedSize(true);
              //  Log.e("test","dealer upcoming rides size : "+mDealerRideList.size());
                //Adapter....
//                DealerRidesListAdapter mDealerRidesListAdapter =
//                        new DealerRidesListAdapter(getActivity(), mDealerRideList, this);
                DealerRidesListAdapter mDealerRidesListAdapter =
                        new DealerRidesListAdapter(getActivity(),mDealerRideList, mDealerRidesListView,isLoadMoreNeeded,this,this);
                mDealerRidesListView.setAdapter(mDealerRidesListAdapter);

            } else {
                mErrorDealerText.setVisibility(View.VISIBLE);
                mErrorDealerText.setText(R.string.text_dealer_rides_error);
            }
        } catch (Exception e) {
            RELog.e(e);
        }

    }


    @Override
    public void onItemClick(View view, int position, long id, Bitmap rideBitmap) {
        clickedPosition=position;
        try {
            if(REUtils.isUserLoggedIn()) {
                Intent intent = new Intent(getContext(), DealerRideDetailsActivity.class);
                intent.putExtra(REConstants.POSITION, position);
                intent.putExtra(REConstants.DEALER_RIDES_TYPE, REConstants.TYPE_JOIN_DEALER_RIDE);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);

            }
            else{
                startActivityForResult(new Intent(getContext(), UserOnboardingActivity.class), LoginActivity.CODE_CREATE_RIDE_ACTIVITY);
               getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_CREATE_RIDE_ACTIVITY) {
            if(clickedPosition>=0) {
                Intent intent = new Intent(getContext(), DealerRideDetailsActivity.class);
                intent.putExtra(REConstants.POSITION, clickedPosition);
                intent.putExtra(REConstants.DEALER_RIDES_TYPE, REConstants.TYPE_JOIN_DEALER_RIDE);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
            }
        }
    }

    @Override
    public void onDealerRideResponse() {
        if (isAdded() && getContext() != null) {
            //This helps if there is an update in firesore dealer rides data
            mDealerRidesListView.setVisibility(View.GONE);
            RidesDistanceCalcAsyncTask ridesDistanceCalcAsyncTask =
                    new RidesDistanceCalcAsyncTask(DEALER_UPCOMING_RIDES, this);
            ridesDistanceCalcAsyncTask.execute();
        }
    }

    @Override
    public void onDealerRideFailure(String errorMessage) {
        if (isAdded() && getContext() != null) {
            mDealerRidesListView.setVisibility(View.GONE);
            mErrorDealerText.setVisibility(View.VISIBLE);
            if (errorMessage.equals(getString(R.string.text_no_dealerrides))) {
                mErrorDealerText.setText(errorMessage);
            } else {
                mErrorDealerText.setText(R.string.text_no_dealerrides);
            }
        }
    }

    @Override
    public void onPopularRideResponse() {

    }

    @Override
    public void onPopularRideResponseFailure(String throwable) {

    }

    @Override
    public void onMarqueeRideResponse() {

    }

    @Override
    public void onMarqueeRideResponseFailure(String errorMessage) {

    }

    @Override
    public void onUserCreatedRideResponse() {

    }

    @Override
    public void onUserCreatedRideResponseFailure(String errorMessage) {

    }

    @Override
    public void onRidesDistanceCalcComplete(List result) {
        if (isAdded() && getContext() != null) {
            if (result.size()<25)
            {
                isLoadMoreNeeded=false;
            }
            mDealerRideList.addAll(result);
            RERidesModelStore.getInstance().setDealerUpcomingRidesResponse(mDealerRideList);
            setDealerRideListView();
        }
    }

    @Override
    public void onRideJoinedComplete() {

    }

    @Override
    public void onLoadMoreClick(View view, int position) {
        //check geoquery enabled or not then apply logic
        FirestoreManager.getInstance().getDealerUpcomingRides(this);
    }

    public static Boolean isGpsEnabled(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
// This is Deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }
}

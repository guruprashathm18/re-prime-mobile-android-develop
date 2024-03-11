package com.royalenfield.reprime.ui.home.rides.fragment.usercreatedrides;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.ui.home.rides.activity.DealerRideDetailsActivity;
import com.royalenfield.reprime.ui.home.rides.adapter.UserCreatedRidesAdapter;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesDistanceCalcAsyncTask;
import com.royalenfield.reprime.ui.home.rides.views.RidesHomeView;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.royalenfield.reprime.utils.REFirestoreConstants.USER_UPCOMING_RIDES;


/**
 * Fragments holds the views related to user created rides.
 */
public class UserCreatedRidesHomeFragment extends REBaseFragment implements RidesHomeView,
        RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List>,UserCreatedRidesAdapter.OnLoadMoreClickListener {

    private RecyclerView mUserCreatedRidesRecyclerView;
    private TextView mErrorText;
    private UserCreatedRidesAdapter mUserCreatedRidesAdapter;
    private final List<UserUpcomingRidesResponse> userUpcomingRidesResponse= new ArrayList<>();
    private final List<UserUpcomingRidesResponse> userUpcomingRidesResponseFiltered= new ArrayList<>();
    Boolean isLoadMoreNeeded=false;
    Boolean isGeoqueryEnabled=false;


    public UserCreatedRidesHomeFragment() {
        // Required empty public constructor
    }

//    public static UserCreatedRidesHomeFragment newInstance() {
//        return new UserCreatedRidesHomeFragment();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_created_rides, container, false);
        initViews(view);
        getUserUpcomingRidesFromFireStore();
        return view;
    }

    /**
     * Fetches the user upcoming rides data from fireStore
     */
    private void getUserUpcomingRidesFromFireStore() {
        double lat= REUserModelStore.getInstance().getLatitude();
        double lon= REUserModelStore.getInstance().getLongitude();
//        FirestoreManager.getInstance().getUserUpcomingRidesGeoQuery(this,lat,lon);
//        FirestoreManager.getInstance().getUserUpcomingRides(this);
        if (REUtils.getFeatures().getDefaultFeatures().getIs_geo_query_enabled().equalsIgnoreCase(REConstants.FEATURE_ENABLED)){
            isGeoqueryEnabled = true;
        }
        if (isGeoqueryEnabled && isLocationEnabled(getContext()) &&isGpsEnabled(getContext()) && lat!=0.0 && lon!=0.0) {
            // FirestoreManager.getInstance().getUserUpcomingRidesGeoQuery(this,0.0,0.0);
            FirestoreManager.getInstance().getUserUpcomingRidesGeoQuery(this,lat,lon);
            isLoadMoreNeeded=false;
        } else {
            // FirestoreManager.getInstance().getUserUpcomingRides(this);
            FirestoreManager.getInstance().getUserUpcomingRides(this);
            isLoadMoreNeeded=true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        /*if (mUserCreatedRidesAdapter!=null)
        mUserCreatedRidesAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirestoreManager.getInstance().removeDealerRidesListener();
    }

    private void initViews(View view) {
        mErrorText = view.findViewById(R.id.textView_error);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(R.string.text_loading);
        mUserCreatedRidesRecyclerView = view.findViewById(R.id.recyclerView_user_created_rides);
        mUserCreatedRidesRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
        RecyclerView.LayoutManager mUserCreatedRidesLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mUserCreatedRidesRecyclerView.setLayoutManager(mUserCreatedRidesLayoutManager);
    }

    private void setUserRideAdapter() {
        userUpcomingRidesResponseFiltered.clear();
        Log.e("test", " user upcoming rides size A: " + userUpcomingRidesResponse.size());
        for (int i=0;i<userUpcomingRidesResponse.size();i++) {
            if (!userUpcomingRidesResponse.get(i).getCreatedBy().equals(REUtils.getUserId())) {
                userUpcomingRidesResponseFiltered.add(userUpcomingRidesResponse.get(i));
            }
        }
        if (userUpcomingRidesResponseFiltered != null && userUpcomingRidesResponseFiltered.size() > 0) {
            Log.e("test", " user upcoming rides size : " + userUpcomingRidesResponseFiltered.size());
            mUserCreatedRidesAdapter = new UserCreatedRidesAdapter(this, getActivity(), isLoadMoreNeeded,
                    userUpcomingRidesResponseFiltered, this);
            mErrorText.setVisibility(View.GONE);
            mUserCreatedRidesRecyclerView.setVisibility(View.VISIBLE);
            mUserCreatedRidesRecyclerView.setAdapter(mUserCreatedRidesAdapter);
            mUserCreatedRidesAdapter.notifyDataSetChanged();
        } else {
            Log.e("test", "upcoming rides is null ");
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(R.string.text_no_userrides);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FirestoreManager.getInstance().removeUserRidesListener();
    }

    @Override
    public void onDealerRideResponse() {

    }

    @Override
    public void onDealerRideFailure(String errorMessage) {

    }

    @Override
    public void onMarqueeRideResponse() {

    }

    @Override
    public void onMarqueeRideResponseFailure(String throwable) {

    }

    @Override
    public void onPopularRideResponse() {

    }

    @Override
    public void onPopularRideResponseFailure(String throwable) {

    }

    @Override
    public void onUserCreatedRideResponse() {
        if (isAdded() && getContext() != null) {
            //This helps if there is an update in firesore user rides data
            mUserCreatedRidesRecyclerView.setVisibility(View.GONE);
            RidesDistanceCalcAsyncTask ridesDistanceCalcAsyncTask =
                    new RidesDistanceCalcAsyncTask(USER_UPCOMING_RIDES, this);
            ridesDistanceCalcAsyncTask.execute();
        }
    }

    @Override
    public void onUserCreatedRideResponseFailure(String errorMessage) {
        if (isAdded() && getContext() != null) {
            mUserCreatedRidesRecyclerView.setVisibility(View.GONE);
            mErrorText.setVisibility(View.VISIBLE);
            if (errorMessage.equals(getString(R.string.text_no_userrides))) {
                mErrorText.setText(errorMessage);
            } else {
                mErrorText.setText(R.string.text_no_userrides);
            }
        }
    }

    @Override
    public void onRidesDistanceCalcComplete(List result) {
        if (isAdded() && getContext() != null) {
            if(result.size()<25)
            {
                isLoadMoreNeeded=false;
            }
            userUpcomingRidesResponse.addAll(result);
            RERidesModelStore.getInstance().setUserUpcomingRidesResponse(userUpcomingRidesResponse);
            setUserRideAdapter();
        }
    }

    @Override
    public void onRideJoinedComplete() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_CREATE_RIDE_ACTIVITY) {
            if(mUserCreatedRidesAdapter!=null){
                mUserCreatedRidesAdapter.gotoDealerRidesActivity();
            }
        }
    }

    @Override
    public void onLoadMoreClick(View view, int position) {
        FirestoreManager.getInstance().
                getUserUpcomingRides(this);
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

package com.royalenfield.reprime.ui.home.homescreen.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.datatransport.runtime.dagger.Reusable;
import com.royalenfield.appIndexing.REAppIndexScreen;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.models.response.remoteconfig.RidesKeys;
import com.royalenfield.reprime.ui.home.homescreen.adapter.HomePagerAdapter;
import com.royalenfield.reprime.ui.home.ourworld.fragment.REOurWorldFragment;
import com.royalenfield.reprime.ui.home.rides.activity.CreateRideActivity;
import com.royalenfield.reprime.ui.home.rides.activity.InRideActivity;
import com.royalenfield.reprime.ui.home.rides.activity.OngoingRidesActivity;
import com.royalenfield.reprime.ui.home.rides.fragment.dealer.RERidesFragment;
import com.royalenfield.reprime.ui.onboarding.login.activity.LoginActivity;
import com.royalenfield.reprime.ui.onboarding.useronboarding.activity.UserOnboardingActivity;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static android.app.Activity.RESULT_OK;
import static com.royalenfield.reprime.utils.REConstants.COMMUNITY_DEFAULT_SCREEN;

public class RECommunityFragment extends REBaseFragment implements ViewPager.OnPageChangeListener
        , View.OnClickListener {

    private RiderProfileViewPager mViewPager;
    private TextView tvRides;
    private TextView tvOurWorld;
    private View viewTab1;
    private View viewTab2;
    private RERidesFragment rideFragment;
    private TextView tvCreateRide;
    private static RECommunityFragment reCommunityFragment;
    private ImageView mRidesImage;
    // ConfigFeatures configFeature;
    RidesKeys ridesKeys;
    private REAppIndexScreen mReAppIndexScreen = REAppIndexScreen.OUR_WORLD;

    public static RECommunityFragment newInstance() {
        // Required empty public constructor
        reCommunityFragment = new RECommunityFragment();

        return reCommunityFragment;
    }

    /**
     *
     */
    private BroadcastReceiver mOngoingRideBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (REConstants.FIRESTORE_ONGOING_RIDE.equals(intent.getAction())) {
                // if ongoing ride is pushed to firestore
                setBottomConstraintTextForRides();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.layout_recommunity_fragment, container, false);
        initViews(rootView);
        setBottomConstraintTextForRides();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mOngoingRideBroadcastReceiver,
                new IntentFilter(REConstants.FIRESTORE_ONGOING_RIDE));
        ridesKeys = REUtils.getRidesKeys();
        return rootView;
    }

    private void initViews(View rootView) {
        setupViewPager(rootView);
    }

    private void setupViewPager(View rootView) {
        mRidesImage = rootView.findViewById(R.id.rides_disabled);
        mViewPager = rootView.findViewById(R.id.view_pager_home);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setPagingEnabled(false);

        tvRides = rootView.findViewById(R.id.tv_rides);
        tvOurWorld = rootView.findViewById(R.id.tv_our_world);

        viewTab1 = rootView.findViewById(R.id.viewTab1);
        viewTab2 = rootView.findViewById(R.id.viewTab2);

        tvCreateRide = rootView.findViewById(R.id.tv_create_ride);

        tvRides.setOnClickListener(this);
        tvOurWorld.setOnClickListener(this);

        tvCreateRide.setOnClickListener(this);

        initViewPagerAdapter();
    }

    private void initViewPagerAdapter() {
        try {
            HomePagerAdapter mPagerAdapter = new HomePagerAdapter(getChildFragmentManager(), mViewPager);
            mPagerAdapter.addFrag(getFragments(), getList());
            mViewPager.setAdapter(mPagerAdapter);
            Bundle bundle = getArguments();
            if (bundle != null) {
                mReAppIndexScreen = (REAppIndexScreen) bundle.get(COMMUNITY_DEFAULT_SCREEN);
            }
            if (mReAppIndexScreen.equals(REAppIndexScreen.OUR_WORLD)) {
                mViewPager.setCurrentItem(0);
                tvOurWorld.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvRides.setTextColor(ContextCompat.getColor(getActivity(), R.color.header_text_inactive));
                viewTab1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                viewTab2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
            } else {
                mViewPager.setCurrentItem(1);
                tvOurWorld.setTextColor(ContextCompat.getColor(getActivity(), R.color.header_text_inactive));
                tvRides.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                viewTab1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                viewTab2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            }
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private ArrayList<String> getList() {
        List<String> fmFeatures = Arrays.asList(getResources().getStringArray(R.array.fm_home_features));
        return new ArrayList<>(fmFeatures);
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new REOurWorldFragment());
        rideFragment = new RERidesFragment();
        fragmentArrayList.add(rideFragment);
        return fragmentArrayList;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //check here rides is enabled or not
       /* ConfigFeatures configFeature = REUtils.getConfigFeatures();
        if (position == 1 && configFeature != null && configFeature.getCommunityrides() != null &&
                configFeature.getCommunityrides().getReleaseFeatureStatus().
                        equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED)) {
            mRidesImage.setVisibility(View.VISIBLE);
            RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext())
                    .asBitmap()
                    .load(configFeature.getCommunityrides().getFeatureImageURL());
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            requestBuilder
                    .apply(options)
                    .into(mRidesImage);*/

        RidesKeys ridesKeys = REUtils.getRidesKeys();
        if (position == 1 && REUtils.getFeatures().getDefaultFeatures().getIs_community_rides_enabled().equalsIgnoreCase(REConstants.FEATURE_DISABLED)) {
            mRidesImage.setVisibility(View.VISIBLE);
            RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext())
                    .asBitmap()
                    .load(ridesKeys.getRideFeatureImageURL_disabled());
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            requestBuilder
                    .apply(options)
                    .into(mRidesImage);
        } else {
            mRidesImage.setVisibility(View.GONE);
           /* Log.e("Geoqry","Rid "+REUtils.getFeatures().getDefaultFeatures().getIs_rides_enabled()+"CommRides"+REUtils.getFeatures().getDefaultFeatures().getIs_community_rides_enabled()+"CommEvents"+REUtils.getFeatures().getDefaultFeatures().getIs_community_events_enabled()
            +" Geo "+REUtils.getFeatures().getDefaultFeatures().getIs_geo_query_enabled()+" radius "+REUtils.getRidesKeys().getGeoquery_radius());*/

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewPager != null)
            initViewPagerAdapter();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_our_world) {
            mReAppIndexScreen = REAppIndexScreen.OUR_WORLD;
            Bundle bundle = getArguments();
            assert bundle != null;
            bundle.putSerializable(COMMUNITY_DEFAULT_SCREEN, mReAppIndexScreen);
            mViewPager.setCurrentItem(0);
            tabSelection(1);
        } else if (view.getId() == R.id.tv_rides) {
            mReAppIndexScreen = REAppIndexScreen.RIDES;
            Bundle bundle = getArguments();
            assert bundle != null;
            bundle.putSerializable(COMMUNITY_DEFAULT_SCREEN, mReAppIndexScreen);
            mViewPager.setCurrentItem(1);
            tabSelection(2);
        } else if (view.getId() == R.id.tv_create_ride) {
            if (REUtils.isUserLoggedIn()) {
                List<ProfileRidesResponse> mOngoingRideResponse = REUserModelStore.getInstance().getOngoingRideResponse();
                if (mOngoingRideResponse != null && mOngoingRideResponse.size() > 0) {
                    if (mOngoingRideResponse.size() == 1) {
                        startExplicitActivity(InRideActivity.class);
                    } else {
                        startExplicitActivity(OngoingRidesActivity.class);
                    }
                } else {
                    if (checkConfigFeatureDetails()) {
                        showErrorDialog(getContext(), ridesKeys.getMessage_ride_disabled(), "");
                    } else {
                        startExplicitActivity(CreateRideActivity.class);
                    }
                }
            } else {
                startActivityForResult(new Intent(getContext(), UserOnboardingActivity.class), LoginActivity.CODE_CREATE_RIDE_ACTIVITY);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
    }

    private void startExplicitActivity(Class activity) {
        startActivity(new Intent(getActivity(), activity));
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    private void tabSelection(int position) {
        if (position == 1) {
            /*Our world tab*/
            tvOurWorld.setTextColor(getResources().getColor(R.color.white));
            tvRides.setTextColor(getResources().getColor(R.color.header_text_inactive));

            viewTab1.setBackgroundColor(getResources().getColor(R.color.white));
            viewTab2.setBackgroundColor(getResources().getColor(R.color.black));
            tvCreateRide.setVisibility(View.GONE);

        } else if (position == 2) {
            /*Rides tab*/
            tvRides.setTextColor(getResources().getColor(R.color.white));
            tvOurWorld.setTextColor(getResources().getColor(R.color.header_text_inactive));

            viewTab1.setBackgroundColor(getResources().getColor(R.color.black));
            viewTab2.setBackgroundColor(getResources().getColor(R.color.white));
            tvCreateRide.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the text for rides bottom constraint based on firestore Ongoing rides data
     */
    private void setBottomConstraintTextForRides() {
        List<ProfileRidesResponse> mOngoingRideResponse = REUserModelStore.getInstance().getOngoingRideResponse();
        if (mOngoingRideResponse != null && mOngoingRideResponse.size() > 0) {
            tvCreateRide.setText(R.string.text_inride);
        } else {
            tvCreateRide.setText(R.string.label_create_ride_btn);
        }
    }

/*    @Override
    public void apiResponseFromRideFragment(int iFragResponse) {
        if (rideFragment != null) {
            rideFragment.displayReceivedData(iFragResponse);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LoginActivity.CODE_CREATE_RIDE_ACTIVITY) {
            if (checkConfigFeatureDetails()) {
                showErrorDialog(getContext(), ridesKeys.getMessage_ride_disabled(), "");
            } else {
                startExplicitActivity(CreateRideActivity.class);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mOngoingRideBroadcastReceiver);
    }

    private void showErrorDialog(final Context context, String message, String title) {
        try {
            TextView alert_title, alert_message, alert_ok;
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Custom layout initialization
            View alertLayout = layoutInflater.inflate(R.layout.dialog_customalert, null);
            alert_message = alertLayout.findViewById(R.id.textView_alert_message);
            alert_title = alertLayout.findViewById(R.id.textView_alert_title);
            if (title != null && title.length() > 0) {
                alert_title.setText(title);
            }
            //setting message for alert dialog
            alert_message.setText(message);
            alert_ok = alertLayout.findViewById(R.id.textView_alert_okbutton);
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // allows cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);
            final AlertDialog dialog = alert.create();
            dialog.show();
            //This is for ok click which dismisses the dialog
            alert_ok.setOnClickListener(v -> {
                dialog.dismiss();
            });
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    private boolean checkConfigFeatureDetails() {
        // configFeature = REUtils.getConfigFeatures();
        /*if (configFeature != null && configFeature.getCreateRide() != null && configFeature.getCreateRide().
                getReleaseFeatureStatus().equalsIgnoreCase(REConstants.FEATURE_STATUS_DISABLED) && configFeature.getCreateRide().getCategory().equalsIgnoreCase("2")) {
            return true;*/
        if (REUtils.getFeatures().getDefaultFeatures().getIs_rides_enabled().equalsIgnoreCase(REConstants.FEATURE_DISABLED)) {
            return true;

        } else {
            return false;
        }
    }
}

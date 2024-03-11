package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.tabs.TabLayout;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;
import com.royalenfield.reprime.ui.onboarding.editprofile.listeners.OnEditProfileFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.activity.VehicleDataModel;
import com.royalenfield.reprime.ui.riderprofile.activity.REProfileActivity;
import com.royalenfield.reprime.ui.riderprofile.adapter.RiderProfileFragmentPagerAdapter;
import com.royalenfield.reprime.ui.riderprofile.interactor.RiderProfileUploadInteractor;
import com.royalenfield.reprime.ui.riderprofile.presenter.RiderProfileUploadPresenter;
import com.royalenfield.reprime.ui.riderprofile.viewpager.RiderProfileViewPager;
import com.royalenfield.reprime.ui.riderprofile.views.UploadProfilePicView;
import com.royalenfield.reprime.ui.userdatavalidation.popup.activity.TransparentPopActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.FEATURE_DISABLED;
import static com.royalenfield.reprime.utils.REConstants.FEATURE_ENABLED;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class REProfileFragment extends REBaseFragment implements View.OnClickListener,
        TabLayout.OnTabSelectedListener, MotorCycleProfileFragment.OnMotorcycleFragmentInteractionListener, UploadProfilePicView {
    private static final String TAG = REProfileFragment.class.getSimpleName();
    private RiderProfileFragmentPagerAdapter mFragmentPagerAdapter;
    private TabLayout mTabLayout;
    private ImageView mProfileImage;
    private TextView mTvRiderName;
    private TextView mTvRiderEmail;
    private TextView mTvAboutMe;
    private View mViewLineAboutMe;
    private RiderProfileViewPager mViewPager;
    private int mTabPosition;
    private List<String> mTabNameList = new ArrayList<>();
    private List<String> mTabValueCount = new ArrayList<>();
    private static final int IMAGE_UPDATE = 101;
    private static final int NAME_AND_EMAIL_UPDATE = 105;
    private Bitmap mProfileImageDownloaded;
    private List<VehicleDataModel> mVehicheDetailsList = new ArrayList<>();
    private int TAB_RIDES = 0;
    private TextView txtGotoRider;
    private TextView txtErrorFirebase;
    /**
     * Broadcast Receiver for listening the Firestore Vehicle & ServiceHistory updates
     */
    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (REConstants.FIRESTORE_UPDATE.equals(intent.getAction())) {
                getVehicleDetailsFromModelStore();
                Objects.requireNonNull(mTabLayout.getTabAt(0)).setCustomView(null);
                initializeTabList();
                setupTabViews();
            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rerider_profile, container, false);
        initViews(rootView);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).
                registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(REConstants.FIRESTORE_UPDATE));

		LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).
				registerReceiver(mProfileBroadcast,
						new IntentFilter(REConstants.FIRESTORE_PROFILE));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getVehicleDetailsFromModelStore();
        bindRiderProfileData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).
                unregisterReceiver(mRegistrationBroadcastReceiver);
		if (getContext() != null)
			LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mProfileBroadcast);
    }


    /**
     * Gets the vehicle list from modelstore
     */
    private void getVehicleDetailsFromModelStore() {
        mVehicheDetailsList = REServiceSharedPreference.getVehicleData(getContext());
    }

    /**
     * Initializes all the views..
     */
    private void initViews(View view) {
        // Sets the flag to false to laod the image from server every time when opened
        REApplication.getInstance().setIsProfileImageCached(false);
        mTvRiderName = view.findViewById(R.id.tv_rider_name);
        mTvRiderEmail = view.findViewById(R.id.tv_rider_email);
        mTvAboutMe = view.findViewById(R.id.tv_about_me);
        mViewLineAboutMe = view.findViewById(R.id.view_about_me);
        mProfileImage = view.findViewById(R.id.iv_rider_or_motorcycle);
        mViewPager = view.findViewById(R.id.view_pager_profile);
        mTabLayout = view.findViewById(R.id.tabLayout_profile);
        mTabLayout.setVisibility(View.GONE);
        txtErrorFirebase=view.findViewById(R.id.txt_firebase_error);
        txtErrorFirebase.setVisibility(View.GONE);
        ImageView mEditProfile = view.findViewById(R.id.iv_edit_profile);
        mEditProfile.setOnClickListener(this);
        ImageView mEditImage = view.findViewById(R.id.iv_edit_image);
        mEditImage.setOnClickListener(this);
        String showEdit=FEATURE_ENABLED;
        try {
            showEdit = REApplication.getInstance().featureCountry.getShowEditProfile();
            if (showEdit == null) {
                if (REApplication.getInstance().Country.equalsIgnoreCase(REConstants.COUNTRY_INDIA))
                    showEdit = FEATURE_ENABLED;
                else
                    showEdit = FEATURE_DISABLED;
            }
        }
        catch (Exception e){
         //   e.printStackTrace();
        }
        if(showEdit!=null&&showEdit.equalsIgnoreCase(FEATURE_DISABLED)){
            mEditImage.setVisibility(View.GONE);
            mEditProfile.setVisibility(View.GONE);
        }
        else{
            mEditImage.setVisibility(View.VISIBLE);
            mEditProfile.setVisibility(View.VISIBLE);
        }
        if(REUtils.isUserLoggedIn())
            REUtils.getProfileDetailsFromServer(new OnEditProfileFinishedListener() {
				@Override
				public void onUpdateSuccess() {

				}

				@Override
				public void onUpdateFailure(String errorMessage) {

				}

				@Override
				public void onGetProfileDetailsSuccess() {
					if (REUserModelStore.getInstance().getEmail() != null) {
						mTvRiderEmail.setText(REUserModelStore.getInstance().getEmail());
					} else {
						mTvRiderEmail.setText(R.string.data_notavailable);
					}



				}

				@Override
				public void onGetProfileDetailsFailure(String errorCode) {

				}
			});
        txtGotoRider=view.findViewById(R.id.tv_view_goto_rider);
        txtGotoRider.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragme=  mFragmentPagerAdapter.getCurrentFragment();
                if(fragme instanceof  BookingFragment){
                    Bundle  params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Bookings clicked");
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
                    ((BookingFragment) fragme).setDataAndAdapter();

                }
                if(fragme instanceof  TripWebActivity){
                    //    ((TripWebActivity) fragme).initViews(null);

                }
                if(fragme instanceof  GMABookingFragment){
                    //    ((TripWebActivity) fragme).initViews(null);
                    Bundle  params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Accessories Booking clicked");
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
                    ((GMABookingFragment) fragme).setDataAndAdapter();
                }
                if(fragme instanceof MotorCycleProfileFragment){
                    Bundle  params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Motorcycles");
                  //  params.putString("eventLabel", );
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
                    ((MotorCycleProfileFragment) fragme).bindAdapterData();
                }
                if(fragme instanceof  RidesProfileFragment){
                    Bundle  params = new Bundle();
                    params.putString("eventCategory", "User Profile");
                    params.putString("eventAction", "Rides clicked");
                    REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Handles binding the rider profile email, name and profile picture.....
     */
    private void bindRiderProfileData() {
        if (REUserModelStore.getInstance() != null) {
            //Check for null values and setData if it not null else Not Available
            if (REUserModelStore.getInstance() != null) {
                mTvRiderName.setText(String.format(getResources().getString(R.string.name),
                        (REUserModelStore.getInstance().getFname() != null) ? REUserModelStore.getInstance().getFname() : "",
                        (REUserModelStore.getInstance().getLname() != null) ? REUserModelStore.getInstance().getLname() : "" ));
            } else {
                mTvRiderName.setText(R.string.data_notavailable);
            }
            if (REUserModelStore.getInstance().getEmail() != null) {
                mTvRiderEmail.setText(REUserModelStore.getInstance().getEmail());
            } else {
                mTvRiderEmail.setText(R.string.data_notavailable);
            }
            String mAboutMe = REUserModelStore.getInstance().getAboutMe();
            if (mAboutMe != null) {
                mAboutMe = mAboutMe.trim();
            }
            if (mAboutMe != null && !mAboutMe.isEmpty()) {
                mTvAboutMe.setVisibility(View.VISIBLE);
                mViewLineAboutMe.setVisibility(View.VISIBLE);
                mTvAboutMe.setText(mAboutMe);
            }
        } else {
            //Need to update the user profile image by default
            mTvRiderName.setText(R.string.data_notavailable);
            mTvRiderEmail.setText(R.string.data_notavailable);
        }
        loadProfileImage();

        initViewPager();
        new Handler().postDelayed(() -> {
			try {
				if(getActivity()!=null) {
					if (((REProfileActivity) getActivity()).navigateFromDashboard && mTabLayout != null && mTabLayout.getTabAt(1) != null)
						(mTabLayout.getTabAt(1)).select();
				}
			}
			catch (Exception e){

			}

		},300);


    }

	private BroadcastReceiver mProfileBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction() != null) {
				if (REUtils.CHECK_FIREBASE_AUTH_INPROGRESS)
					txtErrorFirebase.setText(getResources().getString(R.string.firebase_auth_progress_fetch_data));
				if (REApplication.getInstance().mFirebaseAuth != null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser()!=null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())) {
					txtErrorFirebase.setVisibility(View.GONE);
					mTabLayout.setVisibility(View.VISIBLE);
					mViewPager.setVisibility(View.VISIBLE);
				}
				else{
					mTabLayout.setVisibility(View.GONE);
					mViewPager.setVisibility(View.GONE);
					txtErrorFirebase.setVisibility(View.VISIBLE);

				}
			}
			((REProfileActivity)getActivity()).initView();

		}
	};

    /**
     * Set profile tabs with all the information..
     */
    private void setupTabViews() {
        for (int tabCount = 0; mTabNameList.size() > tabCount; tabCount++) {
            if (tabCount == TAB_RIDES) {
                Objects.requireNonNull(mTabLayout.getTabAt(tabCount)).
                        setCustomView(mFragmentPagerAdapter.getSelectedTabView(getActivity(), "",
                                mTabNameList.get(tabCount)));
            } else {
                Objects.requireNonNull(mTabLayout.getTabAt(tabCount)).
                        setCustomView(mFragmentPagerAdapter.getTabView(getActivity(),"",
                                mTabNameList.get(tabCount)));
            }
        }
    }

    /**
     * THis method method initialized the data for the Profile Pages Tabs.
     */
    private void initializeTabList() {
        //Clears all the existing data.
        mTabValueCount.clear();
        mTabNameList.clear();
        if(REApplication.getInstance().featureCountry!=null&&REApplication.getInstance().featureCountry.getCommunity().equalsIgnoreCase(FEATURE_ENABLED))

            addRidesData();
        if(REApplication.getInstance().featureCountry!=null&&REApplication.getInstance().featureCountry.getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED)) {
            addMotorcycleData();
            addBookingdata();
        }
        if(REApplication.getInstance().featureCountry!=null&&REApplication.getInstance().featureCountry.getTrips().equalsIgnoreCase(FEATURE_ENABLED)&&REApplication.getInstance().isTripsEnabled)

            addTripdata();
    }

    private void addBookingdata() {
        mTabValueCount.add("");
        mTabNameList.add(getResources().getString(R.string.bookings));
        mTabValueCount.add("");
        mTabNameList.add(getResources().getString(R.string.gma_bookings));
    }

    private void addTripdata() {
        mTabValueCount.add("");
        mTabNameList.add(getResources().getString(R.string.trips));

    }

    /**
     * Adds the Profile Tab data set.
     */
    private void addRidesData() {
        //First Tab Data profile tab.
        mTabValueCount.add("");
        mTabNameList.add("Rides");
    }


    /**
     * Adds the Motorcycle data set.
     */
    private void addMotorcycleData() {
        //Second Tab data Motorcycle count.
        if (mVehicheDetailsList != null && mVehicheDetailsList.size() > 0) {
            mTabValueCount.add("");
            if (mVehicheDetailsList.size() == 1) {
                mTabNameList.add(Objects.requireNonNull(getActivity()).getResources().getString(R.string.tab_text_motorcycle));
            } else {
                mTabNameList.add(Objects.requireNonNull(getActivity()).getResources().getString(R.string.tab_text_motorcycles));
            }
        } else {
            mTabValueCount.add("");
            mTabNameList.add(Objects.requireNonNull(getActivity()).getResources().getString(R.string.tab_text_motorcycle));
        }
    }


    /**
     * Highlights the selected tab of the TabLayout.
     *
     * @param position Selected tab.
     */
    private void highLightCurrentTab(int position) {
        for (int value = 0; value <= mTabLayout.getTabCount() - 1; value++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(value);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mFragmentPagerAdapter.getTabView(getActivity(), "",
                    mTabNameList.get(value)));
        }
        TabLayout.Tab tab = mTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mFragmentPagerAdapter.getSelectedTabView(getActivity(), "",
                mTabNameList.get(position)));
    }

    /**
     * Method to create and return the list of fragments in view pager
     *
     * @return List of tabs
     */
    private List<REBaseFragment> getFragments() {
        //Setting arguments for the fragment
        List<REBaseFragment> fList = new ArrayList<>();
        if(REApplication.getInstance().featureCountry!=null&&REApplication.getInstance().featureCountry.getCommunity().equalsIgnoreCase(FEATURE_ENABLED))
            fList.add(RidesProfileFragment.newInstance());

        if(REApplication.getInstance().featureCountry!=null&&REApplication.getInstance().featureCountry.getMotorcycleInfo().equalsIgnoreCase(FEATURE_ENABLED)) {
            fList.add(MotorCycleProfileFragment.newInstance());


            //  fList.add(BookingFragment.newInstance());
            fList.add(BookingFragment.newInstance());
            fList.add(GMABookingFragment.newInstance());
        }
        if(REApplication.getInstance().featureCountry!=null&&REApplication.getInstance().featureCountry.getTrips().equalsIgnoreCase(FEATURE_ENABLED)&&REApplication.getInstance().isTripsEnabled)

            fList.add(TripWebActivity.Companion.newInstance());
        return fList;
    }

    /**
     * Initiates the viewpager when the getUser API is success
     * Loads the pager
     */
    private void initViewPager() {
        mTabLayout.setVisibility(View.VISIBLE);
        try {
            List<REBaseFragment> mFragments = getFragments();

            //Initialize tab list item data set.
            initializeTabList();
            mViewPager.setPagingEnabled(false);
            //Need to set view pager for te profile....
            if(mFragments.size()>0) {
                mFragmentPagerAdapter = new RiderProfileFragmentPagerAdapter(getChildFragmentManager(), mFragments);
                mViewPager.setAdapter(mFragmentPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mTabLayout.addOnTabSelectedListener(this);
                //Setup the tab views for the profile.....
                setupTabViews();
				if (REUtils.CHECK_FIREBASE_AUTH_INPROGRESS)
					txtErrorFirebase.setText(getResources().getString(R.string.firebase_auth_progress_fetch_data));
                if (REApplication.getInstance().mFirebaseAuth != null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser()!=null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())) {
                    txtErrorFirebase.setVisibility(View.GONE);
					mTabLayout.setVisibility(View.VISIBLE);
					mViewPager.setVisibility(View.VISIBLE);
                }
                else{
                    mTabLayout.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.GONE);
                    txtErrorFirebase.setVisibility(View.VISIBLE);

                }
            }
            else{
                mViewPager.setVisibility(View.GONE);
            }



        } catch (Exception e) {
            RELog.d("TAG",e.getMessage());
        }
    }

    /**
     * Loads the profile image from server
     */
    private void loadProfileImage() {
        if (REUserModelStore.getInstance() != null) {
            String imageUrl = REUserModelStore.getInstance().getProfileUrl();
            RequestListener<Bitmap> listener = new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    RELog.e("ProfileImageLoading Exception :" + e.getMessage() + " " + model);
                    mProfileImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_profile_noimage));
                    return true;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    mProfileImageDownloaded = resource;
                    REApplication.getInstance().setIsProfileImageCached(true);
                    mProfileImage.setImageBitmap(mProfileImageDownloaded);
                    return true;
                }
            };
			if(!getActivity().isDestroyed()) {
				RequestBuilder<Bitmap> requestBuilder = Glide.with(getActivity())
						.asBitmap()
						.load(REUtils.getMobileappbaseURLs().getAssetsURL() + imageUrl)
						.listener(listener);
				RequestOptions options = new RequestOptions()
						.skipMemoryCache(true)
						.diskCacheStrategy(DiskCacheStrategy.NONE)
						.signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
						.placeholder(R.drawable.ic_image_loading)
						.error(R.drawable.ic_profile_noimage).centerCrop();
				requestBuilder
						.apply(options)
						.into(mProfileImage);
			}

        }
    }

    @Override
    public void onMotorcycleFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mTabPosition = tab.getPosition();
        highLightCurrentTab(mTabPosition);
        //   loadProfileImage();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_edit_profile) {
//            startActivityForResult(EditProfileActivity.getStartIntent(getActivity(),
//                    REUtils.bitmapToByteArray(mProfileImageDownloaded)), 1);
            //   REUtils.selectImageProfile(getActivity(), getActivity());

            if (REApplication.getInstance().mFirebaseAuth != null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser()!=null&&REApplication.getInstance().mFirebaseAuth.getCurrentUser().getUid().equalsIgnoreCase(REApplication.getInstance().getUserLoginDetails().getData().getUser().getUserId())) {
                Bundle params = new Bundle();
                params.putString("eventCategory", "User Profile");
                params.putString("eventAction", "Edit Details clicked");
                REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
                Intent i = new Intent(getActivity(), TransparentPopActivity.class);
                i.putExtra(REConstants.KEY_NAVIGATION_FROM, "");
                startActivityForResult(i, 1);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.slide_in_right, R.anim.anim_exit);

    }
            else {
				String txt;
				if (REUtils.CHECK_FIREBASE_AUTH_INPROGRESS)
					txt=getResources().getString(R.string.firebase_auth_progress_fetch_data);
				else
					txt=getResources().getString(R.string.firebase_auth_error);
                REUtils.showErrorDialog(getActivity(),txt);
            }
        }
        if(v.getId()==R.id.iv_edit_image){
//                        startActivityForResult(EditProfileActivity.getStartIntent(getActivity(),
//                    REUtils.bitmapToByteArray(mProfileImageDownloaded)), 1);
            Bundle  params = new Bundle();
            params.putString("eventCategory", "User Profile");
            params.putString("eventAction", "Profile Photo Change");
            REUtils.logGTMEvent(REConstants.KEY_USER_PROFILE_GTM, params);
            REUtils.selectImageProfile(getActivity(), getActivity());
        }
        if(v.getId()==R.id.tv_view_goto_rider){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(REUtils.getMobileappbaseURLs().getRiderPortalURL()));
            startActivity(browserIntent);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (REUserModelStore.getInstance().getEmail() != null) {
            mTvRiderEmail.setText(REUserModelStore.getInstance().getEmail());
        } else {
            mTvRiderEmail.setText(R.string.data_notavailable);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IMAGE_UPDATE) {
            byte[] byteArray = data.getByteArrayExtra(REConstants.KEY_PROFILE_IMAGE);
            assert byteArray != null;
            Bitmap mProfileBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //Updating profile image
            if (mTabPosition == TAB_RIDES) {
                //Updating profile image
                mProfileImage.setImageBitmap(mProfileBitmap);
            }
            mProfileImageDownloaded = mProfileBitmap;
        } else if (resultCode == NAME_AND_EMAIL_UPDATE) {
            String name = data.getStringExtra(REConstants.KEY_NAME);
            String aboutMe = data.getStringExtra(REConstants.KEY_ABOUT_ME);
            mTvRiderName.setText(name);
            mTvAboutMe.setText(aboutMe);
        }
		else{
			if (REUserModelStore.getInstance().getEmail() != null) {
				mTvRiderEmail.setText(REUserModelStore.getInstance().getEmail());
			} else {
				mTvRiderEmail.setText(R.string.data_notavailable);
			}
		}
    }

    /**
     * This method save image captured from gallery
     *
     * @param bitmap
     */
    public void setProfileImage(Bitmap bitmap){
        mProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mProfileImage.setImageBitmap(bitmap);
        sendImageOnServer(bitmap);
    }

    /**
     * This method save image captured from camera
     *
     * @param path
     */
    public void setProfileImageSRC(String path) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        RequestListener<Bitmap> listener = new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                RELog.e("ProfileImageLoading Exception :" + e.getMessage() + " " + model);
                mProfileImage.setImageDrawable(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.ic_profile_noimage));
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, width, height, false);
                mProfileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mProfileImage.setImageBitmap(resizedBitmap);
                sendImageOnServer(resizedBitmap);
                return true;
            }
        };
        RequestBuilder<Bitmap> requestBuilder = Glide.with(getActivity())
                .asBitmap()
                .load(path)
                .listener(listener);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        requestBuilder
                .apply(options)
                .into(mProfileImage);
    }

    /**
     * This method send image to server
     *
     * @param resizedBitmap bitmap of image to be passed
     */
    public void sendImageOnServer(Bitmap resizedBitmap){
        showLoading();
        String encodedImage = REUtils.encodeImage(resizedBitmap);
        RiderProfileUploadPresenter mRiderProfileUploadPresenter = new
                RiderProfileUploadPresenter(this, new RiderProfileUploadInteractor());
        mRiderProfileUploadPresenter.uploadRiderProfileImage(encodedImage);

    }

    @Override
    public void onUploadPicSuccess(String message) {
        if(REUserModelStore.getInstance()!=null){
            REUserModelStore.getInstance().setProfileUrl(message);
        }
        hideLoading();
    }

    @Override
    public void onUploadPicFailure(String message) {
        hideLoading();
        if(isAdded())
        Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
    public void realTmeListener() {
        if(mFragmentPagerAdapter!=null) {
            Fragment fragme = mFragmentPagerAdapter.getCurrentFragment();

            if (fragme!=null&&fragme instanceof BookingFragment) {
                ((BookingFragment) fragme).setDataAndAdapter();
            }
            if (fragme!=null&&fragme instanceof GMABookingFragment) {
                ((GMABookingFragment) fragme).setDataAndAdapter();
            }
        }
    }
}

package com.royalenfield.reprime.ui.home.ourworld.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.models.response.firestore.ourworld.NewsResponse;
import com.royalenfield.reprime.models.response.remoteconfig.ConfigFeatures;
import com.royalenfield.reprime.models.response.remoteconfig.FeatureList;
import com.royalenfield.reprime.ui.home.ourworld.adapter.EventsAdapter;
import com.royalenfield.reprime.ui.home.ourworld.adapter.NewsAndLaunchesAdapter;
import com.royalenfield.reprime.ui.home.ourworld.views.OurWorldView;
import com.royalenfield.reprime.ui.home.rides.activity.RidesLightWeightWebActivity;
import com.royalenfield.reprime.ui.home.rides.activity.RidesTourActivity;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.reprime.utils.REConstants.RIDES_LIST_POSITION;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;

public class REOurWorldFragment extends REBaseFragment implements
        OurWorldView, NewsAndLaunchesAdapter.OnItemClickListener,
        EventsAdapter.OnItemClickListener, View.OnClickListener {

    private RecyclerView mNewsAndLaunchesRecyclerView, mEventsRecyclerView;
    private TextView mNewsErrorText, mEventsErrorText, mREWebsiteText,mRERidesText,mREEventsText;
    private ImageView mNoEventImage;

    public REOurWorldFragment() {
        // Required empty public constructor
    }

    public static REOurWorldFragment newInstance() {
        return new REOurWorldFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ourworld, container, false);
        try {
            initViews(view);
            getNewsDetailsFromFireStore();
            loadEvents();
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
        return view;
    }

    /**
     * Loads events based on Remote config data
     * If enabled events will be loaded else Image will be shown
     */
    private void loadEvents() {
        FeatureList featureList = REUtils.getFeatures();
       // ConfigFeatures configFeature = REUtils.getConfigFeatures();
        if (featureList != null && featureList.getDefaultFeatures() != null &&
                featureList.getDefaultFeatures().getIs_community_events_enabled().
                        equalsIgnoreCase(REConstants.FEATURE_DISABLED)) {
            mEventsErrorText.setVisibility(View.GONE);
            mEventsRecyclerView.setVisibility(View.GONE);
            mNoEventImage.setVisibility(View.VISIBLE);
            RequestBuilder<Bitmap> requestBuilder = Glide.with(getContext())
                    .asBitmap()
                    .load(REUtils.getRidesKeys().getEventFeatureImageURL_disabled());
            requestBuilder.into(mNoEventImage);
        } else {
            mNoEventImage.setVisibility(View.GONE);
            mEventsErrorText.setVisibility(View.VISIBLE);
            mEventsErrorText.setText(R.string.text_loading);
            mEventsRecyclerView.setVisibility(View.VISIBLE);
            mEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    RecyclerView.VERTICAL, false));
            if (mEventsRecyclerView.getItemAnimator() != null) {
                ((SimpleItemAnimator) mEventsRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            }
            getEventsDetailsFromFireStore();
        }
    }

    private void initViews(View view) {
        mREWebsiteText = view.findViewById(R.id.text_re_web);
        mRERidesText = view.findViewById(R.id.text_re_rides);
        mREEventsText = view.findViewById(R.id.text_re_events);
        mREWebsiteText.setOnClickListener(this);
        mRERidesText.setOnClickListener(this);
        mREEventsText.setOnClickListener(this);
        mNoEventImage = view.findViewById(R.id.no_event_image);
        mNewsAndLaunchesRecyclerView = view.findViewById(R.id.recyclerView_news_and_launch);
        mNewsErrorText = view.findViewById(R.id.news_tv_error);
        mNewsErrorText.setVisibility(View.VISIBLE);
        mNewsErrorText.setText(R.string.text_loading);
        mNewsAndLaunchesRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
        mNewsAndLaunchesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        if (mNewsAndLaunchesRecyclerView.getItemAnimator() != null) {
            ((SimpleItemAnimator) mNewsAndLaunchesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        }

        mEventsRecyclerView = view.findViewById(R.id.recyclerView_events);
        mEventsErrorText = view.findViewById(R.id.events_tv_error);
    }

    /**
     * Fetches the news data from fireStore
     */
    private void getNewsDetailsFromFireStore() {
        FirestoreManager.getInstance().getNewsDetails(this);
    }

    /**
     * Fetches the events data from fireStore
     */
    private void getEventsDetailsFromFireStore() {
        FirestoreManager.getInstance().getEventsDetails(this);
    }

    private void setNewsAdapter(List<NewsResponse> mNewsResponseList) {
        RecyclerView.Adapter mNewsAndLaunchesAdapter = new NewsAndLaunchesAdapter(getActivity(),
                mNewsResponseList, this);
        mNewsAndLaunchesRecyclerView.setAdapter(mNewsAndLaunchesAdapter);
    }

    private void setEventsAdapter(List<EventsResponse> eventsResponses) {
        RecyclerView.Adapter mEventsAdapter = new EventsAdapter(getActivity(), eventsResponses, this);
        mEventsRecyclerView.setAdapter(mEventsAdapter);
    }

    @Override
    public void onOurWorldNewsSuccess(List<NewsResponse> mNewsResponseList) {
        if (isAdded() && getContext() != null) {
            mNewsErrorText.setVisibility(View.GONE);
            setNewsAdapter(mNewsResponseList);
        }
    }

    @Override
    public void onOurWorldNewsFailure(String errorMessage) {
        if (isAdded() && getContext() != null) {
            mNewsErrorText.setVisibility(View.VISIBLE);
            mNewsAndLaunchesRecyclerView.setAdapter(null);
            if (errorMessage.equals(getString(R.string.text_no_news))) {
                mNewsErrorText.setText(errorMessage);
            } else {
                mNewsErrorText.setText(R.string.text_news_error);
            }
        }
    }

    @Override
    public void onOurWorldEventSuccess(List<EventsResponse> mNewsResponseList) {
        if (isAdded() && getContext() != null) {
            mEventsErrorText.setVisibility(View.GONE);
            setEventsAdapter(mNewsResponseList);
        }
    }

    @Override
    public void onOurWorldEventFailure(String throwable) {
        if (isAdded() && getContext() != null) {
            mEventsErrorText.setVisibility(View.VISIBLE);
            mEventsRecyclerView.setAdapter(null);
            if (throwable.equals(getString(R.string.text_no_events))) {
                mEventsErrorText.setText(throwable);
            } else {
                mEventsErrorText.setText(R.string.text_events_error);
            }
        }
    }

    @Override
    public void onEventsRegisterClick(int position, EventsResponse eventsResponse) {
        Intent intent = new Intent(getActivity(), RidesLightWeightWebActivity.class);
        intent.putExtra(RIDE_TYPE, REConstants.OUR_WORLD_EVENTS);
        intent.putExtra(RIDES_LIST_POSITION, position);
        REUtils.mEventsResponse = eventsResponse;
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onEventsDetailsClick(int position, EventsResponse eventsResponse) {
        Intent intent = new Intent(getActivity(), RidesTourActivity.class);
        intent.putExtra(RIDE_TYPE, REConstants.OUR_WORLD_EVENTS);
        intent.putExtra(RIDES_LIST_POSITION, position);
        REUtils.mEventsResponse = eventsResponse;
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onNewsItemClick(NewsResponse newsResponse) {
//        Intent intent = new Intent(getActivity(), NewsAndLaunchesDetailsActivity.class);
//        REUtils.mNewsResponse = newsResponse;
//        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
        REUtils.mNewsResponse = newsResponse;
        Intent intent = new Intent(getActivity(), RidesLightWeightWebActivity.class);
        intent.putExtra(RIDE_TYPE, REConstants.NEWS_OURWORLD);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_exit);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_re_web) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(REConstants.RE_WEBSITE_URL));
            startActivity(browserIntent);
        }
        else if (v.getId() == R.id.text_re_rides) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(REConstants.RE_WEBSITE_RIDES_URL));
            startActivity(browserIntent);
        }
        else if (v.getId() == R.id.text_re_events) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(REConstants.RE_WEBSITE_EVENTS_URL));
            startActivity(browserIntent);
        }
    }
}

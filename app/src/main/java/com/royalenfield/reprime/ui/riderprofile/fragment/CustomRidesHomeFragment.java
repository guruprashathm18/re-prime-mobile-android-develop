package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesDistanceCalcAsyncTask;
import com.royalenfield.reprime.ui.home.rides.custom.RecyclerViewItemOffsetDecoration;
import com.royalenfield.reprime.ui.home.rides.views.RidesHomeView;
import com.royalenfield.reprime.ui.riderprofile.adapter.CustomRidesAdapter;
import com.royalenfield.reprime.ui.riderprofile.presenter.CustomRidesRowPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REGridSpacingItemDecoration;

import java.util.List;
import java.util.Objects;

import static com.royalenfield.reprime.utils.REFirestoreConstants.MARQUEE_RIDES_FIRESTORE_KEY;

/**
 * This fragment loads the PreviousRides in recyclerview
 */
public class CustomRidesHomeFragment extends REBaseFragment implements RidesHomeView,
        RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> {

    private RecyclerView mPreviousRidesRecyclerView;
    private String mViewType;
    private TextView mErrorText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    /**
     * Gets and sets the bundle data
     */
    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mViewType = bundle.getString(REConstants.RIDECUSTOM_VIEWTYPE, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_rides, container, false);
        initViews(rootView);
        return rootView;
    }


    /**
     * Initialisng views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        mErrorText = rootView.findViewById(R.id.textView_error);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(R.string.text_loading);
        // Initialising row presenter for recyclerview
        mPreviousRidesRecyclerView = rootView.findViewById(R.id.previousrides);
        if (mViewType.equals(REConstants.MARQUEE_RIDE)) {
            mPreviousRidesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            mPreviousRidesRecyclerView.addItemDecoration(new RecyclerViewItemOffsetDecoration(18,
                    RecyclerViewItemOffsetDecoration.HORIZONTAL));
            ((SimpleItemAnimator) Objects.requireNonNull(mPreviousRidesRecyclerView.getItemAnimator())).
                    setSupportsChangeAnimations(false);
            getMarqueeRidesListFromFirestore();
        } else {
            mPreviousRidesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                    getResources().getInteger(R.integer.rides_gridspan_two)));
            ((SimpleItemAnimator) Objects.requireNonNull(mPreviousRidesRecyclerView.getItemAnimator())).
                    setSupportsChangeAnimations(false);
            setSpacingForRecyclerView();
            setMarginForeRecyclerView();
            setAdapter();
        }
    }

    /**
     * Fetches the marquee rides from firestore
     */
    private void getMarqueeRidesListFromFirestore() {
        FirestoreManager.getInstance().getMarqueeRides(this);
    }

    /**
     * Sets the margin for recyclerview for bookmarks List
     */
    private void setMarginForeRecyclerView() {
        if (mViewType.equals(REConstants.BOOKMARKS_TYPE) && getActivity() != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) mPreviousRidesRecyclerView.getLayoutParams();
            marginLayoutParams.setMargins(getActivity().getResources().
                    getInteger(R.integer.rides_bookmarks_marginzero), getActivity().getResources().
                    getInteger(R.integer.rides_bookmarks_margintop), getActivity().getResources().
                    getInteger(R.integer.rides_bookmarks_marginzero), getActivity().getResources().
                    getInteger(R.integer.rides_bookmarks_marginbottom));
            mPreviousRidesRecyclerView.setLayoutParams(marginLayoutParams);
            mPreviousRidesRecyclerView.requestLayout();
        }
    }

    /**
     * Sets the RecyclerView Adapter
     */
    private void setMarqueeRidesAdapter() {
        mErrorText.setVisibility(View.GONE);
        mPreviousRidesRecyclerView.setVisibility(View.VISIBLE);
        CustomRidesRowPresenter mPreviousRideRowPresenter = new CustomRidesRowPresenter();
        mPreviousRidesRecyclerView.setAdapter(new CustomRidesAdapter(getActivity(), getContext(),
                mPreviousRideRowPresenter, mViewType));
    }

    /**
     * Sets the RecyclerView Adapter
     */
    private void setAdapter() {
        CustomRidesRowPresenter mPreviousRideRowPresenter = new CustomRidesRowPresenter();
        mPreviousRidesRecyclerView.setAdapter(new CustomRidesAdapter(getActivity(), getContext(),
                mPreviousRideRowPresenter, mViewType));
    }


    /**
     * Sets dynamic spacing for Grid recyclerView
     */
    private void setSpacingForRecyclerView() {
        int spacing = Math.round(4.6f * getResources().getDisplayMetrics().density);
        mPreviousRidesRecyclerView.addItemDecoration(new REGridSpacingItemDecoration(getResources().
                getInteger(R.integer.rides_gridspan_two),
                spacing, false));
    }

    @Override
    public void onDealerRideResponse() {

    }

    @Override
    public void onDealerRideFailure(String errorMessage) {

    }

    @Override
    public void onPopularRideResponse() {

    }

    @Override
    public void onPopularRideResponseFailure(String throwable) {

    }

    @Override
    public void onMarqueeRideResponse() {
        if (isAdded() && getContext() != null) {
            //This helps if there is an update in firesore marquee rides data
            mPreviousRidesRecyclerView.setVisibility(View.GONE);
            RidesDistanceCalcAsyncTask ridesDistanceCalcAsyncTask =
                    new RidesDistanceCalcAsyncTask(MARQUEE_RIDES_FIRESTORE_KEY, this);
            ridesDistanceCalcAsyncTask.execute();
        }
    }

    @Override
    public void onMarqueeRideResponseFailure(String errorMessage) {
        if (isAdded() && getContext() != null) {
            mPreviousRidesRecyclerView.setVisibility(View.GONE);
            if (errorMessage.equals(getString(R.string.text_no_marqueerides))) {
                mErrorText.setText(errorMessage);
            } else {
                mErrorText.setText(R.string.text_marquee_rides_error);
            }
        }
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
            List<MarqueeRidesResponse> mMarqueeRidesResponse;
            RERidesModelStore.getInstance().setMarqueeRidesResponse(result);
            mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
            if (mMarqueeRidesResponse != null && mMarqueeRidesResponse.size() > 0) {
                setMarqueeRidesAdapter();
            } else {
                mErrorText.setVisibility(View.VISIBLE);
                mErrorText.setText(R.string.text_marquee_rides_error);
            }
        }
    }

    @Override
    public void onRideJoinedComplete() {

    }
}

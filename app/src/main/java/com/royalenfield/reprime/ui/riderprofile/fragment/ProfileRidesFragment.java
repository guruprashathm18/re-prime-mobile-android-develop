package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.ui.riderprofile.adapter.ProfileRidesAdapter;
import com.royalenfield.reprime.ui.riderprofile.presenter.ProfileRidesRowPresenter;
import com.royalenfield.reprime.utils.REConstants;

import java.util.List;
import java.util.Objects;

import com.royalenfield.reprime.utils.RELog;

/**
 * This fragment loads the upcomingRides recyclerView
 */
public class ProfileRidesFragment extends REBaseFragment {

    private static final String TAG = ProfileRidesFragment.class.getSimpleName();
    private String mViewType;
    private RecyclerView mProfileRidesRecyclerView;
    private ProfileRidesRowPresenter mProfileRidesPresenter;
    private TextView mErrorText;

    public static ProfileRidesFragment newInstance() {
        return new ProfileRidesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upcomingrides, container, false);
        initViews(rootView);
        getBundleData();
        getRideDataBasedOnSelection();
        return rootView;
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

    /**
     * Sets the data based on spinner selection
     */
    private void getRideDataBasedOnSelection() {
        List<ProfileRidesResponse> mProfileRidesResponse = null;
        switch (mViewType) {
            case REConstants.PASTRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getPastRideResponse();
                break;
            case REConstants.UPCOMIMGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getUpcomingRideResponse();
                break;
            case REConstants.REJECTEDRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getRejectedRideResponse();
                break;
            case REConstants.PENDINGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getPendingRideResponse();
                break;
            case REConstants.ONGOINGRIDE_TYPE:
                mProfileRidesResponse = REUserModelStore.getInstance().getOngoingRideResponse();
                break;
        }
        if (mProfileRidesResponse != null &&
                mProfileRidesResponse.size() > 0) {
            setAdapter();
        } else {
            showErrorText();
        }
    }

    /**
     * Sets the reccylerview adapter
     */
    private void setAdapter() {
        try {
            mProfileRidesRecyclerView.setVisibility(View.VISIBLE);
            mProfileRidesRecyclerView.setAdapter(new ProfileRidesAdapter(getActivity(), getContext(), mProfileRidesPresenter,
                    mViewType));
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    /**
     * Shows error message
     */
    private void showErrorText() {
        mProfileRidesRecyclerView.setVisibility(View.GONE);
        mErrorText.setText(R.string.text_userprofile_rides_error);
        mErrorText.setVisibility(View.VISIBLE);
    }

    /**
     * Initialisng views
     *
     * @param rootView :View
     */
    private void initViews(View rootView) {
        // Initialising row presenter for recyclerview
        mProfileRidesPresenter = new ProfileRidesRowPresenter();
        mProfileRidesRecyclerView = rootView.findViewById(R.id.upcomingrides);
        mProfileRidesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mProfileRidesRecyclerView.setNestedScrollingEnabled(false);
        ((SimpleItemAnimator) Objects.requireNonNull(mProfileRidesRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        mErrorText = rootView.findViewById(R.id.text_error);
    }
}

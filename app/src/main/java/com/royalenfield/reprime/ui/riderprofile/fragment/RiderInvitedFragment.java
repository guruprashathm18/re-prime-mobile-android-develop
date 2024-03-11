package com.royalenfield.reprime.ui.riderprofile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.riderprofile.adapter.RiderInvitedAdapter;
import com.royalenfield.reprime.ui.riderprofile.presenter.RiderInvitedPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;

import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE;

public class RiderInvitedFragment extends REBaseFragment {

    private int mPosition;
    private String mRideType;

    public RiderInvitedFragment() {
        // Required empty public constructor
    }

    public static RiderInvitedFragment newInstance() {
        return new RiderInvitedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ridertour_key_places, container, false);
        getBundleData();
        initViews(view);
        return view;
    }

    /**
     * Gets and sets the bundle data
     */
    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt(REConstants.RIDES_LIST_POSITION, 0);
            mRideType = bundle.getString(RIDE_TYPE, null);
        }
    }


    /**
     * Initialising views
     *
     * @param view : View
     */
    private void initViews(View view) {
        RecyclerView mRideTourPlacesRecyclerView = view.findViewById(R.id.rides_keyplaces_list);
        mRideTourPlacesRecyclerView.setNestedScrollingEnabled(false);
        mRideTourPlacesRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
        RecyclerView.LayoutManager mCoastalPlacesLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        mRideTourPlacesRecyclerView.setLayoutManager(mCoastalPlacesLayoutManager);

        RiderInvitedAdapter riderInvitedAdapter = new RiderInvitedAdapter(getActivity(), getContext(),
                new RiderInvitedPresenter(), mPosition, mRideType);
        mRideTourPlacesRecyclerView.setAdapter(riderInvitedAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}


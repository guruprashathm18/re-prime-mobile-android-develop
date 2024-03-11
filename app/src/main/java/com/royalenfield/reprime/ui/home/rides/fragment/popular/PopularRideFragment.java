package com.royalenfield.reprime.ui.home.rides.fragment.popular;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.royalenfield.firebase.fireStore.FirestoreManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.adapter.PopularRidesAdapter;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesAsyncTaskListeners;
import com.royalenfield.reprime.ui.home.rides.asynctask.RidesDistanceCalcAsyncTask;
import com.royalenfield.reprime.ui.home.rides.custom.RecyclerViewItemOffsetDecoration;
import com.royalenfield.reprime.ui.home.rides.views.RidesHomeView;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;

import java.util.List;
import java.util.Objects;

import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_FIRESTORE_KEY;

/**
 * Popular rides recyclerview is loaded in this fragment
 */
public class PopularRideFragment extends REBaseFragment implements RidesHomeView,
        RidesAsyncTaskListeners.RidesAsyncTaskCompleteListener<List> {

    private List<PopularRidesResponse> mPopularRidesResponse;
    private RecyclerView mRecyclerView_PopularRides;
    private TextView mErrorText;

    public PopularRideFragment() {
        // Required empty public constructor
    }

    public static PopularRideFragment newInstance() {
        return new PopularRideFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular_rides, container, false);
        initViews(view);
        getPopularRidesFromFirestore();
        return view;
    }

    private void initViews(View view) {
        mErrorText = view.findViewById(R.id.textView_error);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(R.string.text_loading);
        mRecyclerView_PopularRides = view.findViewById(R.id.marquee_rides);
        mRecyclerView_PopularRides.addOnItemTouchListener(new RERecyclerTouchListener());
    }

    /**
     * Fetches data from firestore
     */
    private void getPopularRidesFromFirestore() {
        FirestoreManager.getInstance().getPopularRides(this);
    }


    /**
     * Binds the data to the adapter
     */
    private void showPopularRidesList() {
        mErrorText.setVisibility(View.GONE);
        mRecyclerView_PopularRides.setVisibility(View.VISIBLE);
        //  call the constructor of ServiceCenterAdapter to send the reference and data to Adapter
        mRecyclerView_PopularRides.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView_PopularRides.addItemDecoration(new RecyclerViewItemOffsetDecoration(15,
                RecyclerViewItemOffsetDecoration.HORIZONTAL));
        ((SimpleItemAnimator) Objects.requireNonNull(mRecyclerView_PopularRides.getItemAnimator())).
                setSupportsChangeAnimations(false);
        PopularRidesAdapter customAdapter = new PopularRidesAdapter(getContext(), mPopularRidesResponse);
        mRecyclerView_PopularRides.setAdapter(customAdapter); // set the Adapter to RecyclerView
    }


    @Override
    public void onPopularRideResponse() {
        if (isAdded() && getContext() != null) {
            //This helps if there is an update in firesore popular rides data
            mRecyclerView_PopularRides.setVisibility(View.GONE);
            RidesDistanceCalcAsyncTask ridesDistanceCalcAsyncTask =
                    new RidesDistanceCalcAsyncTask(POPULAR_RIDES_FIRESTORE_KEY, this);
            ridesDistanceCalcAsyncTask.execute();
        }
    }

    @Override
    public void onPopularRideResponseFailure(String error) {
        if (isAdded() && getContext() != null) {
            mRecyclerView_PopularRides.setVisibility(View.GONE);
            if (error.equals(getString(R.string.text_no_popularrides))) {
                mErrorText.setText(error);
            } else {
                mErrorText.setText(R.string.text_popular_rides_error);
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
    public void onMarqueeRideResponse() {

    }

    @Override
    public void onMarqueeRideResponseFailure(String errorMessage) {

    }

    @Override
    public void onDealerRideResponse() {

    }

    @Override
    public void onDealerRideFailure(String errorMessage) {

    }

    @Override
    public void onRidesDistanceCalcComplete(List result) {
        if (isAdded() && getContext() != null) {
            RERidesModelStore.getInstance().setPopularRidesResponse(result);
            mPopularRidesResponse = RERidesModelStore.getInstance().getPopularRidesResponse();
            if (mPopularRidesResponse != null && mPopularRidesResponse.size() > 0) {
                showPopularRidesList();
            } else {
                mErrorText.setVisibility(View.VISIBLE);
                mErrorText.setText(R.string.text_popular_rides_error);
            }
        }
    }

    @Override
    public void onRideJoinedComplete() {

    }
}

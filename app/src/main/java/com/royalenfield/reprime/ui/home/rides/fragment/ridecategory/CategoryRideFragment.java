package com.royalenfield.reprime.ui.home.rides.fragment.ridecategory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseFragment;
import com.royalenfield.reprime.ui.home.rides.adapter.CategoryRidesAdapter;
import com.royalenfield.reprime.ui.home.rides.custom.RecyclerViewItemOffsetDecoration;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesTourPresenter;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;

import java.util.Objects;

/**
 * CategoryRideFragment loads the gallery(Ride images) to recyclerview
 */
public class CategoryRideFragment extends REBaseFragment {

    private final String TAG = CategoryRideFragment.class.getSimpleName();
    private RecyclerView mRecyclerViewInspirationRideList;
    private String mRideType;
    private int mPosition;

    public CategoryRideFragment() {
        // Required empty public constructor
    }

    public static CategoryRideFragment newInstance() {
        return new CategoryRideFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_rides, container, false);
        getBundleData();
        mRecyclerViewInspirationRideList = view.findViewById(R.id.inspiration_rides_list);
        mRecyclerViewInspirationRideList.addOnItemTouchListener(new RERecyclerTouchListener());
        showInspirationRidesList();
        return view;
    }

    /**
     * Gets and sets the bundle data
     */
    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRideType = bundle.getString(REConstants.RIDE_TYPE, null);
            mPosition = bundle.getInt(REConstants.RIDES_LIST_POSITION, 0);
        }
    }

    /***
     * This function used to populate the inspiration rides category in GRID View model
     */
    private void showInspirationRidesList() {
        CategoryRidesAdapter customAdapter = new CategoryRidesAdapter(getActivity(), new RidesTourPresenter(),
                mRideType, mPosition, REConstants.KEY_RIDE_GALLERY);
        mRecyclerViewInspirationRideList.setAdapter(customAdapter); // set the Adapter to RecyclerView

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerViewInspirationRideList.setLayoutManager(gridLayoutManager);
        mRecyclerViewInspirationRideList.addItemDecoration(new RecyclerViewItemOffsetDecoration(18,
                RecyclerViewItemOffsetDecoration.GRID));
        ((SimpleItemAnimator) Objects.requireNonNull(mRecyclerViewInspirationRideList.getItemAnimator())).
                setSupportsChangeAnimations(false);
    }
}
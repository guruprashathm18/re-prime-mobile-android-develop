package com.royalenfield.reprime.ui.home.rides.fragment.planride;

import android.location.Location;
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
import com.royalenfield.reprime.models.response.googlemap.poi.NearBySearchResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.home.rides.adapter.RecommendedRoutesAdapter;
import com.royalenfield.reprime.ui.home.rides.interactor.GoogleMapAPIInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;
import java.util.Objects;

/**
 * This fragment holds the recommended routes list
 */
public class RecommendedRoutesFragment extends REBaseFragment implements RidesListeners.OnPOICallFinishedListener {

    private final String TAG = RecommendedRoutesFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private RecyclerView mRecommendedRouteRecyclerView;
    private TextView mTvRecommendedRouteLabel;
    private View mRecommendedViewLine;

    public static RecommendedRoutesFragment newInstance() {
        return new RecommendedRoutesFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TerrainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendedRoutesFragment newInstance(String param1, int position) {
        RecommendedRoutesFragment fragment = new RecommendedRoutesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommended_routes, container, false);
        initViews(rootView);
        return rootView;
    }

    /**
     * Initialising views
     *
     * @param rootView: View
     */
    private void initViews(View rootView) {
        mTvRecommendedRouteLabel = rootView.findViewById(R.id.tv_ride_routes_title);
        mRecommendedViewLine = rootView.findViewById(R.id.tv_divider_route_details);
        mRecommendedRouteRecyclerView = rootView.findViewById(R.id.recommended_route);
        Location mCurrentLocation = REUtils.getLocationFromModel();
        if (mCurrentLocation != null && mCurrentLocation.getLatitude() != 0 && mCurrentLocation.getLongitude() != 0) {
            showLoading();
            new GoogleMapAPIInteractor().getPOI(mCurrentLocation.getLatitude() + "," +
                    mCurrentLocation.getLongitude(), this);
        }
    }

    /**
     * This sets the places results to adapter
     *
     * @param searchResults used to show POI results
     */
    private void setResults(NearBySearchResponse searchResults) {
        mRecommendedRouteRecyclerView.setVisibility(View.VISIBLE);
        mRecommendedRouteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(mRecommendedRouteRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        List<POIResults> searchResultList = searchResults.getResults();
        if (searchResults != null && searchResultList.size() > 0 && searchResultList.size() > 5) {
            searchResultList = searchResultList.subList(0, 5);
        }
        RecommendedRoutesAdapter recommendedRoutesAdapter = new RecommendedRoutesAdapter(getContext(), searchResultList);
        mRecommendedRouteRecyclerView.setAdapter(recommendedRoutesAdapter);
        mTvRecommendedRouteLabel.setVisibility(View.VISIBLE);
        mRecommendedViewLine.setVisibility(View.VISIBLE);

    }


    @Override
    public void onPOISuccess(NearBySearchResponse nearBySearchResponse) {
        hideLoading();
        setResults(nearBySearchResponse);
    }

    @Override
    public void onPOIFailure(String error) {
        hideLoading();
    }
}

package com.royalenfield.reprime.ui.home.navigation.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.core.conversion.LengthUnit;
import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.midgard.core.search.SearchCallback;
import com.bosch.softtec.components.midgard.core.search.SearchProvider;
import com.bosch.softtec.components.midgard.core.search.models.PoiCategory;
import com.bosch.softtec.components.midgard.core.search.models.Response;
import com.bosch.softtec.components.midgard.core.search.models.SearchError;
import com.bosch.softtec.components.midgard.core.search.models.SearchOptions;
import com.bosch.softtec.components.midgard.core.search.models.results.PoiSearchResult;
import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.royalenfield.googleSearch.SearchProviderManager;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.navigation.adapter.POIAdapter;
import com.royalenfield.reprime.ui.home.navigation.adapter.POIResultAdapter;
import com.royalenfield.reprime.ui.home.navigation.model.POIModel;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REDialogUtils;
import com.royalenfield.reprime.utils.RERecyclerNavigationTouchListener;
import com.royalenfield.reprime.utils.RERecyclerTouchListener;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.view.View.GONE;

public class NavigationPOIFragment extends BottomSheetDialogFragment
        implements View.OnClickListener, POIAdapter.POIClickListener, POIResultAdapter.POIClickListener {

    public static final String TAG = NavigationPOIFragment.class.getSimpleName();

    private static final String ARG_POI = "param1";

    private ItemClickListener mListener;
    private RecyclerView poiRecyclerView, poiResultRecyclerView;
    private String strSelectedPOI;
    private List<POIModel> poiModelList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private FragmentActivity mContext;
    private SearchProvider searchProvider;
    private SearchOptions searchOptions;
    private com.bosch.softtec.components.core.models.LatLng mCurrentLatLng;
    private int mAPIFailureCount = 1;
    private PoiCategory mPOICategory;


    /**
     * The ReverseGeocodingCallback to which the results of a search request will be delivered to.
     * Communicating with BILA for getting search results
     */
    private SearchCallback<SearchResult> searchCallback = new SearchCallback<SearchResult>() {
        @Override
        public void onSearchFinished(@NotNull Response<SearchResult> response) {
            final List<SearchResult> searchResults = response.getResults();
            if (mContext != null) {
                hideLoading();
                mContext.runOnUiThread(() -> setResults(searchResults));
            }
        }

        @Override
        public void onSearchError(@NotNull SearchError searchError, @org.jetbrains.annotations.Nullable String s) {
            if (mContext != null) {
                if (searchError.name().equals(REConstants.TBT_API_UNAUTHORIZED)) {
                    mContext.runOnUiThread(() -> {
                        hideLoading();
                        REUtils.showErrorDialog(mContext, REApplication.getAppContext().getString(R.string.error_nav_unauthorized));
                    });
                } else {
                    if (mAPIFailureCount > REConstants.NAV_API_RETRY_COUNTER) {
                        mContext.runOnUiThread(() -> {
                            hideLoading();
                            REUtils.showErrorDialog(mContext, REApplication.getAppContext().getString(R.string.error_tbt_api));
                        });
                    } else {
                        mAPIFailureCount++;
                        getPOI(mPOICategory);
                    }
                }

            }
        }
    };

    /**
     * This sets the places results to adapter
     *
     * @param searchResults used to show POI results
     */
    private void setResults(List<SearchResult> searchResults) {
        if (searchResults != null && searchResults.size() > 0) {
            poiResultRecyclerView.setVisibility(View.VISIBLE);
            POIResultAdapter poiAdapter = new POIResultAdapter(mContext, searchResults, this);
            poiResultRecyclerView.setAdapter(poiAdapter);
        } else {
            Toast.makeText(mContext, "Result Not found", Toast.LENGTH_SHORT).show();
            poiResultRecyclerView.setAdapter(null);
        }
    }

    public static NavigationPOIFragment newInstance(String strSelectedPOI) {
        NavigationPOIFragment navigationPOIFragment = new NavigationPOIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POI, strSelectedPOI);
        navigationPOIFragment.setArguments(args);
        return navigationPOIFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if (getArguments() != null) {
            strSelectedPOI = getArguments().getString(ARG_POI);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_navigation_list_view, container, false);

        ConstraintLayout constraintLayout = view.findViewById(R.id.bottom_sheet_poiview);
        constraintLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        poiRecyclerView = view.findViewById(R.id.nav_poi_recyclerView);
        poiResultRecyclerView = view.findViewById(R.id.nav_poi_res_recyclerView);
        ImageView imgViewClose = view.findViewById(R.id.close_button);
        imgViewClose.setOnClickListener(this);
        TextView tvShowMap = view.findViewById(R.id.showmap);
        tvShowMap.setOnClickListener(this);

        poiRecyclerView.addOnItemTouchListener(new RERecyclerTouchListener());
        RecyclerView.LayoutManager mUserCreatedRidesLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        poiRecyclerView.setHasFixedSize(true);
        poiRecyclerView.setLayoutManager(mUserCreatedRidesLayoutManager);


        poiResultRecyclerView.setVisibility(GONE);
        poiResultRecyclerView.addOnItemTouchListener(new RERecyclerNavigationTouchListener());
        poiResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        poiResultRecyclerView.setHasFixedSize(true);

        setPOIAdapter();

        return view;

    }

    private void setPOIAdapter() {
        poiModelList = new ArrayList<>();
        POIModel poiModel1 = new POIModel("Restaurant", R.drawable.ic_fork, PoiCategory.RESTAURANT);
        POIModel poiModel2 = new POIModel("Fuel Station", R.drawable.ic_fuelstation, PoiCategory.GAS_STATION);
        POIModel poiModel3 = new POIModel("Café", R.drawable.ic_coffeecup, PoiCategory.CAFE);
        POIModel poiModel4 = new POIModel("Hotel", R.drawable.ic_bed, PoiCategory.LODGING);
        POIModel poiModel5 = new POIModel("Park", R.drawable.ic_park, PoiCategory.PARK);
        poiModelList.add(poiModel1);
        poiModelList.add(poiModel2);
        poiModelList.add(poiModel3);
        poiModelList.add(poiModel4);
        poiModelList.add(poiModel5);

        POIAdapter poiAdapter = new POIAdapter(mContext, poiModelList, this, false);
        poiRecyclerView.setAdapter(poiAdapter);

        //For loop is needed for taking the position of the item
        for (int index = 0; index < poiModelList.size(); index++) {
            if (strSelectedPOI.equalsIgnoreCase(poiModelList.get(index).getName())) {
                poiAdapter.setPOIItemHighlighted(index);
                poiAdapter.notifyDataSetChanged();
                break;
            }
        }
        fetchPOIData(strSelectedPOI);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Resize bottom sheet dialog so it doesn't span the entire width past a particular measurement
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels - 100;
        int height = -1; // MATCH_PARENT
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }


    public void setRouteListener(ItemClickListener routeListener) {
        mListener = routeListener;
    }

    private void getPOIItemResult(PoiCategory category) {
        mAPIFailureCount = 1;
        mPOICategory = category;
        Location mCurrentLocation = REUtils.getLocationFromModel();
        mCurrentLatLng = new com.bosch.softtec.components.
                core.models.LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), null);
        Distance distance = new Distance(10, LengthUnit.KILOMETERS);
        searchProvider = SearchProviderManager.getInstance().searchProvider(mContext);
        searchOptions = new SearchOptions.Builder()
                .searchLimit(10)
                .searchLocale(Locale.getDefault())
                .maxDistance(distance, mCurrentLatLng)
                .build();
        showLoading();
        getPOI(category);
    }

    private void getPOI(PoiCategory category) {
        searchProvider.searchAroundLocation(category, mCurrentLatLng, searchOptions, searchCallback);

    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = REDialogUtils.showLoadingDialog(this.getContext());
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
            REDialogUtils.removeHandlerCallbacks();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        hideLoading();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_button:
                dismiss();
                hideLoading();
                break;
            case R.id.showmap:
                dismiss();
                mListener.onPOIResultShowMap(strSelectedPOI, true);
                break;
        }
    }

    @Override
    public void poiItemClicked(String strPoiName, boolean isChecked) {
        strSelectedPOI = strPoiName;
        if (isChecked) {
            fetchPOIData(strPoiName);
        } else {
            dismiss();
            hideLoading();
        }
        mListener.onPOIResultShowMap(strSelectedPOI, isChecked);
    }

    @Override
    public void poiItemCheckLocationisEnabled() {

    }

    private void fetchPOIData(String strPoiName) {
        for (POIModel poiModel : poiModelList) {
            if (strPoiName.equalsIgnoreCase(poiModel.getName())) {
                getPOIItemResult(poiModel.getPoiCategory());
                break;
            }
        }
    }

    @Override
    public void poiResultItemClicked(SearchResult strPoiName) {
        PoiSearchResult poiSearchResult = (PoiSearchResult) strPoiName;
        mListener.onPOIResultItemClick(poiSearchResult);
        dismiss();
    }

    public interface ItemClickListener {
        void onPOIResultItemClick(PoiSearchResult item);

        void onPOIResultShowMap(String item, boolean isChecked);
    }

}

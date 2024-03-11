package com.royalenfield.reprime.ui.home.rides.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.rides.adapter.SearchPlacesListAdapter;
import com.royalenfield.reprime.ui.home.rides.interactor.GoogleMapAPIInteractor;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;
import com.royalenfield.reprime.ui.home.rides.presenter.SearchPlaceRowPresenter;

import java.util.List;
import java.util.Objects;

public class CreateRideSearchActivity extends REBaseActivity implements TextWatcher,
        SearchPlacesListAdapter.OnItemClickListener, TitleBarView.OnNavigationIconClickListener,
        RidesListeners.OnPlacesCallFinishedListener {

    private SearchPlaceRowPresenter mSearchPlacesRowPresenter;
    private EditText etCurrentLoc;
    private EditText etDestination;
    private RecyclerView mSearchRecyclerView;
    private boolean mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createride_search);
        initViews();
    }

    /**
     * Initialising views
     */
    private void initViews() {
        mSearchPlacesRowPresenter = new SearchPlaceRowPresenter();
        TitleBarView mTitleBarView = findViewById(R.id.create_ride_header);
        mTitleBarView.bindData(this, R.drawable.ic_back_up, getString(R.string.label_create_ride), 0);
        etCurrentLoc = findViewById(R.id.et_current_location);
        etDestination = findViewById(R.id.et_destination_location);
        getIntentData();
        etDestination.setTypeface(ResourcesCompat.getFont(CreateRideSearchActivity.this, R.font.montserrat_light));
        etDestination.addTextChangedListener(this);
        etCurrentLoc.addTextChangedListener(this);
        mSearchRecyclerView = findViewById(R.id.recyclerView_searchlist);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        ((SimpleItemAnimator) Objects.requireNonNull(mSearchRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
    }

    /**
     * Gets intent data from previous screen
     */
    private void getIntentData() {
        mType = getIntent().getBooleanExtra("type", false);
        String mSource = getIntent().getStringExtra("source");
        String mDestination = getIntent().getStringExtra("dest");
        if (mType) {
            etCurrentLoc.setFocusable(true);
            etCurrentLoc.setText("");
            etDestination.setText(mDestination);
            etDestination.setTypeface(ResourcesCompat.getFont(CreateRideSearchActivity.this, R.font.montserrat_bold));
            etDestination.setFocusable(false);
        } else {
            etDestination.setFocusable(true);
            etDestination.setText("");
            etCurrentLoc.setText(mSource);
            etCurrentLoc.setFocusable(false);
        }
    }

    /**
     * This sets the places results to adapter
     *
     * @param searchResults used to display list items in the listview
     */
    private void setResults(List<POIResults> searchResults) {
        mSearchRecyclerView.setVisibility(View.VISIBLE);
        SearchPlacesListAdapter mAdapter = new SearchPlacesListAdapter(searchResults,
                this, mSearchPlacesRowPresenter);
        mSearchRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null && s.length() >= 3) {
            new GoogleMapAPIInteractor().getPlaces(s.toString(), this);
        }
    }

    @Override
    public void onItemClick(String place, double latitude, double longitude) {
        hideKeyboard();
        Intent intent = new Intent();
        intent.putExtra("place", place);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        if (mType) {
            setResult(1, intent);
        } else {
            setResult(2, intent);
        }
        finish();
    }

    @Override
    public void onNavigationIconClick() {
        hideKeyboard();
        Intent intent = new Intent();
        setResult(3, intent);
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_down);
    }

    @Override
    public void onPlaceSuccess(List<POIResults> poiResults) {
        setResults(poiResults);
    }

    @Override
    public void onPlaceFailure(String error) {
    }
}

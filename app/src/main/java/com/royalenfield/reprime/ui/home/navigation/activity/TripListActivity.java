package com.royalenfield.reprime.ui.home.navigation.activity;

import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.micro.tenzing.client.model.InlineResponse200;
import com.bosch.softtec.micro.tenzing.client.model.TripMetadata;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.base.REBaseActivity;
import com.royalenfield.reprime.ui.custom.views.TitleBarView;
import com.royalenfield.reprime.ui.home.navigation.adapter.BCTListAdapter;
import com.royalenfield.reprime.ui.home.navigation.asynctask.FetchAllCloudTripsAsyncTask;
import com.royalenfield.reprime.ui.home.navigation.asynctask.NavigationAsyncTaskListeners;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a ride
 */
public class TripListActivity extends REBaseActivity implements BCTListAdapter.BCTTripListener,
        TitleBarView.OnNavigationIconClickListener, NavigationAsyncTaskListeners.FetchAllTripsListener {


    private RecyclerView bctlistRecyclerView;
    private BCTListAdapter bctListAdapter;
    private List<TripMetadata> tripMetadata = new ArrayList<>();
    private boolean isLoading = false;
    private String nextid = "";
    private static int REQUEST_TRIP_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_list_layout);
        initViews();
        //Swipe

        Bundle aParams = new Bundle();
        aParams.putString("screenName", "Tripper My Routes");
        REUtils.logGTMEvent(REConstants.ScreenViewManual, aParams);

    }

    public void fetchAllServerTrips() {
        showLoading();
        FetchAllCloudTripsAsyncTask fetchAllCloudTripsAsyncTask = new FetchAllCloudTripsAsyncTask(this);
        if (tripMetadata.size() > 0) {
            Log.e("test", "fetch nextid from =" + nextid);
            fetchAllCloudTripsAsyncTask.execute(nextid);
        } else {
            fetchAllCloudTripsAsyncTask.execute("");
        }
    }

    private void initScrollListener() {
        bctlistRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.e("test", "scroll called start = " + linearLayoutManager.findLastCompletelyVisibleItemPosition());
                Log.e("test", "scroll called size = " + (tripMetadata.size() - 1));
                if (!isLoading && nextid != null) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == tripMetadata.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        // if (REApplication.getInstance().getCloudManager().isValidSession()) {
        fetchAllServerTrips();
        //  }
    }


    /**
     * Initialising Views
     */
    private void initViews() {
        TitleBarView mTitleBarView = findViewById(R.id.bct_list_header);
        mTitleBarView.bindData(this, R.drawable.ic_back_arrow, " My Routes");

        bctlistRecyclerView = findViewById(R.id.bct_list_view);
        RecyclerView.LayoutManager mUserCreatedRidesLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        bctlistRecyclerView.setHasFixedSize(true);
        bctlistRecyclerView.setLayoutManager(mUserCreatedRidesLayoutManager);
        bctListAdapter = new BCTListAdapter(tripMetadata, this);
        bctlistRecyclerView.setAdapter(bctListAdapter);
        bctlistRecyclerView.setNestedScrollingEnabled(false);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(bctlistRecyclerView);
        fetchAllServerTrips();
        initScrollListener();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TRIP_INTENT && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getStringExtra("action").equalsIgnoreCase("Home")) {
                    Intent homeIntent = getIntent();
                    homeIntent.putExtra(REConstants.IS_SERVICE_NOTIFICATION, data.
                            getBooleanExtra(REConstants.IS_SERVICE_NOTIFICATION, false));
                    homeIntent.putExtra(REConstants.IS_NAVIGATION_NOTIFICATION, data.
                            getBooleanExtra(REConstants.IS_NAVIGATION_NOTIFICATION, false));
                    homeIntent.putExtra(REConstants.IS_NAVIGATION_DETAILS, data.
                            getBooleanExtra(REConstants.IS_NAVIGATION_DETAILS, false));
                    homeIntent.putExtra(REConstants.NAVIGATION_NOTIFICATION, data.getStringExtra(REConstants.NAVIGATION_NOTIFICATION));
                    setResult(RESULT_OK, homeIntent);
                    finish();
                } else {
                    if (data.getStringExtra("action").equalsIgnoreCase("Delete")) {
                        recreate();
                    }
                    if (data.getStringExtra("action").equalsIgnoreCase("EditBCT")) {
                        recreate();
                    }
                }
            }
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // Row is swiped from recycler view
            // remove it from adapter
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // view the background view
        }
    };

    @Override
    public void bctTripClicked(TripMetadata tripMetadata) {
        Intent bctDetailsIntent = new Intent(this, BCTTripDetailsActivity.class);
        bctDetailsIntent.putExtra("bct_trip_id", tripMetadata.getTripId());
        bctDetailsIntent.putExtra("bct_trip_name", tripMetadata.getName());
        bctDetailsIntent.putExtra("bct_trip_desc", tripMetadata.getDescription());
        bctDetailsIntent.putExtra("bct_trip_visibility", tripMetadata.getVisibility().getValue());
        startActivityForResult(bctDetailsIntent, REQUEST_TRIP_INTENT);
        try {
            Bundle params = new Bundle();
            params.putString("eventCategory", "Tripper");
            params.putString("eventAction", "My Route Selected");
            if (tripMetadata.getVisibility().getValue().equalsIgnoreCase("Public")) {
                params.putString("eventLabel", "Public");
            } else {
                params.putString("eventLabel", "Private");
            }
            params.putString("rideName", tripMetadata.getName());
            REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNavigationIconClick() {
        finish();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }

    @Override
    public void onFetchAllTripsSuccess(InlineResponse200 response200) {
        isLoading = false;
        hideLoading();
        if (response200 != null && response200.getTrips() != null && response200.getTrips().size() > 0) {
            List<TripMetadata> tripMetadatalist = response200.getTrips();
            tripMetadata.addAll(tripMetadatalist);
            nextid = response200.getNext();
            /*Set<TripMetadata> s = new LinkedHashSet<>(tripMetadata);
            tripMetadata.clear();
            tripMetadata.addAll(s);*/
            bctListAdapter.notifyDataSetChanged();

        } else if (response200 != null && response200.getTrips() != null && response200.getTrips().size() == 0) {
            Toast.makeText(this, "No Routes Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFetchAllTripsFailure(String error) {
        isLoading = false;
      //  Log.e( "onFetchAllTripsFailure: ", error);
        Toast.makeText(this, "Something went wrong.Please try again later", Toast.LENGTH_SHORT).show();
        hideLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right);
    }
}
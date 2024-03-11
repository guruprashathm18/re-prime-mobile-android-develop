package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.ourworld.adapter.ItineraryRecyclerViewAdapter;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesTourPresenter;
import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;

import java.util.ArrayList;
import java.util.Map;

import static com.royalenfield.reprime.utils.REConstants.OUR_WORLD_EVENTS;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;

/**
 * RidesTourItineraryAdapter binds the itenerary to the recyclerView
 */
public class RidesTourItineraryAdapter extends RecyclerView.Adapter<RidesTourItineraryAdapter.MyViewHolder> {

    private static final int TYPE_START = 1;
    private static final int TYPE_MIDDLE = 2;
    private static final int TYPE_END = 3;
    private Context mContext;
    private String mRideType, mViewType;
    private int mListItemPosition;
    private int size = 0;
    private RidesTourPresenter mRidesTourItineraryPresenter;


    public RidesTourItineraryAdapter(Context context, RidesTourPresenter ridesTourItineraryPresenter, String ridetype,
                                     int mPosition, String viewtype) {
        mContext = context;
        this.mRideType = ridetype;
        this.mRidesTourItineraryPresenter = ridesTourItineraryPresenter;
        this.mListItemPosition = mPosition;
        this.mViewType = viewtype;
        getItinerarySize();
    }

    private void getItinerarySize() {
        ArrayList<Map<String, Object>> mItenerary = null;
        switch (mRideType) {
            case OUR_WORLD_EVENTS:
                mItenerary = RERidesModelStore.getInstance().getOurWorldEventsResponse().get(mListItemPosition).
                        getItinerary();
                break;
            case RIDE_TYPE_POPULAR:
                mItenerary = RERidesModelStore.getInstance().getPopularRidesResponse().get(mListItemPosition).
                        getItinerary();
                break;
            case RIDE_TYPE_MARQUEE:
                mItenerary = RERidesModelStore.getInstance().getMarqueeRidesResponse().get(mListItemPosition).
                        getItinerary();
                break;
        }
        if (mItenerary != null && mItenerary.size() > 0) {
            size = mItenerary.size();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_START) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_ridetour_start, parent,
                    false);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_MIDDLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_ridetour_middle, parent,
                    false);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_END) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_ridetour_end, parent,
                    false);
            return new MyViewHolder(view);
        } else {
            throw new RuntimeException("The type has to be ");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mRideType.equals(OUR_WORLD_EVENTS)) {
            holder.mTextRideInfo.setVisibility(View.GONE);
            holder.mItineraryRecyclerView.setVisibility(View.VISIBLE);
        }
        mRidesTourItineraryPresenter.onRidesTourRowViewAtPosition(position, holder, mRideType, mListItemPosition, mViewType);

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_START;
        } else if (position == size - 1) {
            return TYPE_END;
        } else {
            return TYPE_MIDDLE;
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements IRidesTourRowView {
        View mView1;
        TextView mName, mTextRideInfo;
        ImageView mStatusSquare;
        RecyclerView mItineraryRecyclerView;
        View mView;

        MyViewHolder(View view) {
            super(view);
            mName = view.findViewById(R.id.text_status);
            mTextRideInfo = view.findViewById(R.id.text_ride_info);
            mStatusSquare = view.findViewById(R.id.imageView_status_square);
            mItineraryRecyclerView = view.findViewById(R.id.itinerary_rv);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
            mItineraryRecyclerView.setLayoutManager(mLayoutManager);
            mItineraryRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mView = view.findViewById(R.id.view13);
            mView1 = view.findViewById(R.id.view13_for_recycler_view);
        }

        @Override
        public void setKeyPlaceImage(String placeImageUrl) {

        }

        @Override
        public void setKeyPlaceName(String placeName) {

        }

        @Override
        public void setItineraryDate(String date) {
            mName.setText(date);
        }

        @Override
        public void setItineraryDesciption(String summary) {
            mTextRideInfo.setText(summary);
            if (size == 1) {
                mView.setVisibility(View.GONE);
                mView1.setVisibility(View.GONE);
            }
        }

        @Override
        public void setRideGalleryImages(String srcPath) {

        }

        @Override
        public void setEventHighlightsRecyclerView(ArrayList<Map<String, Object>> eventHighlightsList) {
            if (eventHighlightsList != null && eventHighlightsList.size() > 0) {
                ItineraryRecyclerViewAdapter mItineraryRecyclerViewAdapter =
                        new ItineraryRecyclerViewAdapter(mContext, eventHighlightsList);
                mItineraryRecyclerView.setAdapter(mItineraryRecyclerViewAdapter);
            }
        }


    }
}

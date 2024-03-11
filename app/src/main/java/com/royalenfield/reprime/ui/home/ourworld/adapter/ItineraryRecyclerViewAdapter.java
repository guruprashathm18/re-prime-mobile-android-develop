package com.royalenfield.reprime.ui.home.ourworld.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.rides.custom.RECircularImageView;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.Map;

import static com.royalenfield.reprime.utils.REFirestoreConstants.EVENT_HIGHLIGHTS_IMAGE;
import static com.royalenfield.reprime.utils.REFirestoreConstants.EVENT_HIGHLIGHTS_TITLE;

/**
 * Adapter creates views for the events and schedule items and binds the data to the views.
 */
public class ItineraryRecyclerViewAdapter extends RecyclerView.Adapter<ItineraryRecyclerViewAdapter.ItineraryViewHolder> {

    private Context mContext;
    private ArrayList<Map<String, Object>> mEventHighlights;

    public ItineraryRecyclerViewAdapter(Context context, ArrayList<Map<String, Object>> eventHighlightsList) {
        this.mContext = context;
        mEventHighlights = eventHighlightsList;
    }

    @NonNull
    @Override
    public ItineraryRecyclerViewAdapter.ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_itinerary_recycler_view_item,
                parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryRecyclerViewAdapter.ItineraryViewHolder holder, int position) {
        holder.mTvEventsAndSchedules.setText((CharSequence) mEventHighlights.get(position).get(EVENT_HIGHLIGHTS_TITLE));
        String eventsAndSchedulesImage = (String) mEventHighlights.get(position).get(EVENT_HIGHLIGHTS_IMAGE);
        REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + eventsAndSchedulesImage,
                holder.mIvEventsAndSchedules);
    }

    @Override
    public int getItemCount() {
        if (mEventHighlights != null && mEventHighlights.size() > 0) {
            return mEventHighlights.size();
        }
        return 0;
    }

    class ItineraryViewHolder extends RecyclerView.ViewHolder {

        RECircularImageView mIvEventsAndSchedules;
        TextView mTvEventsAndSchedules;

        ItineraryViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvEventsAndSchedules = itemView.findViewById(R.id.events_and_schedules_image);
            mTvEventsAndSchedules = itemView.findViewById(R.id.tv_events_and_schedules);
        }
    }

}

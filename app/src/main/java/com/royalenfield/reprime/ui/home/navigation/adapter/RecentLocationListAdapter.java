package com.royalenfield.reprime.ui.home.navigation.adapter;

import static com.royalenfield.reprime.utils.REConstants.KEY_TRIPPER_GTM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.google.android.gms.maps.model.LatLng;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.home.navigation.model.RecentLocation;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;

public class RecentLocationListAdapter extends RecyclerView.Adapter<RecentLocationListAdapter.ViewHolder> {

        private ArrayList<RecentLocation> localDataSet;
        public OnRecentLocationItemClickListener onRecentLocationItemClickListener;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView textView;


            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                textView = (TextView) view.findViewById(R.id.text_place);
                ImageView Img_placeholder = (ImageView) view.findViewById(R.id.location_icon);
                Img_placeholder.setImageResource(R.drawable.recent_item);
                view.setOnClickListener(this);


            }

            public TextView getTextView() {
                return textView;
            }


            @Override
            public void onClick(View v) {
                if (onRecentLocationItemClickListener!=null)

                onRecentLocationItemClickListener.onRecentLocationItemClick(localDataSet.get(getAdapterPosition()).getLocationName(),localDataSet.get(getAdapterPosition()).getLocationCoordinates());
                Bundle params = new Bundle();
                params.putString("eventCategory", "Tripper");
                params.putString("eventAction", "Recent Locations Clicked");
                params.putString("eventLabel", localDataSet.get(getAdapterPosition()).getLocationName());
                REUtils.logGTMEvent(KEY_TRIPPER_GTM, params);

            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public RecentLocationListAdapter(ArrayList<RecentLocation> dataSet, OnRecentLocationItemClickListener onrecentLocationItemClickListener) {
            localDataSet = dataSet;
            onRecentLocationItemClickListener = onrecentLocationItemClickListener;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_places_search, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            viewHolder.getTextView().setText(localDataSet.get(position).getLocationName());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if (localDataSet != null && localDataSet.size() > 0) {
                return localDataSet.size();
            } else {
                return 0;
            }
        }
    public interface OnRecentLocationItemClickListener {
        void onRecentLocationItemClick(String place, LatLng latLng);

    }
    }

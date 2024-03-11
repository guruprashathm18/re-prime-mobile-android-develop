package com.royalenfield.reprime.ui.home.rides.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.home.rides.presenter.SearchPlaceRowPresenter;
import com.royalenfield.reprime.ui.home.rides.views.SearchPlaceRowView;

import java.util.List;

/**
 * This adapter holds the search results
 */
public class SearchPlacesListAdapter extends RecyclerView.Adapter<SearchPlacesListAdapter.SearchPlaceHolder> {

    private List<POIResults> mPlacesList;
    //Listener for individual item click events.
    private OnItemClickListener onItemClickListener;
    private SearchPlaceRowPresenter mSearchPlacesRowPresenter;

    public SearchPlacesListAdapter(List<POIResults> searchResults, OnItemClickListener clickListener,
                                   SearchPlaceRowPresenter placeRowPresenter) {
        mPlacesList = searchResults;
        setOnItemClickListener(clickListener);
        mSearchPlacesRowPresenter = placeRowPresenter;
    }

    /**
     * Setter for {@link SearchPlacesListAdapter.OnItemClickListener} instance.
     *
     * @param onItemClickListener {@link SearchPlacesListAdapter.OnItemClickListener}
     */
    private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchPlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_places_search, parent, false);
        return new SearchPlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPlaceHolder searchPlaceHolder, int position) {
        mSearchPlacesRowPresenter.onBindSearchPlaceRowViewAtPosition(position, searchPlaceHolder, mPlacesList);
    }

    @Override
    public int getItemCount() {
        return mSearchPlacesRowPresenter.getRepositoriesRowsCount(mPlacesList);
    }

    public interface OnItemClickListener {
        void onItemClick(String place, double latitude, double longitude);
    }

    /**
     * A Simple ViewHolder for the SearchPlaceHolder RecyclerView
     */
    public class SearchPlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            SearchPlaceRowView {

        TextView mPlaceText, mDistanceText, mPlaceAddress;


        private SearchPlaceHolder(final View itemView) {
            super(itemView);
            mPlaceText = itemView.findViewById(R.id.text_place);
            mDistanceText = itemView.findViewById(R.id.textView_distance);
            mPlaceAddress = itemView.findViewById(R.id.tv_poi_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(mPlacesList.get(getAdapterPosition()).getName(),
                        mPlacesList.get(getAdapterPosition()).getGeometry().getLocation().getLat(),
                        mPlacesList.get(getAdapterPosition()).getGeometry().getLocation().getLng());
            }
        }

        @Override
        public void setPlace(String place) {
            mPlaceText.setText(place);
        }

        @Override
        public void setArea(String area) {
            mPlaceAddress.setText(area);
        }

        @Override
        public void setDistance(String distance) {
            mDistanceText.setText(distance);
        }
    }
}

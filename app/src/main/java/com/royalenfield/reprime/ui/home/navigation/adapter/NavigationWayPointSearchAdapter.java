package com.royalenfield.reprime.ui.home.navigation.adapter;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bosch.softtec.components.core.conversion.LengthUnit;
import com.bosch.softtec.components.core.models.Distance;
import com.bosch.softtec.components.midgard.core.search.models.results.SearchResult;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.googlemap.poi.POIPredictionResults;
import com.royalenfield.reprime.ui.home.rides.adapter.SearchPlacesListAdapter;
import com.royalenfield.reprime.ui.home.rides.views.SearchPlaceRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

public class NavigationWayPointSearchAdapter extends RecyclerView.Adapter<NavigationWayPointSearchAdapter.SearchPlaceHolder> {

    private final Context mContext;
    private List<POIPredictionResults> mPlacesList;
    //Listener for individual item click events.
    private OnItemClickListener onItemClickListener;
    private double mDistance = 0.0;
    Location mCurrentLocation;
    boolean isMilesUnitSelected =  false;


    public NavigationWayPointSearchAdapter(Context context, List<POIPredictionResults> poiPredictionResults,
                                           OnItemClickListener clickListener,boolean isMilesunitselected) {
        this.mContext = context;
        mPlacesList = poiPredictionResults;
        setOnItemClickListener(clickListener);
        mCurrentLocation = REUtils.getLocationFromModel();
        isMilesUnitSelected = isMilesunitselected;
        Log.e("VV1",""+isMilesUnitSelected);
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
        return new NavigationWayPointSearchAdapter.SearchPlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPlaceHolder searchPlaceHolder, int position) {
        try {
            if (mPlacesList != null && mPlacesList.size() > 0) {
                POIPredictionResults searchResult = mPlacesList.get(position);
                if (searchResult instanceof POIPredictionResults) {
                    POIPredictionResults poiSearchResult = mPlacesList.get(position);
                    searchPlaceHolder.setPlace(poiSearchResult.getStructuredFormatting().getMainText());
                    searchPlaceHolder.setArea(poiSearchResult.getStructuredFormatting().getSecondaryText());
                } else {
                    searchPlaceHolder.setPlace(mPlacesList.get(position).getStructuredFormatting().getSecondaryText());
                }
                getDistance(position);
                if (REUserModelStore.getInstance().getLatitude() != 0 &&
                        REUserModelStore.getInstance().getLongitude() != 0 && mDistance != 0) {
                    if (isMilesUnitSelected){
                        Distance m = new Distance(mDistance, LengthUnit.METERS);
                        searchPlaceHolder.setDistance(REUtils.formatDistanceInMilesUnit(m));
                    }else{
                        searchPlaceHolder.setDistance(REUtils.getDistanceInUnits(mDistance));
                    }

                }
            }
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }
    }

    private void getDistance(int position) {

        mDistance = mPlacesList.get(position).getDistanceMeters();
    }

    @Override
    public int getItemCount() {
        if (mPlacesList != null && mPlacesList.size() > 0) {
            return mPlacesList.size();
        } else {
            return 0;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String place, String placeId);

        void onItemClick(String place, SearchResult searchResults);
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
                POIPredictionResults poiSearchResult = mPlacesList.get(getAdapterPosition());
                /*onItemClickListener.onItemClick(poiSearchResult.getStructuredFormatting().getMainText(),
                        poiSearchResult.getPlaceId());*/
                String mLabel = poiSearchResult.getStructuredFormatting().getMainText();
                Bundle params = new Bundle();
                params.putString("eventCategory", REConstants.KEY_TRIPPER_GTM);
                params.putString("eventAction", REConstants.destinationEntered);
                params.putString("eventLabel", mLabel);
//                params.putString("modelName", REUtils.getDeviceModel());
                REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params);
                onItemClickListener.onItemClick(mLabel, poiSearchResult.getSearchResults());
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

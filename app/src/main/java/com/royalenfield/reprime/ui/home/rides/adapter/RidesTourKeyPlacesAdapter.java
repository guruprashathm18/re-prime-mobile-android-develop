package com.royalenfield.reprime.ui.home.rides.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesTourPresenter;
import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;
import com.royalenfield.reprime.utils.REConstants;

import java.util.ArrayList;
import java.util.Map;

import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_MARQUEE;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;

/**
 * RidesTourKeyPlacesAdapter binds the data to the key places recyclerView
 */
public class RidesTourKeyPlacesAdapter extends RecyclerView.Adapter<RidesTourKeyPlacesAdapter.KeyPlacesViewHolderI> {

    Activity mContext;
    private RidesTourPresenter mRidesTourKeyPlacesPresenter;
    private String mRideType, mViewType;
    private int mListItemPosition;


    public RidesTourKeyPlacesAdapter(Activity context, RidesTourPresenter ridesTourKeyPlacesPresenter,
                                     String ridetype, int mPosition, String viewtype) {
        this.mContext = context;
        this.mRidesTourKeyPlacesPresenter = ridesTourKeyPlacesPresenter;
        this.mRideType = ridetype;
        this.mViewType = viewtype;
        this.mListItemPosition = mPosition;
    }

    @NonNull
    @Override
    public KeyPlacesViewHolderI onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rides_tour_keyplaces, parent,
                false);
        return new KeyPlacesViewHolderI(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyPlacesViewHolderI holder, int position) {
        //Calling Row presenter here
        mRidesTourKeyPlacesPresenter.onRidesTourRowViewAtPosition(position, holder, mRideType,
                mListItemPosition, mViewType);

    }

    @Override
    public int getItemCount() {
        int count = 0;
        ArrayList<Map<String, Object>> mWaypoints = null;
        if (REConstants.KEY_PLACES.equals(mViewType)) {
            if (mRideType.equals(RIDE_TYPE_POPULAR)) {
                mWaypoints = RERidesModelStore.getInstance().getPopularRidesResponse().get(mListItemPosition).
                        getWaypoints();
            } else if (mRideType.equals(RIDE_TYPE_MARQUEE)) {
                mWaypoints = RERidesModelStore.getInstance().getMarqueeRidesResponse().get(mListItemPosition).
                        getWaypoints();
            }
        }
        if (mWaypoints != null) count = mWaypoints.size();
        return count;
    }

    class KeyPlacesViewHolderI extends RecyclerView.ViewHolder implements View.OnClickListener, IRidesTourRowView {

        ImageView placeImage;
        TextView textKeyPlace;

        KeyPlacesViewHolderI(View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.place_image);
            textKeyPlace = itemView.findViewById(R.id.text_key_place);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void setKeyPlaceImage(String placeImageUrl) {

        }

        @Override
        public void setKeyPlaceName(String placeName) {
            if (!placeName.equals(mContext.getString(R.string.text_hyphen_na))) {
                textKeyPlace.setText(placeName);
            } else {
                textKeyPlace.setVisibility(View.GONE);
            }
        }

        @Override
        public void setItineraryDate(String date) {

        }

        @Override
        public void setItineraryDesciption(String desciption) {

        }

        @Override
        public void setRideGalleryImages(String srcPath) {

        }

        @Override
        public void setEventHighlightsRecyclerView(ArrayList<Map<String, Object>> eventHighlightsList) {

        }

    }
}


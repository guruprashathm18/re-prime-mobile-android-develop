package com.royalenfield.reprime.ui.home.rides.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.ui.home.rides.presenter.RidesTourPresenter;
import com.royalenfield.reprime.ui.home.rides.views.IRidesTourRowView;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static com.royalenfield.reprime.utils.REConstants.*;

/**
 * A Simple Adapter for the RecyclerView binds data to gallery RecyclerView
 */
public class CategoryRidesAdapter extends RecyclerView.Adapter<CategoryRidesAdapter.CategoryRidesListHolder> {
    private Context mContext;
    private String mViewType, mRideType;
    private int mListItemPosition;
    private RidesTourPresenter mRidesTourKeyPlacesPresenter;

    public CategoryRidesAdapter(Activity context, RidesTourPresenter ridesTourKeyPlacesPresenter,
                                String ridetype, int mPosition, String viewtype) {
        this.mContext = context;
        this.mRidesTourKeyPlacesPresenter = ridesTourKeyPlacesPresenter;
        this.mViewType = viewtype;
        this.mListItemPosition = mPosition;
        this.mRideType = ridetype;
    }

    @NotNull
    @Override
    public CategoryRidesListHolder onCreateViewHolder(@NotNull final ViewGroup parent, final int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rides_list, parent,
                false);
        return new CategoryRidesListHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CategoryRidesListHolder holder, final int position) {
        mRidesTourKeyPlacesPresenter.onRidesTourRowViewAtPosition(position, holder, mRideType, mListItemPosition, mViewType);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (REConstants.KEY_RIDE_GALLERY.equals(mViewType)) {
            switch (mRideType) {
                case RIDE_TYPE_POPULAR:
                    count = RERidesModelStore.getInstance().getPopularRidesResponse().get(mListItemPosition).
                            getGallery().size() >= 4 ? 4 : RERidesModelStore.getInstance().getPopularRidesResponse().
                            get(mListItemPosition).
                            getGallery().size();
                    break;
                case RIDE_TYPE_MARQUEE:
                    count = RERidesModelStore.getInstance().getMarqueeRidesResponse().get(mListItemPosition).
                            getGallery().size() >= 4 ? 4 : RERidesModelStore.getInstance().getMarqueeRidesResponse().
                            get(mListItemPosition).
                            getGallery().size();
                    break;
                case OUR_WORLD_EVENTS:
                    count = RERidesModelStore.getInstance().getOurWorldEventsResponse().get(mListItemPosition).
                            getGallery().size() >= 4 ? 4 : RERidesModelStore.getInstance().getOurWorldEventsResponse().
                            get(mListItemPosition).
                            getGallery().size();
                    break;
            }
        }
        return count;
    }

    /**
     * A Simple ViewHolder for the Engine RecyclerView
     */
    public class CategoryRidesListHolder extends RecyclerView.ViewHolder implements IRidesTourRowView {
        private ImageView ivInspirationRide;
        private FrameLayout mFrameMapImage;

        private CategoryRidesListHolder(final View itemView) {
            super(itemView);
            ivInspirationRide = itemView.findViewById(R.id.ivInspirationImage);
            mFrameMapImage = itemView.findViewById(R.id.frame_map_image);
            mFrameMapImage.setForeground(mContext.getDrawable(R.drawable.foreground_marqueerides_image));
            itemView.setTag(this);

        }

        @Override
        public void setKeyPlaceImage(String placeImageUrl) {

        }

        @Override
        public void setKeyPlaceName(String placeName) {

        }

        @Override
        public void setItineraryDate(String date) {

        }

        @Override
        public void setItineraryDesciption(String desciption) {

        }

        @Override
        public void setRideGalleryImages(String srcPath) {
            if (srcPath != null && !srcPath.isEmpty()) {
                REUtils.loadImageWithGlide(mContext, REUtils.getMobileappbaseURLs().getAssetsURL() + srcPath, ivInspirationRide);
            }
        }

        @Override
        public void setEventHighlightsRecyclerView(ArrayList<Map<String, Object>> eventHighlightsList) {

        }

    }

}



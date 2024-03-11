package com.royalenfield.reprime.ui.home.rides.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royalenfield.reprime.R;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.models.response.googlemap.poi.Photo;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * This adapter holds the recommended route details
 */
public class RecommendedRoutesAdapter extends RecyclerView.Adapter<RecommendedRoutesAdapter.RecommendedRoutesHolder> {

    private final Context mContext;
    private List<POIResults> mPOIResults;
    private Location mCurrentLocation;

    public RecommendedRoutesAdapter(Context context, List<POIResults> strArrayList) {
        this.mContext = context;
        mPOIResults = strArrayList;
        mCurrentLocation = REUtils.getLocationFromModel();
    }

    @NonNull
    @Override
    public RecommendedRoutesHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_routes, parent, false);
        return new RecommendedRoutesAdapter.RecommendedRoutesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedRoutesHolder recommendedRoutesHolder, int i) {
        try {
            String strPOIUrl = "";
            List<Photo> photos = mPOIResults.get(i).getPhotos();
            if (photos != null && photos.size() > 0) {
                strPOIUrl = REUtils.getMobileappbaseURLs().getTbtURL() +
                        photos.get(0).getPhotoReference();
            }
            recommendedRoutesHolder.tv_Route.setText(mPOIResults.get(i).getName());
            List<String> types = mPOIResults.get(i).getTypes();
            if (types != null && types.size() > 0) {
                String mTypes = types.get(0).substring(0, 1).toUpperCase() + types.get(0).substring(1).toLowerCase();
                recommendedRoutesHolder.tv_Category.setText(mTypes.replace("_", " "));
            }
            double mDistance;
            Location poiLocation = new Location("poi");
            poiLocation.setLatitude(mPOIResults.get(i).getGeometry().getLocation().getLat());
            poiLocation.setLongitude(mPOIResults.get(i).getGeometry().getLocation().getLng());
            mDistance = mCurrentLocation.distanceTo(poiLocation);
            recommendedRoutesHolder.tv_Distance.setText(REUtils.getDistanceInUnits(mDistance));
            REUtils.loadImageWithGlide(mContext, strPOIUrl, recommendedRoutesHolder.iv_PoiImage);
        } catch (IndexOutOfBoundsException e) {
            RELog.e(e);
        } catch (NullPointerException e) {
            RELog.e(e);
        } catch (Exception e) {
            RELog.e(e);
        }

    }

    @Override
    public int getItemCount() {
        if (mPOIResults != null && mPOIResults.size() > 0) {
            return mPOIResults.size();
        } else {
            return 0;
        }
    }

    /**
     * A Simple ViewHolder for the RecommendedRoute RecyclerView
     */
    class RecommendedRoutesHolder extends RecyclerView.ViewHolder {

        private TextView tv_Route;
        private TextView tv_Category;
        private TextView tv_Distance;
        private ImageView iv_PoiImage;

        private RecommendedRoutesHolder(final View itemView) {
            super(itemView);
            tv_Category = itemView.findViewById(R.id.category);
            tv_Route = itemView.findViewById(R.id.textView_route);
            tv_Distance = itemView.findViewById(R.id.textViewKMInfo);
            iv_PoiImage = itemView.findViewById(R.id.imageView);
        }
    }
}

package com.royalenfield.reprime.ui.home.rides.presenter;

import android.location.Location;
import com.royalenfield.reprime.models.response.common.REUserModelStore;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.home.rides.views.SearchPlaceRowView;
import com.royalenfield.reprime.utils.REUtils;

import java.util.List;

import com.royalenfield.reprime.utils.RELog;

/**
 * This presenter is for binding rows in search list
 */
public class SearchPlaceRowPresenter implements ISearchPlacesRowPresenter {

    @Override
    public void onBindSearchPlaceRowViewAtPosition(int position, SearchPlaceRowView rowView, List<POIResults> mPlacesList) {
        Location mCurrentLocation = REUtils.getLocationFromModel();
        try {
            if (mPlacesList != null && mPlacesList.size() > 0) {
                if (rowView != null) {
                    rowView.setPlace(mPlacesList.get(position).getName());
                    rowView.setArea(mPlacesList.get(position).getFormatted_address());
                    if (REUserModelStore.getInstance().getLatitude() != 0 && REUserModelStore.getInstance().getLongitude() != 0) {
                        double mDistance;
                        Location placeLocation = new Location("placeLoc");
                        placeLocation.setLatitude(mPlacesList.get(position).getGeometry().getLocation().getLat());
                        placeLocation.setLongitude(mPlacesList.get(position).getGeometry().getLocation().getLng());
                        mDistance = mCurrentLocation.distanceTo(placeLocation);
                        rowView.setDistance(REUtils.getDistanceInUnits(mDistance));
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

    @Override
    public int getRepositoriesRowsCount(List<POIResults> mPlacesList) {
        if (mPlacesList != null && mPlacesList.size() > 0) {
            return mPlacesList.size();
        } else {
            return 0;
        }
    }

}

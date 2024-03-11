package com.royalenfield.reprime.ui.home.rides.presenter;

import com.royalenfield.reprime.models.response.googlemap.poi.NearBySearchResponse;
import com.royalenfield.reprime.models.response.googlemap.poi.POIResults;
import com.royalenfield.reprime.ui.home.rides.views.SearchPlaceRowView;

import java.util.List;

public interface ISearchPlacesRowPresenter {

    /**
     * For binding the data to a row
     *
     * @param position     : position
     * @param rowView:     SearchPlaceRowView
     * @param mPlacesList: ArrayList<String>
     */
    void onBindSearchPlaceRowViewAtPosition(int position, SearchPlaceRowView rowView,
                                            List<POIResults> mPlacesList);

    /**
     * For getting the list item count
     *
     * @param mPlacesList
     * @return
     */
    int getRepositoriesRowsCount(List<POIResults> mPlacesList);
}

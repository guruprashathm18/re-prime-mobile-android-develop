package com.royalenfield.reprime.ui.home.rides.views;

import java.util.ArrayList;
import java.util.Map;

public interface IRidesTourRowView {

    void setKeyPlaceImage(String placeImageUrl);

    void setKeyPlaceName(String placeName);

    void setItineraryDate(String date);

    void setItineraryDesciption(String desciption);

    void setRideGalleryImages(String srcPath);

    void setEventHighlightsRecyclerView(ArrayList<Map<String, Object>> eventHighlightsList);

}

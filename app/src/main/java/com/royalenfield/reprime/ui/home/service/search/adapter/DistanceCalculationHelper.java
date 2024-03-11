package com.royalenfield.reprime.ui.home.service.search.adapter;

import android.location.Location;
import android.util.Log;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.response.firebase.DealerMasterResponse;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;

/**
 * Calculate the distance between rider current location and Dealers location.
 */
public class DistanceCalculationHelper {
    private static DistanceCalculationHelper distanceCalculationHelper = new DistanceCalculationHelper();
    private List<DealerMasterResponse> mDealerResponseList;
    private List<DealerMasterResponse> mDealerNoLocationList = new ArrayList<>();
    private float distance;
    private Location currentLocation;


    public static DistanceCalculationHelper getInstances() {
        return distanceCalculationHelper;
    }

    /**
     * @param dealerUpcomingRidesResponse : List<DealerUpcomingRidesResponse>
     * @return : List<DealerUpcomingRidesResponse>
     */
    public List<DealerUpcomingRidesResponse> getSortedDealerRidesByDistance(List<DealerUpcomingRidesResponse> dealerUpcomingRidesResponse) {
        List<DealerUpcomingRidesResponse> mDealerUpcomingRidesList = new ArrayList<>();
        List<DealerUpcomingRidesResponse> mDealerUpcomingRidesNoLocationList = new ArrayList<>();
        currentLocation = REUtils.getLocationFromModel();
        if (currentLocation != null && currentLocation.getLatitude() == 0.0 && currentLocation.getLongitude() == 0.0) {
            return dealerUpcomingRidesResponse;
        } else {
            for (int i = 0; i < dealerUpcomingRidesResponse.size(); i++) {
                Location dealersLocation = new Location("src");
                if (dealerUpcomingRidesResponse.get(i).getDealerRideInfo() != null && dealerUpcomingRidesResponse.get(i).getDealerRideInfo().size() > 0
                        && dealerUpcomingRidesResponse.get(i).getDealerRideInfo().
                        get(REFirestoreConstants.DEALER_COORDINATES) != null && dealerUpcomingRidesResponse.get(i).getDealerRideInfo().
                        get(REFirestoreConstants.DEALER_COORDINATES).size() > 0) {
                    dealersLocation.setLatitude(Double.parseDouble(String.valueOf(dealerUpcomingRidesResponse.get(i).
                            getDealerRideInfo().get(REFirestoreConstants.DEALER_COORDINATES).get(REFirestoreConstants.DEALER_LATITUDE))));
                    dealersLocation.setLongitude(Double.parseDouble(String.valueOf(dealerUpcomingRidesResponse.get(i).
                            getDealerRideInfo().get(REFirestoreConstants.DEALER_COORDINATES).get(REFirestoreConstants.DEALER_LONGITUDE))));
                }
                distance = currentLocation.distanceTo(dealersLocation);
                if (distance > 0) {
                    dealerUpcomingRidesResponse.get(i).setDistance(getRoundedOffDistance(distance));
                    mDealerUpcomingRidesList.add(dealerUpcomingRidesResponse.get(i));
                } else {
                    //Adding dealers separately to list which doesn't have location
                    dealerUpcomingRidesResponse.get(i).setDistance(0);
                    mDealerUpcomingRidesNoLocationList.add(dealerUpcomingRidesResponse.get(i));
                }
            }
        }
        Collections.sort(mDealerUpcomingRidesList, (o1, o2) -> (Float.compare(o1.getDistance(), o2.getDistance())));
        mDealerUpcomingRidesList.addAll(mDealerUpcomingRidesNoLocationList);
        return mDealerUpcomingRidesList;

    }

    public List<MarqueeRidesResponse> getSortedMarqueeRidesResponse(List<MarqueeRidesResponse> marqueeRidesResponse) {
        List<MarqueeRidesResponse> mMarqueeRidesResponseList = new ArrayList<>();
        List<MarqueeRidesResponse> mMarqueeRidesResponseNoLocationList = new ArrayList<>();
        currentLocation = REUtils.getLocationFromModel();
        Log.e("test","Marquee rides location = "+currentLocation);
        if (currentLocation != null && currentLocation.getLatitude() == 0.0 && currentLocation.getLongitude() == 0.0) {
            return marqueeRidesResponse;
        } else {
            for (int i = 0; i < marqueeRidesResponse.size(); i++) {
                Location dealersLocation = new Location("src");
                dealersLocation.setLatitude(Double.parseDouble(marqueeRidesResponse.get(i).getStartPointLatitude()));
                dealersLocation.setLongitude(Double.parseDouble(marqueeRidesResponse.get(i).getStartPointLongitude()));
             if(currentLocation!=null)
                distance = currentLocation.distanceTo(dealersLocation);
                if (distance > 0) {
                    marqueeRidesResponse.get(i).setDistanceCalculated(getRoundedOffDistance(distance));
                    mMarqueeRidesResponseList.add(marqueeRidesResponse.get(i));
                } else {
                    //Adding dealers separately to list which doesn't have location
                    marqueeRidesResponse.get(i).setDistanceCalculated(0);
                    mMarqueeRidesResponseNoLocationList.add(marqueeRidesResponse.get(i));
                }
            }
        }
        Collections.sort(mMarqueeRidesResponseList, (o1, o2) -> (Float.compare(o1.getDistanceCalculated(), o2.getDistanceCalculated())));
        mMarqueeRidesResponseList.addAll(mMarqueeRidesResponseNoLocationList);
        return mMarqueeRidesResponseList;
    }

    public List<UserUpcomingRidesResponse> getSortedUserUpcomingRides(List<UserUpcomingRidesResponse> userUpcomingRidesResponse) {
        List<UserUpcomingRidesResponse> mUserUpcomingRidesResponseList = new ArrayList<>();
        List<UserUpcomingRidesResponse> mUserUpcomingRidesResponseNoLocationList = new ArrayList<>();
        currentLocation = REUtils.getLocationFromModel();
        if (currentLocation != null && currentLocation.getLatitude() == 0.0 && currentLocation.getLongitude() == 0.0) {
            return userUpcomingRidesResponse;
        } else {
            for (int i = 0; i < userUpcomingRidesResponse.size(); i++) {
                Location dealersLocation = new Location("src");
                if (userUpcomingRidesResponse.get(i).getStartPointCoordinates() != null
                        && userUpcomingRidesResponse.get(i).getStartPointCoordinates().size() > 0) {
                    dealersLocation.setLatitude(userUpcomingRidesResponse.get(i).getStartPointCoordinates().get(0));
                    dealersLocation.setLongitude(userUpcomingRidesResponse.get(i).getStartPointCoordinates().get(1));
                }
                distance = currentLocation.distanceTo(dealersLocation);
                if (distance > 0) {
                    userUpcomingRidesResponse.get(i).setDistance(getRoundedOffDistance(distance));
                    mUserUpcomingRidesResponseList.add(userUpcomingRidesResponse.get(i));
                } else {
                    //Adding dealers separately to list which doesn't have location
                    userUpcomingRidesResponse.get(i).setDistance(0);
                    mUserUpcomingRidesResponseNoLocationList.add(userUpcomingRidesResponse.get(i));
                }
            }
        }
        Collections.sort(mUserUpcomingRidesResponseList, (o1, o2) -> (Float.compare(o1.getDistance(), o2.getDistance())));
        mUserUpcomingRidesResponseList.addAll(mUserUpcomingRidesResponseNoLocationList);
        return mUserUpcomingRidesResponseList;
    }

    public List<PopularRidesResponse> getSortedPopularRides(List<PopularRidesResponse> popularRidesResponse) {
        List<PopularRidesResponse> mPopularRidesResponseList = new ArrayList<>();
        List<PopularRidesResponse> mPopularRidesResponseNoLocationList = new ArrayList<>();
        currentLocation = REUtils.getLocationFromModel();
        if (currentLocation.getLatitude() == 0.0 && currentLocation.getLongitude() == 0.0) {
            return popularRidesResponse;
        } else {
            for (int i = 0; i < popularRidesResponse.size(); i++) {
                Location dealersLocation = new Location("src");
                dealersLocation.setLatitude(Double.parseDouble(popularRidesResponse.get(i).getStartPointLatitude()));
                dealersLocation.setLongitude(Double.parseDouble(popularRidesResponse.get(i).getStartPointLongitude()));
                distance = currentLocation.distanceTo(dealersLocation);

                if (distance > 0) {
                    popularRidesResponse.get(i).setDistanceCalculated(getRoundedOffDistance(distance));
                    mPopularRidesResponseList.add(popularRidesResponse.get(i));
                } else {
                    //Adding dealers separately to list which doesn't have location
                    popularRidesResponse.get(i).setDistanceCalculated(0);
                    mPopularRidesResponseNoLocationList.add(popularRidesResponse.get(i));
                }
            }
        }
        Collections.sort(mPopularRidesResponseList, (o1, o2) -> (Float.compare(o1.getDistanceCalculated(), o2.getDistanceCalculated())));
        mPopularRidesResponseList.addAll(mPopularRidesResponseNoLocationList);
        return mPopularRidesResponseList;
    }


    private float getRoundedOffDistance(float distance) {
        distance = distance / 1000;  //in km

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.#", otherSymbols);
        df.setRoundingMode(RoundingMode.CEILING);
        return Float.parseFloat(df.format(distance));
    }

    public List<DealerMasterResponse> getDistanceBetweenServiceCenter(List<DealerMasterResponse> dealersList, boolean isSortingRequired) {
        mDealerResponseList = new ArrayList<>();
        mDealerNoLocationList = new ArrayList<>();
        //Fetching current location from preferences
        currentLocation = REUtils.getLocationFromModel();
    if (currentLocation.getLatitude() == 0.0 && currentLocation.getLongitude() == 0.0)
            return dealersList;
        else {
            List<DealerMasterResponse> dealersResponseWithDistanceList = calculateDistance(dealersList);
            if (isSortingRequired) {
                sortDealersListByDistance(dealersResponseWithDistanceList);
                //Adding dealer who are not having location to filtered list
                mDealerResponseList.addAll(mDealerNoLocationList);
                return mDealerResponseList;
            } else {
                REApplication.getInstance().setDealersResponse(dealersResponseWithDistanceList);
                return dealersResponseWithDistanceList;
            }
        }
    }

    /**
     * method to calculate distance between current location and list of dealers location
     *
     * @param dealersList :: dealersResponseList
     * @return returns dealersResponse list with distance.
     */
    private List<DealerMasterResponse> calculateDistance(List<DealerMasterResponse> dealersList) {
        float distance;
        List<DealerMasterResponse> dealersResponseList = new ArrayList<>();
        for (DealerMasterResponse response : dealersList) {
            distance = 0;
            Location dealersLocation = new Location("src");
            if (response.getLatlng() != null && !response.getLatlng().equalsIgnoreCase("no")) {
                if (response.getLatlng().contains(",")) {
                    String[] latlon = response.getLatlng().substring(0,
                            response.getLatlng().length() - 1).split(",");
                    if (latlon.length > 0) {
                        dealersLocation.setLatitude(Double.parseDouble(latlon[0]));
                        dealersLocation.setLongitude(Double.parseDouble(latlon[1]));
                        distance = currentLocation.distanceTo(dealersLocation);
                    }
                }
            }
            if (distance > 0) {
                distance = distance / 1000;  //in km
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);
                distance = Float.parseFloat(df.format(distance));
            }
            if (distance > 0) {
                response.setDistance(String.valueOf(distance));
                dealersResponseList.add(response);
            } else  {
                //Adding dealers separately to list which doesn't have location
                response.setDistance("");
                mDealerNoLocationList.add(response);
            }
        }
        return dealersResponseList;
    }

    /**
     * Method to sort dealersResponse in Ascending order.
     *
     * @param dealersResponseList : List<DealersResponse>
     */
    private void sortDealersListByDistance(List<DealerMasterResponse> dealersResponseList) {
        Collections.sort(dealersResponseList, new Comparator<DealerMasterResponse>() {
            @Override
            public int compare(DealerMasterResponse o1, DealerMasterResponse o2) {
                //return (int) (o1.getDistance() - o2.getDistance());  //Ascending order
//                return (o1.getDistance() < o2.getDistance() ? -1 :
//                        (o1.getDistance() == o2.getDistance() ? 0 : 1));
                return 0;
            }

            @Override
            public Comparator<DealerMasterResponse> thenComparingInt(ToIntFunction<? super DealerMasterResponse> keyExtractor) {
                return null;
            }
        });
        mDealerResponseList = dealersResponseList;
    }
}

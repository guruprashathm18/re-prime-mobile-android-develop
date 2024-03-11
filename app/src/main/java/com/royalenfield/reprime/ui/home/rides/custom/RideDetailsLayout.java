package com.royalenfield.reprime.ui.home.rides.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.royalenfield.reprime.models.response.firestore.ourworld.EventsResponse;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.models.request.web.rides.WayPointsData;
import com.royalenfield.reprime.models.response.firestore.rides.DealerUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.MarqueeRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.PopularRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.RERidesModelStore;
import com.royalenfield.reprime.models.response.firestore.rides.UserUpcomingRidesResponse;
import com.royalenfield.reprime.models.response.firestore.rides.userrideinfo.ProfileRidesResponse;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.RECustomTyperFaceSpan;
import com.royalenfield.reprime.utils.REFirestoreConstants;
import com.royalenfield.reprime.utils.REUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.royalenfield.reprime.utils.REConstants.OUR_WORLD_EVENTS;
import static com.royalenfield.reprime.utils.REConstants.RIDE_TYPE_POPULAR;
import static com.royalenfield.reprime.utils.REFirestoreConstants.DEALER_RIDE_END_DATE;
import static com.royalenfield.reprime.utils.REFirestoreConstants.DEALER_RIDE_START_DATE;
import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_RIDE_PRICE_KEY;
import static com.royalenfield.reprime.utils.REFirestoreConstants.POPULAR_RIDES_RIDE_TYPE_KEY;

/**
 * @author BOP1KOR on 3/28/2019.
 * <p>
 * This is common layout for the displaying the ride details and can be uszed for the multiple screens.
 */
public class RideDetailsLayout extends ConstraintLayout {

    //View id's.
    private TextView tvRideRoute, tvRideDate, tvRideEndDate, tvRideStartTime, tvRideEndTime, tvRideDuration, tvRideDesc, tvRideTerrain, tvRiderJoined,
            tvDifficulty, tvRideStatus, tvLabelRideType;

    private TextView tvlLabelRideRoute, tvLabelRideDate, tvLabelRideEndDate, tvLabelRideStartTime, tvLabelRideEndTime, tvLabelRideDuration, tvLabelRideDesc,
            tvLabelTerrain, tvLabelRidersJoined, tvLabelDifficulty, tvLabelRideStatus, tvRideType;

    private String mRideRoute, mRideDescription, mRidersJoined;

    private Typeface mTypeFaceBold;
    private Typeface mTypeFaceRegular;

    /**
     * Constructor with a context.
     *
     * @param context The context.
     */
    public RideDetailsLayout(Context context) {
        this(context, null);
    }

    /**
     * Constructor with a context and attribute set.
     *
     * @param context The context.
     * @param attrs   The attribute set.
     */
    public RideDetailsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor with a context, attribute set and style resource.
     *
     * @param context      The context.
     * @param attrs        The attribute set.
     * @param defStyleAttr The style resource.
     */
    public RideDetailsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    /**
     * Initializes the views.
     */
    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_ride_details, this);

        tvlLabelRideRoute = findViewById(R.id.tv_label_ride_route);
        tvLabelRideDate = findViewById(R.id.tv_label_ride_start_date);
        tvLabelRideEndDate = findViewById(R.id.tv_label_ride_end_date);
        tvLabelRideStartTime = findViewById(R.id.tv_label_ride_start_time);
        tvLabelRideEndTime = findViewById(R.id.tv_label_ride_end_time);
        tvLabelRideDuration = findViewById(R.id.tv_label_ride_duration);
        tvLabelRideDesc = findViewById(R.id.tv_label_ride_desc);
        tvLabelDifficulty = findViewById(R.id.tv_label_difficulty);
        tvLabelTerrain = findViewById(R.id.tv_label_terrain);
        tvLabelRidersJoined = findViewById(R.id.tv_label_riders_joined);
        tvLabelRideStatus = findViewById(R.id.tv_label_rider_status);
        tvLabelRideType = findViewById(R.id.tv_label_ride_type);

        tvRideRoute = findViewById(R.id.tv_ride_route_name);
        tvRideDate = findViewById(R.id.tv_ride_start_date);
        tvRideEndDate = findViewById(R.id.tv_ride_end_date);
        tvRideStartTime = findViewById(R.id.tv_ride_start_time);
        tvRideEndTime = findViewById(R.id.tv_ride_end_time);
        tvRideDuration = findViewById(R.id.tv_ride_duration);
        tvRideDesc = findViewById(R.id.tv_ride_desc);
        tvDifficulty = findViewById(R.id.tv_difficulty);
        tvRideTerrain = findViewById(R.id.tv_terrain);
        tvRiderJoined = findViewById(R.id.tv_rider_joined);
        tvRideStatus = findViewById(R.id.tv_ride_status);
        tvRideType = findViewById(R.id.tv_ride_type);

        mTypeFaceBold = ResourcesCompat.getFont(getContext(), R.font.montserrat_bold);
        mTypeFaceRegular = ResourcesCompat.getFont(getContext(), R.font.montserrat_regular);
    }

    /**
     * Binds details for Dealer-Upcoming-Rides
     *
     * @param dealer : DealerUpcomingRidesResponse
     */
    public void bindDealerRideDetailsData(Context context, DealerUpcomingRidesResponse dealer) {
        fetchData(dealer);
        String startDate = null, endDate = null;
        String rideStartTime = null, rideDuration = null, rideTerrain = null;
        if (dealer.getRideInfo() != null) {
            startDate = String.valueOf(dealer.getRideInfo().get(DEALER_RIDE_START_DATE));
            endDate = String.valueOf(dealer.getRideInfo().get(DEALER_RIDE_END_DATE));
            startDate = REUtils.getDateObject(context, startDate);
            endDate = REUtils.getDateObject(context, endDate);
            startDate = !startDate.isEmpty() ? startDate :
                    getContext().getResources().getString(R.string.text_hyphen_na);
            endDate = !endDate.isEmpty() ? endDate :
                    getContext().getResources().getString(R.string.text_hyphen_na);
            rideStartTime = String.valueOf(dealer.getRideInfo().get(REFirestoreConstants.DEALER_RIDE_START_TIME));
            rideDuration = String.valueOf(dealer.getRideInfo().get(REFirestoreConstants.DEALER_RIDE_DURATION));
            rideTerrain = String.valueOf(dealer.getRideInfo().get(REFirestoreConstants.DEALER_RIDE_TERRAIN));

        }
        tvRideRoute.setText(mRideRoute != null && !mRideRoute.isEmpty() ? mRideRoute :
                getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideDate.setText(startDate);
        //tvRideEndDate.setText(endDate);
        tvLabelRideStartTime.setVisibility(VISIBLE);
        tvRideStartTime.setVisibility(VISIBLE);

        tvRideStartTime.setText(rideStartTime != null && !rideStartTime.isEmpty() && !rideStartTime.equals("null") ?
                rideStartTime :
                getResources().getString(R.string.text_hyphen_na));
        tvLabelRideDuration.setVisibility(VISIBLE);
        tvLabelRideDuration.setText(getResources().getString(R.string.ride_duration));
        tvRideDuration.setVisibility(VISIBLE);
        tvRideDuration.setText(setDuration(rideDuration));
        tvRideDesc.setText(mRideDescription != null && !mRideDescription.isEmpty() ? mRideDescription :
                getResources().getString(R.string.text_hyphen_na));
        tvRideTerrain.setText(rideTerrain != null && !rideTerrain.isEmpty() ? rideTerrain :
                getResources().getString(R.string.text_hyphen_na));
        tvRiderJoined.setText(mRidersJoined);

        tvDifficulty.setVisibility(GONE);
        tvLabelDifficulty.setVisibility(GONE);
        tvLabelRideStatus.setVisibility(GONE);
        tvRideStatus.setVisibility(GONE);
    }

    /**
     * Fetched the data for Popular and Marquee details
     *
     * @param dealer : DealerUpcomingRidesResponse
     */
    private void fetchData(DealerUpcomingRidesResponse dealer) {
        String description = String.valueOf(dealer.getRideInfo().get(REFirestoreConstants.DEALER_RIDE_DETAILS));
        mRideDescription = !description.isEmpty() ? description : getResources().getString(R.string.text_hyphen_na);
        String rideRoute = String.format(REApplication.getAppContext().getString(R.string.text_dealer_ride_route),
                dealer.getStartPoint(), dealer.getEndPoint());
        mRideRoute = !rideRoute.isEmpty() ? rideRoute : getResources().getString(R.string.text_hyphen_na);
        mRidersJoined = dealer.getRidersJoined() != null && dealer.getRidersJoined().size() > 0 ?
                String.valueOf(dealer.getRidersJoined().size()) : getResources().getString(R.string.text_hyphen_na);
    }

    /**
     * Binds data to views
     */
    public void bindPopularAndMarqueeRideDetailsData(int position, String type) {
        List<PopularRidesResponse> mPopularRidesResponse = RERidesModelStore.getInstance().getPopularRidesResponse();
        List<MarqueeRidesResponse> mMarqueeRidesResponse = RERidesModelStore.getInstance().getMarqueeRidesResponse();
        tvlLabelRideRoute.setText(getContext().getResources().getString(R.string.text_destination));
        tvLabelRideDate.setText(getContext().getResources().getString(R.string.text_ride_dates));
        tvLabelRideStartTime.setText(getContext().getResources().getString(R.string.text_distance));
        tvLabelRideDuration.setVisibility(VISIBLE);
        tvLabelRideDuration.setText(getContext().getResources().getString(R.string.text_terrain));
        tvLabelRideDesc.setText(getContext().getResources().getString(R.string.duration_title));
        tvLabelTerrain.setText(getContext().getResources().getString(R.string.text_price));

        tvLabelRidersJoined.setVisibility(GONE);
        tvRiderJoined.setVisibility(GONE);
        tvDifficulty.setVisibility(GONE);
        tvLabelDifficulty.setVisibility(GONE);
        tvLabelRideStatus.setVisibility(GONE);
        tvRideStatus.setVisibility(GONE);

        String rideRoute = type.equals(RIDE_TYPE_POPULAR) ? mPopularRidesResponse.get(position).getDestination() :
                mMarqueeRidesResponse.get(position).getDestination();

        String rideDate = type.equals(RIDE_TYPE_POPULAR) ? String.format(getResources().
                        getString(R.string.ride_date_format), REUtils.getDateObject(getContext(),mPopularRidesResponse.
                        get(position).getStartDateString()),
                getResources().getString(R.string.ride_date_hyphen), REUtils.getDateObject(getContext(),mPopularRidesResponse.get(position).getEndDateString()))
                : String.format(getResources().getString(R.string.ride_date_format), REUtils.getDateObject(getContext(),mMarqueeRidesResponse.get(position).getStartDateString()),
                getResources().getString(R.string.ride_date_hyphen), REUtils.getDateObject(getContext(),mMarqueeRidesResponse.get(position).getEndDateString()));


//        String rideDate = type.equals(RIDE_TYPE_POPULAR) ? mPopularRidesResponse.
//                get(position).getStartDateString() :
//                mMarqueeRidesResponse.get(position).getStartDateString();
//        String rideEndDate = type.equals(RIDE_TYPE_POPULAR) ? mPopularRidesResponse.
//                get(position).getEndDateString() :
//                mMarqueeRidesResponse.get(position).getEndDateString();
////        @SuppressLint("StringFormatMatches")
////        String rideDate = type.equals(RIDE_TYPE_POPULAR) ? String.format(getResources().p
////                getString(R.string.ride_date_format), mPopularRidesResponse.
////                get(position).getStartDateString())
////                : String.format(getResources().getString(R.string.ride_date_format), mMarqueeRidesResponse.get(position).getStartDateString());
////        @SuppressLint("StringFormatMatches")
////        String rideEndDate = type.equals(RIDE_TYPE_POPULAR) ? String.format(getResources().
////                getString(R.string.ride_date_format), mPopularRidesResponse.
////                get(position).getStartDateString()) : String.format(getResources().getString(R.string.ride_date_format), mMarqueeRidesResponse.get(position).getStartDateString());
        String rideStartTime = type.equals(RIDE_TYPE_POPULAR) ? mPopularRidesResponse.get(position).getDistance() :
                mMarqueeRidesResponse.get(position).getDistance();
        String rideDuration = type.equals(RIDE_TYPE_POPULAR) ? mPopularRidesResponse.get(position).getTerrain() :
                mMarqueeRidesResponse.get(position).getTerrain();
        String rideDesc = type.equals(RIDE_TYPE_POPULAR) ? mPopularRidesResponse.get(position).getDuration() :
                mMarqueeRidesResponse.get(position).getDuration();

        tvRideRoute.setText(rideRoute != null && !rideRoute.isEmpty() ? rideRoute :
                getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideRoute.setTypeface(mTypeFaceBold);
        tvRideDate.setText(!rideDate.isEmpty() ? rideDate :
                getContext().getResources().getString(R.string.text_hyphen_na));
//        tvRideEndDate.setText(!rideEndDate.isEmpty() ? rideEndDate :
//                getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideStartTime.setText(rideStartTime != null && !rideStartTime.isEmpty() ? rideStartTime :
                getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideDuration.setVisibility(VISIBLE);
        tvRideDuration.setText(rideDuration != null && !rideDuration.isEmpty() ? rideDuration :
                getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideDesc.setText(rideDesc != null && !rideDesc.isEmpty() ? rideDesc :
                getContext().getResources().getString(R.string.text_hyphen_na));

        setPriceString(type, type.equals(REConstants.RIDE_TYPE_POPULAR) ?
                mPopularRidesResponse.get(position).getPrice() : mMarqueeRidesResponse.get(position).getPrice());
    }

    /**
     * Binds data to views
     */
    public void bindEventDetailsData(int position, String type) {
        EventsResponse mEventsResponse = REUtils.getmEventsResponse();

        tvlLabelRideRoute.setText(getContext().getResources().getString(R.string.text_destination));
        tvLabelRideDate.setText(getContext().getResources().getString(R.string.text_event_dates));
        tvLabelRideStartTime.setText(getContext().getResources().getString(R.string.text_registration_amount));
        tvLabelRideDuration.setVisibility(VISIBLE);
        tvLabelRideDuration.setText(getContext().getResources().getString(R.string.text_inclusions));
        tvLabelRideDesc.setVisibility(GONE);
        tvLabelDifficulty.setVisibility(GONE);
        tvLabelTerrain.setVisibility(GONE);
        tvLabelRidersJoined.setVisibility(GONE);
        tvLabelRideStatus.setVisibility(GONE);


        tvRideRoute.setText(mEventsResponse.getDestination() != null &&
                !mEventsResponse.getDestination().isEmpty() ?
                mEventsResponse.getDestination() : getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideRoute.setTypeface(mTypeFaceBold);
        tvRideDate.setText(String.format(getResources().getString(R.string.ride_three_string_placeholder),
                mEventsResponse.getStartDate(), getResources().getString(R.string.ride_date_hyphen),
                mEventsResponse.getEndDate()));

//        tvRideDate.setText(String.format(getResources().getString(R.string.ride_three_string_placeholder),
//                mEventsResponse.getStartDate()));
//        tvRideEndDate.setText(String.format(getResources().getString(R.string.ride_three_string_placeholder),
//                mEventsResponse.getEndDate()));
        setPriceString(type, mEventsResponse.getPrice());
        tvRideDuration.setVisibility(VISIBLE);
        tvRideDuration.setText(getContext().getResources().getString(R.string.text_hyphen_na));
        tvRideDesc.setVisibility(GONE);
        tvDifficulty.setVisibility(GONE);
        tvRideTerrain.setVisibility(GONE);
        tvRiderJoined.setVisibility(GONE);
        tvRideStatus.setVisibility(GONE);

    }

    /**
     * Sets price string for popular and marquee details
     *
     * @param type :String
     */
    private void setPriceString(String type, ArrayList<Map<String, String>> priceList) {
        String price;
        if (priceList != null && priceList.size() == 2) {
            price = String.format(getResources().getString(R.string.ride_price),
                    getResources().getString(R.string.ride_inr), priceList.get(0).
                            get(POPULAR_RIDES_RIDE_PRICE_KEY), getResources().getString(R.string.ride_price_slash),
                    priceList.get(0).get(POPULAR_RIDES_RIDE_TYPE_KEY),
                    Html.fromHtml(getResources().getString(R.string.ride_price_registration_inr)),
                    priceList.get(1).get(POPULAR_RIDES_RIDE_PRICE_KEY), getResources().
                            getString(R.string.ride_price_slash), priceList.
                            get(1).get(POPULAR_RIDES_RIDE_TYPE_KEY), getResources().getString(R.string.ride_price_registration));
        } else if (priceList != null && priceList.size() == 1) {
            price = String.format(getResources().getString(R.string.ride_price_single),
                    getResources().getString(R.string.ride_inr), priceList.get(0).
                            get(POPULAR_RIDES_RIDE_PRICE_KEY), getResources().getString(R.string.ride_price_slash),
                    priceList.get(0).get(POPULAR_RIDES_RIDE_TYPE_KEY),
                    Html.fromHtml(getResources().getString(R.string.ride_price_registration_inr)).subSequence(0, 12));
        } else {
            price = getContext().getResources().getString(R.string.text_hyphen_na);
        }
        if (type.equals(OUR_WORLD_EVENTS)) {
            tvRideStartTime.setText(!price.isEmpty() ? price :
                    getContext().getResources().getString(R.string.text_hyphen_na));

        } else {
            tvRideTerrain.setText(!price.isEmpty() ? price :
                    getContext().getResources().getString(R.string.text_hyphen_na));
        }
    }

    /**
     * Binds data to views
     *
     * @param userUpcomingRidesResponse the context
     */
    public void bindUserUpcomingRideDetailsData(Context context, UserUpcomingRidesResponse userUpcomingRidesResponse) {
        tvLabelTerrain.setText(getResources().getString(R.string.level_of_difficulty));
        tvLabelRidersJoined.setText(getResources().getString(R.string.text_terrain));
        String startPoint = userUpcomingRidesResponse.getStartPoint();
        String endPoint = userUpcomingRidesResponse.getEndPoint();
        List<WayPointsData> wayPointsData = userUpcomingRidesResponse.getWaypoints();
        setRideRoute(startPoint, endPoint, wayPointsData);
        String startDate = userUpcomingRidesResponse.getRideInfo().getRideStartDateIso();
        String endDate = userUpcomingRidesResponse.getRideInfo().getRideEndDateIso();
        String startTime = userUpcomingRidesResponse.getRideInfo().getStartTime();
        String endTime = userUpcomingRidesResponse.getRideInfo().getEndTime();
        setRideDateForRides(context, startDate, endDate, startTime, endTime);
        String duration = String.valueOf(userUpcomingRidesResponse.getRideInfo().getDurationInDays());
        String rideStartTime = userUpcomingRidesResponse.getRideInfo().getStartTime();
        String rideEndTime = userUpcomingRidesResponse.getRideInfo().getEndTime();
        String rideDifficulty = userUpcomingRidesResponse.getRideInfo().getDifficulty();
        String rideTerrain = userUpcomingRidesResponse.getRideInfo().getTerrainType();
        String rideDesc = userUpcomingRidesResponse.getRideInfo().getRideDetails();
        tvLabelRideStartTime.setVisibility(GONE);
        tvRideStartTime.setVisibility(GONE);
        tvLabelRideEndTime.setVisibility(GONE);
        tvRideEndTime.setVisibility(GONE);
//        tvRideStartTime.setText(!rideStartTime.isEmpty() ? rideStartTime :
//                getContext().getResources().getString(R.string.text_hyphen_na));
//        tvLabelRideDuration.setVisibility(VISIBLE);
//        tvRideDuration.setVisibility(VISIBLE);
        tvRideDuration.setText(setDuration(duration));
        tvRideDesc.setText(rideDesc != null && !rideDesc.isEmpty() ? rideDesc :
                getContext().getResources().getString(R.string.text_hyphen_na));

        tvRideTerrain.setText(rideDifficulty != null && !rideDifficulty.isEmpty() ? rideDifficulty :
                getContext().getResources().getString(R.string.text_hyphen_na));
        tvRiderJoined.setText(rideTerrain != null && !rideTerrain.isEmpty() ? rideTerrain :
                getContext().getResources().getString(R.string.text_hyphen_na));
//        tvRideStartTime.setText(rideStartTime);
        tvDifficulty.setVisibility(GONE);
        tvLabelDifficulty.setVisibility(GONE);
        tvLabelRideStatus.setVisibility(GONE);
        tvRideStatus.setVisibility(GONE);
    }

    /**
     * Sets duration by checking the no of days 0 , 1 or more
     *
     * @param duration
     * @return
     */
    private String setDuration(String duration) {
        String labelDays = duration != null && !duration.equals("0") && !duration.equals("1") ?
                getContext().getResources().getString(R.string.text_days) : getContext().getResources().getString(R.string.text_day);
        return duration != null && !duration.isEmpty() ? String.format(getResources().getString(R.string.ride_two_string_placeholder),
                duration, labelDays) :
                getContext().getResources().getString(R.string.text_hyphen_na);
    }

    /**
     * Binds data to views
     *
     * @param profileRideResponse the context
     */
    public void bindRiderProfileData(Context context, ProfileRidesResponse profileRideResponse) {
        String startPoint = profileRideResponse.getStartPoint();
        String endPoint = profileRideResponse.getEndPoint();
        List<WayPointsData> wayPointsData = profileRideResponse.getWaypoints();
        setRideRoute(startPoint, endPoint, wayPointsData);
        String startDate = profileRideResponse.getRideInfo().getRideStartDateIso();
        String endDate = profileRideResponse.getRideInfo().getRideEndDateIso();
        String startTime = profileRideResponse.getRideInfo().getStartTime();
        String endTime = profileRideResponse.getRideInfo().getEndTime();
        setRideDateForRides(context, startDate, endDate, startTime, endTime);

        String rideStartTime = profileRideResponse.getRideInfo().getStartTime();
        String rideDuration = String.valueOf(profileRideResponse.getRideInfo().getDurationInDays());
        String rideDifficulty = profileRideResponse.getRideInfo().getDifficulty();
        String rideTerrain = profileRideResponse.getRideInfo().getTerrainType();
        String rideDesc = profileRideResponse.getRideInfo().getRideDetails();

        tvLabelRideStartTime.setVisibility(GONE);
        tvRideStartTime.setVisibility(GONE);
        tvLabelRideEndTime.setVisibility(GONE);
        tvRideEndTime.setVisibility(GONE);

//        tvRideStartTime.setText(rideStartTime != null && !rideStartTime.isEmpty() ? rideStartTime :
//                getContext().getResources().getString(R.string.text_hyphen_na));
//        tvLabelRideDuration.setVisibility(VISIBLE);
//        tvRideDuration.setVisibility(VISIBLE);
        tvRideDuration.setText(setDuration(rideDuration));
        tvRideDesc.setText(rideDesc != null && !rideDesc.isEmpty() ? rideDesc :
                getContext().getResources().getString(R.string.text_hyphen_na));

        tvRideTerrain.setText(rideTerrain != null && !rideTerrain.isEmpty() ? rideTerrain :
                getContext().getResources().getString(R.string.text_hyphen_na));
        tvDifficulty.setText(rideDifficulty);
        tvLabelRidersJoined.setVisibility(GONE);
        tvRiderJoined.setVisibility(GONE);
        tvRideStatus.setText(profileRideResponse.getModerationStatus());
        tvLabelRideType.setVisibility(View.VISIBLE);
        tvRideType.setVisibility(View.VISIBLE);
        tvRideType.setText(profileRideResponse.getRideType());
    }

    /**
     * Sets ride route for rides details
     *
     * @param startPoint
     * @param endPoint
     * @param wayPointsData
     */
    private void setRideRoute(String startPoint, String endPoint, List<WayPointsData> wayPointsData) {
        startPoint = REUtils.splitPlaceName(startPoint != null && !startPoint.isEmpty() ? startPoint :
                getContext().getResources().getString(R.string.text_hyphen_na));
        endPoint = REUtils.splitPlaceName(endPoint != null && !endPoint.isEmpty() ? endPoint :
                getContext().getResources().getString(R.string.text_hyphen_na));
        StringBuilder wayPointBuilder = new StringBuilder();
        if (wayPointsData != null && wayPointsData.size() > 0) {
            for (int i = 0; i < wayPointsData.size(); i++) {
                String wayPoint;
                wayPoint = " > " + wayPointsData.get(i).getPlaceName();
                wayPointBuilder.append(wayPoint);
            }
            wayPointBuilder.append(" > ");
        }
        if (wayPointBuilder.length() > 0) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(startPoint + wayPointBuilder + endPoint);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), 0,
                    startPoint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceRegular)), startPoint.length(),
                    startPoint.length() + wayPointBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceBold)), startPoint.length()
                            + wayPointBuilder.length(), startPoint.length() + wayPointBuilder.length() + endPoint.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvRideRoute.setText(spannable);
        } else {
            String nextArrow = " > ";
            SpannableStringBuilder spannable = new SpannableStringBuilder(startPoint + nextArrow + endPoint);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceRegular)), 0, startPoint.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceRegular)), startPoint.length(),
                    startPoint.length() + nextArrow.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan((new RECustomTyperFaceSpan(mTypeFaceRegular)), startPoint.length() +
                            nextArrow.length(), startPoint.length() + nextArrow.length() + endPoint.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvRideRoute.setText(spannable);
        }
    }

    /**
     * Gets the formatted date & validates
     *
     * @param context   : Context
     * @param startDate : String
     * @param endDate   : String
     * @param startTime : String
     */
    private void setRideDateForRides(Context context, String startDate, String endDate, String startTime, String endTime) {
        if (startDate != null && endDate != null && startTime != null) {
            tvLabelRideDate.setVisibility(VISIBLE);
            tvRideEndDate.setVisibility(VISIBLE);
            tvLabelRideDate.setText(getContext().getResources().getString(R.string.text_ride_start_date));
            tvLabelRideEndDate.setVisibility(VISIBLE);
            tvLabelRideEndDate.setText(getContext().getResources().getString(R.string.text_ride_end_date));

            startDate = REUtils.getDateObject(context, startDate);
            endDate = REUtils.getDateObject(context, endDate);
            tvRideDate.setText(startDate + " " + startTime);
            tvRideEndDate.setVisibility(VISIBLE);
            if (endTime != null) {
                tvRideEndDate.setText(endDate + " " + endTime);
            } else {
                tvRideEndDate.setText(endDate + " " + getContext().getResources().getString(R.string.text_hyphen_na));
            }

        } else {
            tvLabelRideDate.setVisibility(VISIBLE);
            tvRideEndDate.setVisibility(VISIBLE);
            tvLabelRideDate.setText(getContext().getResources().getString(R.string.text_ride_start_date));
            tvLabelRideEndDate.setVisibility(VISIBLE);
            tvLabelRideEndDate.setText(getContext().getResources().getString(R.string.text_ride_end_date));

            tvRideDate.setText(R.string.text_hyphen_na);
            tvRideEndDate.setVisibility(VISIBLE);
            tvRideEndDate.setText(R.string.text_hyphen_na);

        }
    }

    /**
     * Checks the start & end ride and then sets the date
     *
     * @param startDate : String
     * @param endDate:  String
     * @return: String
     */
    private String setDateAfterValidation(String startDate, String endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.equals(endDate)) {
                return startDate;
            } else {
                return String.format(getResources().getString(R.string.ride_three_string_placeholder),
                        startDate, getResources().getString(R.string.ride_date_hyphen), endDate);
            }
        }
        return getResources().getString(R.string.ride_date_hyphen);
    }

}

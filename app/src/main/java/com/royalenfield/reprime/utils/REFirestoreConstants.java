package com.royalenfield.reprime.utils;

/**
 * Constants for firestore
 */
public class REFirestoreConstants {

    // Vehicle details constants
    public static final String VEHICLE_DETAILS_COLLECTION = "Vehicle";
    public static final String VEHICLE_DETAILS_ARRAY = "vehicleInfo";
    public static final String ACTIVECUSTOMERNAME = "activeCustomerName";
    public static final String CHAISSISNO = "chaissisNo";
    public static final String DATEOFMFG = "dateOfMfg";
    public static final String ENGINENO = "engineNo";
    public static final String VEHICLEDETAIL_ID = "id";
    public static final String MOBILENO = "mobileNo";
    public static final String MODELCODE = "modelCode";
    public static final String MODELNAME = "modelName";
    public static final String PURCHASEDATE = "purchaseDate";
    public static final String REGISTRATION_NO = "registrationNo";
    public static final String IMAGE_URL = "imageUrl";
    public static final String BIKE_NAME = "name";
    public static final String VARIANT_CONNECTED = "isVariantConnected";

    //UserInfo constants
    public static final String USER_INFO_COLLECTION = "userInfo";

    public static final String SERVICE_PAYMENT = "Payment";
    public static final String SERVICE_PAYMENT_STATUS = "Status";
    public static final String SERVICE_PAYMENT_CASEID = "CaseId";

    //User rides
    public static final String USER_RIDES = "rides";
    public static final String USER_PROFILE_PAST_RIDES = "pastRides";
    public static final String USER_PROFILE_PENDING_RIDES = "pendingRides";
    public static final String USER_PROFILE_REJECTED_RIDES = "rejectedRides";
    public static final String USER_PROFILE_UPCOMING_RIDES = "upcomingRides";
    public static final String USER_PROFILE_ONGOING_RIDES = "ongoingRides";
    public static final String USER_PROFILE_START_DATE = "rideInfo.rideStartDateIso";

    //ServiceHistory constants
    public static final String SERVICEHISTORY_INFO = "serviceHistoryInfo";
    public static final String BILL_AMOUNT = "billAmount";
    public static final String DEALERID = "dealerId";
    public static final String INVOICE_DATE = "invoiceDate";
    public static final String PAYMENT_METHOD = "paymentMethod";
    public static final String SERVICE_INVOICENUM = "serviceInvoicenum";
    public static final String USERID = "userId";
    public static final String LAST_UPDATED_ON = "lastUpdatedOn";

    //Dealer Upcoming Rides
    public static final String DEALER_UPCOMING_RIDES = "dealer-upcoming-rides";
    public static final String RIDE_IMAGE_PATH = "srcPath";

    //Marquee Rides
    public static final String MARQUEE_RIDES_FIRESTORE_KEY = "marquee-rides";
    public static final String MARQUEE_RIDES_START_DATE = "startDateString";

    //Popular Rides
    public static final String POPULAR_RIDES_FIRESTORE_KEY = "popular-rides";
    public static final String POPULAR_RIDES_RIDE_PRICE_KEY = "amount";
    public static final String POPULAR_RIDES_RIDE_TYPE_KEY = "type";
    public static final String POPULAR_RIDES_KEY_PLACE_NAME = "name";
    public static final String POPULAR_RIDES_ITINERY_DATE = "displayDate";
    public static final String POPULAR_RIDES_ITINERY_SUMMARY = "daySummary";
    public static final String REGISTRATION_POLICIES_TYPE = "type";
    public static final String REGISTRATION_POLICIES_URL = "url";

    //User Upcoming Rides
    public static final String USER_UPCOMING_RIDES = "user-upcoming-rides";
    public static final String USER_INFO_CITY = "city";
    public static final String USER_INFO_NAME = "name";
    public static final String USER_INFO_PHONE_NO = "phoneNo";
    public static final String USER_INFO_PROFILE_URL = "profileURL";
    public static final String USER_INFO_RIDES_CREATED_COUNT = "ridesCreatedCount";
    public static final String RIDE_INFO_RIDE_DETAILS = "rideDetails";
    public static final String RIDE_IMAGES_SRC_PATH = "srcPath";

    //Our World
    public static final String OUR_WORLD_NEWS_FIRE_STORE_DATA = "news";
    public static final String OUR_WORLD_NEWS_CREATED_DATE = "createdOn";
    public static final String OUR_WORLD_EVENTS_FIRE_STORE_DATA = "events";
    public static final String EVENT_HIGHLIGHTS = "eventHighlights";
    public static final String EVENT_HIGHLIGHTS_TITLE = "title";
    public static final String EVENT_HIGHLIGHTS_IMAGE = "imagePath1";

    //Past Rides
    public static final String RIDERS_JOINED_USER_PROFILE_URL = "profileUrl";
    public static final String RIDERS_JOINED_USER_FNAME = "fname";
    public static final String RIDERS_JOINED_USER_LNAME = "lname";
    public static final String RIDERS_JOINED_USER_ID = "guid";
    public static final String PAGE_STATUS_PUBLISHED = "PUBLISHED";
    public static final String RIDE_TYPE_SOLO = "SOLO";
    public static final String RIDE_TYPE_PRIVATE = "PRIVATE";

    //    Dealer rides
    public static final String DEALER_COORDINATES = "coordinates";
    public static final String DEALER_LATITUDE = "latitude";
    public static final String DEALER_LONGITUDE = "longitude";
    public static final String DEALER_ADDRESS = "address";
    public static final String DEALER_IMAGE = "dealer_image";
    public static final String DEALER_RIDE_START_DATE = "rideStartDateIso";
    public static final String DEALER_RIDE_END_DATE = "rideEndDateIso";
    public static final String DEALER_RIDE_START_TIME = "startTime";
    public static final String DEALER_RIDE_DURATION = "durationInDays";
    public static final String DEALER_RIDE_TERRAIN = "terrainType";
    public static final String DEALER_RIDE_DETAILS = "rideDetails";
    public static final String DEALER_RIDE_USER = "user";

    //vehicle and user data validation popup constants
    public static final String USER_SETTINGS = "usersettings";
    public static final String VEHICLE_DOCUMENT = "VehicleInfo";
    public static final String CONNECTED_DOCUMENT = "connectedInfo";

    public static final String VEHICLE_SERVICE_HISTORY = "VehicleServiceHistory";
    public static final String SERVICE_HISTORY = "serviceHistory";

    //RE_FOTA
    public static final String RE_FOTA = "RE_FOTA_V2_1";
    public static final String RE_FOTA_FULL = "RE_FOTA_FULL";

    //UserInfo constants
    public static final String CONSENT_COLLECTION = "consents";
    public static final String CONNECTED_PROVISIONING="connected_provisioning_data";
    public static final String APP_SECRETS="Secrets";

    public static final String REMOTE_CONFIG = "remoteconfig";
    public static final String REALTIME_DB = "realtimedb";
    public static final String STATIC_REALTIME_DB = "static-files";
    public static final String STATE_REALTIME_DB = "statemaster";
    public static final String CITY_REALTIME_DB = "citymaster";
    public static final String CONFIGURATION_REALTIME_DB = "configurations";
    public  static final  String VARIANT_REALTIME_DB="variantmaster";

}

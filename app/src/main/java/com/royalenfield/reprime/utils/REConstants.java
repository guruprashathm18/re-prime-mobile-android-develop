package com.royalenfield.reprime.utils;

/**
 * @author BOP1KOR on 11/21/2018.
 */

public class REConstants {
    public static final int timeSlotErrorCode = 0;
    public static final int API_SUCCESS_CODE = 200;
    public static final int API_DMS_FAILURE_CODE = 400;
    public static final int API_UNAUTHORIZED = 401;
    public static final int TOKEN_EXPIRED = 402;
    public static final int FIFTEEN_MINUTES = 15 * 60 * 1000;
    public static final String API_SUCCESS_RESULT = "OK";
    public static final String MY_ADDRESS_PREFS_NAME = "AddressPref";
    public static final String PREF_ADDRESS_KEY = "address";
    public static final String DEFAULT_RE_URL = "https://www.royalenfield.com/";
    public static String IMAGE_FILE_NAME_CONSTANT = null;
    public static final String TBT_API_UNAUTHORIZED = "UNAUTHORIZED";
    public static final String MY_APPOINTMET_PREFS_NAME = "AppointmentPref";
    public static final String MY_BOOKING_SERVICE_CENTER = "BookedServiceCenter";
    public static final String PREF_APPOINTMENT_USERID = "appointmentUserID";
    public static final String PREF_BOOKED_TIME = "servicebooked_time";
    public static final String PREF_CANCEL = "service_cancel";
    public static final String PREF_BRANCH_ID = "branchId";
    public static final String MY_CURRENT_LOCATION = "CurrentLocation";
    public static final String PREF_CURRENT_LATTITUDE_KEY = "lattitude";
    public static final String PREF_CURRENT_LONGITUDE_KEY = "longitude";

    public static final String RIDE_WAYPOINT_TYPE = "ride_waypoint_type";
    public static final String IS_LOCATION_REQUIRED = "isLocationRequired";
    public static final String IS_NAVIGATION = "isNavigation";
    public static final String WAYPOINT_PLANRIDE = "waypoint_planride";
    public static final String WAYPOINT_INRIDE = "waypoint_inride";
    public static final String WAYPOINT_UPCOMINGRIDE = "waypoint_upcomingride";
    public static final String WAYPOINT_MODIFYRIDE = "waypoint_modifyride";
    public static final String WAYPOINT_NAVIGATION = "waypoint_navigation";

    public static final String WAY_POINTS_LIST = "wayPointsList";
    public static final String FIRST_TIME = "firstTime";
    public static final int APP_ID = 2;
    public static final String CUSTOM_LANG = "en";
    public static final String CUSTOM_COUNTRY = "in";
    public static final int API_SUCCESS_NO_DATA_CODE = 1002;
    public static final String KEY_HOME_COMMUNITY = "home_community";
    public static final int OTP_REQUEST_CODE = 11;
    public static final String COUNTRY_CODE = "CountryCode";
    public static final String KEY_APP_ID = "app_id";
    public static final String KEY_AUTHORIZATION = "Authorization";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static String CONTENT_TYPE = "application/json";
    public static final String KEY_CUSTOM_LANG = "x-custom-language";
    public static final String KEY_CUSTOM_COUNTRY = "x-custom-country";
    public static String PREF_APPOINTMENT_BOOKINGNO = "";

    public static final String DATE_TOMORROW = "tomorrow";
    public static final String USERDATA = "user_data";
    public static final String DATE_DAY_AFTER_TOMORROW = "dayaftertomorrow";
    public static final String NEXT_DATE_DAY_AFTER_TOMORROW = "nextdayaftertomorrow";

    public static final String DEALERS_FILTERED_LIST_EXTRA = "filteredList";

    //Service Intent data keys
    public static final String SERVICE_BOOKING_CANCEL = "bookingcancelled";

    //service Request/ Result code
    public static final int REQUESTCODE_BOOKINGCANCELLED = 1;
    public static final int REQUESTCODE_PAYMENTSTATUS = 2;

    public static final String PAYMENT_STATUS_MESSAGE = "paymentStatusMessage";

    //RE Youtube BASE_URL
    public static final String RE_YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/";

    //RE GoogleMap BASE_URL
   public static final String RE_GOOGLEMAP_BASE_URL = "https://maps.googleapis.com/maps/api/";

    //Push Notifications
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String PUSH_NOTIFICATION_SPECIFIC_MOTORCYCLE = "pushNotificationMotorcycle";


    // Jobcard Status Values
    public static final String SERVICE_REQUEST_RECEIVED = "Service Request Received";
    public static final String PRE_JOBCARD = "Pre job Card";
    public static final String JC_OPENING = "JC Opening";
    public static final String JC_ESTIMATE = "JC Estimate";
    public static final String IN_TRAY_WORKSHOP = "In Tray Workshop";
    public static final String FQI = "In FQI";
    public static final String READY_FOR_DELIVERY = "Ready for Delivery";
    public static final String READY_FOR_INVOICING = "Ready for Invoicing";
    public static final String INVOICED = "Invoiced";


    //Notification
    public static final String JOB_CARD_STATUS = "jobCardStatus";
    public static final String REG_NO = "regNo";
    public static final String NOTIF_DATA_JOB_CARD_STATUS = "JobCardStatus";
    public static final String NOTIF_DATA_REG_NO = "RegistrationNumber";
    public static final String NOTIF_DATA_TITLE = "title";
    public static final String NOTIF_DATA_IMAGE = "image";
    public static final String NOTIF_DATA_BODY = "body";
    public static final String NOTIF_DATA_ID = "id";
    public static final String NOTIF_DATA_PARAMS = "params";
    public static final String NOTIF_DATA_TYPE = "type";
    public static final String NOTIF_CHASSIS_NUMBER = "chassisNo";
    public static final String NOTIF_CAMPAIGN_URL = "campaign_url";
    public static final String CHASSIS_NO = "chassisNumber";

    public static final String KEY_SERVICE_ISRESCHEDULE = "isReschedule";

    //Youtube Package Name
    public static final String YOUTUBE_PACKAGENAME = "com.google.android.youtube";

    //GoogleMap Package Name
    public static final String GOOGLE_MAP_PACKAGENAME = "com.google.android.apps.maps";

    //GoogleMap Play store link
    public static final String GOOGLE_MAP_PLAY_STORE_PATH = "market://details?id=com.google.android.apps.maps";

    //Firestore vehicle update
    public static final String FIRESTORE_UPDATE = "firestoreVehicleUpdate";

    //Firestore vehicle update
    public static final String VEHICLE_DATA = "vehicleData";

    //Fierstore update
    public static final String FIRESTORE_ONGOING_RIDE = "ongoingRideUpdate";
    public static final String FIRESTORE_PENDING_RIDE = "pendingRideUpdate";
    public static final String FIRESTORE_PAST_RIDE = "pastRideUpdate";
    public static final String FIRESTORE_UPCOMING_RIDE = "upcomingRideUpdate";
    public static final String FIRESTORE_REJECTED_RIDE = "rejectedRideUpdate";
	public static final String FIRESTORE_PROFILE = "profileDataUpdate";
    //Email Customer Care
    public static final String EXTRA_EMAIL =
            REUtils.getREKeys().getCustomerSupport();
    public static final String EXTRA_SUBJECT = "";
    public static final String EXTRA_TEXT = "";

    public static final String CC_NUMBER = REUtils.getREKeys().getCustomerCare();

    public static final String MOTORCYCLE = "MOTORCYCLE";
    public static final String COMMUNITY = "COMMUNITY";
    public static final String CONFIGURATION = "CONFIGURATION";
    public static final String NAVIGATION = "NAVIGATION";
    public static final String RIDES = "RIDES";
    //RE Preference Keys
    public static final String USERID_KEY = "userId";
    public static final String MOBILE_NO_KEY = "mobileNo";
    public static final String REFRESH_TOKEN_KEY = "refreshtoken";
    public static final String VEHICLE_DETAILS_SYNC = "vehicledetailssync";
    public static final String JWT_TOKEN_KEY = "jwttoken";
    public static final String IS_SERVICE_NOTIFICATION = "isServiceNotification";
    public static final String IS_NAVIGATION_NOTIFICATION = "isNavigationNotification";
    public static final String IS_NAVIGATION_DEEPLINK = "isNavigationDeepLink";
    public static final String NAVIGATION_DEEPLINK = "nav_deeplink";
    public static final String DEEPLINK_DATA = "deeplink_data";
    public static final String IS_NAVIGATION_DETAILS = "isNavigationDetails";
    public static final String NAVIGATION_NOTIFICATION = "tripId";
    public static final String SUCCESS_ACTIVITY = "successActivity";
    public static final String AUTHORIZED_ACTIVITY = "authorizedActivity";
    public static final String VERIFY_ACCOUNT = "VerifyAccount";

    public static final String DEVICE_TOKEN_PREF = "deviceTokenPref";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String DEVICE_TOKEN_SENT = "deviceTokenSent";

    //DEEPLINK Key values
    public static final String ORIGIN_TEXT = "origin_text";
    public static final String ORIGIN_LOCATION = "origin_location";
    public static final String DESTINATION_TEXT = "destination_text";
    public static final String DESTINATION_LOCATION = "destination";
    public static final String WAYPOINTS_LOCATION = "wp_locations";
    public static final String WAYPOINTS_TEXT = "wp_texts";
    public static final String USELIVETRAFFIC = "useLiveTraffic";
    public static final String AVOID_FERRIES = "avoidFerries";
    public static final String AVOID_TOLLROADS = "avoidTollRoads";
    public static final String AVOID_HIHWAYS = "avoidHighways";
    public static final String CANONICAL_IDENTIFIER = "RE_Sharing";
    public static final String SHARE_TYPE = "shareType";
    public static final String SHARE_TYPE_TBT_ROUTE = "TBT_Route";
    public static final String SHARE_TYPE_BCT_ROUTE = "BCT_Route";
    public static final String SHARE_CODE_KEY = "code";
    public static final String COUNTRY_KEY = "country_data";
    public static final String COUNTRY_URL_KEY = "country_url";

    //RE Vehicle details key Firestore
    public static final String GET_VEHICLE_DETAIL_FLAG = "getVehicleDetailsFromFirestore";
    public static final String SHOW_USER_VALIDATION_POPUP = "showUserValidationPopup";
    public static final String SHOW_VEHICLE_ONBOARDING_POPUP = "showVehicleOnboardingPopup";
    public static final String CONSENT_RECEIVED_DATE = "consentReceivedDate";

    //RE Vehicle connected details key Firestore
    public static final String DEVICE_IMEI = "device_imei";
    public static final String IS_CONSENT_TAKEN = "is_consent_taken";
    public static final String ALERT_TIMESTAMP = "alert_timestamp";


    //Rides
    public static final String RIDECUSTOM_VIEWTYPE = "viewtype";
    public static final String BOOKMARKS_TYPE = "bookmarks";
    public static final String PASTRIDE_TYPE = "pastride";
    public static final String UPCOMIMGRIDE_TYPE = "upcomingride";
    public static final String ENDRIDE_TYPE = "endride";
    public static final String PENDINGRIDE_TYPE = "pendingride";
    public static final String REJECTEDRIDE_TYPE = "rejectedride";
    public static final String ONGOINGRIDE_TYPE = "ongoingride";
    public static final String MARQUEE_RIDE = "marqueeride";
    public static final String ITINERARY = "itinerary";
    public static final String KEY_PLACES = "places";
    public static final String KEY_RIDE_GALLERY = "gallery";

    public static final String CONTACTS_FILTERED_LIST_EXTRA = "contact filtered list";

    //BLE
    public static final String BLE_DEVICE_PAIRED = "bleDevicePaired";
    public static final String BLE_ACTION_CONFIRMATION = "bleActionConfirm";
    public static final String BLE_USER_RESPONSE = "bleUserResponse";

    //DealerRides
    public static final String DEALER_RIDES_TYPE = "dealerRidesType";
    public static final String TYPE_JOIN_DEALER_RIDE = "joinRide";
    public static final String POSITION = "position";
    public static final String DEALER_JOIN_RIDE_TYPE = "dealer-ride";

    //TAGS
    public static final String RIDE_CATEGORY_TYPE = "rideCategoryType";
    public static final String CREATE_RIDE = "createRide";
    public static final String SIGN_UP = "signUp";


    //PopularRides
    public static final String RIDES_LIST_POSITION = "position";
    public static final String ADAPTER_POSITION = "adapter_position";

    public static final String RIDES_DETAILS_WEB_TYPE = "details_web";
    public static final String RIDES_DETAILS_NATIVE_TYPE = "details_native";

    public static final String RIDE_TYPE = "type";
    public static final String RIDE_TYPE_POPULAR = "type_popular";
    public static final String RIDE_TYPE_ONGOING = "type_ongoing";

    public static final String RIDE_ONGOING_CLOSE = "ride_ongoing_close";

    //MArquee
    public static final String RIDE_TYPE_MARQUEE = "type_marquee";

    //UserUpcomingRides
    public static final String TYPE_USER_CREATED_RIDE_DETAILS = "userCreatedRideDetails";
    public static final String USER_JOIN_RIDE_TYPE = "user-ride";

    //Setting activity
    public static final String SETTING_ACTIVITY_INPUT_TYPE = "setting";
    public static final String SETTING_ACTIVITY_POLICIES = "policies";
    public static final String INPUT_SPLASH_ACTIVITY = "splash";
    public static final String INPUT_PROFILE_ACTIVITY = "profile";

    public static final String INPUT_TITLE = "title";

    //Edit Profile
    public static final String KEY_PROFILE_IMAGE = "profile_image";
    public static final String KEY_FLAT_NO = "flatNo";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_IS_EDIT_PROFILE = "IsEditProfileOrIsRidesAndService";
    public static final String KEY_NAME = "FirstAndLastName";
    public static final String KEY_ABOUT_ME = "AboutMe";


    public static final String USER_CREATED_JOIN_RIDE = "userCreatedJoinRide";
    public static final String CREATOR_PHONE_NO = "CreatorPhoneNo";
    public static final String PUBLISH_RIDE = "PublishRide";

    public static final String TEXT_SPLASH_DESC_SUCCESS = "text_user_joinride_success";

    //Our World
    public static final String OUR_WORLD_EVENTS = "ourWorldEvents";

    //News
    public static final String NEWS_OURWORLD = "type_news";

    //Search Activity
    public static final String SEARCH_ACTIVITY_RIDE_ID = "searchActivityRideID";
    public static final String SEARCH_ACTIVITY_INPUT_TYPE = "searchActivityInputType";
    public static final String SEARCH_ACTIVITY_DEALER_LIST = "searchActivityDealerList";
    public static final String SEARCH_ACTIVITY_CHECK_IN_LIST = "searchActivityCheckInList";

    //Enable location settings
    public static final int REQUEST_CHECK_SETTINGS = 1111;

    public static final String RIDE_ID = "rideId";


    // Request codes for image from camera & gallery
    public static final int REQUEST_CHECK_GALLERY = 110;
    public static final int REQUEST_CHECK_CAMERA = 111;
    public static final int REQUEST_CHECK_CAMERA_PROFILE = 112;
    //Payment
    public static final String TYPE_PAYMENT = "Payment";
    public static final String PAYMENT_CHECKSUM = "PaymentCheckSum";

    //NavigationSearch
    public static final String NAVIGATION_SEARCH_ACTIVITY = "Navigation";

    public static final String NAVIGATION_TYPE_KEY = "nav_type";
    public static final String NAVIGATION_TYPE_STOP = "nav_type_stop";
    public static final String NAVIGATION_TYPE_RECORD = "nav_type_record";
    public static final String NAVIGATION_TYPE_RECORD_RESUME = "nav_type_record_resume";
    public static final String NAVIGATION_TYPE_RECORD_PAUSE = "nav_type_record_pause";
    public static final String NAVIGATION_TYPE_RECORD_STOP = "nav_type_record_stop";

    public static final int REQUESTCODE_BCT_SAVE = 2;

    public static final int RIDER_INVITED_CALL_PERMISSIONS_REQUESTS = 4;

    public static final int SAVE = 1;
    public static final int SHARE = 2;
    public static final int RE_SHARE = 3;

    public static final String NAV_GOOGLE_API_BASE_URL = "https://www.google.com/maps/dir/?api=1&origin=";

    public static final String BLE_CONNECTION_UPDATE = "bleConnectionUpdate";
    public static final String BLE_CONNECTION_STATE = "bleConnectionState";
    public static final int RE_APP_ID = 2;
    public static final String MIY_URL = REUtils.getMobileappbaseURLs().getMiyURL();
    public static final String OT_URL = REUtils.getMobileappbaseURLs().getOrderTrackingBaseURL();
    public static final String FINANCE_URL = REUtils.getMobileappbaseURLs().getFinanceYourRE();
    public static final String ConnectedSocketUrl = REUtils.getMobileappbaseURLs().getConnectedSocketURL();
    public static final String ConnectedBaseUrl = REUtils.getMobileappbaseURLs().getConnectedBaseURL();
    public static final String DRSA_URL = REUtils.getMobileappbaseURLs().getDrsaUrl();
    public static final String WANDERLUST_URL = REUtils.getMobileappbaseURLs().getWanderLustUrl();
    public static final String SERVICE_URL = REUtils.getMobileappbaseURLs().getServiceBookingURL();
    public static final String TRIPS_URL = REUtils.getMobileappbaseURLs().getTripsUrl();
    public static final String KEY_NAVIGATION_FROM = "key_navigation_from";
    public static final String KEY_NAVIGATION_FROM_AUTHORIZE = "key_navigation_from_authorize";
    public static final String KEY_ACCOUNT_PENDING = "PENDING";
    public static final String KEY_ACCOUNT_VERIFIED = "VERIFIED";

    public static final int IMAGE_MODE_UPLOAD = 1;
    public static final int IMAGE_MODE_VIEW = 2;
    // Remote Config keys
    public static final String RE_PRIME_APP_LOGOUT_ALL_USERS = "re_prime_app_logout_all_users";
    public static final String RE_PRIME_APP_MINIMUM_ANDROID_VERSION = "re_prime_app_minimum_android_version";
    public static final String RE_PRIME_APP_USER = "re_prime_app_user";
    public static final String RE_PRIME_APP_CONFIG = "re_prime_app_config";
    public static final String RE_PRIME_APP_VERSION = "re_prime_app_version";
    public static final String RE_KEYS = "re_keys";
    public static final String MOBILE_BASE_URLS = "mobile_base_urls";
    public static final String CONNECTED_KEYS = "connected_keys";
    public static final String GLOBAL_VALIDATION = "global_validations";
    public static final String FIREBASE_URLS = "firebase_urls";
    public static final String GOOGLE_KEYS = "google_keys";
    public static final String Logger_BASE_URLS = "logger_service";
    public static final int NAVIGATION_CALL_PERMISSIONS_REQUESTS = 5;
    public static final int NAVIGATION_BG_LOC_PERMISSION_REQUEST = 7;
    public static final String CONFIG_FEATURES = "configurable_features";
    public static final String RIDE_FEATURES = "rides_keys";
    public static final String NAVIGATION_KEYS = "navigation_keys";
    public static final String APP_UPDATE_INTERVAL = "app_update_interval";
    public static final String DIY_VIDEO_LIST = "diyVideos";
    public static final String FEATURE_STATUS_DISABLED = "disabled";
    public static final String IS_DUMMYSLOTS_ENABLED = "isDummySlotsEnabled";
    public static final String SERVICE_BOOKING_CUTOFFTIME = "servicebookingCutOffTime";
    public static final String SEARCH_SERVICE_CENTRE_LWPURL = "serviceCentersLWPURL";
    public static final String FEATURE_STATUS = "feature_status";
    public static final String FEATURE_DISABLED = "N";
    public static final String FEATURE_ENABLED = "Y";
    public static final String GEOQUERY_RADIUS = "geo_query_radius";

    public static final String DEALER_SOURCE_ONLINE = "D";
    public static final String DEALER_SOURCE_OFFLINE = "E";

    public static final String SERVICE_BOOKING_TYPE_SELFDROP = "1";
    public static final String SERVICE_BOOKING_TYPE_DOORSTEP = "2";
    public static final String SERVICE_BOOKING_TYPE_PICKUPANDDROP = "3";

    public static final String SERVICE_AVAILABLE_YES = "Y";
    public static final String SERVICE_AVAILABLE_NO = "N";

    public static final String BRANCH_TYPE_IDENTIFIER_FIREBASE = "Service";

    public static final String RE_WEBSITE_URL = "https://www.royalenfield.com/";
    public static final String RE_WEBSITE_EVENTS_URL = "https://www.royalenfield.com/in/en/rides/events";
    public static final String RE_WEBSITE_RIDES_URL = "https://www.royalenfield.com/in/en/rides";

    public static final String KEY_LOGIN_GTM = "Login";
    public static final String KEY_SIGNUP_GTM = "Sign_up";
    public static final String KEY_GUEST_GTM = "Guest";
    public static final String KEY_MYO_GTM = "Make_your_Own";
    public static final String KEY_DIY_GTM = "DIY";
    public static final String KEY_SERVICE_GTM = "Motorcycles_Service";
    public static final String KEY_MOTORCYCLES_GTM = "Motorcycles";
    public static final String KEY_SUPPORT_GTM = "Support";
    public static final String KEY_CONTACT_US_GTM = "Contact_Us";
    public static final String KEY_OWNER_MANUAL_GTM = "Owner_Manual";
    public static final String KEY_SPARE_GENUINITY_GTM = "Spares_Genuinity_check";
    public static final String KEY_SIGNUP_LOGIN = "Spares_Genuinity_check";
    public static final String KEY_SIGNUP_LOGIN_GTM = "Sign_up_and_Login";
    public static final String KEY_OTP_GTM = "OTP";
    public static final String KEY_OTP_RESEND_GTM = "OTP_Resend";
    public static final String KEY_SIGNUP_FORM_GTM = "Sign_Up_Form";
    public static final String KEY_CONNECTED_MODULE_GTM = "Connected_Module";
	public static final String LOGOUT_GTM = "Logout_Pop_Up";

    public static final String KEY_SCREEN_GTM = "openScreen";
    public static final String KEY_RSA_GTM = "RSA";
    public static final String KEY_SETTINGS_GTM = "Settings";
    public static final String KEY_ACCOUNT_GTM = "Account_Verification";
    public static final String KEY_USER_PROFILE_GTM = "User_Profile";
    public static final String KEY_TRACK_ORDER_GTM = "track_your_order";
    public static final String VEHICLE_ISSUE_PREF = "VEHICLE_ISSUE_PREF";
    public static final String VEHICLE_ISSUE_DATA = "VEHICLE_ISSUE_DATA";
    public static final String KEY_UDV_GTM = "UDV";
    public static final String TWO_WHEELER_COUNTRIES = "twoWheelerCountries";

    //Alert Fragment constant dependencies
    public static final String ALERT_MESSAGE = "ALERT_MESSAGE";
    public static final String ALERT_TYPE = "ALERT_TYPE";
    public static final String ALERT_TYPE_NAVIGATION_LOGIN = "ALERT_TYPE_NAVIGATION_LOGIN";

    public static final String ACM_CONFIGURATION = "acm_configuration";
    public static final String ACM_MOBILE_NUMBER = "mobilenumber";
    public static final String ACM_APP_VERISON = "appversion";
    public static final String PROFILE_VERIFIED = "PROFILE_VERIFIED";
    public static final String PUSH_REMOTE_MESSAGE = "PUSH_REMOTE_MESSAGE";
    public static final String PUSH_ACM_TRACKING_MID_MESSAGE = "PUSH_ACM_MID_TRACKING_MESSAGE";
    public static final String PUSH_ACM_TRACKING_DID_MESSAGE = "PUSH_ACM_DID_TRACKING_MESSAGE";
    public static final String COMMUNITY_DEFAULT_SCREEN = "COMMUNITY_DEFAULT_SCREEN";
    public static final String FEATURE_INDEX = "FeatureIndex";
    public static final String FEATURE_SUB_INDEX = "FeatureSubIndex";
    public static final String KEY_FAQ = "key_faq";
    public static final String KEY_EW = "key_ew";
    public static final String KEY_TNC = "key_tnc";
    public static final String KEY_PRIVACY = "key_pp";
    public static final String KEY_TNC_DRSA = "key_tnc_drsa";
    public static final String KEY_RSA = "key_rsa";
    public static final String KEY_RTC = "key_rtc";
    public static final String KEY_DIY_CONTAINER = "key_diy_containerName";
    public static final String KEY_DIY_DIRNAME = "key_diy_dirName";
    public static final String KEY_DIY_FILENAME = "key_diy_fileName";

    //TBT
    public static final int NAV_API_RETRY_COUNTER = 2;
    public static final String BCT_DURATION = "BCT_DURATION";
    public static final String BCT_DISTANCE = "BCT_DISTANCE";
    public static final String BCT_SPEED = "BCT_SPEED";

    public static final String SENSOR_ACCURACY_HIGH = "high";
    public static final String SENSOR_ACCURACY_MEDIUM = "medium";
    public static final String SENSOR_ACCURACY_LOW = "low";
    public static final String SENSOR_UNRELIABLE = "unreliable";

    public static final String KEY_D_RSA_URL = "key_d_rsa-url";
    public static final String D_RSA = "D_RSA";
    //Manufacturer names - TBT-3002
    public static final String OPPO = "OPPO";
    public static final String ONEPLUS = "OnePlus";
    public static final String MOTO = "motorola";
    public static final String VIVO = "vivo";
    public static final String SAMSUNG = "samsung";
    public static final String HUAWEI = "HUAWEI";
    public static final String XIAOMI = "Xiaomi";
    public static final String REALME = "Realme";
    public static final String DEFAULT_HEADER = "NA";
    public static final String COUNTRY_INDIA = "IN";


    //Navigation
    public static final String KEY_TRIPPER_GTM = "Tripper";
    public static final String ScreenViewManual = "screenview_manual";
    public static final String helmetIconClicked = "Helmet icon clicked";
    public static final String destinationEntered = "Destination Entered";
    public static final String myRoutesClicked = "My Route clicked";
    public static final String addWaypointClicked = "Add Waypoint";
    public static final String getDirectionClicked = "Get Direction";
    public static final String navigateClicked = "Navigate";
    public static final String recordClicked = "Record";
    public static final String routeShareBtnClicked = "Share button clicked";
    public static final String poiCategoriesClicked = "POI Icon clicked";
    public static final String viewListClicked = "View List clicked";
    public static final String poiItemClickedFromList = "POI Item Selected";

    public static final int REQUEST_UPDATE = 100;
    public static final int REQUEST_UPDATE_IMMIDIATE = 102;
    //Trip Summary Save Api
    public static final String TRIP_START_TIME = "TRIP_START_TIME";
    public static final String TRIP_END_TIME = "TRIP_END_TIME";
    public static final String TRIP_START_POINT = "TRIP_START_POINT";
    public static final String TRIP_END_POINT = "TRIP_END_POINT";
    public static final String TRIP_SERIAL_NO = "TRIP_SERIAL_NO";
    public static final String TRIP_OS_VERSION = "TRIP_OS_VERSION";
    public static final String KEY_COMMA_OR_POINT = "key_comma_Point";
    public static final String KEY_COMMA = "Comma";
    public static final String KEY_POINT = "Point";
    public static final String KEY_KM_OR_MILES = "key_km_miles";
    public static final String KEY_KMS = "Kilometers";
    public static final String KEY_MILES = "Miles";
    public static final String FOREGROUND_NOTIFICATION = "Foreground_Notification";

    public static final String APP_SEETINGS = "app_settings";
    public static final String NAV_PREF_RECENT_LOCATION_LIST_KEY = "myrecentlocationlist";
    public static final String NAV_PREF_RECENT_ROUTE_LIST_KEY = "myrecentroutelist";
    public static final String NAV_PREF_ADDRESS_LIST_KEY = "addaddress";
    public static final String REMOTE_VERSION = "remote_version";
    public static final String REMOTE_DATA = "remote_data";
    public static final String APP_VERSION_DATA = "app_version_data";

    public static final String TRANS_SUCCESS = "Successful Transaction";
    public static final String KEY_COUNT_SMS_RETRIEVER_API = "SmsRetrieverApiTriggerCount";
    public static final String KEY_UPDATE_PROVISIONING = "key_update_provisioning";

    public static final String NAVIGATION_DUMMY_KEY = "dummyKey";

    }
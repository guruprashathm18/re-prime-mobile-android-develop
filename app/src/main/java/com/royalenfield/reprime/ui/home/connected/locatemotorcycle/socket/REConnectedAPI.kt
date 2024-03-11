package com.royalenfield.reprime.rest.connected

import android.util.Log
import com.google.gson.Gson
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseActivity
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.ConnectedResponse
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.socket.ConnectedServiceInterface
import com.royalenfield.reprime.ui.motorcyclehealthalert.fragment.HealthAlertInterface
import com.royalenfield.reprime.ui.motorcyclehealthalert.models.VehicleAlertModel
import com.royalenfield.reprime.ui.motorcyclehealthalert.models.VehicleAlertResponse
import com.royalenfield.reprime.ui.tripdetail.presenter.TripDetailCallback
import com.royalenfield.reprime.ui.tripdetail.view.TravelHistoryCallback
import com.royalenfield.reprime.ui.triplisting.response.*
import com.royalenfield.reprime.ui.triplisting.view.TripMergeCallback
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils
import com.royalenfield.reprime.utils.REUtils.decrypt
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.util.*

class REConnectedAPI {
    private lateinit var baseActivity: REBaseActivity
    private val TAG: String = "STOMP Connection"
    val KEY_APP_ID = "appId"
    val KEY_AUTHORIZATION = "Authorization"

    val appId = "2"
    var deviceId = REUtils.getDeviceId(REApplication.getAppContext())
    var vehicleNum = "it_170809677"
    var vehicleNum1 = "it_170809599"
    val topicDeviceLatestLocation = "/topic/cbpResponse/deviceLatestLocation/"
    val getDeviceLatestLocation = "/app/getDeviceLatestLocation"
    val topicTripDetails = "/topic/cbpResponse/tripDetails/"
    val topicTripDelete = "/topic/cbpResponse/deleteActivity/"
    val topicTravelHistory = "/topic/cbpResponse/fetchCompleteTripInfo/"

    val topicOBDDataPoints = "/topic/cbpResponse/obdDataPoints/"
    val getOBDDataPoints = "/app/getObdDataPoints"
    val topicHealthALert = "/topic/cbpResponse/allAlertsByUniqueId/"
    val getHealthAlert = "/app/getAllAlertsByUniqueId"
    val getTravelHistory = "/app/fetchCompleteTripInfo"
    val topicMergeTrip = "/topic/cbpResponse/createTripMerge/"
    val mergeTrip = "/app/createTripMerge"
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var mStompClient: StompClient

    lateinit var callback: ConnectedServiceInterface
    lateinit var tripResponseCallback: TripResponseCallback
    lateinit var healthAlertCallback: HealthAlertInterface
    lateinit var tripDetailCallback: TripDetailCallback
    lateinit var travelHistoryCallback: TravelHistoryCallback
    lateinit var tripMergeCallback: TripMergeCallback
    lateinit var currentDate: String
    lateinit var pastDate: String
    lateinit var connectedStatus: String
    var deviceLatestLocation = "deviceLatestLocation"
    var tripDetail = "tripDetail"
    var healthAlert = "healthAlert"
    var timestamp: String?=null
    lateinit var count: String
    lateinit var type: String


    /* initializer block*/
    init {
        //mSocketUrl = "http://34.93.90.136:1414/websocketApp/websocket"     //local
        //  getStompBaseUrlFromFireStore()
    }

    private fun createStompInstance(): StompClient {
        if (!::mStompClient.isInitialized) {
            mStompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                REConstants.ConnectedSocketUrl
            )
            mStompClient.withClientHeartbeat(10000)?.withServerHeartbeat(10000)
        }
        return mStompClient

    }

//    private fun getStompBaseUrlFromFireStore():StompClient {
//        val myRef = FirebaseDatabase.getInstance("https://re-platform-dev-configurations.firebaseio.com/")
//                .reference
//        myRef.child("configurations").child(PCUtils.PC_FIREBASE_COUNTRY_KEY).child("connectedendpoints").addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                mSocketUrl = dataSnapshot.child("SIT")
//                        .getValue(String::class.java).toString()
//                Log.d("REconnectAPI", mSocketUrl)
//                //  pcFirebaseView.onSuccessResponse(pcChooseModelList);
//
//                /*Instantiate StompClient */
//                mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, mSocketUrl)
//                mStompClient.withClientHeartbeat(10000)?.withServerHeartbeat(10000)

    //        }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                //  pcFirebaseView.onFailureResponse(error.getMessage());
//            }
//        })
//        return mStompClient
//    }

    fun updateDeviceId(deviceID: String) {
        vehicleNum = deviceID
        vehicleNum1 = deviceID
    }

    fun resetSubscriptions() {
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    fun getCompositeDisposable(): Disposable {
        return compositeDisposable!!
    }


    fun getStompClientInstance(): StompClient {
        return createStompInstance()
    }

    fun getStompHeaderList(): List<StompHeader> {
        val headerList = ArrayList<StompHeader>()
        headerList.add(StompHeader("app_id", this.appId))
        headerList.add(
            StompHeader(
                "Authorization", REApplication.getInstance()
                    .userTokenDetails))
        return headerList
    }

    /*fun createConnection(callback: ConnectedServiceInterface) {
        this.callback = callback
        connectedStatus = deviceLatestLocation
        buildConnection()
        setTopicForLatestLocation()
        connectStomp()
    }*/

    fun createConnectionForTripDetails(
        callback: TripResponseCallback,
        pastDate: String,
        date: String
    ) {
        this.tripResponseCallback = callback
        this.currentDate = date
        this.pastDate = pastDate
        connectedStatus = tripDetail
        buildConnection()
        setTopicForTripDetails()
        connectStomp()
    }

    fun createConnectionForHealthAlert(
        healthAlertCallback: HealthAlertInterface,
        timestamp: String?,
        pagination: String,type:String
    ) {
        this.healthAlertCallback = healthAlertCallback
        this.timestamp = timestamp
        this.count = pagination
        this.type=type
        connectedStatus = healthAlert
        buildConnection()
        setTopicForHealthAlert()
        connectStomp()
    }

    private fun buildConnection() {
        resetSubscriptions()
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, REConstants.ConnectedSocketUrl)
//        mStompClient.withClientHeartbeat(10000)?.withServerHeartbeat(10000)
        compositeDisposable?.add(createDisposalLifecycle())
    }

    /*fun setTopicForLatestLocation() {
        compositeDisposable?.add(getGreetingForLatestLocation())
    }*/

    private fun setTopicForHealthAlert() {
        compositeDisposable?.add(getGreetingForHealthAlert())
    }

    private fun setTopicForTripDetails() {
        compositeDisposable?.add(getGreetingForTripDetails())
        compositeDisposable?.add(getGreetingForTripDelete())
        compositeDisposable?.add(getGreetingForTravelHistory())
        compositeDisposable?.add(getGreetingForTripMerge())
    }

    fun disconnectStomp() {
        mStompClient.disconnect()
    }

    fun connectStomp() {
        if (mStompClient.isConnected) {
            sendEchoForSocketCall()
        } else {
            mStompClient.connect(getStompHeaderList())
        }
    }

    private fun createDisposalLifecycle(): Disposable {
        val disposalLifecycle = mStompClient.lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        printLog("Stomp connection opened")
                        sendEchoForSocketCall()
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, "Stomp connection error", lifecycleEvent.exception)
                        printLog("Stomp connection error")
                        // REBaseActivity().hideLoading()
                        REUtils.showErrorDialog(
                            REApplication.getAppContext(),
                            REApplication.getAppContext()
                                .getString(R.string.sorry_please_try_again)
                        )
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        printLog("Stomp connection closed")
                        resetSubscriptions()
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->
                        printLog("Stomp failed server heartbeat")
                }
            }
        return disposalLifecycle!!
    }

    private fun sendEchoForSocketCall() {
        if (connectedStatus.equals(deviceLatestLocation)) {
            sendEchoForLatestLocation()
        } else if (connectedStatus.equals(tripDetail)) {
            sendEchoForTripDetails(pastDate, currentDate)
        } else if (connectedStatus.equals(healthAlert)) {
            sendEchoForHealthAlert(timestamp, count,type)
        }
    }

    fun sendEchoForLatestLocation() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("deviceId", vehicleNum)
            jsonObject.put("deviceUniqueId", deviceId)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mStompClient.send(
            "/app/getDeviceLatestLocation", jsonObject.toString()
        )
            ?.compose { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
            }
            ?.subscribe({ Log.d(TAG, "STOMP echo send successfully") }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
                throwable.message?.let { printLog(it) }
            })?.let { compositeDisposable?.add(it) }
    }

    fun sendEchoForTripDetails(fromDateFormat: String, toDateFormat: String) {
        val customModel = CustomModel(deviceId, vehicleNum, fromDateFormat, toDateFormat,REUtils.getConnectedFeatureKeys().tripCount.toInt())

        Log.d(TAG, Gson().toJson(customModel))
        compositeDisposable?.add(mStompClient.send(
            "/app/getTripDetails",
            Gson().toJson(customModel).toString()
        )
            .compose { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({ Log.d(TAG, "STOMP echo send successfully") }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
                printLog(throwable.message.toString())
            })
        )
    }

    fun sendEchoForTripDelete(fromDateFormat: String,toTs:String,tripId:String) {
        val tripDeleteModel = TripDeleteModel(deviceId, vehicleNum, fromDateFormat,toTs,tripId)

        Log.d(TAG, Gson().toJson(tripDeleteModel))

        compositeDisposable?.add(mStompClient.send(
            "/app/deleteActivity", Gson().toJson(
                tripDeleteModel
            ).toString()
        )
            .compose { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({ Log.d(TAG, "STOMP echo send successfully") }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
                printLog(throwable.message.toString())
            })
        )
    }

    fun sendEchoForHealthAlert(timestamp: String?, count: String,type:String) {
        val VehicleAlertModel = VehicleAlertModel(vehicleNum1, deviceId, timestamp, count,type)

        compositeDisposable?.add(mStompClient.send(
            getHealthAlert,
            Gson().toJson(VehicleAlertModel).toString()
        )
            .compose { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({ Log.d(TAG, "STOMP echo send successfully") }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
                printLog(throwable.message.toString())
            })
        )
    }

    /*fun getGreetingForLatestLocation(): Disposable {
        // Receive greetings
        return mStompClient.topic(topicDeviceLatestLocation + deviceId)
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({ topicMessage ->
                    Log.d(TAG, "Received " + topicMessage.payload)

                    if (callback != null) {
                        callback.serviceSuccess(parceToJson(topicMessage.payload))
                    }
                    //addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                }, { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) })!!
    }*/

    fun sendEchoForTravelHistory( tripId: String) {

        val TravelHistoryModel = TravelHistoryModel(
            deviceId,
            vehicleNum,
            tripId
        )
        compositeDisposable?.add(mStompClient.send(
            getTravelHistory, Gson().toJson(
                TravelHistoryModel
            ).toString()
        )
            .compose { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({ Log.d(TAG, "STOMP echo send successfully") }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
                printLog(throwable.message.toString())
            })
        )
    }
    fun sendEchoForTripMerge( tripIds: Array<String>) {

        val TripMergeRequestModel = TripMergeRequestModel(
            deviceId,
            vehicleNum,
            tripIds
        )
        compositeDisposable?.add(mStompClient.send(
            mergeTrip, Gson().toJson(
                TripMergeRequestModel
            ).toString()
        )
            .compose { upstream ->
                upstream
                    .subscribeOn(Schedulers.io())
            }
            .subscribe({ Log.d(TAG, "STOMP echo send successfully") }, { throwable ->
                Log.e(TAG, "Error send STOMP echo", throwable)
                printLog(throwable.message.toString())
            })
        )
    }
    fun getGreetingForTripDetails(): Disposable {
        // Receive greetings
        // Receive greetings
        return mStompClient.topic(topicTripDetails + deviceId)
            .subscribeOn(Schedulers.io())
            .subscribe({ topicMessage ->
                Log.d(TAG, Gson().toJson(topicMessage.payload))
                tripResponseCallback.tripResponseSuccess(parceTripResponseToJson(topicMessage.payload))
                //                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
            }, { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) })
    }

    fun getGreetingForTravelHistory(): Disposable {
        // Receive greetings
        return mStompClient.topic(topicTravelHistory + deviceId)
            .subscribeOn(Schedulers.io())
            .subscribe({ topicMessage ->
                travelHistoryCallback.onTravelHistorySuccess(
                    parseTravelHistoryResponseToJson(
                        topicMessage.payload
                    )
                )
                //                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
            }, { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) })
    }

    fun getGreetingForTripMerge(): Disposable {
        // Receive greetings
        return mStompClient.topic(topicMergeTrip + deviceId)
            .subscribeOn(Schedulers.io())
            .subscribe({ topicMessage ->
                tripMergeCallback.onTripMergeSuccess(
                    parseTripMergeResponseToJson(
                        topicMessage.payload
                    )
                )
                //                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
            }, { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) })
    }

    fun getGreetingForTripDelete(): Disposable {
        // Receive greetings
        return mStompClient.topic(topicTripDelete + deviceId)
            .subscribeOn(Schedulers.io())
            .subscribe({ topicMessage ->

                tripDetailCallback.onTripDeleteSuccess(parseDeleteTripResponseToJson(topicMessage.payload))
                //                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
            }, { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) })
    }

    fun getGreetingForHealthAlert(): Disposable {
        // Receive greetings
        return mStompClient.topic(topicHealthALert + deviceId)
            .subscribeOn(Schedulers.io())
            .subscribe({ topicMessage ->
                healthAlertCallback.vehicleAlertResponseSuccess(
                    parseVehicleAlertResponseToJson(
                        topicMessage.payload
                    )
                )
                //                    addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
            }, { throwable -> Log.e(TAG, "Error on subscribe topic", throwable) })
    }

    private fun parseTravelHistoryResponseToJson(payload: String?): TravelHistoryResponseModel {
        var decrypted=decrypt(REApplication.getInstance().getSecret(), payload?.substring(0,24),  payload?.substring(24));
        Log.e("DECRYPT",decrypted);
        return Gson().fromJson(decrypted, TravelHistoryResponseModel::class.java)
    }

    private fun parseTripMergeResponseToJson(payload: String?): TripMergeResponseModel {
        var decrypted=decrypt(REApplication.getInstance().getSecret(), payload?.substring(0,24),  payload?.substring(24));
        Log.e("DECRYPT",decrypted);
        return Gson().fromJson(decrypted, TripMergeResponseModel::class.java)
    }

    private fun parseDeleteTripResponseToJson(payload: String?): TripDeleteResponseModel {
        var decrypted=decrypt(REApplication.getInstance().getSecret(), payload?.substring(0,24),  payload?.substring(24));
        Log.e("TRIP DELETE",decrypted);
        return Gson().fromJson(decrypted, TripDeleteResponseModel::class.java)
    }

    private fun parceTripResponseToJson(payload: String?): TripListingResponseModel {
//       val encypted:String="HEQpxGueFpRWA3ixLuKJD/eCjdrkHNHqCFbgSqTvLaZgnRLYL1saQZj6lyAENn4ffpWUtvodBhpgRU5oIGtvoGNMRSDxrQGYR7rm7qvQvr9ym+rdnJxH8zlQ5kLnI5GRrP1x5vdG8p9C3jthAIstSm6ECcVUuCrgV4+bsIMvsU0TcIfbGnBBIzDvlk3cNPKCvF7s+ejjQM985U1jhWIRc6uSONGi+q9zm0PgO4VpCcRydpzt0eOrhG6udkPrQ73TKKA5E+Ms/RapRqjC9u+QGuef3aaSJpQ0uHbngZ5iRn+ljzgWBRUeQ3jE3eR/lD8eSXNf488DuHgsOi7XVewbcekuIiPeVAU0aaZ8ixvjskLzgdlM+FCp9mnwFsq3m433rO0k/5sYL+GB68yglKC5UVRB0qVzHZOP4DgzJ+nC8R9bIgWWF8ngwtJDridRQywPgLpqeFUBeXBaphGcCoyyBT6qifbuAZaWgENt7sOUJZIqsmKhT3YiL8hMu4wegx84lr7djSXz29m7HYa7v6AYXzq/sEeqOVga2DxXNGV4tLO+uJcsZFG/BPKzOsUMfEw5iCs96F1+YleDp+jO+WFPPaLFfIvPBJFqhuuHspqAzs4h+XiaErtYeY2qpnGZQ29O";
// val dcypted:String=decrypt(encypted, secretKey);
//        val model:TripListingResponseModel=  Gson().fromJson(dcypted, TripListingResponseModel::class.java)

        var decrypted=decrypt(REApplication.getInstance().getSecret(), payload?.substring(0,24),  payload?.substring(24));
        Log.e("DECRYPT",decrypted);
        return Gson().fromJson(decrypted, TripListingResponseModel::class.java)
    }

    private fun parseVehicleAlertResponseToJson(payload: String?): VehicleAlertResponse {
        var decrypted=decrypt(REApplication.getInstance().getSecret(), payload?.substring(0,24),  payload?.substring(24));
        Log.e("Response",decrypted)
        return Gson().fromJson(decrypted, VehicleAlertResponse::class.java)
    }

    private fun parceToJson(payload: String?): ConnectedResponse {
        return Gson().fromJson(payload, ConnectedResponse::class.java)
    }

    private fun printLog(text: String) {
        Log.i(TAG, text)
        //        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    fun sendForTripDetailEvent(
        callback: TripResponseCallback,
        fromDateFormat: String,
        toDateFormat: String
    ) {

        this.tripResponseCallback = callback
        sendEchoForTripDetails(fromDateFormat, toDateFormat)
    }

    fun sendForTripDeleteEvent(callback: TripDetailCallback, fromDateFormat: String, toTs:String,tripId:String) {

        this.tripDetailCallback = callback
        sendEchoForTripDelete(fromDateFormat,toTs,tripId)
    }

    fun sendForHealthAlertEvent(callback: HealthAlertInterface, timestamp: String?, count: String,type:String) {
        this.healthAlertCallback = callback
        sendEchoForHealthAlert(timestamp, count,type)
    }

    fun getTravelHistoryData(
        callback: TravelHistoryCallback,
        tripId: String
    ) {
        this.travelHistoryCallback = callback;
        sendEchoForTravelHistory(tripId)
    }

    fun mergeTrip(
        callback: TripMergeCallback,
        tripId: Array<String>
    ) {
        this.tripMergeCallback = callback;
        sendEchoForTripMerge(tripId)
    }


    /*Inner class
    Custom model for trip Details*/

}
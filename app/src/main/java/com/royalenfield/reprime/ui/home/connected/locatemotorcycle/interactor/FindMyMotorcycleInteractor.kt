package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.interactor

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.rest.connected.REConnectedAPI
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.listeners.OnFinishedListener
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.ConnectedResponse
import com.royalenfield.reprime.utils.REUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import com.royalenfield.reprime.utils.RELog
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

@SuppressLint("CheckResult")
class FindMyMotorcycleInteractor : IFindMyMotorcycleInteractor {

    private var reConnectedInstance: REConnectedAPI
    private var stompClient: StompClient
    private val androidId:String
    private var compositeDisposable: CompositeDisposable? = null
    init {
        reConnectedInstance = REApplication
                .getInstance().reConnectedAPI
        stompClient = reConnectedInstance.getStompClientInstance()

        resetSubscriptions()
        androidId = REUtils.getDeviceId(REApplication.getAppContext())

    }
    fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }


    override fun fetchDeviceData(deviceId: String, onfinishedListener: OnFinishedListener) {

        topicSubscription(deviceId, onfinishedListener)
        val dispLifecycle=   stompClient
                .lifecycle()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({ lifecycleEvent ->
                    when (lifecycleEvent.type) {
                        LifecycleEvent.Type.OPENED -> {
                            sendEchoForLatestLocation(deviceId)
                        }
                        LifecycleEvent.Type.ERROR -> {
                          //  RELog.e(lifecycleEvent.exception.toString(), "Stomp connection error")
                            onfinishedListener.onFailure(LifecycleEvent.Type.ERROR.toString())
                            topicUnSubscription(deviceId);
                            resetSubscriptions();
                        }
                        LifecycleEvent.Type.CLOSED -> {
                            printLog("Stomp connection closed")
                            onfinishedListener.onFailure("Stomp connection closed")
                        }
                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->
                            printLog("Stomp failed server heartbeat")
                    }
                })

        if (dispLifecycle != null) {
            compositeDisposable?.add(dispLifecycle)
        }
        if (stompClient.isConnected) {
            sendEchoForLatestLocation(deviceId)
        } else {
            stompClient.connect(reConnectedInstance.getStompHeaderList())
        }

    }

    private fun topicSubscription(deviceId: String, onfinishedListener: OnFinishedListener) {
      val dispTopic=  stompClient.topic(reConnectedInstance.topicDeviceLatestLocation + androidId)
                .subscribeOn(Schedulers.io())
                .subscribe({ topicMessage ->
                    RELog.d("Received topicDeviceLatestLocation " ,""+ topicMessage.getPayload())
                    onfinishedListener.onSuccess(parceToJson(topicMessage.payload))
                  //  resetSubscriptions()
                }, { throwable -> RELog.e("TAG", "Error on subscribe topic",throwable) });
        compositeDisposable?.add(dispTopic)
    }

    override fun fetchDataInEveryTenSecs(deviceId: String) {
        if (stompClient.isConnected) {
            sendEchoForLatestLocation(deviceId)
        }
    }

    fun topicUnSubscription(deviceId: String) {
        stompClient.topic(reConnectedInstance.topicDeviceLatestLocation + androidId).unsubscribeOn(Schedulers.io())
    }

    private fun printLog(msg: String) {
        RELog.e(msg)
    }

    private fun sendEchoForLatestLocation(deviceId: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("deviceId", deviceId)//tcu id it_11313
            jsonObject.put("deviceUniqueId", androidId)//phone id

        } catch (e: Exception) {
            e.printStackTrace()
        }


        stompClient.send(reConnectedInstance.getDeviceLatestLocation
                , jsonObject.toString())
                ?.compose { upstream ->
                    upstream.unsubscribeOn(Schedulers.io()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                }
                ?.subscribe({
                    RELog.d("STOMP","STOMP echo send successfully")
                    RELog.d("STOMP","Request DeviceLatestLocation : $deviceId")
                }
                        , { throwable ->
                    RELog.e("STOMP", "Error send STOMP echo",throwable)
                    throwable.message?.let { printLog(it) }
                })
    }

    private fun parceToJson(payload: String?): ConnectedResponse {
        var decrypted=
            REUtils.decrypt(REApplication.getInstance().getSecret(), payload?.substring(0, 24), payload?.substring(24));
Log.e("RESPONSE",decrypted);
        resetSubscriptions()
        return Gson().fromJson(decrypted, ConnectedResponse::class.java)
    }

    fun getStompclient(): StompClient {
        return stompClient
    }

    fun disconnectStompClient() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
        if (compositeDisposable != null)
            compositeDisposable?.dispose()
    }
}
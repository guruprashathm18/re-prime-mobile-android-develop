package com.royalenfield.reprime.ui.home.connected.motorcycle.interactor

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.rest.connected.REConnectedAPI
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.OBDResponseData
import com.royalenfield.reprime.ui.home.connected.motorcycle.listener.MotorcycleListener
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.*
import com.royalenfield.reprime.ui.onboarding.login.interactor.BaseCallback
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils
import com.royalenfield.reprime.utils.REUtils.decrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import com.royalenfield.reprime.utils.RELog
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


@SuppressLint("CheckResult")
class MotorcycleInteractor : IMotorcycleInteractor {

    private var androidId: String
    private var reConnectedInstance: REConnectedAPI
    private var stompClient: StompClient
    private var compositeDisposable: CompositeDisposable? = null

    init {
        reConnectedInstance = REApplication
            .getInstance().reConnectedAPI
        stompClient = reConnectedInstance.getStompClientInstance()

        resetSubscriptions()

        androidId = REUtils.getDeviceId(REApplication.getAppContext())
    }

    override fun fetchOBDDataPoints(deviceId: String, listener: MotorcycleListener) {

        topicSubscription(androidId, listener)

        val dispLifecycle = stompClient
            .lifecycle()
            ?.subscribeOn(Schedulers.io())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        sendEchoForOBDDataPoints(deviceId)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        //   RELog.e(lifecycleEvent.exception.toString(), "Stomp connection error")
                        listener.onFailure(LifecycleEvent.Type.ERROR.toString())
                        topicUnSubscription(deviceId);
                        resetSubscriptions();
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        listener.onFailure("Stomp connection closed")
                    }
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT ->{
                        // RELog.e(lifecycleEvent.exception.toString(), "Stomp failed server heartbeat")
                    }
                }
            }

        if (dispLifecycle != null) {
            compositeDisposable?.add(dispLifecycle)
        }

        if (stompClient.isConnected) {
            sendEchoForOBDDataPoints(deviceId)
        } else {
            stompClient.connect(reConnectedInstance.getStompHeaderList())
        }
    }

    private fun topicSubscription(androidId: String, listener: MotorcycleListener) {
        val dispTopic = stompClient.topic(reConnectedInstance.topicOBDDataPoints + androidId)
            .subscribeOn(Schedulers.io())
            .subscribe({ topicMessage ->
                RELog.d("Received topicOBDDataPoints " , topicMessage.getPayload())
                listener.onSuccess(parceToJson(topicMessage.payload))
                resetSubscriptions()
            }, { throwable -> RELog.e("topic OBD", "Error on subscribe topic",throwable) })

        compositeDisposable?.add(dispTopic)
    }

    fun topicUnSubscription(deviceId: String) {
        stompClient.topic(reConnectedInstance.topicOBDDataPoints + deviceId).unsubscribeOn(Schedulers.io())
    }

    override fun fetchDataInEveryTenSecs(deviceId: String) {
        if (stompClient.isConnected) {
            sendEchoForOBDDataPoints(deviceId)
        }
    }

    private fun sendEchoForOBDDataPoints(deviceId: String) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("deviceId", androidId)
            jsonObject.put("uniqueId", deviceId)
            jsonObject.put("alertTimeStamp", REApplication.getInstance().healthAlertTimeStamp)

            jsonObject.put("from", "")
            jsonObject.put("to", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        stompClient.send(
            reConnectedInstance.getOBDDataPoints
            , jsonObject.toString()
        )
            .compose { upstream ->
                upstream.unsubscribeOn(Schedulers.newThread()).subscribeOn(Schedulers.io())
                    .observeOn(
                        AndroidSchedulers.mainThread()
                    )
            }
            .subscribe({
                RELog.d("STOMP","STOMP echo send successfully")
                RELog.d("Android Id","Request OBDDataPoints : " + androidId)
            }
                , { throwable ->
                    RELog.e("STOMP", "Error send STOMP echo",throwable)
                    throwable.message?.let { RELog.e("TAG", it,throwable) }
                })
    }


    private fun parceToJson(payload: String?): OBDResponseData {
        var decrypted=decrypt(REApplication.getInstance().getSecret(), payload?.substring(0,24),  payload?.substring(24));
        Log.e("DECRYPT",decrypted);
        return Gson().fromJson(decrypted, OBDResponseData::class.java)
    }

    fun getStompclient(): StompClient {
        return stompClient
    }

    fun disconnectStompClient() {
        if (stompClient.isConnected) {
            stompClient.disconnect()

            if (compositeDisposable != null)
                compositeDisposable?.dispose()

        }
    }


    /*Rest Api Call*/

    fun fetchPairingKey(
        requestModel: PairingMotorcycleRequestModel,
        pairingListener: MotorcycleListener
    ) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .localAPI
            .getPairingKey(requestModel)
            .enqueue(object : BaseCallback<PairingMotorcycleResponseModel>() {
                override fun onResponse(
                    call: Call<PairingMotorcycleResponseModel>,
                    response: Response<PairingMotorcycleResponseModel>
                ) {
                    super.onResponse(call, response)
                    if (response.isSuccessful && response.body() != null && response.body()!!.data != null) {
                        if (response.body()!!.data.getDeviceData != null) {
                            pairingListener.onPairingSuccess(response.body()!!.data.getDeviceData)
                        } else {
                            pairingListener.onPairingFailure(response.body()?.errorMessage)
                        }
                    } else {
                        pairingListener.onPairingFailure(response.body()?.errorMessage)
                    }
                }

                override fun onFailure(call: Call<PairingMotorcycleResponseModel>, t: Throwable) {
                    super.onFailure(call, t)
                    if (t is NetworkErrorException) {
                        pairingListener.onPairingFailure(
                            REApplication.getAppContext().resources.getString(
                                R.string.network_failure
                            )
                        )
                    } else {
                        pairingListener.onPairingFailure("")
                    }
                }
            })
    }

    /*Read settings from Rest*/
    fun readSettings(requestModel: SettingRequestModelRead, settingListener: MotorcycleListener) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .localAPI
            .settingsRead(requestModel)
            .enqueue(object : BaseCallback<CommonResponseModel<SettingResponseModel>>() {
                override fun onResponse(
                    call: Call<CommonResponseModel<SettingResponseModel>>,
                    response: Response<CommonResponseModel<SettingResponseModel>>
                ) {
                    super.onResponse(call, response)
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.data != null) {

                            settingListener.onReadSettingsSuccess(response.body()!!.data!!)
                        }

                        else {
                            settingListener.onReadSettingsFailure(response.body()!!.errorMessage.toString())
                        }
                    }
                    else if(response.code()== REConstants.API_UNAUTHORIZED)
                        settingListener.onReadSettingsFailure( REApplication.getAppContext().resources.getString(
                            R.string.unauthorized_login
                        ))

                    else {
                        settingListener.onReadSettingsFailure(
                            null)

                    }
                }

                override fun onFailure(
                    call: Call<CommonResponseModel<SettingResponseModel>>,
                    t: Throwable
                ) {
                    super.onFailure(call, t)
                    if (t is NetworkErrorException) {
                        settingListener.onReadSettingsFailure(
                            REApplication.getAppContext().resources.getString(
                                R.string.network_failure
                            )
                        )
                    } else {
                        settingListener.onReadSettingsFailure(null)
                    }
                }
            })
    }

    /*Update settings from Rest*/
    fun updateSettings(requestModel: SettingRequestModel, settingListener: MotorcycleListener) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .localAPI
            .settingsUpdate(requestModel)
            .enqueue(object : BaseCallback<CommonResponseModel<String>>() {
                override fun onResponse(
                    call: Call<CommonResponseModel<String>>,
                    response: Response<CommonResponseModel<String>>
                ) {
                    super.onResponse(call, response)
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.data != null) {
                            settingListener.onUpdateSettingsSuccess(response.body()!!.data.toString())
                        } else {
                            settingListener.onUpdateSettingsFailure(response.body()!!.errorMessage.toString())
                        }
                    } else {
                        settingListener.onUpdateSettingsFailure(null)
                    }
                }

                override fun onFailure(call: Call<CommonResponseModel<String>>, t: Throwable) {
                    super.onFailure(call, t)
                    if (t is NetworkErrorException) {
                        settingListener.onUpdateSettingsFailure(
                            REApplication.getAppContext().resources.getString(
                                R.string.network_failure
                            )
                        )
                    } else {
                        settingListener.onUpdateSettingsFailure(t.message.toString())
                    }
                }
            })
    }

    fun getPairingStubData(): PairingMotorcycleResponseModel.GetDeviceData {
        return Gson().fromJson(
            REUtils.loadJSONFromAsset(
                REApplication.getAppContext(),
                "pairing_key.json"
            ), PairingMotorcycleResponseModel::class.java
        ).data.getDeviceData
    }

    fun getSettingStubData(): SettingResponseModel {
        return (Gson().fromJson(
            REUtils.loadJSONFromAsset(
                REApplication.getAppContext(),
                "settings.json"
            ), SettingResponseModel::class.java
        ))
    }

    fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable?.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    fun disconnectStomp(view: View?) {
        stompClient.disconnect()
    }


    /*Rest Api Call for provision/updateStatus*/

    fun provisionUpdateStatus(
        requestModel: ProvisionUpdateStatusRequestModel,
        pairingListener: MotorcycleListener
    ) {
        REApplication
            .getInstance()
            .reWebsiteApiInstance
            .localAPI
            .provisionUpdateStatus(requestModel)
            .enqueue(object : retrofit2.Callback<CommonResponseModel<String>> {
                override fun onResponse(
                    call: Call<CommonResponseModel<String>>,
                    response: Response<CommonResponseModel<String>>
                ) {
                    if (response.isSuccessful && response.body() != null && response.body()!!.data != null) {
                        pairingListener.onProvisionUpdateStatusSuccess(response.body()!!.data.toString())

                    } else {
                        // RELog.d("response code  : " + response.code() + " : ", response.message())
                        pairingListener.onProvisionUpdateStatusFailure(response.body()!!.errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<CommonResponseModel<String>>, t: Throwable) {
                    if (t is NetworkErrorException) {
                        pairingListener.onPairingFailure(
                            REApplication.getAppContext().resources.getString(
                                R.string.network_failure
                            )
                        )
                    } else {
                        pairingListener.onProvisionUpdateStatusFailure(t.message.toString())
                    }
                }
            })
    }
}
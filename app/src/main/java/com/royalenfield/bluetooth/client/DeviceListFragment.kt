package com.royalenfield.bluetooth.client

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.WorkerThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.royalenfield.bluetooth.ble.BLEModel
import com.royalenfield.bluetooth.ble.BleManagerProvider
import com.royalenfield.bluetooth.ble.DeviceInfo
import com.royalenfield.bluetooth.otap.BlockSegmentUtils
import com.royalenfield.bluetooth.otap.interactor.OtapInteractor
import com.royalenfield.bluetooth.utils.BLEConstants
import com.royalenfield.bluetooth.utils.BLEDeviceManager
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseActivity.manualDisconnectBase
import com.royalenfield.reprime.models.request.web.otap.RemoveTripperDeviceApiRequest
import com.royalenfield.reprime.models.response.web.otap.OtapSaveDeviceinfoApiRequest
import com.royalenfield.reprime.preference.PreferenceException
import com.royalenfield.reprime.preference.REPreference
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.RELog
import com.royalenfield.reprime.utils.REUtils
import java.util.*


/**
 * DeviceListFrgament is for displaying the available and connected devices list
 */
@Suppress("DEPRECATION")
class DeviceListFragment : Fragment(), DeviceListContract.View,
    PINAuthorizationScreen.ItemClickListener, DeviceListContract.OnDisconnectClickListener {

    companion object {
        lateinit var cContext: Context
        lateinit var installation_layout: ConstraintLayout
        lateinit var device_NotConnected_RecyclerView: RecyclerView
        lateinit var text_otherDevices: TextView
        lateinit var bleRefresh: TextView
        lateinit var install: Button
        var addressToConnect: String = ""
        var guid: String = ""
        var randomUUID: String = ""
        private var manager = BleManagerProvider.getInstance()
        val messageByteArrayQueue: Queue<ByteArray> = manager.getQueue()
        var mIsConnected = false
        var mIsAutoConnecting = false
        var mMyTBTDeviceClicked = false
        var mIsManualDisconnect = false
        var mIsForgetClicked = false
        var mIsScanFailed = false
        var mIsScanInitiatedConnection = false
        var mPosition: Int = -1
        var mRetryCount: Int = 0
        var mPINAuthorised = false
        lateinit var otapFirestoreData: Map<String, Object>
        var mOtherDevicesList: MutableList<DeviceInfo> = ArrayList()
        private var prefs: SharedPreferences? = null
        var myTBTDeviceList: MutableList<DeviceInfo> = ArrayList()
        lateinit var deviceInfo: DeviceInfo
        var navFilterFragment: PINAuthorizationScreen? = null
        private var device_RecyclerView: RecyclerView? = null

        private var imgClose: ImageView? = null
        private var mScanStatusText: TextView? = null
        private var ble_tv_actionbar_title: TextView? = null
        private var deviceSwitch: Switch? = null
        lateinit var deviceNotConnectedAdapter: DeviceAdapter
        lateinit var deviceConnectedAdapter: DeviceAdapter
        lateinit var mListener: DeviceListContract.View
        private var devicesSet: MutableList<DeviceInfo> = ArrayList()
        lateinit var mBluetoothAdapter: BluetoothAdapter
        var isForAddTripper: Boolean = false

        @JvmStatic
        fun showInstallLayout(show: Boolean) {
            if (show) {
                Handler(Looper.getMainLooper()).post {
                    installation_layout.visibility = VISIBLE
                    device_NotConnected_RecyclerView.visibility = GONE
                    text_otherDevices.visibility = GONE
                    bleRefresh.visibility = GONE
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    installation_layout.visibility = GONE
                    device_NotConnected_RecyclerView.visibility = VISIBLE
                    text_otherDevices.visibility = VISIBLE
                    bleRefresh.visibility = VISIBLE
                }
            }
        }

    }

    fun setListener(
        listener: DeviceListContract.View,
        isConnected: Boolean, otapData: Map<String, Object>
    ) {
        mIsConnected = isConnected
        if (mIsConnected) {
            mPINAuthorised = true
        }
        otapFirestoreData = otapData
        mListener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cContext = context
    }

    override fun onScanStart() {
        mScanStatusText!!.visibility = VISIBLE
        if (deviceSwitch!!.isChecked)
            mScanStatusText?.setText(R.string.tbt_scan_status_switch_on)
        else
            mScanStatusText?.setText(R.string.tbt_scan_status_switch_off)
    }

    override fun onScanInitFailed() {
        mIsScanFailed = true
        mIsAutoConnecting = false
        handleBluetooth(false)
        BLEModel.getInstance().setScanning(false)
        if (!mIsConnected) {
            mScanStatusText?.setText(R.string.tbt_device_scan_failed)
        } else {
            mScanStatusText?.visibility = GONE
        }
    }

    /**
     * Enables/Disables bluetooth
     */
    private fun handleBluetooth(enable: Boolean) {
        if (enable) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        cContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }else{
                    mBluetoothAdapter.disable()
                }
            }else{
                mBluetoothAdapter.disable()
            }
        }
    }

    override fun onScanStop() {
        if (mOtherDevicesList.size == 0) {
            if (!mIsConnected) {
                if (mIsScanFailed) {
                    mScanStatusText?.setText(R.string.tbt_device_scan_failed)
                } else {
                    mScanStatusText?.setText(R.string.tbt_no_device_found)
                }
            } else {
                mScanStatusText?.visibility = GONE
            }
            mMyTBTDeviceClicked = false
            mIsForgetClicked = false
            mIsManualDisconnect = false
            mIsAutoConnecting = false
            BLEModel.getInstance().isManualDisconnect = false
        }
        mIsScanFailed = false
    }

    /**
     * onDisconnectClicked  is for closing the connection
     * This method sends a broadcast to REPrime App for displaying an alert in App.
     */
    override fun onDisconnectClicked(forget: Boolean, position: Int) {
        val devicePaired = Intent(BLEConstants.BLE_INTENT_ACTION_USER_ACTION)
        if (forget) {
            devicePaired.putExtra(BLEConstants.BLE_USER_ACTION_TYPE, BLEConstants.BLE_DEVICE_FORGET)
        } else {
            devicePaired.putExtra(
                BLEConstants.BLE_USER_ACTION_TYPE,
                BLEConstants.BLE_DEVICE_DISCONNECT
            )
        }
        devicePaired.putExtra(BLEConstants.BLE_DEVICE_POSITION, position)
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(devicePaired)
        }
    }

    /**
     * This is used to notify the RE App for the connected device Info
     */
    override fun connectedDeviceInfo(deviceInfo: MutableList<DeviceInfo>) {
        mListener.connectedDeviceInfo(deviceInfo)
    }

    private fun disconnectDevice() {
        disconnect()
    }


    /**
     * Callback from BluetoothGatt regarding the connection state
     */
    @WorkerThread
    fun updateConnectionState(address: String, boolean: Boolean, status: Int) {
        try {
            //Log.e("CHK","inside updateconnection BCR WTHREAD")
            mIsConnected = boolean
            mListener.dismissProgress()
            addressToConnect = address
            mPINAuthorised = BLEDeviceManager.isTrustedDevice(address, context)
            if (mIsConnected) {
                //            mIsScanInitiatedConnection = true
                mIsScanInitiatedConnection = false
                mIsManualDisconnect = false
                mMyTBTDeviceClicked = false
                mIsForgetClicked = false
                mIsAutoConnecting = false
                mRetryCount = 0
                BLEModel.getInstance().isManualDisconnect = false
                //            presenter!!.stopScan()
                if (!mPINAuthorised) {
                    showBottomSheetPINDialog()
                    /*val editor: Editor = prefs!!.edit()
                    editor.remove("currentSoftwareVersion")
                    editor.apply()*/
                }
            } else if (!mIsConnected && addressToConnect.isNotEmpty() && mPINAuthorised) {
                //            mIsAutoConnecting = false
                activity?.runOnUiThread {
                    mScanStatusText!!.visibility = GONE
                }
                if (status == 133 || status == 257) {
                    if (mRetryCount <= 3) {
                        RELog.d("updateConnectionState", "Retry:" + mRetryCount)
                        mRetryCount++
                        mIsAutoConnecting = false
                        mIsScanInitiatedConnection = false
                    }
                }
                if (mMyTBTDeviceClicked) {
                    activity?.runOnUiThread {
                        REUtils.showErrorDialogCustomTitle(
                            context,
                            context?.getString(R.string.text_connection_unsuccess),
                            context?.getString(R.string.text_turnon_display)
                        )
                    }
                }
                mIsScanInitiatedConnection = false
                showInstallLayout(false)
                if (mIsForgetClicked) {
                    if (myTBTDeviceList.size > 0)
                    // Remove Tripper from the cloud api
                        OtapInteractor().removeTripperFromCloud(
                            RemoveTripperDeviceApiRequest(
                                myTBTDeviceList.get(mPosition).userGUID,
                                myTBTDeviceList.get(mPosition).deviceToken,
                                myTBTDeviceList.get(mPosition).serialNumber
                            )
                        )
                    myTBTDeviceList.removeAt(mPosition)
                    BLEDeviceManager.updateMyTBTDevice(myTBTDeviceList, context)
                }
                activity?.runOnUiThread {
                    if (context != null) myTBTDeviceList = BLEDeviceManager.getMyTBTList(context)
                    if (myTBTDeviceList.size > 0 && context != null) {
                        BLEDeviceManager.updateDisconnectedDeviceStatus(
                            context,
                            address,
                            myTBTDeviceList
                        )
                    } else {
                        myTBTDeviceList = ArrayList()
                    }
                    // Log.e("CHK","inside updateconnectionstate")
                    populateConnectedDevices()
                }
                devicesSet.clear()
                mOtherDevicesList = ArrayList()
                deviceNotConnectedAdapter =
                    DeviceAdapter(mOtherDevicesList, otapFirestoreData, this, false)
                setNotConnectedRecyclerView()
                //Tries to reconnect the device by using the address saved
                reConnect()
            }
        } catch (e: Exception) {
            RELog.e(e)
        }

    }

    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private fun onDeviceConnected(deviceInfo: DeviceInfo) {
        try {
            // Log.e("CHK","inside onDeviceConected")
            deviceInfo.isConnected = mIsConnected
            if (context != null) myTBTDeviceList = BLEDeviceManager.getMyTBTList(context)
            for (i in myTBTDeviceList.indices) {
                myTBTDeviceList[i].isConnected = myTBTDeviceList[i] == deviceInfo
            }
            BLEDeviceManager.updateMyTBTDevice(myTBTDeviceList, context)
            connectedDeviceInfo(myTBTDeviceList)
            if (mOtherDevicesList.size > 0) {
                mOtherDevicesList.remove(deviceInfo)
            }
            activity?.runOnUiThread {
                mOtherDevicesList = ArrayList()
                deviceNotConnectedAdapter =
                    DeviceAdapter(mOtherDevicesList, otapFirestoreData, this, false)
                setNotConnectedRecyclerView()
                deviceNotConnectedAdapter.notifyDataSetChanged()
                populateConnectedDevices()
            }
        } catch (e: UninitializedPropertyAccessException) {
            e.stackTrace
        }


    }

    /**
     * This method is used for the PIN authorization
     * For saving the address to shared preference
     */
    fun updatePINAuthorization(boolean: Boolean, deviceInfo: DeviceInfo) {
//        if (userVisibleHint) {
        mPINAuthorised = boolean
        if (boolean) {
            onDeviceConnected(deviceInfo)
            isForAddTripper = true
            val osver = requestCurrentTripperOSVersion()
        } else {
            activity?.runOnUiThread {
                Toast.makeText(activity, R.string.text_invalid_pin, Toast.LENGTH_SHORT).show()
            }

        }
//        }
    }

    private fun getCurrentTripperOSVersion(): String {
        val currentVersion = prefs!!.getString("currentSoftwareVersion", "")
        return if (currentVersion != null && currentVersion.isNotEmpty()) {
            currentVersion
        } else {
            ""
        }
    }

    private fun getTripperUniqueId(): String {
        val tripperid = prefs!!.getString("currentTripperUniqueId", "")
        return if (tripperid != null && tripperid.isNotEmpty()) {
            tripperid
        } else {
            ""
        }
    }

    @SuppressLint("MissingPermission")
    @WorkerThread
    fun reConnect() {
        //Tries to reconnect if it is disconnected without user ineraction
        if (!mIsManualDisconnect && context != null && !mMyTBTDeviceClicked && !mIsForgetClicked &&
            BLEDeviceManager.isAutoConnectEnabled(context) && BluetoothAdapter.getDefaultAdapter().isEnabled
        ) {
            onScanStart()
            manager.scan()
        }

    }

    override fun updateDevice(result: DeviceInfo) {
        if (result.name != null) {
            if (context != null && !BLEDeviceManager.getMyTBTList(context).contains(result)) {
                mScanStatusText!!.visibility = GONE
                mOtherDevicesList.add(result)
                deviceNotConnectedAdapter =
                    DeviceAdapter(mOtherDevicesList, otapFirestoreData, this, false)
                setNotConnectedRecyclerView()
            } else {
                autoConnect(result)
            }
        }
    }


    /**
     * Connects the device
     */
    @WorkerThread
    private fun autoConnect(result: DeviceInfo) {
        // This is for auto connection once the store device is found in scan results
        // We are doing auto connect if there is no existing connection
        if (!mIsManualDisconnect && !mIsConnected && context != null && BLEDeviceManager.isAutoConnectEnabled(
                context
            ) &&
            !mIsAutoConnecting && !mIsScanInitiatedConnection && BluetoothAdapter.getDefaultAdapter().isEnabled
        ) {
            mIsAutoConnecting = true;
            mIsScanInitiatedConnection = true
            addressToConnect = ""
            mListener.showScanningProgress()
            deviceInfo = result
            result.address?.let { manager.connect(it, false) }
        }
    }

    override fun showError(error: String) {
        mListener.showError(error)
        mListener.dismissProgress()
        activity?.runOnUiThread {
            Toast.makeText(activity, """Error displayed!!$error""", Toast.LENGTH_SHORT).show()

        }
    }

    private fun showBottomSheetPINDialog() {
        try {
            navFilterFragment = PINAuthorizationScreen.newInstance()

            navFilterFragment?.setRouteListener(this)
            navFilterFragment?.show(
                childFragmentManager,
                PINAuthorizationScreen.TAG
            )
        } catch (e: IllegalStateException) {
            e.stackTrace
        }
    }

    override fun showScanningProgress() {
        mListener.showScanningProgress()
        activity?.runOnUiThread {

            Toast.makeText(activity, "Scanning in progress!!", Toast.LENGTH_SHORT).show()
        }


    }

    override fun dismissProgress() {
        mListener.dismissProgress()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guid = REApplication.getInstance().userLoginDetails.data.user.userId
        try {
            randomUUID = REPreference.getInstance().getString(context, "RandomUUID")
        } catch (e: PreferenceException) {
            e.printStackTrace()
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mBroadcastReceiver,
                IntentFilter("updateconnection")
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mPinAuthReceiver,
                IntentFilter("pinAuth")
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mOSVersionBroadcastReceiver,
                IntentFilter("didOSVersionReceived")
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mSerialNumberBroadcastReceiver,
                IntentFilter("didSerialNumberReceived")
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mScanStopReceiver,
                IntentFilter("scanStop")
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mScanFailReceiver,
                IntentFilter(BLEConstants.BLE_SCAN_FAILED)
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mScanResultsReceiver,
                IntentFilter("scanresults")
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                mUserResponseReceiver,
                IntentFilter(REConstants.BLE_USER_RESPONSE)
            )
        }
        if (!activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)!!) {
            activity?.runOnUiThread {
                //  textStatus!!.text = "BLE is not supported!!"
                Toast.makeText(activity, "BLE is not supported!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        devicesSet.clear()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mBroadcastReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mPinAuthReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mOSVersionBroadcastReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mSerialNumberBroadcastReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mScanStopReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mScanFailReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mScanResultsReceiver
            )
        }
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(
                mUserResponseReceiver
            )
        }
    }

    private fun populateConnectedDevices() {
        //  Log.e("CHK","inside populateConnectedDevices")
        if (context != null) myTBTDeviceList = BLEDeviceManager.getMyTBTList(context)
        deviceConnectedAdapter = DeviceAdapter(myTBTDeviceList, otapFirestoreData, this, true)
        device_RecyclerView?.adapter = deviceConnectedAdapter
        deviceConnectedAdapter.notifyDataSetChanged()
        device_RecyclerView?.invalidate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_device_list, container, false)
        imgClose = view.findViewById(R.id.iv_nav_ble) as ImageView
        imgClose?.setOnClickListener {
            val backPress = Intent(BLEConstants.BLE_BACK_PRESS)
            LocalBroadcastManager.getInstance(cContext).sendBroadcast(backPress)
//            activity?.finish()
//            activity?.overridePendingTransition(R.anim.anim_exit, R.anim.slide_out_right)
        }
        mScanStatusText = view.findViewById(R.id.nav_text_info) as TextView
        installation_layout = view.findViewById(R.id.constraint_device_update) as ConstraintLayout

        device_NotConnected_RecyclerView =
            view.findViewById(R.id.deviceNotConnectedRecyclerView) as RecyclerView
        device_RecyclerView = view.findViewById(R.id.deviceRecyclerView) as RecyclerView
        bleRefresh = view.findViewById(R.id.text_refresh) as TextView
        text_otherDevices = view.findViewById(R.id.text_other_devices) as TextView
        install = view.findViewById(R.id.button_install) as Button
        ble_tv_actionbar_title = view.findViewById(R.id.ble_tv_actionbar_title) as TextView
        ble_tv_actionbar_title!!.setText(R.string.text_navigation_devices)
        deviceSwitch = view.findViewById(R.id.ble_switch) as Switch
        deviceSwitch!!.isChecked = BLEDeviceManager.isAutoConnectEnabled(context)
        prefs = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        if (context != null) myTBTDeviceList = BLEDeviceManager.getMyTBTList(context)
        initRecyclerViews()
        populateConnectedDevices()
        if (!mIsConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    startScan()
                } else {
                  Toast.makeText(cContext,"Enable Nearby Permission from settings",Toast.LENGTH_LONG).show()
                }
            } else {
                startScan()
            }
        }
        applyClickListeners()
        return view
    }

    private fun initRecyclerViews() {
        val layoutManager = LinearLayoutManager(activity)
        val layoutManager2 = LinearLayoutManager(activity)

        layoutManager.orientation = RecyclerView.VERTICAL
        layoutManager2.orientation = RecyclerView.VERTICAL

        device_NotConnected_RecyclerView.layoutManager = layoutManager
        device_NotConnected_RecyclerView.setHasFixedSize(true)
        mOtherDevicesList = ArrayList()
        deviceNotConnectedAdapter = DeviceAdapter(mOtherDevicesList, otapFirestoreData, this, false)


        device_NotConnected_RecyclerView.adapter = deviceNotConnectedAdapter
        deviceNotConnectedAdapter.notifyDataSetChanged()
        device_NotConnected_RecyclerView.invalidate()


        device_RecyclerView?.layoutManager = layoutManager2
        device_RecyclerView?.setHasFixedSize(true)

    }

    private fun applyClickListeners() {
        device_NotConnected_RecyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (!mIsConnected) {
                    myTBTDeviceList = BLEDeviceManager.getMyTBTList(context)
                    if (myTBTDeviceList.size < 5) {
                        addressToConnect = ""
                        mListener.showScanningProgress()
                        if (mOtherDevicesList.size > 0) {
                            deviceInfo = mOtherDevicesList[position]
                            addressToConnect = mOtherDevicesList[position].address!!
                            mOtherDevicesList[position].address?.let { manager.connect(it, false) }
                        }
                    } else {
                        val bleDialog = Intent(BLEConstants.BLE_INTENT_SHOW_DIALOG)
                        bleDialog.putExtra(
                            BLEConstants.BLE_INTENT_DIALOG_TYPE,
                            BLEConstants.BLE_DEVICE_LIMIT_EXCEEDED
                        )
                        LocalBroadcastManager.getInstance(cContext).sendBroadcast(bleDialog)
                    }
                } else if (mIsConnected && !mPINAuthorised && addressToConnect == mOtherDevicesList[position].address) {
                    showBottomSheetPINDialog()
                } else if (mIsConnected && !mPINAuthorised && addressToConnect != mOtherDevicesList[position].address) {
                    disconnect()
                    mListener.showScanningProgress()
                    deviceInfo = mOtherDevicesList[position]
                    addressToConnect = mOtherDevicesList[position].address!!
                    mOtherDevicesList[position].address?.let { manager.connect(it, false) }
                }
            }
        })

        device_RecyclerView?.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (!mIsConnected && mBluetoothAdapter.isEnabled) {
                    val params = Bundle()
                    params.putString("eventCategory", "Tripper")
                    params.putString("eventAction", "Connect Device")
                    params.putString("eventLabel", "Connect Device Clicked")
                    REUtils.logGTMEvent(REConstants.KEY_TRIPPER_GTM, params)
                    mMyTBTDeviceClicked = true
                    manualDisconnectBase=false
                    BLEModel.getInstance().isManualDisconnect = true
                    mListener.showScanningProgress()
                    myTBTDeviceList[position].address?.let { manager.connect(it, false) }
                } else if (!mBluetoothAdapter.isEnabled) {
                    handleBluetooth(true)
                }
            }
        })

        bleRefresh.setOnClickListener {
            mIsAutoConnecting = false
            mMyTBTDeviceClicked = false
            mIsScanInitiatedConnection = false
            mIsManualDisconnect = false
            BLEModel.getInstance().isManualDisconnect = false
            manualDisconnectBase=false
            mRetryCount = 0
            devicesSet.clear()
            mOtherDevicesList = ArrayList()
            deviceNotConnectedAdapter =
                DeviceAdapter(mOtherDevicesList, otapFirestoreData, this, false)
            setNotConnectedRecyclerView()
            if (!mIsConnected) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.BLUETOOTH_SCAN
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startScan()
                    } else {
                        Toast.makeText(cContext,"Enable Nearby Permission from settings",Toast.LENGTH_LONG).show()
                    }
                } else {
                    startScan()
                }
            } else if (mIsConnected && !mPINAuthorised) {
                //                startScan()
            } else if (mIsConnected && mPINAuthorised) {
                activity?.runOnUiThread {
                    Toast.makeText(activity, "Already connected", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Set an checked change listener for switch button
        deviceSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            BLEDeviceManager.setAutoConnectUserPreference(context, isChecked)

        }
    }

    private fun setNotConnectedRecyclerView() {
        device_NotConnected_RecyclerView.adapter = deviceNotConnectedAdapter
        deviceNotConnectedAdapter.notifyDataSetChanged()
        device_NotConnected_RecyclerView.invalidate()
    }


    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (!mIsConnected && mBluetoothAdapter.isEnabled && !manualDisconnectBase) {
            mScanStatusText!!.visibility = VISIBLE
            if (deviceSwitch!!.isChecked)
                mScanStatusText?.setText(R.string.tbt_scan_status_switch_on)
            else
                mScanStatusText?.setText(R.string.tbt_scan_status_switch_off)
            if (!BLEModel.getInstance().isScanning) {
                onScanStart()
                manager.scan()
            }
        } else {
            if (!manualDisconnectBase){
                handleBluetooth(false)
                manager.stopScan(requireContext())
                disconnect()
            }else{
                handleBluetooth(true)}
            mScanStatusText!!.visibility = GONE
        }
    }

    private fun disconnect() {
        Thread(Runnable {
            manager.disconnect()
        }).start()
    }

    override fun onPinEnryCallback(pinEntry: String?) {
        val pinInput = pinEntry!!.toCharArray()

        val byteArray = ByteArray(20)
        byteArray[0] = 0X20

        byteArray[1] = pinInput[0].toByte()
        byteArray[2] = pinInput[1].toByte()
        byteArray[3] = pinInput[2].toByte()
        byteArray[4] = pinInput[3].toByte()
        byteArray[5] = pinInput[4].toByte()
        byteArray[6] = pinInput[5].toByte()

        val temp = ByteArray(18)
        for (i in 0..17) {
            temp[i] = byteArray[i]
        }

        val crc: ByteArray = CRCCalculator.calculateCRC(temp)

        byteArray[18] = crc[0]
        byteArray[19] = crc[1]
        messageByteArrayQueue.offer(byteArray)

    }

    override fun showHideInstallView(show: Boolean) {
        //showInstallLayout(show)
    }


    /**
     *
     */
    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("updateconnection".equals(intent.action)) {
//                Log.e("CHK","inside updateconnection BCR")
                var mBLEConnected = intent.getBooleanExtra("connected", false)
                var mAddress = intent.getStringExtra("deviceaddress")
                val mName = intent.getStringExtra("devicename")
                val status = intent.getIntExtra("status", -1)
                if (mAddress != null) {
                    updateConnectionState(mAddress, mBLEConnected, status)
                }
            }
        }
    }

    /**
     *
     */
    private val mOSVersionBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("didOSVersionReceived".equals(intent.action)) {
                var tripperOSVersion = intent.getStringExtra("Osversion")
                //deviceInfo.OSVersion = tripperOSVersion
                //request tripper serial number
                //requestCurrentTripperUniqueId()
                if (isForAddTripper) {
                    requestCurrentTripperUniqueId()
                }
                isForAddTripper = false
            }
        }
    }

    /**
     *
     */
    private val mSerialNumberBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("didSerialNumberReceived".equals(intent.action)) {
                var tripperUniqueId = intent.getStringExtra("SerialNo")

                //save to pref and call add api
                // var tripperOSVersion = prefs!!.getString("currentSoftwareVersion", "").toString()
                // var tripperUniqueId = prefs!!.getString("currentTripperUniqueId", "").toString()
                //println("TRRRR7"+"OS "+tripperOSVersion+" ID "+tripperUniqueId)

                deviceInfo.OSVersion = prefs!!.getString("currentSoftwareVersion", "").toString()
                if (tripperUniqueId != null) {
                    deviceInfo.serialNumber = tripperUniqueId
                }
                if (!myTBTDeviceList.contains(deviceInfo)) {
                    // After getting  OS version and uniqueID(serial number) save the tripper in preference
                    BLEDeviceManager.saveDeviceToMyTBTList(deviceInfo, context)
                    //New Api to call save device List to cloud database
                    saveTripperDeviceListtoCloud(deviceInfo)
                    // send broadcast to enable dynamic call notification after 6digit pin authentication
                    val dyn_callNotfn = Intent("DynamicCallNotifyToStart")
                    dyn_callNotfn.putExtra("TripperMacAddress", deviceInfo.address)
                    LocalBroadcastManager.getInstance(cContext).sendBroadcast(dyn_callNotfn)
                }
                //    Log.e("CHK","inside SerialNumber Received")
                if (mIsConnected) {
                    //        Log.e("CHK","inside SeR mIsconnected")
                    onDeviceConnected(deviceInfo)
                }
            }
        }
    }

    /**
     *
     */
    private val mPinAuthReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("pinAuth".equals(intent.action)) {
                deviceInfo = intent.getStringExtra("deviceAddress")?.let {
                    DeviceInfo(
                        "ANDROID",
                        "N", intent.getBooleanExtra("auth", false),
                        true,
                        intent.getStringExtra("deviceName"),
                        it, 0, randomUUID, "", guid, ""
                    )
                }!!
                if (!manualDisconnectBase) {
                updatePINAuthorization(intent.getBooleanExtra("auth", false), deviceInfo)
                }else{
                    startScan()
            }
        }

        }
    }

    private val mScanStopReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("scanStop".equals(intent.action)) {
                if (myTBTDeviceList.size > 0 && !mIsConnected) {
                    mScanStatusText?.setText(R.string.tbt_no_device_found)
                } else if (mOtherDevicesList.size == 0 && !mIsConnected) {
                    if (deviceSwitch!!.isChecked)
                        mScanStatusText?.setText(R.string.tbt_scan_status_switch_on_timeout)
                    else
                        mScanStatusText?.setText(R.string.tbt_scan_status_switch_off_timeout)
                } else {
                    mScanStatusText?.visibility = GONE
                }
            }
        }
    }

    private val mScanFailReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (BLEConstants.BLE_SCAN_FAILED.equals(intent.action)) {
                onScanInitFailed()
            }
        }
    }

    private val mScanResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (BLEConstants.BLE_INTENT_SCAN_RESUlTS.equals(intent.action)) {
                mIsScanFailed = false
                val dev = intent.getStringExtra(BLEConstants.BLE_DEVICE_ADDRESS)?.let {
                    DeviceInfo(
                        "ANDROID",
                        "N", false, false, intent.getStringExtra(
                            BLEConstants.BLE_DEVICE_NAME
                        ),
                        it, 0, randomUUID, "", guid, ""
                    )
                }
                dev?.let { onLeScanResult(it) }
            }
        }
    }

    fun onLeScanResult(deviceInfo: DeviceInfo) {
        if (!devicesSet.contains(deviceInfo)) {
            devicesSet.add(deviceInfo)
            updateDevice(deviceInfo)
        }
    }

    //This receiver is sent from ServiceCancelConfirmationActivity in REPrime App
    private val mUserResponseReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (REConstants.BLE_USER_RESPONSE.equals(intent.action)) {
                    val type = intent.getStringExtra("type")
                    val response = intent.getBooleanExtra("actionResponse", false)
                    val position = intent.getIntExtra(BLEConstants.BLE_DEVICE_POSITION, -1)
                    if (response) {
                        mIsManualDisconnect = true
                        manualDisconnectBase = true
                        BLEModel.getInstance().isManualDisconnect = true
                        if (type != null && type.equals(BLEConstants.BLE_DEVICE_FORGET)) {
                            mPosition = position
                            mIsForgetClicked = true
                            BLEModel.getInstance().isManualDisconnect = true
                            myTBTDeviceList = BLEDeviceManager.getMyTBTList(context)
                            if (myTBTDeviceList.size > 0) {
                                if (mIsConnected && myTBTDeviceList[position].isConnected) {
                                    disconnectDevice()
                                } else {
                                    mIsForgetClicked = false
                                    //New api for Remove Tripper from Cloud database
                                    // Log.e("remove","");
                                    OtapInteractor().removeTripperFromCloud(
                                        RemoveTripperDeviceApiRequest(
                                            myTBTDeviceList.get(mPosition).userGUID,
                                            myTBTDeviceList.get(mPosition).deviceToken,
                                            myTBTDeviceList.get(mPosition).serialNumber
                                        )
                                    )
                                    myTBTDeviceList.removeAt(mPosition) //need to check in multiple devices for crash.
                                    BLEDeviceManager.updateMyTBTDevice(myTBTDeviceList, context)
                                    populateConnectedDevices()
                                }
                            }
                        } else if (type != null && type.equals(BLEConstants.BLE_DEVICE_DISCONNECT)) {
                            mIsForgetClicked = false
                            mIsManualDisconnect = true
                            manualDisconnectBase = true
                            BLEModel.getInstance().isManualDisconnect = true
                            disconnectDevice()
                        }
                    }
                }
            } catch (e: Exception) {
                RELog.e(DeviceListFragment.javaClass.simpleName, e.message);
            }
        }
    }

    // method to get TripperOS Version
    private fun requestCurrentTripperOSVersion() {
        BlockSegmentUtils(activity).sendSoftwareVersionMessage();
        println("TRRRR -#$ os")
    }

    // method to get TripperUniqueId Version
    private fun requestCurrentTripperUniqueId() {
        BlockSegmentUtils(activity).sendTripperUniqueIdMessage();
        println("TRRRR -#$ id")

    }

    private fun saveTripperDeviceListtoCloud(t: DeviceInfo) {

        var data = OtapSaveDeviceinfoApiRequest(
            t.deviceToken,
            t.serialNumber,
            t.userGUID,
            t.OSVersion,
            t.status,
            t.mobileDeviceOS,
            t.name,
            t.address,
            "Active",
            "NA",
            "",
            ""
        )
        // Api call to Add Tripper to Cloud database
        OtapInteractor().saveDeviceListFromPreferenceApi(data)
    }
}

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}

private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {


    class OnChildItemAttachStateChangeListener : RecyclerView.OnChildAttachStateChangeListener {

        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    }

    this.addOnChildAttachStateChangeListener(OnChildItemAttachStateChangeListener())
}

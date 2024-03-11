package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.royalenfield.reprime.R
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.interactor.FindMyMotorcycleInteractor
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.ConnectedResponse
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response.DeviceLatestLocation
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.presenter.FindMyMotorcyclePresenter
import com.royalenfield.reprime.ui.home.connected.locatemotorcycle.socket.ConnectedServiceInterface
import com.royalenfield.reprime.ui.home.connected.motorcycle.fragment.VehicleSettingsFragment
import com.royalenfield.reprime.ui.home.connected.motorcycle.model.SettingResponseModel
import com.royalenfield.reprime.ui.home.homescreen.fragments.MotorCycleFragment
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.RELocation
import com.royalenfield.reprime.utils.REUtils
import com.royalenfield.reprime.utils.REUtils.logConnected

private const val ARG_MODEL_CODE = "model_code"
private const val ARG_CHESSIS_NUMBER = "chessis_number"
private const val ARG_REGISTRATON_NUMBER = "registration_number"
private const val ARG_DEVICE_SETTINGS = "device_settings"

class FindMyMotorcycleFragment : REBaseFragment(), OnMapReadyCallback, View.OnClickListener,
    ConnectedServiceInterface {

    private lateinit var runnable: Runnable
    private var mMarker: Marker? = null
    private lateinit var handler: Handler
    private var deviceId: String = ""
    private lateinit var motorcyclePresenter: FindMyMotorcyclePresenter
    private var gMap: GoogleMap? = null
    //   private lateinit var compositeDisposable: Disposable
    private lateinit var latestAddress: String
    private var deviceLatestLocation: DeviceLatestLocation? = null
    private var ignitionStatus: Boolean = false
    private var mapFragment: SupportMapFragment? = null
    private lateinit var txtBikeModel: TextView
    private lateinit var txtIgnitionStatus: TextView
    private lateinit var imgIgnitionIcon: ImageView
    private lateinit var backImage: ImageView
    private lateinit var txtAddress: TextView
    private lateinit var txtLastLocationUpdatedTime: TextView
    private lateinit var txtLocateMotorcycle: TextView
    private lateinit var txtInfoSetting: TextView
    private lateinit var llIgnition: LinearLayout
    private var deviceSettings: SettingResponseModel? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var prevLatitude: Double = 0.0
    private var prevLongitude: Double = 0.0
    private var lastIgnitionStatus: Boolean = true
    private var lastIgnitionTime: Long = 0
    private lateinit var llVehicleSettingContainer : LinearLayout
    private  lateinit var tvHeader: TextView
    private var chessisNumber: String? = null
    private var modelName: String? = null
    private var registrationNumber: String? = null
    companion object {
        @JvmStatic
        fun newInstance(
            chessisNumber: String?,
            modelName: String?,
            registrationNumber: String?,
            deviceSetting: SettingResponseModel?
        ): FindMyMotorcycleFragment =
            FindMyMotorcycleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MODEL_CODE, modelName)
                    putString(ARG_CHESSIS_NUMBER, chessisNumber)
                    putString(ARG_REGISTRATON_NUMBER, registrationNumber)
                    putSerializable(ARG_DEVICE_SETTINGS, deviceSetting)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chessisNumber = it.getString(ARG_CHESSIS_NUMBER) as String?
            registrationNumber = it.getString(ARG_REGISTRATON_NUMBER) as String?
            modelName = it.getString(ARG_MODEL_CODE) as String?
            deviceSettings = it.getSerializable(ARG_DEVICE_SETTINGS) as SettingResponseModel?
            //   REApplication.getInstance().reConnectedAPI.deviceId = vehicleData?.deviceId.toString()
            //deviceId = "it_170800732"//"it_170809688"//"it_170804872"//170820342//170804872//170809677
            deviceId = deviceSettings?.deviceSerialNumber.toString()
        }
        hideREHeaderFromActivity()
    }

    private fun hideREHeaderFromActivity() {
        (activity as REHomeActivity).showHideREHeader(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_find_my_motorcycle, container, false)
        motorcyclePresenter = FindMyMotorcyclePresenter(this, FindMyMotorcycleInteractor())
        initViews(mView)
        val paramsScr = Bundle()
        paramsScr.putString("screenname", "Connected Locate")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, paramsScr)
        return mView
    }

    private fun initViews(mView: View) {
        //ignitionStatus = true
        handler = Handler()
        initiateRunnable()
         tvHeader = mView.findViewById<TextView>(R.id.tv_search_banner)
        tvHeader.text = getString(R.string.moto_buddy)
        //tvHeader.isAllCaps = true
        //tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.sp_16));
        latestAddress = getString(R.string.text_dummy_address1)
        getCurrentLocationData()
        txtBikeModel = mView.findViewById(R.id.txtBikeModel)
        txtIgnitionStatus = mView.findViewById(R.id.txtIgnitionStatus)
        imgIgnitionIcon = mView.findViewById(R.id.imgIgnitionIcon)
        txtAddress = mView.findViewById(R.id.txtAddress)
        txtAddress.setMovementMethod(ScrollingMovementMethod());
        txtLastLocationUpdatedTime = mView.findViewById(R.id.txtLastLocationUpdatedTime)
        txtLocateMotorcycle = mView.findViewById(R.id.txtLocateMotorcycle)
        llIgnition = mView.findViewById(R.id.llIgnition)
        txtInfoSetting = mView.findViewById(R.id.txtInfoSetting)
        llVehicleSettingContainer = mView.findViewById(R.id.vehicle_settings_container)

        txtLocateMotorcycle.setOnClickListener(this)
        backImage = mView.findViewById<ImageView>(R.id.back_image)
        backImage.setOnClickListener(this)

        /*set bike model*/
        txtBikeModel.text = modelName?: ""

        /*create dummy data*/
        //createDummyModelData()

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        /*set setting view*/
        setLocateMotorcycleView()
    }

    private fun initiateRunnable() {
        runnable = Runnable { this.sendEchoForData() }
    }

    private fun sendEchoForData() {
      //  if (motorcyclePresenter.stompClient.isConnected && !deviceId.isEmpty())
           // motorcyclePresenter.getDataInEveryTenSec(deviceId)
       // else
            motorcyclePresenter.fetchDeviceData(deviceId,false)
    }

    private fun setLocateMotorcycleView() {
        if (deviceSettings != null) {
            if (deviceSettings!!.locationAccessFlag) {
                motorcyclePresenter.fetchDeviceData(deviceId,true)
                REUtils.countDownTimer(this, 0, this::showConnectionLostDialog)
            }
            setLocationSettingView(!deviceSettings!!.locationAccessFlag)
        }
    }

    private fun showConnectionLostDialog(type: Int) {
        logConnected("getDeviceLatestLocation",deviceId,chessisNumber);
        REUtils.showConnectionLostDialog(activity, object : REUtils.OnDialogClickListener {
            override fun onOkCLick() {
                setLocateMotorcycleView()
            }

            override fun onCancelCLick() {
                onClick(backImage)
            }
        })
    }

    private fun getCurrentLocationData() {
        val currentLocation = REApplication.getInstance().reLocationInstance.location

        if (currentLocation != null) {
            latitude = currentLocation.latitude
            longitude = currentLocation.longitude
        }
    }

    private fun getLastUpdatedTime(): Long {
//        var removeTime = 0L
//        if (!getIgnitionStatus()) {
//            removeTime = getTimeDifference() //(32 * 60 * 1000)
//        }
        //return System.currentTimeMillis() - removeTime
        val l: Long = if (deviceLatestLocation?.timestamp != null) deviceLatestLocation?.timestamp!!.toLong() else -1
        return l
    }

    override fun onResume() {
        super.onResume()
        hideREHeaderFromActivity()
    }

    override fun onDetach() {
        super.onDetach()
        motorcyclePresenter.disconnectStompClient()
        (activity as REHomeActivity).showHideREHeader(true)
        (parentFragment as MotorCycleFragment?)?.onResume()
    }

    private fun setLocationSettingView(showLocationView: Boolean) {
        txtLocateMotorcycle.text = if (showLocationView)
            getString(R.string.text_my_motorcycle_settings)
        else
            getString(R.string.text_walk_to_my_motorcycle)

        llIgnition.visibility = if (showLocationView) View.GONE else View.VISIBLE
        txtInfoSetting.visibility = if (showLocationView) View.VISIBLE else View.GONE
        if (mMarker != null) {
            mMarker!!.isVisible = !showLocationView
        }

        mapFragment?.view?.alpha = if (showLocationView) 0.3f else 1f
        txtLocateMotorcycle.setOnClickListener(if (showLocationView) this else null)
        setLocateButtonColor(!showLocationView)
    }

    override fun onClick(view: View?) {
        if (view!!.id == R.id.txtLocateMotorcycle) {
            val params = Bundle()
            params.putString("eventCategory", "Connected Module")
            params.putString("eventAction", "Locate")
            params.putString("eventLabel", txtLocateMotorcycle.text.toString())
            params.putString("modelName", txtBikeModel.text.toString())
            REUtils.logGTMEvent(REConstants.KEY_CONNECTED_MODULE_GTM, params)

            if (txtLocateMotorcycle.text.equals(getString(R.string.text_my_motorcycle_settings))) {
                loadFragment(VehicleSettingsFragment.getInstance(this, modelName,registrationNumber, deviceSettings))
            } else {
                //Toast.makeText(context, "Navigate to map", Toast.LENGTH_SHORT).show()
                navigateToMap()
            }
        } else if (view.id == R.id.back_image) {
                activity?.onBackPressed()
        }
    }
    
    private fun navigateToMap() {
        val latitude = this.latitude
        val longitude = this.longitude
        val label = latestAddress
        val uriBegin = "geo:$latitude,$longitude"
        val query = "$latitude,$longitude($label)"
        val encodedQuery = Uri.encode(query)
        val uriString = "$uriBegin?q=$encodedQuery&z=16"
        val uri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(mapIntent)
    }

    private fun setGestureEnable(gMap: GoogleMap?) {
        gMap!!.uiSettings.setScrollGesturesEnabled(true)
        gMap.uiSettings.setTiltGesturesEnabled(true)
        gMap.uiSettings.setZoomGesturesEnabled(true)
    }

    private fun getIgnitionStatus(): Boolean {
        return ignitionStatus
    }

    private fun setIgnitionStatus(value: Boolean) {
        ignitionStatus = value /*set static false for ignition off case*/
    }

    private fun getBitmap(value: Boolean): Bitmap? {
//        val resId = if (value) R.drawable.ic_ignition_on_dot_68 else R.drawable.ic_ignition_off_12
//        return REUtils.getBitmapFromVectorDrawable(context, resId)
        val resId = if (value) R.drawable.ic_bike_green else R.drawable.ic_bike_red
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    private fun createDummyModelData() {
        deviceLatestLocation = Gson().fromJson(
            REUtils.loadJSONFromAsset(
                REApplication.getAppContext(),
                "device_latest_location.json"
            ), ConnectedResponse::class.java
        )
            .data?.deviceLatestLocation
        //updateUIWithLatestData(deviceLatestLocation)
    }

    private fun updateUIWithLatestData(deviceLatestLocation: DeviceLatestLocation?) {
        if (deviceLatestLocation == null) {
            return
        }
       if (deviceLatestLocation.vehicleNumber.equals(chessisNumber)) {
            prevLatitude = latitude
            prevLongitude = longitude

            latitude = deviceLatestLocation.latitude //28.4855133
            longitude = deviceLatestLocation.longitude//77.0654595

            //getAddressFromLatLong(latitude, longitude)
            latestAddress = deviceLatestLocation.address
            setIgnitionStatus(!deviceLatestLocation.haltStatus)

            /*set static data*/
            if (isSafeFragment(this)) {
                setMotorCycleData()
            }
            if (REApplication.CODE_STUB_DEMO) {
                setMotorCycleData()
            } else {
                updateMapData()
            }
        }
    }

    private fun updateMapData() {
        val bearingPoint: Float = RELocation.getBearing(
            LatLng(prevLatitude, prevLongitude),
            LatLng(latitude, longitude)
        )

        val distance: Double =
            RELocation.distanceBetweenTwoLocations(prevLatitude, prevLongitude, latitude, longitude)

        if (mMarker != null) {
            //mMarker!!.position = LatLng(latitude, longitude)
            mMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmap(getIgnitionStatus())!!))
        }

        if (!bearingPoint.isNaN()) {
            //RELocation.rotateMarker(false, mMarker, bearingPoint - 25)
            mMarker!!.rotation = (bearingPoint - 25)
        }

        if (distance > 10.0) {
            mMarker?.setPosition(LatLng(latitude, longitude))
        } else {
            RELocation.animateMarker(gMap, mMarker, LatLng(latitude, longitude))
        }

//        if (!(gMap?.getProjection()?.getVisibleRegion()?.latLngBounds)!!.contains(
//                LatLng(
//                    latitude,
//                    longitude
//                )
//            )
//        ) {
            gMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude, longitude), gMap?.cameraPosition!!.zoom
                )
            )
       // }


    }

    @SuppressLint("NewApi")
    private fun setMotorCycleData() {
        //txtIgnitionStatus.text = getString(R.string.text_ignition_on)
        txtAddress.text = latestAddress
        //txtAddress.tooltipText = latestAddress
        txtLastLocationUpdatedTime.text = getString(
            R.string.text_last_updated_time,
            REUtils.getDate(REUtils.FULL_DATE_FORMAT, getLastUpdatedTime())
        )

        /*set ignition status*/
        setIgnition(getIgnitionStatus())
    }

    fun isSafeFragment(frag: Fragment): Boolean {
        return !(frag.isRemoving() || frag.getActivity() == null || frag.isDetached() || !frag.isAdded() || frag.getView() == null)
    }

    private fun setIgnition(value: Boolean) {
        txtIgnitionStatus.keepScreenOn = value
        txtIgnitionStatus.text = if (value) getString(R.string.text_ignition_on)
        else getString(
            R.string.text_ignition_off
        )
        val resDrawableId = if (value) R.drawable.ic_ignition_on_dot else R.drawable.ic_ignition_off
        imgIgnitionIcon.setImageDrawable(resources.getDrawable(resDrawableId))
        val resColorId = if (value) R.color.color_light_green else R.color.color_light_red
        txtIgnitionStatus.setTextColor(resources.getColor(resColorId))
        txtLocateMotorcycle.setOnClickListener(if (value) null else this)
        tvHeader.text= if (value) getString(R.string.wheel_spooter)
        else getString(
            R.string.moto_buddy
        )
        setLocateButtonColor(value)
    }

    private fun setLocateButtonColor(value: Boolean) {
        val resTextColorId =
            if (value) R.color.text_color_light_orange else R.color.colorSelectedTab
        txtLocateMotorcycle.setTextColor(resources.getColor(resTextColorId))
    }

    private fun getAddressFromLatLong(latitude: Double, longitude: Double) {
        val add = REUtils.getCurrentAddressList(activity, this.latitude, this.longitude)
        if (add.size > 0) {
            latestAddress = add.get(0).featureName.plus(", ").plus(add.get(0).subAdminArea)
        }
    }

    private fun getTimeDifference(): Long {
        /*use lastIgnitionTime for actual ignition off*/
        if (deviceLatestLocation?.lastIgnitionTime == null) {
            return 0
        }
        val timeDiff =
            (System.currentTimeMillis() / 1000) - deviceLatestLocation?.lastIgnitionTime!!.toLong()
        return timeDiff
    }

    private fun getDifferenceTimeFromSeconds(timeDiff: Long): String {
        val day = 86400
        val hour = 3600
        val minute = 60

        var diff = timeDiff

        val numOfDays = (diff / day)
        diff = diff % day
        val hours = (diff / hour)
        diff = diff % hour
        val minutes = (diff / minute)
        diff = diff % minute
        val seconds = diff

        if (numOfDays >= 1) {
            return getString(R.string.time_ago, getDays(numOfDays))
        } else if (hours >= 1) {
            return getString(R.string.time_ago, getTimeHistory(hours, minutes, seconds))
        } else if (minutes >= 1) {
            return getString(R.string.time_ago, getMinSecHistory("", minutes, seconds))
        } else if (seconds >= 1) {
            return getString(R.string.time_ago, getSecHistory("", seconds))
        }
        return getString(R.string.time_ago, getSecHistory("", 1))
    }

    private fun getDays(numOfDays: Long): String {
        var historyTime = ""
        historyTime = historyTime.plus(getString(R.string.no_of_day, numOfDays.toString()))
        if (numOfDays > 1) historyTime = historyTime.plus("s")
        return historyTime
    }

    private fun getTimeHistory(hours: Long, minutes: Long, seconds: Long): String {
        var historyTime = getString(R.string.no_of_hour, hours.toString())
        if (hours > 1) historyTime = historyTime.plus("s ") else historyTime = historyTime.plus(" ")
        return getMinSecHistory(historyTime, minutes, seconds)
    }

    private fun getMinSecHistory(time: String, minutes: Long, seconds: Long): String {
        var historyTime = time
        historyTime = historyTime.plus(getString(R.string.no_of_minute, minutes.toString()))
        if (minutes > 1) historyTime = historyTime.plus("s ") else historyTime =
            historyTime.plus(" ")
        return getSecHistory(historyTime, seconds)
    }

    private fun getSecHistory(value: String, seconds: Long): String {
        var historyTime = value
        historyTime = historyTime.plus(getString(R.string.no_of_sec, seconds.toString()))
        if (seconds > 1) historyTime = historyTime.plus("s")
        return historyTime
    }

    override fun serviceSuccess(payload: ConnectedResponse) {
        if (REUtils.countDownTimer != null) {
            REUtils.countDownTimer.cancel()
        }
        deviceLatestLocation = payload.data?.deviceLatestLocation
        if (REApplication.CODE_STUB_DEMO && deviceLatestLocation == null) {
            deviceLatestLocation = Gson().fromJson(
                REUtils.loadJSONFromAsset(
                    REApplication.getAppContext(),
                    "device_latest_location.json"
                ), ConnectedResponse::class.java
            )
                .data?.deviceLatestLocation
        }
        /*update UI data*/

        if (REApplication.CODE_STUB_DEMO) {
            updateUIWithLatestData(deviceLatestLocation)
        } else {
            activity?.runOnUiThread {
                if (this.isVisible) {

                    updateUIWithLatestData(deviceLatestLocation)
                    fetchDataAfterTenSec()
                }
            }
        }
    }

    private fun fetchDataAfterTenSec() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(
            runnable,
            Integer.parseInt(REUtils.getConnectedFeatureKeys().otherRefreshInterval) * 1000L
        )
    }

    override fun serviceFails(errorMsg: String) {
        if (errorMsg.equals("2")) {
            Toast.makeText(context, "Stomp connection error", Toast.LENGTH_SHORT).show()
            onClick(backImage)
        }
    }

    fun updateDeviceSettingData(deviceSetting: SettingResponseModel) {
        deviceSettings = deviceSetting
        setLocateMotorcycleView()
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
//        val transaction = fragmentManager!!.beginTransaction()
//        transaction.setCustomAnimations(
//            R.anim.slide_in_right,
//            R.anim.slide_out_left,
//            0,
//            R.anim.slide_out_right
//        )
//        transaction.add(R.id.frame_container_home, fragment)
//        transaction.addToBackStack(null)
//        transaction.commitAllowingStateLoss()

        val fragmentManager = fragmentManager
        if (fragmentManager != null) {
            val fragTrans = fragmentManager.beginTransaction()
            fragTrans.add(R.id.vehicle_settings_container, fragment)
            fragTrans.addToBackStack(null)
            fragTrans.commit()
        }

    }

    override fun onMapReady(gMap: GoogleMap) {
        this.gMap = gMap
        //gMap?.clear()
        gMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude), 14f
            )
        )
        mMarker = gMap?.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(getIgnitionStatus())!!))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(LatLng(latitude, longitude))
                .flat(true)
        )
        if (deviceSettings != null) {
            mMarker!!.isVisible = deviceSettings!!.locationAccessFlag
        }

        /*Gesture enable for moving and zooming map*/
        setGestureEnable(gMap)
    }
}

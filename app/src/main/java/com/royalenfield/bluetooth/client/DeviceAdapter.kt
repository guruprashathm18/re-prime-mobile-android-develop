package com.royalenfield.bluetooth.client

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.RecyclerView
import com.royalenfield.bluetooth.ble.DeviceInfo
import com.royalenfield.bluetooth.DeviceSettingsActivity
import com.royalenfield.reprime.R
import java.io.Serializable


class DeviceAdapter(
    private val devices: MutableList<DeviceInfo>,
    private var mOtapMapData: Map<String, Object>,
    private val listener: DeviceListContract.OnDisconnectClickListener,
    private val isMyTBTDeviceList: Boolean
) :
    RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private lateinit var navDeviceStatusIcon: ImageView
    private lateinit var deviceName: TextView
    private lateinit var navDeviceDisconnect: ImageView
    private lateinit var imageViewSettings: ImageView

    class DeviceViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_card, parent, false)
        navDeviceStatusIcon = view.findViewById(R.id.nav_device_status_icon)
        deviceName = view.findViewById(R.id.device_name)
        navDeviceDisconnect = view.findViewById(R.id.nav_device_disconnect)
        imageViewSettings = view.findViewById(R.id.imageView_settings)
        return DeviceViewHolder(view)
    }


    override fun getItemCount(): Int {

        return devices.size

    }

    override fun onBindViewHolder(holders: DeviceViewHolder, position: Int) {
        try {
            with(holders.view) {
                if (devices[position].isConnected) {
                    Log.e("test", "device connected " + devices[position].name)
                }
                Log.e("DeviceScanCallback", "Adapter called :")
                deviceName.text = devices[position].name
                val mDeviceForget = navDeviceDisconnect as ImageView
                val mDeviceDisconnect = navDeviceStatusIcon as ImageView
                mDeviceForget.setOnClickListener {
                    if (listener != null) {
                        listener.onDisconnectClicked(true, position)
                    }
                }
                imageViewSettings.setOnClickListener {
                    val intent = Intent(context, DeviceSettingsActivity::class.java)
                    intent.putExtra("devicename", devices[position].name)
                    intent.putExtra("connectionstatus", devices[position].isConnected)
                    intent.putExtra("otapmap", mOtapMapData as Serializable?)
                    intent.putExtra("deviceindexinArraylist", position.toString())
                    context.startActivity(intent)
                }
                if (devices[position].isConnected) {
                    navDeviceDisconnect.visibility = View.VISIBLE
                    mDeviceDisconnect.visibility = View.VISIBLE
                    imageViewSettings.visibility = View.VISIBLE
                    mDeviceDisconnect.setImageResource(R.drawable.ic_device_connected)
//                    setGuidelinePercentage(guideLine, View.VISIBLE)
//                    myView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick_close, 0)
                    mDeviceDisconnect.setOnClickListener {
                        if (listener != null) {
                            listener.onDisconnectClicked(false, position)
                        }
                    }
//                    imageView_settings.setOnClickListener {
//                        val context = holders.view.context
//                        val intent = Intent(context, DeviceSettingsActivity::class.java)
//                        intent.putExtra("devicename", devices[position].name)
//                        intent.putExtra("connectionstatus", devices[position].isConnected);
//                        intent.putExtra("otapmap", mOtapMapData as Serializable?)
//                        context.startActivity(intent)
//                    }

                } else {
                    if (isMyTBTDeviceList) {
                        imageViewSettings.visibility = View.VISIBLE
                        navDeviceDisconnect.visibility = View.VISIBLE
                        mDeviceDisconnect.visibility = View.VISIBLE
                        mDeviceDisconnect.setImageResource(R.drawable.ic_device_disconnected)
                        mDeviceDisconnect.setOnClickListener {
                        }
                    } else {
                        mDeviceDisconnect.visibility = View.GONE
                        imageViewSettings.visibility = View.GONE
                        navDeviceDisconnect.visibility = View.INVISIBLE
                    }
//                    setGuidelinePercentage(guideLine, View.GONE)
//                    myView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_name, 0)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            e.stackTrace
        }
    }

}

/**
 * to set guideline percentage dyanamically
 *
 * @param i
 */
private fun setGuidelinePercentage(guideLine: Guideline, i: Int) {
    val params = guideLine.getLayoutParams() as ConstraintLayout.LayoutParams
    if (i == View.GONE) {
        params.guidePercent = 0.95f
        guideLine.setLayoutParams(params)
    } else {
        params.guidePercent = 0.85f
        guideLine.setLayoutParams(params)
    }
}


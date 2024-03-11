package com.royalenfield.bluetooth.ble
import android.Manifest
import android.bluetooth.BluetoothGatt
import android.content.Context
import androidx.annotation.RequiresPermission
import com.royalenfield.bluetooth.ble.BleManager
import com.royalenfield.bluetooth.ble.OnConnectionChange
import java.util.*

interface IBleManager {
    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun scan()
    fun connect(
        address: String, autoConnect: Boolean
    )
    fun setOTAPListener(connectionChange: OnConnectionChange)
    fun openGattServer(context: Context)
    fun disconnect()
    fun stopScan(context: Context)
    fun start()
    fun getCRC(bytes: ByteArray) : ByteArray
    fun sendNextMsg(byteArray : ByteArray)
    fun getQueue() : Queue<ByteArray>
    fun onCommandRecieved(command: ByteArray)
    fun showHideViews(show:Boolean)
    fun processData(reply: ByteArray,bleManager: BleManager)

    fun displayOtapProgress(status:Boolean, completed:Int,  total:Int)
    fun progressStatus(progressStatus:Int)

    fun onBluetoothDisconnected(gatt: BluetoothGatt, status: Int, newState: Int)
}
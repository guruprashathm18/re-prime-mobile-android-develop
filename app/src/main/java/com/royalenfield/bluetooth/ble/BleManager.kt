package com.royalenfield.bluetooth.ble

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.ParcelUuid
import androidx.annotation.WorkerThread
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.royalenfield.bluetooth.ble.GattCallback.DISPLAY_UNIT_UUID
import com.royalenfield.bluetooth.ble.GattCallback._DISPLAY_UNIT_UUID_
import com.royalenfield.bluetooth.client.CRCCalculator
import com.royalenfield.bluetooth.otap.OtapProcessDisplayResponse
import com.royalenfield.bluetooth.utils.BLEDeviceManager
import com.royalenfield.reprime.application.REApplication
import com.royalenfield.reprime.utils.RELog
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue


class BleManager : IBleManager {

    var characteristicToWrite: BluetoothGattCharacteristic? = null
    lateinit var mBluetoothGatt: BluetoothGatt
    private var conCallback: OnConnectionChange? = null
    lateinit var deviceScanCallback: DeviceScanCallback
    var gattCallback = GattCallback(this)
    var gattServerCallback = BluetoothGattServerCallback(this)
    private val RE_SERVICE_UUID = ParcelUuid.fromString("01FF0100-BA5E-F4EE-5CA1-EB1E5E4B1CE0")

    lateinit var mContext: Context
    lateinit var mBluetoothManager: BluetoothManager
    lateinit var mBluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothLeScanner: BluetoothLeScanner

    private var addressToConnect: String? = null
    private lateinit var device: BluetoothDevice
    lateinit var handler: Handler

    companion object {
        val messageByteArrayQueue: Queue<ByteArray> = ConcurrentLinkedQueue<ByteArray>()
    }

    var MSG_DELIVERED = true
    var isDeviceReadyToReceive = false

    override fun start() {

        if (!thread.isAlive) {
            thread.start()
        }
    }

    fun setup() {
        try {
            mContext = REApplication.getAppContext()
            mBluetoothManager =
                mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            mBluetoothAdapter = mBluetoothManager.adapter
            if (mBluetoothAdapter.bluetoothLeScanner != null)
                bluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner
        } catch (e: Exception) {
            RELog.e("setup: ", "setup>>>>>>>>" + e.printStackTrace())
        }
    }

    @WorkerThread
    override fun connect(address: String, autoConnect: Boolean) {
        setup()
        addressToConnect = address
        device = getDeviceForAddress(address)
        //val isAutoconnectEnabled = BLEDeviceManager.isAutoConnectEnabled(mContext)
        //Log.e("AUtoconnect val is ",""+isAutoconnectEnabled);
        if (!BLEModel.getInstance().isDeviceConnected && mBluetoothAdapter.isEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            mBluetoothGatt = device.connectGatt(
                mContext, autoConnect, gattCallback,
                BluetoothDevice.TRANSPORT_LE
            )
            BLEModel.getInstance().bluetoothGatt = mBluetoothGatt
        }
    }


    private fun openGattServer() {
        gattServerCallback = BluetoothGattServerCallback(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        val mGattServer: BluetoothGattServer = mBluetoothManager.openGattServer(
            mContext.applicationContext,
            gattServerCallback
        )
        // Add a service for a total of three services (Generic Attribute and Generic Access
        // are present by default).

        val reCharcterstic = BluetoothGattCharacteristic(
            DISPLAY_UNIT_UUID,
            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        val service_wireless_uart = BluetoothGattService(
            _DISPLAY_UNIT_UUID_,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )
        service_wireless_uart.addCharacteristic(reCharcterstic)
        mGattServer.addService(service_wireless_uart)
        BLEModel.getInstance().bluetoothGattServer = mGattServer
    }

    override fun disconnect() {
        setup()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        if (BLEModel.getInstance().bluetoothGatt != null) {
            BLEModel.getInstance().bluetoothGatt.disconnect()
        }
        thread.interrupt()
    }

    override fun scan() {
        mContext = REApplication.getAppContext()
        mBluetoothManager =
            mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager.adapter
        if (!BLEModel.getInstance().isScanning) {
            setup()
            deviceScanCallback = DeviceScanCallback()
            startScan()
        }
    }

    private fun startScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        handler = Handler()
        handler.postDelayed({
            //Do something after 60000ms
            stopScan(mContext)
            handler.removeCallbacksAndMessages(null)
        }, 60000)
        BLEModel.getInstance().isScanning = true
        bluetoothLeScanner.startScan(
            buildScanFilters(),
            buildScanSettings(),
            deviceScanCallback
        )
    }

    override fun stopScan(context: Context) {
        try {
            setup()
            if (mBluetoothAdapter.isEnabled) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.BLUETOOTH_SCAN
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                }
                bluetoothLeScanner.stopScan(deviceScanCallback)
                bluetoothLeScanner.flushPendingScanResults(deviceScanCallback)
                BLEModel.getInstance().isScanning = false
                val scanStop = Intent("scanStop")
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(scanStop)
            } else {
                val scanStop = Intent("scanStop")
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(scanStop)
            }
            handler.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
        }
    }

    override fun getCRC(bytes: ByteArray): ByteArray {
        var crc = 0xFFFF          // initial value
        val polynomial = 0x1021   // 0001 0000 0010 0001  (0, 5, 12)


        for (b in bytes) {
            for (i in 0..7) {

                var bb = b.toInt()
                val bit = ((bb shr 7 - i)) == 1

                val c15 = crc shr 15 and 1 == 1
                crc = crc shl 1
                if (c15 xor bit) crc = crc xor polynomial
            }
        }

        crc = crc and 0xffff
        val b = ByteBuffer.allocate(4)
        b.putInt(crc)
        val crcArray = b.array()

        val ret = ByteArray(2)
        ret[0] = crcArray[2]
        ret[1] = crcArray[3]
        return ret
    }

    override fun getQueue(): Queue<ByteArray> {
        return messageByteArrayQueue
    }

    override fun onCommandRecieved(command: ByteArray) {
        val bleAuth = Intent("pinAuth")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        if (command[0].toInt() == 0X20) {

            if (command[1].toInt() == 0X01) {
                sendTimeMsg()
                bleAuth.putExtra("auth", true)

                bleAuth.putExtra("deviceName", BLEModel.getInstance().bluetoothGatt.device.name)
                bleAuth.putExtra(
                    "deviceAddress",
                    BLEModel.getInstance().bluetoothGatt.device.address
                )
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(bleAuth)
                //This broadcast is for shwoing device pairing popup
                val devicePaired = Intent("devicePaired")
                mContext.let {
                    LocalBroadcastManager.getInstance(it).sendBroadcast(devicePaired)
                }
            } else {
                bleAuth.putExtra("auth", false)
                bleAuth.putExtra("deviceName", BLEModel.getInstance().bluetoothGatt.device.name)
                bleAuth.putExtra(
                    "deviceAddress",
                    BLEModel.getInstance().bluetoothGatt.device.address
                )
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(bleAuth)
            }
        }
    }

    override fun showHideViews(show: Boolean) {
        conCallback?.showHideViews(show)
    }

    override fun processData(reply: ByteArray, bleManager: BleManager) {
        val obj = OtapProcessDisplayResponse(mContext)
        obj.processReplyFromDisplay(reply, bleManager)
    }

    override fun displayOtapProgress(status: Boolean, completed: Int, total: Int) {
        val otapProgress = Intent("otapprogress")
        otapProgress.putExtra("status", status)
        otapProgress.putExtra("completed", completed)
        otapProgress.putExtra("total", total)
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(otapProgress)

//        conCallback?.displayOtapProgress(status, completed, total)
    }

    override fun progressStatus(progressStatus: Int) {
        val otapstatus = Intent("otapstatus")
        otapstatus.putExtra("status", progressStatus)
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(otapstatus)
//        conCallback?.progressStatus(progressStatus)
    }

    override fun onBluetoothDisconnected(gatt: BluetoothGatt, status: Int, newState: Int) {
        onConnectionChange(gatt, status, newState)
    }


    override fun setOTAPListener(connectionChange: OnConnectionChange) {
        conCallback = connectionChange
    }

    override fun openGattServer(context: Context) {
        setup()
        if (mBluetoothAdapter.isEnabled) {
            openGattServer()
        }
    }

    val thread = Thread {
        while (MSG_DELIVERED) {
            if (characteristicToWrite != null && isDeviceReadyToReceive) {
                var byteArray = messageByteArrayQueue.poll()
                if (byteArray != null) {
                    sendNextMsg(byteArray)
                }
            }
        }
    }

    fun onConnectionChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        mBluetoothGatt = gatt
        BLEModel.getInstance().bluetoothGatt = mBluetoothGatt
        val bleUpdate = Intent("updateconnection")
        if (newState == BluetoothGatt.STATE_CONNECTED && !BLEModel.getInstance()
                .isDeviceConnected
        ) {
            messageByteArrayQueue.clear()
            thread.interrupt()
            BLEModel.getInstance().setDeviceConnected(true)
            bleUpdate.putExtra("deviceaddress", gatt.device.address)
            bleUpdate.putExtra("connected", true)

            bleUpdate.putExtra("devicename", gatt.device.name)
            mBluetoothGatt.discoverServices()
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(bleUpdate)
            stopScan(mContext)
        }
        if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            if (status == 133 || status == 257) {
                disconnect()
            }
            BLEModel.getInstance().bluetoothGatt.close()
            isDeviceReadyToReceive = false
            BLEModel.getInstance().isDeviceConnected = false
            messageByteArrayQueue.clear()
            thread.interrupt()
            bleUpdate.putExtra("deviceaddress", gatt.device.address)
            bleUpdate.putExtra("connected", false)
            bleUpdate.putExtra("devicename", gatt.device.name)
            bleUpdate.putExtra("status", status)
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(bleUpdate)
        }
    }

    fun onCharacteristicFound(chara: BluetoothGattCharacteristic) {
        isDeviceReadyToReceive = true
        characteristicToWrite = chara
        start()
        if (!BLEDeviceManager.isTrustedDevice(addressToConnect, mContext)) {
            sendPINShowMsg(false)
        } else {
            sendPINShowMsg(true)
        }

    }

    private fun getDeviceForAddress(address: String): BluetoothDevice {
        return mBluetoothAdapter.getRemoteDevice(address)
    }

    private fun buildScanFilters(): List<ScanFilter> {

        val scanFilters = ArrayList<ScanFilter>()

        val builder = ScanFilter.Builder()
        builder.setServiceUuid(RE_SERVICE_UUID)
        scanFilters.add(builder.build())

        return scanFilters
    }

    private fun buildScanSettings(): ScanSettings {
        val builder = ScanSettings.Builder()
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        builder.setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
        builder.setScanMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
        return builder.build()
    }

    fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        val b = messageByteArrayQueue.poll()

        if (b != null) {
            MSG_DELIVERED = false
            sendNextMsg(b)

        } else {
            MSG_DELIVERED = true
        }
    }

    override fun sendNextMsg(byteArray: ByteArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
        }
        characteristicToWrite?.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        characteristicToWrite?.value = byteArray
        if (characteristicToWrite != null) {
            mBluetoothGatt.writeCharacteristic(characteristicToWrite)
        }
    }

    private fun sendPINShowMsg(isTrusted: Boolean) {
        val byteArray = ByteArray(20)
        byteArray[0] = 0X21
        if (isTrusted) {
            byteArray[1] = 0x00
        } else {
            byteArray[1] = 0x01
        }
        byteArray[2] = 0x00
        byteArray[3] = 0x00
        byteArray[4] = 0x00
        byteArray[5] = 0x00
        byteArray[6] = 0x00

        val temp = ByteArray(18)
        for (i in 0..17) {
            temp[i] = byteArray[i]
        }

        val crc: ByteArray = CRCCalculator.calculateCRC(temp)

        byteArray[18] = crc[0]
        byteArray[19] = crc[1]
        messageByteArrayQueue.offer(byteArray)
    }

    fun sendTimeMsg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        val bleAuth = Intent("pinAuth")
        bleAuth.putExtra("auth", true)
        bleAuth.putExtra("deviceName", BLEModel.getInstance().bluetoothGatt.device.name)
        bleAuth.putExtra("deviceAddress", BLEModel.getInstance().bluetoothGatt.device.address)
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(bleAuth)

        val timeFrame = ByteArray(20)
        timeFrame[0] = 0x50

        val currentTime = Calendar.getInstance().time
        var hours = currentTime.hours
        val min = currentTime.minutes

        val is24HourFormat = android.text.format.DateFormat.is24HourFormat(mContext)

        val eta_format = if (is24HourFormat) {
            0
        } else {
            if (hours == 0) {
                hours = 12
                1
            } else if (hours < 12) {
                1
            } else if (hours == 12) {
                2
            } else {
                hours -= 12
                2
            }
        }
        timeFrame[1] =
            (((eta_format and 0X00000003) shl 6) or (hours and 0X0000003F)).toByte()
        timeFrame[2] = (min and 0X000000FF).toByte()

        val temp = ByteArray(18)
        for (i in 0..17) {
            temp[i] = timeFrame[i]
        }

        val crc = CRCCalculator.calculateCRC(temp)
        timeFrame[18] = crc[0]
        timeFrame[19] = crc[1]
        messageByteArrayQueue.offer(timeFrame)
    }

}


interface OnConnectionChange {

    fun showHideViews(boolean: Boolean)

}
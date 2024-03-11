package com.royalenfield.bluetooth.client

import android.util.Log
import java.nio.ByteBuffer

object NavProtocol {

    val data = ByteArray(20)

    fun getNavMessageFrame(
        bat: Int = 0, signal: Int = 0,
        tbt_1_id: Int = -1, tbt_1_dist: Int = 0, tbt_1_progress: Int = 5, tbt_1_units: Int = 1,
        tbt_2_id: Int = -1, tbt_2_dist: Int = 0, tbt_2_units: Int = 1, tbt_2_progress: Int = 1,
        total_dist: Int = 0, total_dist_units: Int = 1,
        eta_format: Int = 0, eta_hour: Int = 0, eta_min: Int = 0,
        isNight: Int = 0
    ): ByteArray {

        data[0] = 0x10


        var bat = bat
        var signal = signal
        bat = (bat and 0X0F).toByte().toInt()
        bat = (bat shl 4).toByte().toInt()
        signal = (signal and 0X0F).toByte().toInt()


        data[1] = (bat or signal).toByte()

        data[2] = (tbt_1_id and 0x000000FF).toByte()

        val b = ByteBuffer.allocate(4)
        b.putInt(tbt_1_dist)
        val tbt1DisArray = b.array()


        data[3] = tbt1DisArray[2]
        data[4] = tbt1DisArray[3]

        data[5] = (tbt_1_units and 0x000000FF).toByte()
        data[6] =
            (((tbt_1_progress and 0x0F) shl 4) or (isNight and 0X0F)).toByte() // byte 6 MSB - progress , LSB da/night mode


        data[7] = (tbt_2_id and 0x000000FF).toByte()

        val b2 = ByteBuffer.allocate(4)
        b2.putInt(tbt_2_dist)
        val tbt2DisArray = b2.array()

        data[8] = tbt2DisArray[2]
        data[9] = tbt2DisArray[3]

//        data[10] = (tbt_2_units and 0x000000FF).toByte() ///

        //---------------



        var tbt_2_u = tbt_2_units
        var tbt_2_color = tbt_2_progress

        tbt_2_u = (tbt_2_u and 0X0F).toByte().toInt()
        tbt_2_u = (tbt_2_u shl 4).toByte().toInt()

        tbt_2_color = (tbt_2_color and 0X0F).toByte().toInt()


        data[10] = (tbt_2_u or tbt_2_color).toByte()

        //---------------

        data[13] = (total_dist_units and 0x000000FF).toByte()

        if (total_dist_units == 0) { // ETA


            data[11] = (((eta_format and 0X00000003) shl 6) or (eta_hour and 0X0000003F)).toByte()
            data[12] = (eta_min and 0X000000FF).toByte()

        } else { // Distance
            val b3 = ByteBuffer.allocate(4)
            b3.putInt(total_dist)
            val totalDisArray = b3.array()
            data[11] = totalDisArray[2]
            data[12] = totalDisArray[3]
        }


        val temp = ByteArray(18)
        for (i in 0..17) {
            temp[i] = data[i]
        }


//        var crcKotlin = getCRC(temp)


//        val crcStr = byteArray2Hex(crcKotlin)

        val hexString = byteArray2Hex(temp)
        var crcJava = CRCCalculator.calculateNavCRC(temp)

//    Log.d("CRC", crcStr)
        data[18] = crcJava[0] //checksum MSB
        data[19] = crcJava[1] //checksum LSB

//    Log.d("hexString", hexString)
//    Log.d("crcKotlin", byteArray2Hex(crcKotlin))
//    Log.d("crcJava", byteArray2Hex(crcJava))


        return data

    }





    fun getCRC(bytes: ByteArray): ByteArray {
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


    private fun byteArray2Hex(a: ByteArray): String {

        val stringBuilder = StringBuilder(a.size * 2)
        //        for(byte b : a){
        //            stringBuilder.append(String.format("%02X  ",b));
        //        }
        stringBuilder.append("\n")
        for (i in a.indices) {
//        stringBuilder.append(String.format(i.toString() + ": " + "%02X  ", a[i]))
            stringBuilder.append(String.format("%02X", a[i]))
            //            stringBuilder.append(String.format("%02X  ",a[i]));
            if (i == 4 || i == 9 || i == 14) {
//            stringBuilder.append("\n")
            }
        }
        //        Log.d("", "byteArray2Hex: "+stringBuilder.toString());
        return stringBuilder.toString()
    }
}
package com.royalenfield.bluetooth.otap;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OTAPUtils {

    static long ms = System.currentTimeMillis();
    static Date date = new Date(ms);
    SimpleDateFormat formatter = new SimpleDateFormat("d-M-HH:mm:ss:SSS aaa");
    String time = formatter.format(date);

    byte[] toByteArray(String str) { // to check with // 7d8f7fe4f31bbd91d39aae54d092dec5f30dc7141b26d370809b4cbe3e613900
        byte[] val = new byte[str.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(str.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }


    String byteArray2Hex(byte[] a, String space) {

        StringBuilder stringBuilder = new StringBuilder(a.length * 2);
        for (int i = 0; i < a.length; i++) {
            stringBuilder.append(String.format("%02x" + space, a[i]));
            if (i == 4 || i == 9 || i == 14) {
//                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Convert byte array to int
     *
     * @param b
     * @return
     */
    int byteArrayToInt(byte[] b) {
        final ByteBuffer bb = ByteBuffer.wrap(b);
        return bb.getInt();
    }

    String byte2Hex(byte mByte) {
        return String.format("%02x", mByte);
    }
}

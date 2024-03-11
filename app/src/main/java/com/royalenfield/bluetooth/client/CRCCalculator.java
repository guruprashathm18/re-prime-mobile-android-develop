package com.royalenfield.bluetooth.client;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Checksum;

public class CRCCalculator implements Checksum {
    //
    public static byte[] calculateNavCRC(byte[] bytes) {
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)


        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }

        crc &= 0xffff;
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(crc);
        byte[] crcArray = b.array();
        byte[] ret = new byte[2];
        ret[0] = crcArray[2];
        ret[1] = crcArray[3];
        return ret;
    }

    /**
     * CRC-CCITT polynomial, used by X.25, V.41, Bluetooth, PPP, IrDA, BACnet; known as CRC-CCITT
     */
    public static final int POLYNOMIAL_CCITT = 0x1021;
    public static final short INIT_CCITT = (short) 0xffff;

    //  private final int   polynomial;
    private final short init;
    private final short[] crcTable = new short[256];
    private short value;

    /**
     * Construct a CRCCalculator specifying the polynomial and initial value.
     *
     * @param polynomial Polynomial, typically one of the POLYNOMIAL_* constants.
     * @param init       Initial value, typically either 0xffff or zero.
     */
    public CRCCalculator(int polynomial, short init) {
//      this.polynomial = polynomial;
        this.value = this.init = init;
        for (int dividend = 0; dividend < 256; dividend++) {
            int remainder = dividend << 8;
            for (int bit = 8; bit > 0; --bit)
                if ((remainder & 0x8000) != 0)
                    remainder = (remainder << 1) ^ polynomial;
                else
                    remainder <<= 1;
            crcTable[dividend] = (short) remainder;
        }
    }

    @Override
    public void update(byte[] buffer, int offset, int len) {
        for (int i = 0; i < len; i++) {
            int data = buffer[offset + i] ^ (value >>> 8);
            value = (short) (crcTable[data & 0xff] ^ (value << 8));
//            System.out.println("value=" + value);
        }
    }

    /**
     * Updates the current checksum with the specified array of bytes.
     * Equivalent to calling <code>update(buffer, 0, buffer.length)</code>.
     *
     * @param buffer the byte array to update the checksum with
     */
    public void update(byte[] buffer) {
        update(buffer, 0, buffer.length);
    }

    @Override
    public void update(int b) {
        update(new byte[]{(byte) b}, 0, 1);
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public void reset() {
        value = init;
    }

    /**
     * Tester
     */
    public static byte[] calculateCRC(byte[] data) throws IOException {
        CRCCalculator crc16;
        crc16 = new CRCCalculator(POLYNOMIAL_CCITT, INIT_CCITT);
        crc16.update(data, 0, data.length);
       int crc = (int) crc16.getValue();

//        ByteBuffer b = ByteBuffer.allocate(4);
//        b.putInt(crc);
//        byte[] crcArray = b.array();
        byte[] byteArray = intToByteArray(crc);

        byte[] ret = new byte[2];
        ret[0] = byteArray[2];
        ret[1] = byteArray[3];
        return ret;
    }


    private static byte[] intToByteArray(final int i) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(i);
        dos.flush();
        return bos.toByteArray();
    }

}

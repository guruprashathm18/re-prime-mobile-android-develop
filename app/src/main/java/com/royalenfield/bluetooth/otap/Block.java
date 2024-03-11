package com.royalenfield.bluetooth.otap;

import java.util.Arrays;
import java.util.List;

import com.royalenfield.reprime.utils.RELog;

import static com.royalenfield.bluetooth.otap.BlockSegmentUtils.blockSize;
import static com.royalenfield.bluetooth.otap.BlockSegmentUtils.getSHABytes;
import static com.royalenfield.bluetooth.otap.BlockSegmentUtils.leftOver;


public class Block {

    public static final String TAG = Block.class.getSimpleName();

    public static int getRetryCount() {
        return NO_RETRY;
    }

    private static int NO_RETRY = 2;


    private byte[] startAddress = new byte[4];

    private byte[] buffer = new byte[blockSize];

    private int getFilledBufferSize() {
        return filledBufferSize;
    }

    private int filledBufferSize = 0;

    public int getFilledSize() {
        return this.filledBufferSize;
    }

    private List<Segment> segmentArrayList;

    public List<Segment> getSegmentArrayList() {
        return segmentArrayList;
    }

    public void setSegmentArrayList(List<Segment> segmentArrayList) {
        this.segmentArrayList = segmentArrayList;
    }

    public byte[] getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(byte[] startAddress) {
        this.startAddress = startAddress;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    int remainingSize = 4096;
    byte[] inserted;

    public void append3Buffer(byte[] input) {
        if (input == null) return;
        int length = input.length;

        if (remainingSize > length) {// it has more space than input

            for (byte b : input) {
                buffer[blockSize - remainingSize] = b;
                remainingSize--;
                filledBufferSize++;
            }


        } else if (remainingSize == length) { // it has  space equal to  input

            for (byte b : input) {
                buffer[blockSize - remainingSize] = b;
                remainingSize--;
                filledBufferSize++;
            }


        } else if (remainingSize < length) { // it has less space for the input
            int toTake = remainingSize;
            inserted = new byte[toTake];
            leftOver = new byte[length - toTake];


            for (int k = 0; k < toTake; k++) {
                inserted[k] = input[k];
            }

            for (int j = 0; j < leftOver.length; j++) {
                leftOver[j] = input[j + toTake];
            }
            for (byte b : inserted) {
                buffer[blockSize - remainingSize] = b;
                remainingSize--;
                filledBufferSize++;
            }


        }

    }


    public byte[] getSha() {
       /* ByteBuffer dataBuffer = ByteBuffer.wrap(buffer);
        dataBuffer.clear();
        dataBuffer.get(buffer);
        byte[] blockbyte = dataBuffer.array();
        String shaStr = getSHA(blockbyte);*/

        RELog.d(TAG,"getSha: " + buffer.length + "//" + getFilledBufferSize());

        if (buffer.length != getFilledBufferSize()) {
            byte[] lastBuffer = Arrays.copyOfRange(buffer, 0, getFilledBufferSize());

            //TODO check the below
//           return toByteArray(getSHA(lastBuffer));
            return getSHABytes(lastBuffer);
        } else {
//           return toByteArray(getSHA(buffer));
            return getSHABytes(buffer);
        }

    }

    public boolean isFilled() {
        return filledBufferSize >= blockSize;
    }
}

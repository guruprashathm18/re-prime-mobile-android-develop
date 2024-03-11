package com.royalenfield.bluetooth.otap;

import static com.royalenfield.bluetooth.otap.BlockSegmentUtils.segmentSize;

public class Segment {

    public static int getRetryCount() {
        return NO_RETRY;
    }

    private static int NO_RETRY = 5;


    private byte[] segmentData = new byte[segmentSize];
    private int segmentFieldSize;


    public int getSegmentFilledSize() {
        return segmentFieldSize;
    }

    public void setSegmentFieldSize(int segmentFieldSize) {
        this.segmentFieldSize = segmentFieldSize;
    }

    public byte[] getSegmentData() {
        return segmentData;
    }

    public void setSegmentData(byte[] segmentData) {
        this.segmentData = segmentData;
    }


}

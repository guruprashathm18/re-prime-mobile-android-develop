package com.royalenfield.bluetooth.otap;

public class RegionMapping {

    private byte[] regionNumber = new byte[4];
    private byte[] regionStartAddress = new byte[4];
    private byte[] regionSize = new byte[4];
    private OTAPUtils otapUtils = new OTAPUtils();

    public byte[] getRegionNumber() {
        return regionNumber;
    }

    public void setRegionNumber(byte[] regionNumber) {
        this.regionNumber = regionNumber;
    }

    public byte[] getRegionStartAddress() {
        return regionStartAddress;
    }

    public void setRegionStartAddress(byte[] regionStartAddress) {
        this.regionStartAddress = regionStartAddress;
    }

    public byte[] getRegionSize() {
        return regionSize;
    }

    public void setRegionSize(byte[] regionSize) {
        this.regionSize = regionSize;
    }

    public int getRegionSizeInt() {
        return otapUtils.byteArrayToInt(getRegionSize());
    }
}

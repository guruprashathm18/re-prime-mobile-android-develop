package com.royalenfield.bluetooth.otap;

import java.util.ArrayList;

public class RegionInfo {

    private byte[] totalRegions = new byte[4];
    private ArrayList<RegionMapping> myRegionMappingArrayList;

    public byte[] getTotalRegions() {
        return totalRegions;
    }

    public void setTotalRegions(byte[] totalRegions) {
        this.totalRegions = totalRegions;
    }

    public ArrayList<RegionMapping> getRegionMappingArrayList() {
        return myRegionMappingArrayList;
    }

    public void setRegionMappingArrayList(ArrayList<RegionMapping> aRegionMappingArrayList) {
        this.myRegionMappingArrayList = aRegionMappingArrayList;
    }
}

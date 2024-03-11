package com.royalenfield.reprime.ui.home.navigation.model;

import com.royalenfield.reprime.models.request.web.rides.WayPointsData;

import java.util.ArrayList;

public class RecentRoute {

    public ArrayList<WayPointsData> mWayPointList;

    public RecentRoute(ArrayList<WayPointsData> wayPointList){
    this.mWayPointList = wayPointList;
    }
    public ArrayList<WayPointsData> getmWayPointList() {
        return mWayPointList;
    }

    public void setmWayPointList(ArrayList<WayPointsData> mWayPointList) {
        this.mWayPointList = mWayPointList;
    }
}

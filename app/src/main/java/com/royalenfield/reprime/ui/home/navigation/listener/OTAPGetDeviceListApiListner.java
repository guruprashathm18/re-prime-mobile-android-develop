package com.royalenfield.reprime.ui.home.navigation.listener;

import com.royalenfield.reprime.models.response.web.otap.Data;

public interface OTAPGetDeviceListApiListner {

    void getTripperDeviceListFromApiSuccess(Data response);
    void getTripperDeviceListFromApiFailed();
}

package com.royalenfield.bluetooth.otap.listener;

import java.util.Map;

public interface OnOTAPCallback {

    void onOTAPFirestoreSuccess(Map<String, Object> map);

    void onOTAPFirestoreFailure(String message);
}




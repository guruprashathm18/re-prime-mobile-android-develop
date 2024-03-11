package com.royalenfield.bluetooth.otap.listener;

public interface OtapInstallationStatusListener {
    void updateBrickStatus(String value,String value2);
    void onInstallationStarted();
}

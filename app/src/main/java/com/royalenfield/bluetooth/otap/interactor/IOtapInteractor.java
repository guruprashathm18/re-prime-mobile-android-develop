package com.royalenfield.bluetooth.otap.interactor;

public interface IOtapInteractor {

    void saveFeedback(String firmwareId, String deviceId, int status);

}

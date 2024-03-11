package com.royalenfield.reprime.ui.home.homescreen.interactor;

import com.royalenfield.reprime.ui.home.homescreen.listener.KYCUploadListener;
import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;

import java.io.File;
import java.util.List;

public interface IUploadKYCInteractor {

    void uploadKYC(File file, String chessis, final KYCUploadListener listener);

}

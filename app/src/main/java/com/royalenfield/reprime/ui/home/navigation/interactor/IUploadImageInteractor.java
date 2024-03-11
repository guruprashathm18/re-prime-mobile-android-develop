package com.royalenfield.reprime.ui.home.navigation.interactor;

import com.royalenfield.reprime.ui.home.rides.listeners.RidesListeners;

import java.util.List;

public interface IUploadImageInteractor {

    void uploadImage(List<String> filePath, final RidesListeners.NavigationImageUploadListener listener,
                     String guid,String UUID);

}

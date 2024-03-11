package com.royalenfield.reprime.ui.home.homescreen.interactor;

import com.royalenfield.reprime.ui.home.homescreen.listener.FirebaseCustomTokenListener;
import com.royalenfield.reprime.ui.home.homescreen.listener.OnVehicleDetailsFinishedListener;
import com.royalenfield.reprime.ui.onboarding.login.listeners.OnLoginFinishedListener;

public interface IHomeActivityInteractor {
    void getVehicleDetails( final OnVehicleDetailsFinishedListener onVehicleDetailsFinishedListener);
    void getVehicleDetailsFromFirestore(final OnVehicleDetailsFinishedListener onVehicleDetailsFinishedListener);
    void getFirebaseCustomToken(final FirebaseCustomTokenListener firebaseCustomTokenListener);
}

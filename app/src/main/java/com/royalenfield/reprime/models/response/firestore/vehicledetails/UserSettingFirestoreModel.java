package com.royalenfield.reprime.models.response.firestore.vehicledetails;

public class UserSettingFirestoreModel {

    private boolean getVehicleDetailsFromFirestore;
    private boolean showUserValidationPopup;
    private boolean showVehicleOnboardingPopup;
    private boolean updatePersonalDetail;

    public UserSettingFirestoreModel() {
    }

    public UserSettingFirestoreModel(boolean getVehicleDetailsFromFirestore, boolean showUserValidationPopup, boolean showVehicleOnboardingPopup) {
        this.getVehicleDetailsFromFirestore = getVehicleDetailsFromFirestore;
        this.showUserValidationPopup = showUserValidationPopup;
        this.showVehicleOnboardingPopup = showVehicleOnboardingPopup;
    }

    public boolean isGetVehicleDetailsFromFirestore() {
        return getVehicleDetailsFromFirestore;
    }

    public void setGetVehicleDetailsFromFirestore(boolean getVehicleDetailsFromFirestore) {
        this.getVehicleDetailsFromFirestore = getVehicleDetailsFromFirestore;
    }

    public boolean isShowUserValidationPopup() {
        return showUserValidationPopup;
    }

    public void setShowUserValidationPopup(boolean showUserValidationPopup) {
        this.showUserValidationPopup = showUserValidationPopup;
    }

    public boolean isShowVehicleOnboardingPopup() {
        return showVehicleOnboardingPopup;
    }

    public void setShowVehicleOnboardingPopup(boolean showVehicleOnboardingPopup) {
        this.showVehicleOnboardingPopup = showVehicleOnboardingPopup;
    }
}

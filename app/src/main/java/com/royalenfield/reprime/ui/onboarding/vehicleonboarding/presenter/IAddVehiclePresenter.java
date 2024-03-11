package com.royalenfield.reprime.ui.onboarding.vehicleonboarding.presenter;

public interface IAddVehiclePresenter {

    void validatePurchaseDate(String date);
    boolean validateAddVehicleDetails(String purchaseDate, String engineNumber,boolean isVarifiedClick);
    void validateVehicleDetail(String purchaseDate, String engineNumber);
}

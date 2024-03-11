package com.royalenfield.reprime.ui.userdatavalidation.singlemotorcycle.view;

public class UpdateVehicleData {

    private String engineNo;
    private String modelName;
    private String registrationNumber;
    private boolean removeBike;
    private String chassisNo;

    public UpdateVehicleData(String engineNo, String modelName, String registrationNumber, boolean removeBike,String chassisNo) {
        this.engineNo = engineNo;;
        this.modelName = modelName;
        this.registrationNumber = registrationNumber;
        this.removeBike = removeBike;
        this.chassisNo=chassisNo;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public String getModelName() {
        return modelName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public boolean isRemoveBike() {
        return removeBike;
    }

    public String getChassisNo() {
        return chassisNo;
    }
}

package com.royalenfield.bluetooth.models;

public class DeviceSettingsItems {

    private int itemId;
    private String itemName = "";
    private String softwareUpdate = "";

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSoftwareUpdate() {
        return softwareUpdate;
    }

    public void setSoftwareUpdate(String softwareUpdate) {
        this.softwareUpdate = softwareUpdate;
    }
}

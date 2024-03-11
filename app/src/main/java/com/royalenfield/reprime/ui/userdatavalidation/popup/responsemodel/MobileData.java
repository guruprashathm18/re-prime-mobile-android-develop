package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class MobileData implements Serializable {

    @SerializedName("primary")
    @Expose
    private PrimaryData primary;
    @SerializedName("secondary")
    @Expose
    private SecondaryData secondary;
    @SerializedName("emergency")
    @Expose
    private EmergencyData emergency;
    private final static long serialVersionUID = -8484596934096859262L;

    public PrimaryData getPrimary() {
        return primary;
    }

    public void setPrimary(PrimaryData primary) {
        this.primary = primary;
    }

    public SecondaryData getSecondary() {
        return secondary;
    }

    public void setSecondary(SecondaryData secondary) {
        this.secondary = secondary;
    }

    public EmergencyData getEmergency() {
        return emergency;
    }

    public void setEmergency(EmergencyData emergency) {
        this.emergency = emergency;
    }
}

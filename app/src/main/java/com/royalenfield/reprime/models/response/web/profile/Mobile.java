package com.royalenfield.reprime.models.response.web.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mobile {

    @SerializedName("primary")
    @Expose
    private MobileDetails primary;
    @SerializedName("secondary")
    @Expose
    private MobileDetails secondary;
    @SerializedName("emergency")
    @Expose
    private MobileDetails emergency;

    public MobileDetails getPrimary() {
        return primary;
    }

    public void setPrimary(MobileDetails primary) {
        this.primary = primary;
    }

    public MobileDetails getSecondary() {
        return secondary;
    }

    public void setSecondary(MobileDetails secondary) {
        this.secondary = secondary;
    }

    public MobileDetails getEmergency() {
        return emergency;
    }

    public void setEmergency(MobileDetails emergency) {
        this.emergency = emergency;
    }
}




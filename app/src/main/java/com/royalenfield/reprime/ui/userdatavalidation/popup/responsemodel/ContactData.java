package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContactData implements Serializable {

    @SerializedName("mobile")
    @Expose
    private MobileData mobile;
    @SerializedName("emailVerified")
    @Expose
    private boolean emailVerified;
    @SerializedName("email")
    @Expose
    private String email;

    public MobileData getMobile() {
        return mobile;
    }

    public void setMobile(MobileData mobile) {
        this.mobile = mobile;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

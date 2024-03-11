package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.web.login.LoginData;

import java.io.Serializable;

public class UpdateContactResponseModel implements Serializable {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private Object data;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}

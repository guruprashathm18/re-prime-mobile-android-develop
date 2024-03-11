package com.royalenfield.reprime.models.response.googlemap.poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author kiran
 */
public class POIDetailsResponse {

    @SerializedName("result")
    @Expose
    private POIResults result = null;
    @SerializedName("status")
    @Expose
    private String status;

    public POIResults getResult() {
        return result;
    }

    public void setResult(POIResults result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

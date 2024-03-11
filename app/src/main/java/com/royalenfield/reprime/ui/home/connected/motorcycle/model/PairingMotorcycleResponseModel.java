package com.royalenfield.reprime.ui.home.connected.motorcycle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PairingMotorcycleResponseModel {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("errorMessage")
    @Expose
    public String errorMessage;

    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {
        @SerializedName("getDeviceData")
        @Expose
        public GetDeviceData getDeviceData;
    }

    public class GetDeviceData {
        @SerializedName("device_imei")
        @Expose
        public String deviceImei;
        @SerializedName("batch_no")
        @Expose
        public String batchNo;
        @SerializedName("manufacturing_date")
        @Expose
        public String manufacturingDate;
        @SerializedName("iccid")
        @Expose
        public String iccid;
        @SerializedName("model_name")
        @Expose
        public String modelName;
        @SerializedName("vendor_name")
        @Expose
        public String vendorName;
        @SerializedName("sim_provider_name")
        @Expose
        public String simProviderName;
        @SerializedName("admin_key")
        @Expose
        public String adminKey;
        @SerializedName("user_key")
        @Expose
        public String userKey;
    }
}

package com.royalenfield.reprime.models.response.web.qr;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class QrResponse {
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("error")
        @Expose
        private Boolean error;
        @SerializedName("data")
        @Expose
        private Data data;
        @SerializedName("errorMessage")
        @Expose
        private String errorMessage;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Boolean getError() {
            return error;
        }

        public void setError(Boolean error) {
            this.error = error;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
}

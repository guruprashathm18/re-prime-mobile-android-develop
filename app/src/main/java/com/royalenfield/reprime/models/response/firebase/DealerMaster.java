package com.royalenfield.reprime.models.response.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealerMaster implements Parcelable
{

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("response")
    @Expose
    private List<DealerMasterResponse> response = null;
    public final static Parcelable.Creator<DealerMaster> CREATOR = new Creator<DealerMaster>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DealerMaster createFromParcel(Parcel in) {
            return new DealerMaster(in);
        }

        public DealerMaster[] newArray(int size) {
            return (new DealerMaster[size]);
        }

    };

    protected DealerMaster(Parcel in) {
        this.result = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.response, (DealerMasterResponse.class.getClassLoader()));
    }

    public DealerMaster() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<DealerMasterResponse> getResponse() {
        return response;
    }

    public void setResponse(List<DealerMasterResponse> response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeValue(status);
        dest.writeList(response);
    }

    public int describeContents() {
        return 0;
    }
}

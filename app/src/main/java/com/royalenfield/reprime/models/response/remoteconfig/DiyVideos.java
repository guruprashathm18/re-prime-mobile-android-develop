
package com.royalenfield.reprime.models.response.remoteconfig;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiyVideos {

    @SerializedName("diyData")
    @Expose
    private List<DiyDatum> diyData = null;

    public List<DiyDatum> getDiyData() {
        return diyData;
    }

    public void setDiyData(List<DiyDatum> diyData) {
        this.diyData = diyData;
    }

}

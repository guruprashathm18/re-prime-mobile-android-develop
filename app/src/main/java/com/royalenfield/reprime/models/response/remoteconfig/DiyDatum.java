
package com.royalenfield.reprime.models.response.remoteconfig;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiyDatum {

    @SerializedName("motorcycleName")
    @Expose
    private String motorcycleName;
    @SerializedName("diyVideos")
    @Expose
    private List<DiyVideo> diyVideos = null;

    public String getMotorcycleName() {
        return motorcycleName;
    }

    public void setMotorcycleName(String motorcycleName) {
        this.motorcycleName = motorcycleName;
    }

    public List<DiyVideo> getDiyVideos() {
        return diyVideos;
    }

    public void setDiyVideos(List<DiyVideo> diyVideos) {
        this.diyVideos = diyVideos;
    }

}

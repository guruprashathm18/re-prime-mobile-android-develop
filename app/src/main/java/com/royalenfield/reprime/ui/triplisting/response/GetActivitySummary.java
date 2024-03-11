package com.royalenfield.reprime.ui.triplisting.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GetActivitySummary implements Serializable {

    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;
    @SerializedName("fromTs")
    @Expose
    private String fromTs;
    @SerializedName("toTs")
    @Expose
    private String toTs;
	@SerializedName("tripCreatedTs")
	@Expose
	private String tripCreatedTs="0";
    @SerializedName("startLoc")
    @Expose
    private String startLoc;
    @SerializedName("endLoc")
    @Expose
    private String endLoc;
    @SerializedName("startAddress")
    @Expose
    private String startAddress;
    @SerializedName("endAddress")
    @Expose
    private String endAddress;
    @SerializedName("totalDist")
    @Expose
    private Float totalDist;
    @SerializedName("totalRunningTime")
    @Expose
    private Long totalRunningTime=0l;
    @SerializedName("totalHaCount")
    @Expose
    private Integer totalHaCount;
    @SerializedName("totalHbCount")
    @Expose
    private Integer totalHbCount;
    @SerializedName("totalIdlingTime")
    @Expose
    private Integer totalIdlingTime;

    @SerializedName("overspeedCount")
    @Expose
    private Integer overSpeedCount;

    @SerializedName("averageSpeed")
    @Expose
    private String averageSpeed;

    @SerializedName("topSpeed")
    @Expose
    private String topSpeed;

	@SerializedName("tripType")
	@Expose
	private String tripType;

	@SerializedName("tripId")
	@Expose
	private String tripId;

    private final static long serialVersionUID = 7169950103922169174L;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getFromTs() {
        return fromTs;
    }

    public void setFromTs(String fromTs) {
        this.fromTs = fromTs;
    }

    public String getToTs() {
        return toTs;
    }

    public void setToTs(String toTs) {
        this.toTs = toTs;
    }

    public String getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    public String getEndLoc() {
        return endLoc;
    }

    public void setEndLoc(String endLoc) {
        this.endLoc = endLoc;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Float getTotalDist() {
        return totalDist;
    }

    public void setTotalDist(Float totalDist) {
        this.totalDist = totalDist;
    }

    public Long getTotalRunningTime() {
        return totalRunningTime;
    }

    public void setTotalRunningTime(Long totalRunningTime) {
        this.totalRunningTime = totalRunningTime;
    }

    public Integer getTotalHaCount() {
        return totalHaCount;
    }

    public void setTotalHaCount(Integer totalHaCount) {
        this.totalHaCount = totalHaCount;
    }

    public Integer getTotalHbCount() {
        return totalHbCount;
    }

    public void setTotalHbCount(Integer totalHbCount) {
        this.totalHbCount = totalHbCount;
    }

    public Integer getTotalIdlingTime() {
        return totalIdlingTime;
    }

    public void setTotalIdlingTime(Integer totalIdlingTime) {
        this.totalIdlingTime = totalIdlingTime;
    }



    public Integer getOverSpeedCount() {
        return overSpeedCount;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public String getTopSpeed() {
        return topSpeed;
    }

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getTripCreatedTs() {
		return tripCreatedTs;
	}

	public void setTripCreatedTs(String tripCreatedTs) {
		this.tripCreatedTs = tripCreatedTs;
	}
}

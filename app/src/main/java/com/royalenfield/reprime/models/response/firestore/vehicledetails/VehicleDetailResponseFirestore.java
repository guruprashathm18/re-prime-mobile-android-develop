
package com.royalenfield.reprime.models.response.firestore.vehicledetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleDetailResponseFirestore {

    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("userVehicles")
    @Expose
    private List<UserVehicle> userVehicles = null;
    @SerializedName("_class")
    @Expose
    private String _class;
    @SerializedName("lastModified")
    @Expose
    private String lastModified;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
	@SerializedName("firebaseStatus")
	@Expose
	private String firebaseStatus;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<UserVehicle> getUserVehicles() {
        return userVehicles;
    }

    public void setUserVehicles(List<UserVehicle> userVehicles) {
        this.userVehicles = userVehicles;
    }

    public String getClass_() {
        return _class;
    }

    public void setClass_(String _class) {
        this._class = _class;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

	public String getFirebaseStatus() {
		return firebaseStatus;
	}

	public void setFirebaseStatus(String firebaseStatus) {
		this.firebaseStatus = firebaseStatus;
	}
}

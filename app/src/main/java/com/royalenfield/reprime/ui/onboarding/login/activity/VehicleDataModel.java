package com.royalenfield.reprime.ui.onboarding.login.activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.royalenfield.reprime.models.response.firestore.servicehistory.ServiceHistoryResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleDataModel implements Serializable {

    private String mUserName;

    private String mChaissisNo;

    private String mDateOfMfg;

    private String mEngineNo;

    private String mId;

    private String mPurchaseDate;

    private String mMobileNo;

    private String mModelCode;

    private String mModelName;

    private String mRegistrationNo;

    private String mInvoiceNo;

    private String mAlternateNo;

    private Boolean mIsConnected = false;

    private String deviceId;

    private long odoValue=0;

    private String batVolt=null;

    private Integer fuel=null;
    private String fuelBar=null;

    private String mImageUrl;
    private Map<String, String> mapVehicleData = new HashMap<>();
    private String displayName;
    private String ownerManual;
    private String vehicleUpdateStatus;
    private int alertCount;
	private Boolean lowFuel;
	private Boolean lowBat;
    private Boolean haltStatus=null;
    private int gpsStatus=-1;
    private int gsmStatus=-1;
    private int tripFstate=0;


    private String timeStamp=null;

    public String getVehicleUpdateStatus() {
        return vehicleUpdateStatus;
    }

    public void setVehicleUpdateStatus(String vehicleUpdateStatus) {
        this.vehicleUpdateStatus = vehicleUpdateStatus;
    }

    public Map<String, String> getMapVehicleData() {
        return mapVehicleData;
    }


    public void setMapVehicleData(Map<String, String> mapVehicleData) {
        this.mapVehicleData = mapVehicleData;
    }

    private boolean vehicleConnected;

    private boolean vehicleAuthorized;

    private ArrayList<ServiceHistoryResponse> serviceHistoryResponse = new ArrayList<>();

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getChaissisNo() {
        return mChaissisNo;
    }

    public void setChaissisNo(String mChaissisNo) {
        this.mChaissisNo = mChaissisNo;
    }

    public String getDateOfMfg() {
        return mDateOfMfg;
    }

    public void setDateOfMfg(String mDateOfMfg) {
        this.mDateOfMfg = mDateOfMfg;
    }

    public String getEngineNo() {
        return mEngineNo;
    }

    public void setEngineNo(String mEngineNo) {
        this.mEngineNo = mEngineNo;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getPurchaseDate() {
        return mPurchaseDate;
    }

    public void setPurchaseDate(String mPurchaseDate) {
        this.mPurchaseDate = mPurchaseDate;
    }

    public String getMobileNo() {
        return mMobileNo;
    }

    public void setMobileNo(String mMobileNo) {
        this.mMobileNo = mMobileNo;
    }

    public String getModelCode() {
        return mModelCode;
    }

    public void setModelCode(String mModelCode) {
        this.mModelCode = mModelCode;
    }

    public String getModelName() {
        return mModelName;
    }

    public void setModelName(String mModelName) {
        this.mModelName = mModelName;
    }

    public String getRegistrationNo() {
        return mRegistrationNo;
    }

    public void setRegistrationNo(String mRegistrationNo) {
        this.mRegistrationNo = mRegistrationNo;
    }

    public String getInvoiceNo() {
        return mInvoiceNo;
    }

    public void setInvoiceNo(String mInvoiceNo) {
        this.mInvoiceNo = mInvoiceNo;
    }

    public String getAlternateNo() {
        return mAlternateNo;
    }

    public void setAlternateNo(String mAlternateNo) {
        this.mAlternateNo = mAlternateNo;
    }

    public ArrayList<ServiceHistoryResponse> getServiceHistoryResponse() {
        return serviceHistoryResponse;
    }

    public void setServiceHistoryResponse(ArrayList<ServiceHistoryResponse> serviceHistoryResponse) {
        this.serviceHistoryResponse = serviceHistoryResponse;
    }

    public void insertServiceHistoryResponse(ServiceHistoryResponse serviceHistoryResponse) {
        this.serviceHistoryResponse.add(serviceHistoryResponse);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Boolean getIsConnected() {
        return mIsConnected;
    }

    public void setIsConnected(Boolean value) {
        this.mIsConnected = value;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isVehicleAuthorized() {
        return vehicleAuthorized;
    }

    public void setVehicleAuthorized(boolean vehicleAuthorized) {
        this.vehicleAuthorized = vehicleAuthorized;
    }

    public long getOdoValue() {
        return odoValue;
    }

    public void setOdoValue(long odoValue) {
        this.odoValue = odoValue;
    }

    public String getBatVolt() {
        return batVolt;
    }

    public void setBatVolt(String batVolt) {
        this.batVolt = batVolt;
    }

    public Integer getFuel() {
        return fuel;
    }

    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    public String getFuelBar() {
        return fuelBar;
    }

    public void setFuelBar(String fuelBar) {
        this.fuelBar = fuelBar;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }

    public String getOwnerManual() {
        return ownerManual;
    }

    public void setOwnerManual(String ownerManual) {
        this.ownerManual = ownerManual;
    }

    public int getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public int getGsmStatus() {
        return gsmStatus;
    }

    public void setGsmStatus(int gsmStatus) {
        this.gsmStatus = gsmStatus;
    }

    public int getTripFstate() {
        return tripFstate;
    }

    public void setTripFstate(int tripFstate) {
        this.tripFstate = tripFstate;
    }



    public Boolean isHaltStatus() {
        return haltStatus;
    }

    public void setHaltStatus(Boolean haltStatus) {
        this.haltStatus = haltStatus;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

	public boolean isLowFuel() {
		return lowFuel;
	}

	public void setLowFuel(boolean lowFuel) {
		this.lowFuel = lowFuel;
	}

	public Boolean isLowBat() {
		return lowBat;
	}

	public void setLowBat(Boolean lowBat) {
		this.lowBat = lowBat;
	}
}

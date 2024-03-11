package com.royalenfield.reprime.ui.userdatavalidation.popup.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccountStatus implements Serializable {
	@SerializedName("isActive")
	@Expose
	private Boolean isActive;

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}
}

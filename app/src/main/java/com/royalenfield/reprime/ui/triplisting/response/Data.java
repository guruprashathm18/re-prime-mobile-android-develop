package com.royalenfield.reprime.ui.triplisting.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
	@SerializedName("createTripMerge")
	@Expose
	private CreateTripMerge createTripMerge;

	public CreateTripMerge getCreateTripMerge() {
		return createTripMerge;
	}

	public void setCreateTripMerge(CreateTripMerge createTripMerge) {
		this.createTripMerge = createTripMerge;
	}


}

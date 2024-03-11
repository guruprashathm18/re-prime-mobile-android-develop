package com.royalenfield.reprime.ui.onboarding;

import com.royalenfield.reprime.models.request.logs.Log;

public class LogResponse {
	private String api;
	private Long time;
	private String requestId;
	public LogResponse(String title,Long startTime,String requestId){
		this.api=title;
		this.time=startTime;
		this.requestId=requestId;

	}
	public LogResponse(String title,Long startTime){
		this.api=title;
		this.time=startTime;

	}

	public String getText() {
		return api;
	}

	public void setText(String text) {
		this.api = text;
	}

	public Long getStartTime() {
		return time;
	}

	public void setStartTime(Long startTime) {
		this.time = startTime;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}

package com.royalenfield.reprime.models.request.web.tbtauthentication;

public class TbtAuthRequest {

    private String guid;

    public TbtAuthRequest(String guid){
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }




}

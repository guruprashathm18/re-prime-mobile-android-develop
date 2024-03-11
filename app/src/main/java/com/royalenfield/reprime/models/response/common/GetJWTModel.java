package com.royalenfield.reprime.models.response.common;

public class GetJWTModel {
    private String jwtAccessToken;

    public GetJWTModel() {
    }

    public String getJwtAccessToken() {
        return jwtAccessToken;
    }

    public void setJwtAccessToken(String jwtAccessToken) {
        this.jwtAccessToken = jwtAccessToken;
    }
}

package com.royalenfield.reprime.models.response.firestore.rides.userrideinfo;

import java.util.Map;

public class RidersJoined {

    private String _id;

    private String status;

    private Map<String,Object> user;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }
}

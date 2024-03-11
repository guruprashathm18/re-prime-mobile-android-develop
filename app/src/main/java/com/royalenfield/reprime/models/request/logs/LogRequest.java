package com.royalenfield.reprime.models.request.logs;

import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class LogRequest {

    @SerializedName("logs")
    @Expose
    private List<Log> logs = null;

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

}
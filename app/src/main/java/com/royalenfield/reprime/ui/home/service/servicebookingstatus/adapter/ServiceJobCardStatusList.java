package com.royalenfield.reprime.ui.home.service.servicebookingstatus.adapter;

public class ServiceJobCardStatusList {


    private String service_name;

    private boolean service_status;

    public ServiceJobCardStatusList(String service_name, boolean service_status) {
        this.service_name = service_name;
        this.service_status = service_status;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public boolean getService_status() {
        return service_status;
    }

    public void setService_status(boolean service_status) {
        this.service_status = service_status;
    }

}

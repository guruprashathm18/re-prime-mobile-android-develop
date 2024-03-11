package com.royalenfield.reprime.ui.home.service.specificissue.model;

/**
 * @author JAL7KOR on 1/29/2019.
 */

public class IssuesModel {

    public static final int GENERAL_ISSUES_TYPE = 0;
    public static final int ENGINE_ISSUES_TYPE = 1;
    public static final int VEHICLE_ISSUES_TYPE = 2;
    public static final int ELECTRICAL_ISSUES_TYPE = 3;
    public int type;
    private String issueName;
    private boolean isSelected;

    public IssuesModel(int fType, String name) {
        this.type = fType;
        this.issueName = name;
    }

    public String getIssueName() {
        return issueName;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
package com.royalenfield.reprime.ui.userdatavalidation.popup.presenter;

enum APIStatus {

    DEFAULT(0),
    API_SUCCESS(1),
    API_FAILURE(2);

    private int val;

    APIStatus(int val) {
        this.val = val;
    }

    int getVal() {
        return this.val;
    }

}

package com.royalenfield.reprime.ui.home.ourworld.listeners;

/**
 * @author BOP1KOR on 4/1/2019.
 */
public interface OnOurWorldResponseListener {

    void onOurWorldEventSuccess();

    void onOurWorldEventFailure(String errorMessage);

    void onOurWorldNewsSuccess();

    void onOurWorldNewsFailure(String string);
}

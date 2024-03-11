package com.royalenfield.reprime.ui.riderprofile.views;

import com.royalenfield.reprime.base.REMvpView;

/**
 * @author BOP1KOR on 1/8/2019.
 */

public interface REProfileView extends REMvpView {

    void onGetUserSuccess();

    void onGetUserFailure(String errorMessage);

    void onVehicleDetailsSuccess();

    void onVehicleDetailsFailure(String errorMessage);

}

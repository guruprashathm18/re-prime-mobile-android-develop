package com.royalenfield.reprime.ui.riderprofile.views;

import com.royalenfield.reprime.base.REMvpView;

/**
 * @author BOP1KOR on 12/27/2018.
 */

public interface MotorCycleProfileView extends REMvpView {

    void onBikeSelection();

    void onViewServiceHistory();

    void addNote();

    void onVisitNearestServiceCenter();

    void updateSpinnerList();
}

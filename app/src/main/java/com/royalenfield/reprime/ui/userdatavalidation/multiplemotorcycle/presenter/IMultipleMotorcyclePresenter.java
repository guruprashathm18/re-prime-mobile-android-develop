package com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.presenter;

import com.royalenfield.reprime.ui.userdatavalidation.multiplemotorcycle.view.MotorcyclesItem;

import java.util.ArrayList;

interface IMultipleMotorcyclePresenter {
    void onConfirmButtonClicked(ArrayList<MotorcyclesItem> mOriginalList, ArrayList<MotorcyclesItem> listData);
}

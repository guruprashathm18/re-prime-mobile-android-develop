package com.royalenfield.reprime.ui.barcode.view;

import com.google.gson.JsonObject;
import com.royalenfield.reprime.base.REMvpView;
import com.royalenfield.reprime.models.response.web.qr.Data;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface BarcodeReaderView extends REMvpView {

    void barCodeSuccess(Data jsonObject);

    void barCodeFailure(String errorMessage);



}

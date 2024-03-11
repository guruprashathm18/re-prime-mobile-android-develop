package com.royalenfield.reprime.ui.barcode.listeners;

import com.google.gson.JsonObject;
import com.royalenfield.reprime.models.response.web.qr.Data;
import com.royalenfield.reprime.models.response.web.qr.QrResponse;

/**
 * @author BOP1KOR on 12/19/2018.
 */

public interface OnBarcodeResponseListener {

    void onSuccess(Data data);

    void onFailure(String errorMessage);
}

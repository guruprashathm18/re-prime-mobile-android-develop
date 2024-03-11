package com.royalenfield.reprime.ui.barcode.interactor;

import com.royalenfield.reprime.ui.barcode.listeners.OnBarcodeResponseListener;
import com.royalenfield.reprime.ui.forgot.listeners.OnSendOtpResponseListener;


public interface IBarcodeReaderInteractor {


    void checkBarcode(String id, OnBarcodeResponseListener onBarcodeResponseListener);
}

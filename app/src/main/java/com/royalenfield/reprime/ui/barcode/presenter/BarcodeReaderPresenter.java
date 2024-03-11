package com.royalenfield.reprime.ui.barcode.presenter;

import com.google.gson.JsonObject;
import com.royalenfield.reprime.models.response.web.qr.Data;
import com.royalenfield.reprime.ui.barcode.interactor.BarcodeReaderInteractor;
import com.royalenfield.reprime.ui.barcode.listeners.OnBarcodeResponseListener;
import com.royalenfield.reprime.ui.barcode.view.BarcodeReaderView;


public class BarcodeReaderPresenter implements OnBarcodeResponseListener, IBarcodeReaderPresenter {
    private static final String TAG = BarcodeReaderPresenter.class.getSimpleName();

    private BarcodeReaderView mBarcodeReaderView;
    private BarcodeReaderInteractor mBarcodeReaderInteractor;

    public BarcodeReaderPresenter(BarcodeReaderView mBarcodeReaderView, BarcodeReaderInteractor mBarcodeReaderInteractor) {
        this.mBarcodeReaderView = mBarcodeReaderView;
        this.mBarcodeReaderInteractor = mBarcodeReaderInteractor;
    }

    @Override
    public void checkBarCode(String userID) {
        //validate field should not be empty......
        //Show progress bar.
        if (mBarcodeReaderView != null) {
            mBarcodeReaderView.showLoading();
        }

        mBarcodeReaderInteractor.checkBarcode(userID, this);
    }


    @Override
    public void onDestroy() {
        mBarcodeReaderView = null;
    }


    @Override
    public void onSuccess(Data jObj) {
        if (mBarcodeReaderView != null) {
            mBarcodeReaderView.hideLoading();
            mBarcodeReaderView.barCodeSuccess(jObj);
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        if (mBarcodeReaderView != null) {
            mBarcodeReaderView.hideLoading();
            mBarcodeReaderView.barCodeFailure(errorMessage);
        }
    }


}

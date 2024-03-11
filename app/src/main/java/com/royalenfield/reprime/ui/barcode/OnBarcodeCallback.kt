package com.royalenfield.reprime.ui.barcode


interface OnBarcodeCallback{
     fun barCodeSuccess(value:String)
     fun barCodeFailure()
}
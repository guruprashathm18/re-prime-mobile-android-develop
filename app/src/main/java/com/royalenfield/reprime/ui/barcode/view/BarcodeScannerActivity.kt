package com.android.assignment.util.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.royalenfield.reprime.R
import com.royalenfield.reprime.base.REBaseActivity
import com.royalenfield.reprime.databinding.ActivityBarcodeScannerBinding
import com.royalenfield.reprime.models.response.web.qr.Data
import com.royalenfield.reprime.ui.barcode.OnBarcodeCallback
import com.royalenfield.reprime.ui.barcode.QrCodeAnalyzer
import com.royalenfield.reprime.ui.barcode.interactor.BarcodeReaderInteractor
import com.royalenfield.reprime.ui.barcode.presenter.BarcodeReaderPresenter
import com.royalenfield.reprime.ui.barcode.view.BarcodeReaderView
import com.royalenfield.reprime.ui.barcode.view.BarcodeSuccessActivity
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BarcodeScannerActivity : REBaseActivity(),BarcodeReaderView, OnBarcodeCallback {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeBoxView: BarcodeBoxView
    private var flashMode: Int = ImageCapture.FLASH_MODE_OFF
    private lateinit var mBarcodePresenter:BarcodeReaderPresenter
    private lateinit var binding :ActivityBarcodeScannerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val paramsScr = Bundle()
        paramsScr.putString("screenname", "QR Code Scanner")
        REUtils.logGTMEvent(REConstants.KEY_SCREEN_GTM, paramsScr)
        //instantiate presenter.
        mBarcodePresenter = BarcodeReaderPresenter(this, BarcodeReaderInteractor())

        cameraExecutor = Executors.newSingleThreadExecutor()

        barcodeBoxView = BarcodeBoxView(this)
        addContentView(
            barcodeBoxView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        binding.imageView2.setOnClickListener {
            val params = Bundle()
            params.putString("eventCategory", "QR code")
            params.putString("eventAction", "Cancel clicked")
            REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params)
            finish()
        }
        checkCameraPermission()
    }

    override fun onDestroy() {
        super.onDestroy()

        cameraExecutor.shutdown()
    }

    /**
     * This function is executed once the user has granted or denied the missing permission
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    /**
     * This function is responsible to request the required CAMERA permission
     */
    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    /**
     * This function will check if the CAMERA permission has been granted.
     * If so, it will call the function responsible to initialize the camera preview.
     * Otherwise, it will raise an alert.
     */
    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted: start the preview
            startCamera()
        } else {
            // Permission denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )) {
                // case 4 User has denied permission but not permanently
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setTitle("Permission required")
                builder.setMessage("This application needs to access the camera to process barcode")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                    checkCameraPermission()
                }
                builder.show()
            } else {
                // case 5. Permission denied permanently.
                // You can open Permission setting's page from here now.
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setTitle("Permission required")
                builder.setMessage("This application needs to access the camera to process barcode, Enable the permission from settings")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
                builder.setNegativeButton("Cancel") { dialog, which ->
                 finish()
                }

                builder.setPositiveButton("Go to Settings") { dialog, which ->
                    val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                builder.show()

            }


//            MaterialAlertDialogBuilder(this)
//                .setTitle("Permission required")
//                .setMessage("This application needs to access the camera to process barcodes")
//                .setPositiveButton("Ok") { _, _ ->
//                    // Keep asking for permission until granted
//                    checkCameraPermission()
//                }
//                .setCancelable(false)
//                .create()
//                .apply {
//                    setCanceledOnTouchOutside(false)
//                    show()
//                }
        }
    }

    /**
     * This function is responsible for the setup of the camera preview and the image analyzer.
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        QrCodeAnalyzer(
                            this,
                            barcodeBoxView,
                            binding.previewView.width.toFloat(),
                            binding.previewView.height.toFloat(), this
                        )
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                var cam: Camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

                binding.torch.setOnClickListener {
                    val params = Bundle()
                    params.putString("eventCategory", "QR code")
                    params.putString("eventAction", "Torch clicked")
                    REUtils.logGTMEvent(REConstants.KEY_SUPPORT_GTM, params)
                    if (cam.getCameraInfo().hasFlashUnit()) {
                        when (flashMode) {
                            ImageCapture.FLASH_MODE_OFF -> {
                                flashMode = ImageCapture.FLASH_MODE_ON
                                cam.getCameraControl().enableTorch(true);
                            }

                            ImageCapture.FLASH_MODE_ON -> {
                                flashMode = ImageCapture.FLASH_MODE_OFF
                                cam.getCameraControl().enableTorch(false);
                            }

                            ImageCapture.FLASH_MODE_AUTO -> {
                                flashMode = ImageCapture.FLASH_MODE_OFF
                                cam.getCameraControl().enableTorch(false);
                            }

                        }
                    }
                }
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun barCodeSuccess(value: String) {

        mBarcodePresenter.checkBarCode(value)
    }

    override fun barCodeFailure() {
        Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show()
    }

    override fun barCodeSuccess(data: Data) {

           var params:Bundle = Bundle()
            params.putString("eventCategory", "Spares Genuinity Check")
            params.putString("eventAction", "Success")
        params.putString("eventLabel", data.partName)
        params.putString("dealerName", data.dealerName)
            REUtils.logGTMEvent(REConstants.KEY_SPARE_GENUINITY_GTM, params)
            val intentSuccess = Intent(
                this,
                BarcodeSuccessActivity::class.java
            )
        intentSuccess.putExtra(
            "DATA",
            Gson().toJson(data)
        )
            startActivityForResult(intentSuccess, 2)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
         //   finish()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 2) {
          startCamera()
        }

    }

    override fun barCodeFailure( errorMessage: String?) {
      var   msg:String?=errorMessage
        if(errorMessage.equals(""))
            msg=resources.getString(R.string.sorry_please_try_again)
        var params:Bundle = Bundle()
        params.putString("eventCategory", "Spares Genuinity Check")
        params.putString("eventAction", "Fail")
        params.putString("eventLabel", "NA")
        params.putString("dealerName", "NA")
        params.putString("errorMessage", msg?.substring(0, msg.length.coerceAtMost(100)))
        REUtils.logGTMEvent(REConstants.KEY_SPARE_GENUINITY_GTM, params)
        REUtils.showErrorDialog(
            this,
            msg,
            { checkCameraPermission() })

    }

}
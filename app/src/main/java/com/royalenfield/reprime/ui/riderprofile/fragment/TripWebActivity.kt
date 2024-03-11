package com.royalenfield.reprime.ui.riderprofile.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import com.facebook.FacebookSdk.getApplicationContext
import com.royalenfield.reprime.R
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.databinding.FragmentCommunityWebViewBinding
import com.royalenfield.reprime.databinding.FragmentTripWebViewBinding
import com.royalenfield.reprime.ui.wonderlust.interfaces.CommonWebAppInterface
import com.royalenfield.reprime.ui.wonderlust.interfaces.WebviewListener
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils
import com.royalenfield.reprime.utils.RELog
import java.util.*

class TripWebActivity : REBaseFragment(), WebviewListener {

    private lateinit var viewOfLayout: View
    private val permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val EXTERNAL_WRITE_PERMISSIONS = 10
    private var url: String? =
        null
    private  var userAgent:String? = null
    private  var contentDisposition:String? = null
    private  var mimeType:String? = null
 private  var urlToLoad: String?=null
    private var _binding: FragmentTripWebViewBinding?=null
    private  val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=FragmentTripWebViewBinding.inflate(inflater, container, false)
        urlToLoad=arguments?.getString("url")
        initViews(_binding?.root)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(viewOfLayout)
    }
    public fun initViews(rootView: View?) {
        var webView: WebView?=_binding?.webview
        var setting =_binding?.webview?.settings
        // viewOfLayout.webview.setBackgroundColor(Color.parseColor("#141518"))
       setting?.builtInZoomControls=true
        setting?.displayZoomControls = false
        if (REUtils.isNetworkAvailable(getApplicationContext())) {
           setting?.setCacheMode(WebSettings.LOAD_DEFAULT)//Decide whether to retrieve data from the network based on cache-control.
        } else {
           setting?.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)//If there is no network, it is obtained locally, i.e. offline loading.
        }
       setting?.setDomStorageEnabled(true) // Turn on the DOM storage API function
       setting?.setDatabaseEnabled(true)   //Turn on the database storage API function
      // setting?.setAppCacheEnabled(true)//Open Application Caches

        //Setting up the Application Caches cache directory
        webView?.setVerticalScrollBarEnabled(true);
        webView?.setHorizontalScrollBarEnabled(true);
       setting?.setSupportZoom(false)
       setting?.setLoadWithOverviewMode(true)
       setting?.setUseWideViewPort(true)
       setting?.setJavaScriptCanOpenWindowsAutomatically(true)
       setting?.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
       setting?.setRenderPriority(WebSettings.RenderPriority.HIGH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            // older android version, disable hardware acceleration
            webView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
        webView?.webViewClient=WebViewClient()
        webView?.setVisibility(View.VISIBLE)
        setting?.setJavaScriptEnabled(true)
        setting?.setBuiltInZoomControls(true)
        webView?.requestFocusFromTouch()
        webView?.addJavascriptInterface(
            CommonWebAppInterface(
                baseActivity,
                webView,
                this
            ), "Android"
        )
        webView?.setDownloadListener(DownloadListener { url: String, userAgent: String, contentDisposition: String, mimeType: String, contentLength: Long ->
            this.url = url
            this.userAgent = userAgent
            this.contentDisposition = contentDisposition
            this.mimeType = mimeType
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions(url, userAgent, contentDisposition, mimeType, contentLength)
            } else downLoadFile(url, userAgent, contentDisposition, mimeType)
        })

        webView?.loadUrl(urlToLoad?:REConstants.TRIPS_URL)


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkPermissions(
        url: String,
        userAgent: String,
        contentDisposition: String,
        mimeType: String,
        contentLength: Long
    ): Boolean {
        val result: Int
        var p: Int
        val listPermissionsNeeded: List<String> =
            ArrayList()
        result = activity?.checkSelfPermission(permissions)!!
        if (result != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_WRITE_PERMISSIONS
            )
        } else {
            downLoadFile(url, userAgent, contentDisposition, mimeType)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                EXTERNAL_WRITE_PERMISSIONS
            )
            return false
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        try {
            when (requestCode) {
                EXTERNAL_WRITE_PERMISSIONS ->                     // If request is cancelled, the result arrays are empty.
                    if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        downLoadFile(url, userAgent, contentDisposition, mimeType)
                    } else {
                    }
            }
        } catch (e: NullPointerException) {
            RELog.e(e)
        } catch (e: Exception) {
            RELog.e(e)
        }
    }

    private fun downLoadFile(
        url: String?,
        userAgent: String?,
        contentDisposition: String?,
        mimeType: String?
    ) {
//
        val request = DownloadManager.Request(
            Uri.parse(url)
        )
        request.setMimeType(mimeType)
        val cookies = CookieManager.getInstance().getCookie(url)
        request.addRequestHeader("cookie", cookies)
        request.addRequestHeader("User-Agent", userAgent)
        request.setDescription("Downloading File...")
        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                url, contentDisposition, mimeType
            )
        )
        val dm =
            activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)
        // webView.loadUrl(CommonWebAppInterface.getBase64StringFromBlobUrl(url));
        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show()
    }


    companion object {
        fun newInstance(): TripWebActivity {
            return TripWebActivity()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun showLoginActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun finishWebView(error: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun chnageOrientation(type: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToScreen(id: String?) {
        TODO("Not yet implemented")
    }

    override fun showHideKeyboard(isShown: Boolean) {
        TODO("Not yet implemented")
    }
}
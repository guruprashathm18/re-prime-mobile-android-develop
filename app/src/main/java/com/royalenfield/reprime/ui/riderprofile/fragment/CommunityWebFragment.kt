package com.royalenfield.reprime.ui.riderprofile.fragment;



import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import com.facebook.FacebookSdk.getApplicationContext
import com.royalenfield.reprime.R
import com.royalenfield.reprime.base.REBaseFragment
import com.royalenfield.reprime.databinding.FragmentCommunityWebViewBinding
import com.royalenfield.reprime.ui.custom.views.TitleBarView
import com.royalenfield.reprime.ui.home.homescreen.view.REHomeActivity
import com.royalenfield.reprime.ui.phoneconfigurator.utils.PCUtils
import com.royalenfield.reprime.ui.wonderlust.interfaces.CommonWebAppInterface
import com.royalenfield.reprime.ui.wonderlust.interfaces.WebviewListener
import com.royalenfield.reprime.utils.REConstants
import com.royalenfield.reprime.utils.REUtils
import com.royalenfield.reprime.utils.RELog
import java.util.*


class CommunityWebFragment : REBaseFragment(), WebviewListener ,
    TitleBarView.OnNavigationIconClickListener {

    private var progresssDialog: ProgressDialog? = null
    private val permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val EXTERNAL_WRITE_PERMISSIONS = 10
    private var url: String? =
        null
    private  var userAgent:String? = null
    private  var contentDisposition:String? = null
    private  var mimeType:String? = null
    private  var urlToLoad: String?=null
    private var _binding: FragmentCommunityWebViewBinding?=null
    private  val binding get()=_binding!!

    // variables para manejar la subida de archivos
    private val FILECHOOSER_RESULTCODE = 1
    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentCommunityWebViewBinding.inflate(inflater, container, false)
        //   viewOfLayout = inflater!!.inflate(R.layout.fragment_community_web_view, container, false)


        urlToLoad=arguments?.getString("url")
        initViews(_binding?.root)


        return _binding?.root
    }
    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initViews(_binding?.root)

    }

    override fun onNavigationIconClick() {
        if (_binding?.webview1?.canGoBack() == true) {
            _binding?.webview1?.goBack();
        }

    }

    public fun initViews(rootView: View?) {
        // viewOfLayout.webview1.setBackgroundColor(Color.parseColor("#141518"))
        _binding?.titleBarView1?.bindData(
            activity,
            R.drawable.back_arrow,
            arguments?.getString("title")
        )
        var webView: WebView?=_binding?.webview1
        var setting =_binding?.webview1?.settings
        _binding?.titleBarView1?.ivNavButton?.visibility=View.GONE
        _binding?.titleBarView1?.setInterface(this)
        setting?.setBuiltInZoomControls(true)
        _binding?.webview1?.getSettings()?.setDisplayZoomControls(false)
        if (REUtils.isNetworkAvailable(getApplicationContext())) {
            setting?.setCacheMode(WebSettings.LOAD_DEFAULT)//Decide whether to retrieve data from the network based on cache-control.
        } else {
            setting?.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)//If there is no network, it is obtained locally, i.e. offline loading.
        }
        setting?.setDomStorageEnabled(true) // Turn on the DOM storage API function
        setting?.setDatabaseEnabled(true)   //Turn on the database storage API function
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
        webView?.webViewClient= object:WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                //  getBackForwardList()
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                getBackForwardList()
                if(activity!=null)
                    ( activity as REHomeActivity).bottom_navigation.visibility=View.VISIBLE
                if(progresssDialog==null&&context!=null)
                    progresssDialog=  PCUtils.showLoadingDialog(context)
                else if (progresssDialog?.isShowing != true&&context!=null)
                    progresssDialog = PCUtils.showLoadingDialog(context)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if(progresssDialog?.isShowing == true)
                    progresssDialog?.dismiss()
                progresssDialog?.hide()


            }

        }
        webView?.webChromeClient=object :WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if(newProgress>=80){
                    if(progresssDialog?.isShowing == true)
                        progresssDialog?.dismiss()
                    progresssDialog?.hide()

                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                // asegurar que no existan callbacks
                // asegurar que no existan callbacks
                mUploadMessage?.onReceiveValue(null)

                mUploadMessage = filePathCallback

                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.setType("*/*") // set MIME type to filter


                startActivityForResult(
                    Intent.createChooser(
                        i,
                        "File Chooser"
                    ), FILECHOOSER_RESULTCODE
                )

                return true
            }
        }
        webView?.setVisibility(View.VISIBLE)
        setting?.setJavaScriptEnabled(true)
        setting?.setBuiltInZoomControls(true)
        webView?.requestFocusFromTouch()
        webView?.addJavascriptInterface(
            CommonWebAppInterface(
                activity as REHomeActivity,
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
                checkPermissions(
                    url,
                    userAgent,
                    contentDisposition,
                    mimeType,
                    contentLength
                )
            } else downLoadFile(url, userAgent, contentDisposition, mimeType)
        })

        webView?.loadUrl(urlToLoad ?: REConstants.TRIPS_URL)


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

    override fun onResume() {
        super.onResume()
        if (progresssDialog != null && progresssDialog?.isShowing() == true) {
            progresssDialog?.dismiss();
            progresssDialog?.hide();
        }

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
        // webview1.loadUrl(CommonWebAppInterface.getBase64StringFromBlobUrl(url));
        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show()
    }


    companion object {
        fun newInstance(): CommunityWebFragment {
            return CommunityWebFragment()
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

    fun getBackForwardList() {
        try {

            if (_binding?.webview1?.canGoBack() == true) {
                _binding?.titleBarView1?.ivNavButton?.visibility = View.VISIBLE
            } else {
                _binding?.titleBarView1?.ivNavButton?.visibility = View.GONE
            }

//        val currentList: WebBackForwardList = webView?.copyBackForwardList()
//        val currentSize = currentList.size
//        for (i in 0 until currentSize) {
//            val item = currentList.getItemAtIndex(i)
//            val url = item.url
//            RELog.e("SIZE"+currentSize.toString())
//        }
        }
        catch (e:Exception){

        }
    }

    override fun onPause() {
        super.onPause()
        if(progresssDialog?.isShowing == true) {
            progresssDialog?.dismiss();
            progresssDialog?.hide();
        }

    }

    override fun navigateToScreen(id: String?) {
        var jsonObject = com.google.gson.JsonObject()
        jsonObject.addProperty("id", id)
        (activity as REHomeActivity).initAppIndexManager(jsonObject)
    }

    override fun showHideKeyboard(isShown: Boolean) {
        if(isShown)
            ( activity as REHomeActivity).bottom_navigation.visibility=View.VISIBLE
        else
            ( activity as REHomeActivity).bottom_navigation.visibility=View.GONE
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {

        // manejo de seleccion de archivo
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage || intent == null || resultCode != RESULT_OK) {
                return
            }
            var result: Array<Uri>? = null
            val dataString = intent.dataString
            if (dataString != null) {
                result = arrayOf(Uri.parse(dataString))
            }
            mUploadMessage?.onReceiveValue(result)
            mUploadMessage = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
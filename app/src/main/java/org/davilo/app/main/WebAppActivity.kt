package org.davilo.app.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_completed_app.view.*
import org.davilo.app.R
import org.davilo.app.Repository
import org.davilo.app.databinding.ActivityWebAppBinding
import org.davilo.app.databinding.DialogCompletedAppBinding
import org.davilo.app.model.App
import org.davilo.app.model.AppPreferences
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class WebAppActivity : AppCompatActivity() {


    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityWebAppBinding

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var appPreferences: AppPreferences
    lateinit var app: App

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        AndroidUtilities.applicationHandler = Handler(mainLooper);
        app = Gson().fromJson(intent.extras?.getString("json"), App::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_app)
        var webView = binding.webApp
        title = app.description
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");

        val dir: File = getCacheDir()

        if (dir != null && dir.isDirectory()) {
            try {
                val children: Array<File> = dir.listFiles()
                if (children.size > 0) {
                    for (i in children.indices) {
                        val temp: Array<File> = children[i].listFiles()
                        for (x in temp.indices) {
                            temp[x].delete()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Cache", "failed cache clean")
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.settings.mediaPlaybackRequiresUserGesture = false
        }
        webView.settings.loadWithOverviewMode = false
        webView.settings.useWideViewPort = true
        webView.settings.displayZoomControls = true
        webView.settings.builtInZoomControls = false
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.clearCache(true)
        webView.settings.saveFormData = false;
        webView.clearHistory();
        webView.addJavascriptInterface(
            WebViewJavaScriptInterface(context = this),
            "androidApp"
        )
        webView.loadUrl(
            app.url + "&script=https://faraz.s3.ir-thr-at1.arvanstorage.com/web20.js"
        );
        binding.buttonDone.setOnClickListener {
            onAppCompleted()
        }
    }

    fun onAppCompleted() {
        repository.completeApp(app.id).subscribe({}, {})
        NotificationCenter.getInstance().postNotificationName(NotificationCenter.appDone, app.id)
        SolvedAlertDialog().show()

    }

    inner class WebViewJavaScriptInterface(private val context: Context) {

        @JavascriptInterface
        fun appDone() {
            AndroidUtilities.runOnUIThread {
                onAppCompleted()
            }
        }


    }

    inner class SolvedAlertDialog(

    ) : AlertDialog(this) {
        init {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var binding = DialogCompletedAppBinding.inflate(inflater)
            setView(binding.root)
            binding.root.continueBottom.setOnClickListener { dismiss() }
            setOnDismissListener { finish() }



        }
    }

    override fun onDestroy() {
        binding.webApp.destroy()
        super.onDestroy()
    }
}

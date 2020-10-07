package org.davilo.app.ui.activity.webApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.ActivityWebAppBinding
import org.davilo.app.databinding.DialogCompletedAppBinding
import org.davilo.app.model.App
import org.davilo.app.ui.base.BaseActivity
import org.davilo.app.utils.AndroidUtilities
import org.davilo.app.utils.NotificationCenter
import java.io.File


@AndroidEntryPoint
class ActivityWebApp : BaseActivity<ActivityWebAppBinding>(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_web_app

    private val viewModel: ActivityWebAppViewModel by viewModels()

    private var app: App? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        AndroidUtilities.applicationHandler = Handler(mainLooper)

        deleteDatabase("webview.db")
        deleteDatabase("webviewCache.db")
        deleteCacheDirs()

        getExtras()

        initViews()

        observeLiveData()
    }

    private fun getExtras() {
        app = Gson().fromJson(intent.extras?.getString(EXTRA_JSON), App::class.java)
    }

    private fun initViews() {

        title = app?.description

        configWebView()

        binding.buttonDone.setOnClickListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configWebView() {

        binding.webApp.settings.javaScriptEnabled = true
        binding.webApp.settings.domStorageEnabled = true
        binding.webApp.settings.allowContentAccess = true
        binding.webApp.settings.allowFileAccess = true
        binding.webApp.settings.allowFileAccessFromFileURLs = true
        binding.webApp.settings.allowUniversalAccessFromFileURLs = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            binding.webApp.settings.mediaPlaybackRequiresUserGesture = false
        }
        binding.webApp.settings.loadWithOverviewMode = false
        binding.webApp.settings.useWideViewPort = true
        binding.webApp.settings.displayZoomControls = true
        binding.webApp.settings.builtInZoomControls = false
        binding.webApp.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webApp.settings.pluginState = WebSettings.PluginState.ON
        binding.webApp.clearCache(true)
        binding.webApp.settings.saveFormData = false
        binding.webApp.clearHistory()
        binding.webApp.addJavascriptInterface(
            WebViewJavaScriptInterface(context = this),
            "androidApp"
        )
        binding.webApp.loadUrl(
            app?.url + "&script=https://faraz.s3.ir-thr-at1.arvanstorage.com/web20.js"
        )
    }

    private fun observeLiveData() {
        viewModel.completeAppLiveData.observe(this, Observer {

        })
        viewModel.errorLiveData.observe(this, Observer {

        })
    }

    private fun deleteCacheDirs() {
        if (cacheDir.isDirectory) {
            try {
                val children: Array<File>? = cacheDir.listFiles()
                if (children.orEmpty().isNotEmpty()) {
                    for (i in children.orEmpty().indices) {
                        val temp: Array<File>? = children?.get(i)?.listFiles()
                        for (j in temp.orEmpty().indices) {
                            temp?.get(j)?.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Cache", "failed cache clean")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.webApp -> {
                onAppCompleted()
            }
        }
    }

    override fun onDestroy() {
        binding.webApp.destroy()
        super.onDestroy()
    }

    private fun onAppCompleted() {

        viewModel.completeApp(app?.id)

        NotificationCenter.getInstance().postNotificationName(
            NotificationCenter.appDone, app?.id
        )
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

    inner class SolvedAlertDialog : AlertDialog(this) {
        init {
            val binding =
                DialogCompletedAppBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            setView(binding.root)
            binding.continueBottom.setOnClickListener { dismiss() }
            setOnDismissListener { finish() }
        }
    }

    companion object {

        const val EXTRA_JSON = "_EXTRA.JSON"

        fun navigate(context: Context?, json: String) {
            context?.startActivity(Intent(context, ActivityWebApp::class.java).apply {
                putExtra(EXTRA_JSON, json)
            })
        }
    }

}

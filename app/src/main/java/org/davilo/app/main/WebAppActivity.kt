package org.davilo.app.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.davilo.app.R
import org.davilo.app.databinding.ActivityWebAppBinding
import org.davilo.app.model.App
import org.davilo.app.model.AppPreferences
import javax.inject.Inject

@AndroidEntryPoint
class WebAppActivity : AppCompatActivity() {


    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityWebAppBinding

    @Inject
    lateinit var appPreferences: AppPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var app: App = Gson().fromJson(intent.extras?.getString("json"), App::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_app)
        var webView = binding.webApp
        title = app.description
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = false
        webView.settings.allowContentAccess = false
        webView.settings.allowFileAccess = false
        webView.settings.allowFileAccessFromFileURLs = false
        webView.settings.allowUniversalAccessFromFileURLs = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.settings.mediaPlaybackRequiresUserGesture = false
        }
        webView.settings.loadWithOverviewMode = false
        webView.settings.useWideViewPort = true
        webView.settings.displayZoomControls = true
        webView.settings.builtInZoomControls = false
        webView.settings.javaScriptCanOpenWindowsAutomatically = false
        webView.settings.pluginState = WebSettings.PluginState.ON
        webView.loadUrl(app.url);

    }


}

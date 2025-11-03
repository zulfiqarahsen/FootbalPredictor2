package com.apps.footbalpredictor.hsgrjs

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.apps.footbalpredictor.hsgrjs.utils.PreferencesManager

class FootballScreenActivity : AppCompatActivity() {
    
    private lateinit var contentDisplayView: WebView
    private lateinit var loadingContainer: FrameLayout
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var preferencesManager: PreferencesManager
    private var isInitialLoad = true
    private var initialPageAddress: String? = null
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_football_screen)
        
        supportActionBar?.hide()
        setupFullscreenMode()
        
        preferencesManager = PreferencesManager(this)
        
        contentDisplayView = findViewById(R.id.content_display_view)
        loadingContainer = findViewById(R.id.loading_container)
        loadingIndicator = findViewById(R.id.loading_indicator)
        
        setupContentDisplayView()
        setupBackPressHandler()
        
        val destinationLink = preferencesManager.getDestinationLink()
        if (destinationLink != null) {
            loadContent(destinationLink)
        } else {
            finish()
        }
    }
    
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (contentDisplayView.canGoBack()) {
                    val currentAddress = getCurrentPageAddress()
                    if (currentAddress != initialPageAddress) {
                        contentDisplayView.goBack()
                    }
                }
            }
        })
    }
    
    private fun getCurrentPageAddress(): String? {
        return contentDisplayView.url
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupContentDisplayView() {
        contentDisplayView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            setSupportZoom(true)
            builtInZoomControls = false
            loadWithOverviewMode = true
            useWideViewPort = true
            javaScriptCanOpenWindowsAutomatically = true
            mediaPlaybackRequiresUserGesture = false
            allowFileAccess = true
            allowContentAccess = true
        }
        
        contentDisplayView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(display: WebView?, address: String?, favicon: Bitmap?) {
                super.onPageStarted(display, address, favicon)
                if (isInitialLoad) {
                    showLoading()
                }
            }
            
            override fun onPageFinished(display: WebView?, address: String?) {
                super.onPageFinished(display, address)
                if (isInitialLoad) {
                    hideLoading()
                    isInitialLoad = false
                    initialPageAddress = address
                }
            }
            
            override fun shouldOverrideUrlLoading(display: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                return false
            }
        }
        
        contentDisplayView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(display: WebView?, newProgress: Int) {
                super.onProgressChanged(display, newProgress)
                if (newProgress == 100 && isInitialLoad) {
                    hideLoading()
                    isInitialLoad = false
                }
            }
        }
    }
    
    private fun loadContent(link: String) {
        showLoading()
        loadPageByAddress(link)
    }
    
    private fun loadPageByAddress(address: String) {
        contentDisplayView.loadUrl(address)
    }
    
    private fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
    }
    
    private fun hideLoading() {
        loadingContainer.visibility = View.GONE
    }
    
    override fun onResume() {
        super.onResume()
        setupFullscreenMode()
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            setupFullscreenMode()
        }
    }
    
    private fun setupFullscreenMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            )
        }
    }
    
}


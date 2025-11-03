package com.apps.footbalpredictor.hsgrjs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.apps.footbalpredictor.hsgrjs.network.NetworkManager
import com.apps.footbalpredictor.hsgrjs.utils.DeviceInfoProvider
import com.apps.footbalpredictor.hsgrjs.utils.PreferencesManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var deviceInfoProvider: DeviceInfoProvider
    private lateinit var networkManager: NetworkManager
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        supportActionBar?.hide()
        setupFullscreenMode()
        
        preferencesManager = PreferencesManager(this)
        deviceInfoProvider = DeviceInfoProvider(this)
        networkManager = NetworkManager()
        
        checkAuthenticationAndProceed()
    }
    
    private fun checkAuthenticationAndProceed() {
        if (preferencesManager.hasAccessToken()) {
            Log.d(TAG, "Access token found, launching FootballScreenActivity")
            launchFootballScreen()
        } else {
            Log.d(TAG, "No access token found, making server request")
            setupMainMenu()
            makeServerRequest()
        }
    }
    
    private fun setupMainMenu() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
    
    private fun makeServerRequest() {
        lifecycleScope.launch {
            try {
                val os = deviceInfoProvider.getOperatingSystem()
                val language = deviceInfoProvider.getLanguage()
                val region = deviceInfoProvider.getRegion()
                val deviceModel = deviceInfoProvider.getDeviceModel()
                val batteryStatus = deviceInfoProvider.getBatteryStatus()
                val batteryLevel = deviceInfoProvider.getBatteryLevel()
                
                Log.d(TAG, "Device Info - OS: $os, Lang: $language, Region: $region, Model: $deviceModel, Battery: $batteryStatus/$batteryLevel")
                
                val response = networkManager.fetchServerResponse(
                    os, language, region, deviceModel, batteryStatus, batteryLevel
                )
                
                if (response != null && response.contains("#")) {
                    Log.d(TAG, "Valid response received with separator")
                    val parts = response.split("#", limit = 2)
                    if (parts.size == 2) {
                        val token = parts[0].trim()
                        val link = parts[1].trim()
                        
                        Log.d(TAG, "Token and link extracted successfully")
                        
                        preferencesManager.saveAccessToken(token)
                        preferencesManager.saveDestinationLink(link)
                        
                        launchFootballScreen()
                    } else {
                        Log.w(TAG, "Response format invalid")
                    }
                } else {
                    Log.d(TAG, "Response doesn't contain separator or is null")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during server request", e)
            }
        }
    }
    
    private fun launchFootballScreen() {
        val intent = Intent(this, FootballScreenActivity::class.java)
        startActivity(intent)
        finish()
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
    
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
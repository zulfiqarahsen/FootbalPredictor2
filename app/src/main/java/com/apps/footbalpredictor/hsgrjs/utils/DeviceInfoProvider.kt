package com.apps.footbalpredictor.hsgrjs.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import java.util.Locale

class DeviceInfoProvider(private val context: Context) {
    
    fun getOperatingSystem(): String {
        return "Android ${Build.VERSION.RELEASE}"
    }
    
    fun getLanguage(): String {
        return Locale.getDefault().language
    }
    
    fun getRegion(): String {
        return Locale.getDefault().country
    }
    
    fun getDeviceModel(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer, ignoreCase = true)) {
            model
        } else {
            "$manufacturer $model"
        }
    }
    
    fun getBatteryStatus(): String {
        val batteryIntent = context.registerReceiver(
            null, 
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        
        val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "NotCharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            else -> "Unknown"
        }
    }
    
    fun getBatteryLevel(): String {
        val batteryIntent = context.registerReceiver(
            null, 
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        
        if (level == -1 || scale == -1) {
            return "0.0"
        }
        
        val batteryPct = level / scale.toFloat()
        return String.format("%.2f", batteryPct)
    }
}


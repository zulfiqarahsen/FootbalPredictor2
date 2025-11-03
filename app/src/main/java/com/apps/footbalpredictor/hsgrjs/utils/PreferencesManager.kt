package com.apps.footbalpredictor.hsgrjs.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "football_prefs", 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_DESTINATION_LINK = "destination_link"
    }
    
    fun saveAccessToken(token: String) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }
    
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }
    
    fun saveDestinationLink(link: String) {
        prefs.edit().putString(KEY_DESTINATION_LINK, link).apply()
    }
    
    fun getDestinationLink(): String? {
        return prefs.getString(KEY_DESTINATION_LINK, null)
    }
    
    fun hasAccessToken(): Boolean {
        return getAccessToken() != null
    }
    
    fun clearData() {
        prefs.edit().clear().apply()
    }
}



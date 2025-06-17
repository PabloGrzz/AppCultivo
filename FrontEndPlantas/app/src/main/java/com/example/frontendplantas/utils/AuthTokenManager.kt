package com.example.frontendplantas.utils

import android.content.Context
import android.util.Log
import org.json.JSONObject

class AuthTokenManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val TAG = "AuthTokenManager"

    fun saveToken(token: String) {
        // Log the token being saved for debugging
        Log.d(TAG, "Saving token: $token")
        sharedPreferences.edit().putString("jwt_token", token).apply()

        // Verify the token was saved
        val savedToken = getToken()
        Log.d(TAG, "Verified saved token: $savedToken")

        // Log the role immediately after saving
        val role = getUserRole()
        Log.d(TAG, "User role after saving: $role")
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString("jwt_token", null)
        Log.d(TAG, "Retrieved token: $token")
        return token
    }

    fun clearToken() {
        Log.d(TAG, "Clearing token")
        sharedPreferences.edit().remove("jwt_token").apply()
    }

    fun getUserRole(): String? {
        val token = getToken()
        Log.d(TAG, "Getting user role from token: $token")

        return token?.let {
            try {
                val parts = it.split(".")
                Log.d(TAG, "Token has ${parts.size} parts")

                if (parts.size != 3) {
                    Log.e(TAG, "Invalid token format: doesn't have 3 parts")
                    return null
                }

                val payload = parts[1]

                // Add padding if needed
                val paddedPayload = when (payload.length % 4) {
                    0 -> payload
                    1 -> payload + "==="
                    2 -> payload + "=="
                    else -> payload + "="
                }

                try {
                    val payloadDecoded = String(
                        android.util.Base64.decode(paddedPayload, android.util.Base64.URL_SAFE),
                        Charsets.UTF_8
                    )
                    Log.d(TAG, "Decoded payload: $payloadDecoded")

                    val jsonObject = JSONObject(payloadDecoded)
                    val role = jsonObject.getString("role")
                    Log.d(TAG, "Extracted role: $role")
                    role
                } catch (e: Exception) {
                    Log.e(TAG, "Error decoding token payload", e)
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing token", e)
                null
            }
        }
    }
}
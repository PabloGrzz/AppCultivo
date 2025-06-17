package com.example.frontendplantas.core

import android.content.Context
import com.example.frontendplantas.network.AuthInterceptor
import com.example.frontendplantas.network.WebService
import com.example.frontendplantas.utils.AuthTokenManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var _webService: WebService? = null
    private lateinit var tokenManager: AuthTokenManager // 👈 Esta es la única declaración

    fun init(context: Context) {
        if (_webService == null) {
            tokenManager = AuthTokenManager(context) // 👈 Inicializa la propiedad de clase

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(tokenManager)) // 👈 Usa la propiedad
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(client)
                .build()

            _webService = retrofit.create(WebService::class.java)
        }
    }

    val webService: WebService
        get() = _webService ?: throw IllegalStateException("RetrofitClient no inicializado")

    fun updateToken(newToken: String) {
        tokenManager.saveToken(newToken) // 👈 Ahora funciona (tokenManager está inicializado)
    }

    fun clearToken() {
        tokenManager.clearToken()
    }
}
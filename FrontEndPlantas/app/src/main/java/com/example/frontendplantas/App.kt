package com.example.frontendplantas

import android.app.Application
import com.example.frontendplantas.core.RetrofitClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inicializás Retrofit una vez, con contexto de aplicación
        RetrofitClient.init(this)
    }
}

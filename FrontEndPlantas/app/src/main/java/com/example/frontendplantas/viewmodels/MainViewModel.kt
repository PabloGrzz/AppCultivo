package com.example.frontendplantas.viewmodels

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState.asStateFlow()

    private var scanner: GmsBarcodeScanner? = null
    var isScannerReady = false

    sealed class ScanState {
        object Idle : ScanState()
        object Initializing : ScanState()
        object Ready : ScanState()
        object Scanning : ScanState()
        data class Success(val content: String) : ScanState()
        data class Error(val message: String) : ScanState()
    }

    // Inicialización del escáner
    @OptIn(UnstableApi::class)
    fun initializeScanner(context: Context) {
        _scanState.value = ScanState.Initializing
        try {
            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            scanner = GmsBarcodeScanning.getClient(context, options)
            isScannerReady = true
            _scanState.value = ScanState.Ready
            Log.d("QRScanner", "Escáner inicializado correctamente")
        } catch (e: Exception) {
            _scanState.value = ScanState.Error("Error al inicializar el escáner: ${e.message}")
            Log.e("QRScanner", "Error al inicializar el escáner: ${e.message}")
        }
    }

    // Iniciar el escaneo
    @OptIn(UnstableApi::class)
    fun startScan() {
        if (!isScannerReady) {
            _scanState.value = ScanState.Error("Escáner no listo")
            return
        }

        _scanState.value = ScanState.Scanning
        Log.d("QRScanner", "Iniciando escaneo...")

        scanner?.startScan()
            ?.addOnSuccessListener { barcode ->
                barcode.rawValue?.let {
                    _scanState.value = ScanState.Success(it)
                    Log.d("QRScanner", "Escaneo exitoso: $it")
                } ?: run {
                    _scanState.value = ScanState.Error("El QR no contiene datos")
                }
            }
            ?.addOnFailureListener { e ->
                if (e.message?.contains("Waiting for the Barcode UI module to be downloaded") == true) {
                    _scanState.value = ScanState.Error("Descargando módulo de escaneo, por favor espera...")
                    Log.e("QRScanner", "Esperando descarga del módulo: ${e.message}")
                } else {
                    _scanState.value = ScanState.Error("Error al escanear: ${e.message}")
                    Log.e("QRScanner", "Error al escanear: ${e.message}")
                }
            }
            ?.addOnCanceledListener {
                _scanState.value = ScanState.Idle
                Log.d("QRScanner", "Escaneo cancelado por el usuario")
            }
    }

}
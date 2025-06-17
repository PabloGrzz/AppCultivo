package com.example.frontendplantas.viewmodels

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QRViewModel: ViewModel() {

    //Al final creo el QR en el back, no usare esto ... Ya lo tengo en el back

    private val _qrBitmap = mutableStateOf<Bitmap?>(null)
    val qrBitmap: State<Bitmap?> = _qrBitmap

    // Genera un Bitmap del código QR
    fun generateQRCode(content: String, width: Int = 400, height: Int = 400): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            width,
            height
        )

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }
        return bitmap
    }

}
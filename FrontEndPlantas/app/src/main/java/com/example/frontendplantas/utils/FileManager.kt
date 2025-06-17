package com.example.frontendplantas.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import java.util.Date

class FileManager (
    private val context : Context
){

    suspend fun saveImage(
        contentUri: Uri,
        fileName: String
    ){
        withContext(Dispatchers.IO){
            context.contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    context.openFileOutput(fileName,Context.MODE_PRIVATE)
                        .use { outputStream ->
                            inputStream.copyTo(outputStream)

                        }
                }
        }
    }

    suspend fun saveImage(bytes: ByteArray, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                    outputStream.write(bytes)
                    Log.d("FILE_DEBUG", "✅ Archivo guardado en: ${context.filesDir}/$fileName")
                    Log.d("FILE_DEBUG", "📏 Tamaño: ${bytes.size} bytes")
                    true
                }
            } catch (e: Exception) {
                Log.e("FILE_DEBUG", "❌ Error al guardar: ${e.message}")
                false
            }
        }
    }

    suspend fun saveAndVerifyImage(bytes: ByteArray, fileName: String) {

        saveImage(bytes, fileName)

        debugFileInfo(fileName)

        try {
            val internalFile = File(context.filesDir, fileName)
            val externalPath = File(
                context.getExternalFilesDir(null),
                "export_$fileName"
            )

            internalFile.copyTo(externalPath, overwrite = true)

            Log.d("EXPORT_SUCCESS", "Archivo exportado a: ${externalPath.absolutePath}")
        } catch (e: Exception) {
            Log.e("EXPORT_ERROR", "No se pudo exportar", e)
        }
    }

    fun debugFileInfo(fileName: String) {
        val file = File(context.filesDir, fileName)

        val info = """
        ===== DEBUG FILE INFO =====
        📁 Ruta completa: ${file.absolutePath}
        ✅ Existe: ${file.exists()}
        📏 Tamaño: ${file.length()} bytes
        🕒 Última modificación: ${Date(file.lastModified())}
        👉 Permisos: ${file.canRead()}/${file.canWrite()}
    """.trimIndent()

        Log.d("FILE_DEBUG", info)

        Toast.makeText(
            context,
            "Debug info:\n${file.absolutePath}\nSize: ${file.length()} bytes",
            Toast.LENGTH_LONG
        ).show()
    }

    /*suspend fun saveImage(
        bytes: ByteArray,
        fileName: String
    ){
        withContext(Dispatchers.IO){
            context.openFileOutput(fileName, Context.MODE_PRIVATE)
                .use { outputStream ->
                    outputStream.write(bytes)

                }
        }
    }*/

    suspend fun createTempFile(bytes: ByteArray): File {
        return withContext(Dispatchers.IO) {
            val file = File.createTempFile("upload_", ".jpg")
            file.outputStream().use { output ->
                output.write(bytes)
            }
            file
        }
    }

    fun rememberQrBitmapFromBase64(base64: String): ImageBitmap {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap.asImageBitmap()
    }

    fun base64ToImageBitmap(base64: String): ImageBitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedBytes)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            Log.e("FILE_MANAGER", "Error al convertir base64 a imagen: ${e.message}")
            null
        }
    }
}
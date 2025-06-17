package com.example.frontendplantas.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendplantas.core.RetrofitClient
import com.example.frontendplantas.models.Imagenes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImagenesViewModel : ViewModel() {

    private val _listaImagenesGenerales = MutableLiveData<List<Imagenes>>()
    val listaImagenesGenerales: LiveData<List<Imagenes>> get() = _listaImagenesGenerales

    private val _listaImagenesPersonales = MutableLiveData<List<Imagenes>>()
    val listaImagenesPersonales: LiveData<List<Imagenes>> get() = _listaImagenesPersonales

    private val _imagenSubida = MutableLiveData<Boolean>()
    val imagenSubida: LiveData<Boolean> get() = _imagenSubida

    private val _errorSubida = MutableLiveData<String>()
    val errorSubida: LiveData<String> get() = _errorSubida

    fun subirImagenGeneral(plantaId: Long, imageBytes: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Guardar en archivo temporal
                val tempFile = File.createTempFile("upload", ".jpg")
                tempFile.writeBytes(imageBytes)

                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData(
                    "archivo",
                    tempFile.name,
                    requestFile
                )

                val response = RetrofitClient.webService.subirImagenGeneral(
                    plantaId = plantaId,
                    imagen = body,
                   /* token = "Bearer TU_TOKEN_AQUI"*/
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _imagenSubida.value = true
                    } else {
                        _errorSubida.value = "Error al subir la imagen: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorSubida.value = "Error: ${e.message}"
                }
            }
        }
    }



    fun obtenerImagenesGeneralesDeUnaPlanta(plantaId : Long){
        viewModelScope.launch(Dispatchers.IO){
            val response = RetrofitClient.webService.obtenerImagenesGenerales(plantaId)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    _listaImagenesGenerales.value = response.body()
                }
            }else{
                withContext(Dispatchers.Main){
                    _listaImagenesGenerales.value = emptyList()
                }
            }
        }
    }

    fun obtenerImagenesPersonalesDeUnaPlanta(plantaId : Long, usuarioId : Long){
        viewModelScope.launch(Dispatchers.IO){
            val response = RetrofitClient.webService.obtenerImagenesPersonales(plantaId,usuarioId)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    _listaImagenesPersonales.value = response.body()
                }
            }else{
                withContext(Dispatchers.Main){
                    _listaImagenesPersonales.value = emptyList()
                }
            }
        }
    }
}

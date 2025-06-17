package com.example.frontendplantas.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Imagenes(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("datos")
    val datos: String?, // Los datos de la imagen en formato Base64 como String (para ser enviado como JSON)

    @SerializedName("nombreArchivo")
    val nombreArchivo: String,

    @SerializedName("tipoMime")
    val tipoMime: String,

    @SerializedName("fechaSubida")
    val fechaSubida: List<Int>,

    @SerializedName("planta")
    var planta: Planta? = null, // Ahora es un objeto de tipo Planta

    @SerializedName("usuario")
    var usuario: Users? = null, // Ahora es un objeto de tipo Users

    @SerializedName("tipo")
    var tipo: TipoImagen // Enum para el tipo de imagen
) {
    // Enum que puede ser convertido directamente con Gson
    enum class TipoImagen {
        GENERAL, PERSONAL
    }
}

package com.example.frontendplantas.models

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("id")
    val id: Int? = null, // El backend se encarga del ID

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("plantasUsuario")
    val plantasUsuario: List<Planta> = mutableListOf(),

    @SerializedName("imagenes")
    var imagenes: List<Imagenes> = mutableListOf()
) {

    fun agregarImagenPersonal(imagen: Imagenes, planta: Planta) {
        imagen.tipo = Imagenes.TipoImagen.PERSONAL
        imagen.planta = planta
        imagen.usuario = this
        imagenes += imagen
    }

    override fun toString(): String {
        return "Users(id=$id, username='$username')"
    }
}


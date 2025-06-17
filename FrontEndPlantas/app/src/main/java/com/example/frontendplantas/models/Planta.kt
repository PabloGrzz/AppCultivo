package com.example.frontendplantas.models

import com.google.gson.annotations.SerializedName

data class Planta(
    @SerializedName("id")
    val id: Long? = null, // El backend asigna el ID

    @SerializedName("nombre")
    val nombre: String? = null, // Ahora es nullable con valor predeterminado

    @SerializedName("nombreCientifico")
    val nombreCientifico: String? = null, // Ahora es nullable con valor predeterminado

    @SerializedName("familia")
    val familia: String = "",

    @SerializedName("frecuenciaRiego")
    val frecuenciaRiego: String = "",

    @SerializedName("temperatura")
    val temperatura: String = "",

    @SerializedName("humedad")
    val humedad: String = "",

    @SerializedName("suelo")
    val suelo: String = "",

    @SerializedName("fertilizacion")
    val fertilizacion: String = "",

    @SerializedName("beneficios")
    val beneficios: String = "",

    @SerializedName("toxicidad")
    val toxicidad: String = "",

    @SerializedName("precio")
    val precio: Double = 0.0,

    @SerializedName("imagenes")
    val imagenes: List<Imagenes> = emptyList(),

    @SerializedName("lotes")
    val lotes: List<Lote> = emptyList()
)
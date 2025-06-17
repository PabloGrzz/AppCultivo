package com.example.frontendplantas.models

import com.google.gson.annotations.SerializedName

data class Lote(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("numeroLote")
    val numeroLote: Int? = null,

    @SerializedName("procesos")
    val procesos: List<ProcesoConFecha> = emptyList(),

    @SerializedName("planta")
    val planta: Planta? = null, // Ahora es nullable para manejar casos cuando viene null

    @SerializedName("plantaId")
    val plantaId: Long? = null, // ID de referencia para la planta

    @SerializedName("qrCode")
    val qrCode: String? = null
) {
    // Función segura para obtener el nombre de la planta
    fun getNombrePlanta(): String = planta?.nombre ?: "Planta no especificada"
}
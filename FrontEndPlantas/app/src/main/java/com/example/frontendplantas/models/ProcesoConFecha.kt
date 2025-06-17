package com.example.frontendplantas.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ProcesoConFecha(

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("fecha")
    val fecha: Long // Recibimos como Long (milisegundos desde 1970)
) {
    fun getFechaComoDate(): Date {
        return Date(fecha) // Convertimos milisegundos en un objeto Date
    }

    fun getFechaComoString(): String {
        val date = Date(fecha)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date) // Devolvemos la fecha como un String
    }

    // Si necesitas una fecha específica (por ejemplo: día, mes, año) puedes usar Calendar
    fun getFechaComoCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = Date(fecha) // Usamos Date para convertir milisegundos en tiempo
        return calendar
    }
}

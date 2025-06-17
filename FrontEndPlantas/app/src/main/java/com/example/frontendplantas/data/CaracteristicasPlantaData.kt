package com.example.frontendplantas.data

import com.example.frontendplantas.R

data class CaracteristicasPlantaData(
    val icon: Int,
    val title: String,
    val value: String
)

// Datos de ejemplo para previsualización
val CaracteristicasPlantaDatas = listOf(
    CaracteristicasPlantaData(
        icon = R.drawable.ic_plus,
        title = "Familia",
        value = "Cactaceae"
    ),
    CaracteristicasPlantaData(
        icon = R.drawable.ic_plus,
        title = "Nombre científico",
        value = "Cactus sp."
    ),
    CaracteristicasPlantaData(
        icon = R.drawable.ic_plus,
        title = "Toxicidad",
        value = "No tóxico"
    ),
    CaracteristicasPlantaData(
        icon = R.drawable.ic_plus,
        title = "Tipo de suelo",
        value = "Arenoso"
    ),
    CaracteristicasPlantaData(
        icon = R.drawable.ic_plus,
        title = "Humedad",
        value = "Baja"
    ),
    CaracteristicasPlantaData(
        icon = R.drawable.ic_plus,
        title = "Precio",
        value = "15.99€"
    )
)
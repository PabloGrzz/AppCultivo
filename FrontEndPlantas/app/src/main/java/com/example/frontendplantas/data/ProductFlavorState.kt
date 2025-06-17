package com.example.frontendplantas.data

import com.example.frontendplantas.R

data class ProductFlavorState(
    val name: String,
    val imgRes: Int,
    val value: String = ""
)

// Datos de ejemplo para previsualización
val ProductFlavorsData = listOf(
    ProductFlavorState(
        name = "Temperatura",
        imgRes = R.drawable.ic_plus,
        value = "20-25°C"
    ),
    ProductFlavorState(
        name = "Riego",
        imgRes = R.drawable.ic_plus,
        value = "Cada 5 días"
    ),
    ProductFlavorState(
        name = "Luz",
        imgRes = R.drawable.ic_plus,
        value = "Indirecta"
    )
)
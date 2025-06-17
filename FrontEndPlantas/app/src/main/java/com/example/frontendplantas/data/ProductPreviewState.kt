package com.example.frontendplantas.data

import com.example.frontendplantas.R
import com.example.frontendplantas.models.Planta

data class ProductPreviewState(
    val headline: String,
    val productImg: Int,
    val highlights: List<ProductHighlightState>
) {
    companion object {
        fun fromPlanta(planta: Planta): ProductPreviewState {
            return ProductPreviewState(
                headline = planta.nombre!!, // Usar !! solamente si estás seguro de que nunca será nulo
                productImg = R.drawable.celosia,
                highlights = listOf(
                    ProductHighlightState(
                        text = planta.familia,
                        type = ProductHighlightType.SECONDARY
                    ),
                    ProductHighlightState(
                        text = planta.nombreCientifico!!,  // Usar !! solamente si estás seguro de que nunca será nulo
                        type = ProductHighlightType.SECONDARY
                    ),
                    ProductHighlightState(
                        text = "Precio: ${planta.precio}€",
                        type = ProductHighlightType.PRIMARY
                    )
                )
            )
        }
    }
}
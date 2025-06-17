package com.example.frontendplantas.data

enum class ProductHighlightType {
    PRIMARY,
    SECONDARY
}

data class ProductHighlightState(
    val text: String?,  // Cambia a nullable
    val type: ProductHighlightType?
)
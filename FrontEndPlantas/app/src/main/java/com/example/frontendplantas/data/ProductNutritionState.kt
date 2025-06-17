package com.example.frontendplantas.data

import com.example.frontendplantas.R

data class ProductNutritionState(
    val title: String = "Fertilización",
    val description: String = "Usa un fertilizante equilibrado para plantas de interior. Diluir en agua según las instrucciones. Aplicar cada 2-4 semanas durante la temporada de crecimiento.",
    val fertilizantes: Fertilizantes = Fertilizantes(value = "NPK", unit = ""),
    val nutrition: List<NutritionState> = listOf(
        NutritionState(
            title = "N",
            amount = "5",
            unit = "g"
        ),
        NutritionState(
            title = "P",
            amount = "3",
            unit = "g"
        ),
        NutritionState(
            title = "K",
            amount = "8",
            unit = "g"
        )
    )
)

data class NutritionState(
    val title: String,
    val amount: String,
    val unit: String
)

data class Fertilizantes(
    val value: String,
    val unit: String
)

// Datos de ejemplo para previsualización
val ProductNutritionData = ProductNutritionState(
    title = "Fertilización",
    description = "Usa un fertilizante equilibrado para plantas de interior. Diluir en agua según las instrucciones. Aplicar cada 2-4 semanas durante la temporada de crecimiento.",
    fertilizantes = Fertilizantes(value = "NPK", unit = ""),
    nutrition = listOf(
        NutritionState(
            title = "N",
            amount = "5",
            unit = "g"
        ),
        NutritionState(
            title = "P",
            amount = "3",
            unit = "g"
        ),
        NutritionState(
            title = "K",
            amount = "8",
            unit = "g"
        )
    )
)
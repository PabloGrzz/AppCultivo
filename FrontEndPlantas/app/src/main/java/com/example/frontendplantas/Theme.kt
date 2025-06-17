package com.example.frontendplantas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography // Asegúrate de importar esto
import androidx.compose.runtime.Composable

@Composable
fun FrontEndPlantasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = extendedColorScheme,
        typography = extendedTypography,
        content = content
    )
}

object FrontEndPlantasTheme {
    val colors: AppColors
        @Composable
        get() = extendedColor

    val typography: Typography // Cambia el tipo de retorno a androidx.compose.material3.Typography
        @Composable
        get() = extendedTypography
}
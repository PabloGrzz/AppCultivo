package com.example.frontendplantas.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Pink = Color(0xFFF28482)
val Pink1 = Color(0xFFF1AACB)
val Green = Color(0xFF84A59D)
val Yellow = Color(0xFFF7EDE2)
val YellowLight = Color(0xFFEEE96E)
val VerdeClaro = Color(0xFFFFF7FF)
val Dark = Color(0xFF3D405B)
val Dark1 = Color(0xFF20542A)
val White = Color(0xFFFFFFFF)

val ColorGradient1 = Color(0xFF7BE8D2)
val ColorGradient2 = Color(0xFF4AD9D3)
val ColorGradient3 = Color(0xFF667AC0)

@Immutable
data class AppColors(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val secondarySurface: Color,
    val onSecondarySurface: Color,
    val regularSurface: Color,
    val onRegularSurface: Color,
    val actionSurface: Color,
    val onActionSurface: Color,
    val highlightSurface: Color,
    val onHighlightSurface: Color
)

val extendedColor = AppColors(
    background = Color.White,
    onBackground = Dark,
    surface = Color.White,
    onSurface = Dark,
    secondarySurface = Pink,
    onSecondarySurface = Color.White,
    regularSurface = YellowLight,
    onRegularSurface = Dark,
    actionSurface = Yellow,
    onActionSurface = Pink,
    highlightSurface = Green,
    onHighlightSurface = Color.White
)

val extendedColorScheme = lightColorScheme(
    primary = Pink,
    onPrimary = Color.White,
    secondary = Green,
    onSecondary = Color.White,
    background = extendedColor.background,
    onBackground = extendedColor.onBackground,
    surface = extendedColor.surface,
    onSurface = extendedColor.onSurface
)
package com.siele.countries.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = DarkBlue,
    primaryVariant = Gray500,
    secondary = Teal200,
    background = DarkBlue,
    surface = DarkBlue,
    onPrimary = Gray200,
    onSecondary = Color.Black,
    onBackground = Gray200,
    onSurface = Gray200,
)

private val LightColorPalette = lightColors(
    primary = SurfaceLight,
    primaryVariant = Gray900,
    secondary = Teal200,
    background = SurfaceLight,
    surface = SurfaceLight,
    onPrimary = Gray900,
    onSecondary = Color.Black,
    onBackground = Gray900,
    onSurface = Gray900,
)

@Composable
fun CountriesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
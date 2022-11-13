package com.siele.countries.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DarkBlue,
    primaryVariant = Gray200,
    secondary = Gray100,
    background = DarkBlue,
    surface = DarkBlue,
    onPrimary = Gray100,
    onSecondary = Color.Black,
    onBackground = Gray100,
    onSurface = Gray100,
)

private val LightColorPalette = lightColors(
    primary = SurfaceLight,
    primaryVariant = Gray025,
    secondary = Gray900,
    background = SurfaceLight,
    surface = SurfaceLight,
    onPrimary = Gray900,
    onSecondary = Gray500,
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
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = colors.surface
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
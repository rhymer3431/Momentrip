package com.mp.momentrip.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel


class ThemeViewModel : ViewModel() {
    var currentTheme by mutableStateOf(
        AppColors(
            primary = Color(0xFF2196F3),
            secondary = Color(0xFF03A9F4),
            background = Color(0xFFE3F2FD),
            surface = Color.White
        )
    )

    // 색상 변경 메서드들
    fun updatePrimaryColor(color: Color) {
        currentTheme = currentTheme.copy(primary = color)
    }

    fun updateSecondaryColor(color: Color) {
        currentTheme = currentTheme.copy(secondary = color)
    }

    fun updateBackgroundColor(color: Color) {
        currentTheme = currentTheme.copy(background = color)
    }

    fun updateSurfaceColor(color: Color) {
        currentTheme = currentTheme.copy(surface = color)
    }

    // 프리셋 테마 적용
    fun applyPresetTheme(theme: AppColors) {
        currentTheme = theme
    }
}

// 프리셋 테마들
object PresetThemes {
    val BlueTheme = AppColors(
        primary = Color(0xFF2196F3),
        secondary = Color(0xFF03A9F4),
        background = Color(0xFFE3F2FD),
        surface = Color.White
    )

    val GreenTheme = AppColors(
        primary = Color(0xFF4CAF50),
        secondary = Color(0xFF8BC34A),
        background = Color(0xFFE8F5E9),
        surface = Color.White
    )

    val PurpleTheme = AppColors(
        primary = Color(0xFF9C27B0),
        secondary = Color(0xFFE91E63),
        background = Color(0xFFF3E5F5),
        surface = Color.White
    )
}
data class AppColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color = Color.White,
    val onSecondary: Color = Color.White,
    val onBackground: Color = Color.Black,
    val onSurface: Color = Color.Black
)
@Composable
fun MomenTripTheme(
    themeState: ThemeState,
    content: @Composable () -> Unit
) {

    val colorScheme = if (themeState.isDarkMode) {
        darkColorScheme(
            primary = themeState.primaryColor,
            secondary = themeState.secondaryColor,
            background = themeState.backgroundColor,
            surface = themeState.surfaceColor
        )
    } else {
        lightColorScheme(
            primary = themeState.primaryColor,
            secondary = themeState.secondaryColor,
            background = themeState.backgroundColor,
            surface = themeState.surfaceColor
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

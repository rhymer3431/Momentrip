package com.mp.momentrip.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    secondary = Teal200,
    background = PurpleDark,
    surface = BlueDark
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFFFEFAE0),       // 따뜻한 크림 (기본 배경)
    onPrimary = Color(0xFFccd5ae),     // primary 위 텍스트 (소프트 다크 그레이)
    secondary = Color(0xFFD4A373),     // 포인트 브라운
    onSecondary = Color(0xFFFFFFFF),   // secondary 위 텍스트 (화이트)
    background = Color(0xFFFFFFFF),    // 전체 배경 (살짝 더 따뜻한 베이지)
    surface = Color(0xFFE6E0B2),       // 카드/섹션 배경
    onSurface = Color(0xFF5E6450),     // surface 위 텍스트
    error = Color(0xFFD62828),         // 따뜻한 레드
    onError = Color(0xFFFFFFFF),        // 에러 텍스트 (화이트)
    tertiary = Color(0xFF1A2DFE),
    onTertiary = Color(0xFFF3F9FD)
)


@Composable
fun MomenTripTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
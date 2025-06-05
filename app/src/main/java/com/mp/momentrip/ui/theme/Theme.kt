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
    primary = Color(0xFF24BAEC),             // 메인 푸른색
    onPrimary = Color.White,

    primaryContainer = Color(0xFFD9F3FF),    // 밝은 하늘색 배경
    onPrimaryContainer = Color(0xFF002D3A),  // 대비 높은 텍스트

    inversePrimary = Color(0xFF7EDAFF),      // 다크모드 대비용 아쿠아

    secondary = Color(0xFF4F6D87),           // 부드러운 블루그레이 (보조 색상)
    onSecondary = Color.White,

    secondaryContainer = Color(0xFFDFEAF1),  // 연한 블루-그레이 배경
    onSecondaryContainer = Color(0xFF1B2B38),

    tertiary = Color(0xFF567DA7),            // 강조용 블루-그레이 (포인트 색)
    onTertiary = Color.White,

    tertiaryContainer = Color(0xFFD9E8F6),   // 연한 회청색 (선택된 배경)
    onTertiaryContainer = Color(0xFF1D2F45),


    background = Color(0xFFFAFCFD),       // 완전 화이트보다 살짝 부드러운 톤
    onBackground = Color(0xFF1C1C1C),

    surface = Color(0xFFF5F7F9),          // 카드/시트용 따뜻한 연회색
    onSurface = Color(0xFF1C1C1C),

    surfaceVariant = Color(0xFFE9EFF3),   // 약간 구분감 있는 회색 (그리드/탭)
    onSurfaceVariant = Color(0xFF37424A),

    surfaceTint = Color(0xFF24BAEC),         // elevation tint

    inverseSurface = Color(0xFF2A3B4A),
    inverseOnSurface = Color(0xFFE6F4FA),

    error = Color(0xFFB00020),
    onError = Color.White,

    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFFB0C9DA),             // 테두리용 라이트 블루 그레이
    outlineVariant = Color(0xFFD7EAF4),      // 비활성 테두리용 더 연한 톤

    scrim = Color(0x991C1C1C)                // modal 블러용
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
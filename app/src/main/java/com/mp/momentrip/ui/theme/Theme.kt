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
    primary = Color(0xFF24BAEC),         // 메인 푸른색
    onPrimary = Color(0xFFEAF6FD),        // primary 위 텍스트 (밝은 하늘색)
    secondary = Color(0xFF1193BF),        // 보조 푸른색
    onSecondary = Color(0xFFFFFFFF),      // secondary 위 텍스트 (화이트)
    tertiary = Color(0xFF3A8DFF),         // 추가 포인트 블루
    onTertiary = Color(0xFFF0F7FF),        // tertiary 위 텍스트 (밝은 블루)
    error = Color(0xFFB00020),             // 차가운 느낌의 진한 레드
    onError = Color(0xFFFFFFFF),           // 에러 텍스트 (화이트)

    background = Color(0xFFFFFFFF),        // 전체 배경 (연한 하늘색)
    onBackground = Color(0xFF1B2D3C),      // 배경 위 텍스트 (짙은 블루그레이)

    surface = Color(0xFFD0E7F5),           // 카드/섹션 배경 (밝은 블루)
    onSurface = Color(0xFF1B2D3C),         // surface 위 텍스트 (짙은 블루그레이)

    surfaceVariant = Color(0xFFA3CBEF),    // 변형된 서피스 색 (밝은 블루톤)
    onSurfaceVariant = Color(0xFFE1F0FB),  // 변형 서피스 위 텍스트 (밝은 하늘색)

    outline = Color(0xFF7EB6E9),            // 경계선 색 (중간 밝기의 블루)
    outlineVariant = Color(0xFFC3DEF7),     // 경계선 변형 (연한 블루)

    surfaceTint = Color(0xFF4F9DDE),        // 틴트 효과 (primary 따라감)

    inverseSurface = Color(0xFF27455E),     // 다크 모드용 surface 반전 색 (짙은 네이비)
    inverseOnSurface = Color(0xFFD6E8F8),   // inverse surface 위 텍스트 (밝은 블루)

    errorContainer = Color(0xFFFFDAD4),     // 에러를 표시하는 박스 색 (조금 부드러운 레드)
    onErrorContainer = Color(0xFF410002),   // 에러 박스 위 텍스트 (딥 레드)

    scrim = Color(0x991B2D3C)               // 스크림(덮개) 색상 (짙은 반투명 블루)
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
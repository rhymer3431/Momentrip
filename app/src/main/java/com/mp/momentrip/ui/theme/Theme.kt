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
    onPrimary = Color(0xFFFFFFFF),       // primary 위 텍스트 (화이트, 대비 확보)

    secondary = Color(0xFF1385B6),       // primary보다 살짝 어두운 보조 블루
    onSecondary = Color(0xFFFFFFFF),     // 보조색 위 텍스트 (화이트)

    tertiary = Color(0xFF3A8DFF),        // 푸른색과 조화로운 포인트 블루
    onTertiary = Color(0xFFFFFFFF),      // tertiary 위 텍스트 (화이트)

    error = Color(0xFFB00020),           // 시각적으로 경고를 주는 진한 레드
    onError = Color(0xFFFFFFFF),         // 에러 위 텍스트 (화이트)

    background = Color(0xFFFFFFFF),      // 전체 배경: 아주 연한 푸른 백색
    onBackground = Color(0xFF1B2D3C),    // 배경 위 텍스트 (짙은 블루그레이)

    surface = Color(0xFFF6F6F6),         // 카드/섹션 배경: primary의 옅은 버전
    onSurface = Color(0xFF1B2D3C),       // surface 위 텍스트 (가독성 높은 진한 컬러)

    surfaceVariant = Color(0xFFC1E3F3),  // 변형된 서피스 색 (살짝 채도 낮춘 블루)
    onSurfaceVariant = Color(0xFF1B2D3C),// 변형 surface 위 텍스트

    outline = Color(0xFF7EB6E9),         // 테두리 (중간 톤 블루)
    outlineVariant = Color(0xFFB5DAF1),  // 옅은 테두리 (투명도 높은 느낌)

    surfaceTint = Color(0xFF24BAEC),     // primary와 동일한 틴트 색

    inverseSurface = Color(0xFF1A3445),  // 다크 모드용 반전 surface (짙은 네이비)
    inverseOnSurface = Color(0xFFE0F4FB),// inverse surface 위 텍스트

    errorContainer = Color(0xFFFFDAD4),  // 에러 영역 배경 (부드러운 살구빛 레드)
    onErrorContainer = Color(0xFF410002),// 에러 박스 위 텍스트

    scrim = Color(0x991B2D3C)            // 스크림(덮개) 색상 (짙은 반투명 블루)
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
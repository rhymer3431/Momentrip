package com.mp.momentrip.ui.screen.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mp.momentrip.R

@Composable
fun AiLoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ai_loading))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6FAFF))  // 연한 파랑 배경 (감성)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ⏳ 애니메이션
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(220.dp)
            )

            Spacer(Modifier.height(32.dp))

            // 🤖 메인 메시지
            Text(
                text = "AI가 맞춤형 여행을 짜고 있어요",
                fontFamily = FontFamily(Font(R.font.haru)),
                fontSize = 20.sp,
                color = Color(0xFF1B1E28),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // ⏱ 서브 메시지
            Text(
                text = "잠시만 기다려주세요...",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        // ✨ 로고 or 타이틀 (상단 고정)
        Text(
            text = "Momentrip",
            fontFamily = FontFamily(Font(R.font.haru)),
            fontSize = 40.sp,
            color = Color(0xFF002C6E),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AiLoadingScreenPreview() {
    AiLoadingScreen()
}

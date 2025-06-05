package com.mp.momentrip.ui.screen.loading


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun LoadingScreen() {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.load))
    val progress = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Title
        Text(
            text = "Momentrip",
            fontFamily = FontFamily(Font(R.font.haru)), // Replace with Rozha One if available
            fontSize = 50.sp,
            lineHeight = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 128.dp)
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier.padding(30.dp)
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress.value }, // ✅ 람다로 넘김
            )
        }
    }



}

@Preview
@Composable
fun LoadingScreenPreview(){
    LoadingScreen()
}
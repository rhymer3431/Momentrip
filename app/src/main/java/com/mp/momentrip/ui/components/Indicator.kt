package com.mp.momentrip.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.mp.momentrip.view.QuestionViewModel

@Composable
fun DotsIndicator(
    currentIndex: Int,
    maxIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(100.dp)) // 테두리 둥글게
            .background(Color(0x70BFBFBF)) // 배경색 회색
            .padding(8.dp) // 내부 패딩 추가
    ) {
        repeat(maxIndex) { index ->
            val isSelected = index == currentIndex
            val color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.3f)
            val animatedSize = animateDpAsState(targetValue = 16.dp, label = "")
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(animatedSize.value)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF// ARGB 형식 (노란색 배경)
)
@Composable
fun DotPreview(){
    DotsIndicator(
        currentIndex = 0,
        maxIndex = 4,
        Modifier
    )
}
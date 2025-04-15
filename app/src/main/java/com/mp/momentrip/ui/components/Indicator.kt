package com.mp.momentrip.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mp.momentrip.ui.theme.TravelAppTheme
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
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        repeat(maxIndex) { index ->
            val isSelected = index == currentIndex
            val color = if (isSelected) Color.Gray else Color.LightGray
            val animatedSize = animateDpAsState(targetValue = if (isSelected) 16.dp else 8.dp, label = "")
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

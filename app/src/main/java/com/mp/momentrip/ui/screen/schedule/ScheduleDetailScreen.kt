package com.mp.momentrip.ui.screen.schedule

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.R

import com.mp.momentrip.data.Schedule
import com.mp.momentrip.util.formatDateRange
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@Composable
fun ScheduleDetailScreen(
    navController: NavController,
    scheduleViewModel: ScheduleViewModel,
    userState: UserViewModel  // 필요 시
) {

    val schedule by scheduleViewModel.schedule.collectAsState()
    val backgroundImage = painterResource(id = R.drawable.q4a1)
    val density = LocalDensity.current
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val sheetHeightDp = 360.dp
    val sheetHeightPx = with(density) { sheetHeightDp.toPx() }

    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val showOverview = remember { mutableStateOf(false) }

    if (showOverview.value) {
        ScheduleOverviewScreen(
            userState = userState,
            scheduleViewModel = scheduleViewModel,
            navController = navController
        )
        return  // ⚠️ 아래 시트는 생략
    }

    val dragGesture = Modifier.pointerInput(Unit) {
        detectVerticalDragGestures(
            onVerticalDrag = { _, dragAmount ->
                scope.launch {
                    val newValue = (offsetY.value + dragAmount).coerceIn(0f, sheetHeightPx)
                    offsetY.snapTo(newValue)
                }
            },
            onDragEnd = {
                scope.launch {
                    if (offsetY.value > sheetHeightPx / 3f) {
                        offsetY.animateTo(sheetHeightPx, tween(300))
                    } else {
                        offsetY.animateTo(0f, tween(300))
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                    )
                )
        )

        Column(
            modifier = dragGesture
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .height(sheetHeightDp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White.copy(alpha = 0.3f))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = schedule?.region ?: "",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDateRange(schedule?.startDate, schedule?.endDate),
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { showOverview.value = true }) {
                    Text("일정 보기")
                }
            }
        }
    }
}


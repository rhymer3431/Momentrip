package com.mp.momentrip.ui.screen.feed

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.ui.components.LikeButton
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PlaceDetailBottomSheet(
    place: Place,
    userState: UserViewModel,
    onClose: () -> Unit,
    cardSize: Dp,
    imageHeight: Dp,
    imageAlpha: Float,
    textAlpha: Float
) {
    val density = LocalDensity.current
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val baseImageHeightPx = with(density) { imageHeight.toPx() }

    val offsetY = remember { Animatable(0f) }
    var isClosing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // 닫기 애니메이션
    LaunchedEffect(isClosing) {
        if (isClosing) {
            offsetY.animateTo(
                targetValue = screenHeightPx,
                animationSpec = tween(durationMillis = 300)
            )
            onClose()
        }
    }

    // 이미지 확장 계산
    val currentImageHeightPx = (baseImageHeightPx + offsetY.value).coerceAtLeast(baseImageHeightPx)
    val expandedImageHeight = with(density) { currentImageHeightPx.toDp() }

    // 드래그 제스처
    val dragGesture = Modifier.pointerInput(isClosing) {
        detectVerticalDragGestures(
            onVerticalDrag = { _, dragAmount ->
                scope.launch {
                    val newValue = (offsetY.value + dragAmount).coerceAtLeast(0f)
                    offsetY.snapTo(newValue)
                }
            },
            onDragEnd = {
                scope.launch {
                    offsetY.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 300)
                    )
                }
            }
        )
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        // 이미지
        ImageCard(
            imageUrl = place.firstImage ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(expandedImageHeight)
                .graphicsLayer {
                    alpha = imageAlpha
                }
                .align(Alignment.TopCenter)
        )

        // 이미지와 시트 연결용 오버레이
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        // 시트
        Column(
            modifier = dragGesture
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .height(cardSize)
                .background(Color.White)
                .align(Alignment.BottomCenter)
        ) {
            Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "닫기",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { isClosing = true }
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = place.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    LikeButton(userState, place)
                }

                Spacer(Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        Image(
                            GoogleMaterial.Icon.gmd_schedule,
                            contentDescription = "운영시간",
                            colorFilter = ColorFilter.tint(Color(0xFF1D1B20)),
                            modifier = Modifier.size(24.dp)
                        )
                        Image(
                            GoogleMaterial.Icon.gmd_phone,
                            contentDescription = "전화번호",
                            colorFilter = ColorFilter.tint(Color(0xFF1D1B20)),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("운영시간: ${place.firstMenu ?: "-"}", fontSize = 18.sp)
                        Text("전화번호: ${place.infoCenter ?: "-"}", fontSize = 18.sp)
                    }
                }

                Spacer(Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = place.overview ?: "",
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        color = Color(0xFF828282),
                        modifier = Modifier.alpha(textAlpha)
                    )
                }

            }
        }
    }
}

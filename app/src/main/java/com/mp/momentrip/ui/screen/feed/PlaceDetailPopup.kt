package com.mp.momentrip.ui.screen.feed

import androidx.compose.animation.core.animate
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.view.UserViewModel
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.OrbitalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PlaceDetailBottomSheet(
    place: Place,
    userState: UserViewModel,
    onClose: () -> Unit,
    cardSize: Dp,
    imageHeight: Dp,
    imageAlpha: Float,
    textAlpha: Float
){
    var offsetY by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val screenHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val imageHeightPx = with(LocalDensity.current) { 400.dp.toPx() }

        // 시트가 내려갈수록 이미지 크기 비율 증가
        val scaleFactor = 1f + (offsetY / 600f)



        val dragGesture = Modifier.pointerInput(Unit) {
            detectVerticalDragGestures(
                onVerticalDrag = { _, dragAmount ->
                    offsetY += dragAmount
                    offsetY = offsetY.coerceAtLeast(0f) // 위로는 제한
                },
                onDragEnd = {
                    scope.launch {
                        animate(
                            initialValue = offsetY,
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300)
                        ) { value, _ -> offsetY = value }
                    }
                },
                onDragCancel = {
                    scope.launch {
                        animate(
                            initialValue = offsetY,
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300)
                        ) { value, _ -> offsetY = value }
                    }
                }
            )
        }

        // 확대되는 이미지
        ImageCard(
            imageUrl = place.firstImage ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .graphicsLayer {
                    alpha = imageAlpha
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                .align(Alignment.TopCenter)
        )

        // 시트와 이미지 연결을 위한 그라데이션 오버레이
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

        // 드래그 가능한 시트
        Column(
            modifier = dragGesture
                .offset { IntOffset(0, offsetY.roundToInt()) }
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
                        .clickable { onClose() }
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
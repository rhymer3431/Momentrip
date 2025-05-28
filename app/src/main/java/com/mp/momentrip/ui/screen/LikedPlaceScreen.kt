package com.mp.momentrip.ui.screen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kakao.vectormap.LatLng   // 필요하다면 제거
import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.ui.screen.feed.PlaceDetailBottomSheet

import com.mp.momentrip.view.UserViewModel
@Composable
fun LikedPlaceScreen(
    userState: UserViewModel,
    onPlaceClick: (Place) -> Unit,
) {
    val user by userState.user.collectAsState()
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    // 전환 상태 정의
    val transition = updateTransition(targetState = selectedPlace, label = "DetailTransition")
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    // 카드 크기 애니메이션: 160.dp → 전체 화면 높이

    // 이미지 높이: 화면의 30%
    val imageHeight by transition.animateDp(
        transitionSpec = { tween(500) },
        label = "ImageHeight"
    ) {
        if (it == null) 0.dp else screenHeight * 0.4f
    }

    // 초기 카드 높이: 화면의 70%
    val cardSize by transition.animateDp(
        transitionSpec = { tween(500) },
        label = "CardSize"
    ) {
        if (it == null) 160.dp else screenHeight * 0.6f
    }



    // 이미지 페이드인
    val imageAlpha by transition.animateFloat(
        transitionSpec = { tween(500) },
        label = "ImageAlpha"
    ) { if (it == null) 0f else 1f }

    // 텍스트 페이드인
    val textAlpha by transition.animateFloat(
        transitionSpec = { tween(500) },
        label = "TextAlpha"
    ) { if (it == null) 0f else 1f }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)  // 전체 마진
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "좋아요한 장소",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)  // 내부 여백
        ) {
            items(user?.liked.orEmpty()) { place ->
                if (place != null) {
                    PlaceCard(
                        place = place,
                        onClick = { selectedPlace = place},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp) // 카드 간 여백
                    )
                }
            }
        }
    }
    selectedPlace?.let { place ->
        PlaceDetailBottomSheet(
            place = place,
            onClose = { selectedPlace = null },
            userState = userState,
            imageHeight = imageHeight,
            cardSize = cardSize,
            imageAlpha = imageAlpha,
            textAlpha = textAlpha

        )
    }
}

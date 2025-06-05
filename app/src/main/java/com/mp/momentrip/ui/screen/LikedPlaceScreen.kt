package com.mp.momentrip.ui.screen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.ui.components.MiniFeedPlaceCard
import com.mp.momentrip.ui.screen.feed.PlaceDetailBottomSheet
import com.mp.momentrip.view.PlaceViewModel
import com.mp.momentrip.view.UserViewModel

@Composable
fun LikedPlaceScreen(
    placeViewModel: PlaceViewModel,
    userState: UserViewModel,
    onPlaceClick: (Place) -> Unit,
    onClose: () -> Unit,
) {
    val user by userState.user.collectAsState()
    val selectedPlace by placeViewModel.selectedPlace.collectAsState()

    val transition = updateTransition(targetState = selectedPlace, label = "DetailTransition")
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val imageHeight by transition.animateDp({ tween(500) }, label = "") {
        if (it == null) 0.dp else screenHeight * 0.4f
    }
    val cardSize by transition.animateDp({ tween(500) }, label = "") {
        if (it == null) 160.dp else screenHeight * 0.6f
    }
    val imageAlpha by transition.animateFloat({ tween(500) }, label = "") {
        if (it == null) 0f else 1f
    }
    val textAlpha by transition.animateFloat({ tween(500) }, label = "") {
        if (it == null) 0f else 1f
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // ⬆️ 상단 타이틀바 + 닫기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "좋아요한 장소",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1E28),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 📌 2열 그리드 뷰
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(user?.liked.orEmpty()) { place ->
                    if (place != null) {
                        MiniFeedPlaceCard(
                            userState = userState,
                            place = place,
                            onClick = { onPlaceClick(place) },
                        )
                    }
                }
            }
        }

        // 👉 상세 뷰 오버레이
        selectedPlace?.let { place ->
            PlaceDetailBottomSheet(
                place = place,
                onClose = onClose,
                userState = userState,
                imageHeight = imageHeight,
                cardSize = cardSize,
                imageAlpha = imageAlpha,
                textAlpha = textAlpha
            )
        }
    }
}



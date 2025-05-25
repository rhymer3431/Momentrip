package com.mp.momentrip.ui.screen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Region
import com.mp.momentrip.service.TourService
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.ui.screen.feed.PlaceDetailBottomSheet
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    userState : UserViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var placeList by remember { mutableStateOf<List<Place>>(emptyList()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var isLoading by remember { mutableStateOf(false) }



    // 전환 상태 정의
    val transition = updateTransition(targetState = selectedPlace, label = "DetailTransition")

    // 카드 크기 애니메이션: 160.dp → 전체 화면 높이
    val cardSize by transition.animateDp(
        transitionSpec = { tween(500) },
        label = "CardSize"
    ) {
        if (it == null) 160.dp else LocalConfiguration.current.screenHeightDp.dp
    }

    // 이미지 높이 애니메이션: 0 → 화면 절반
    val imageHeight by transition.animateDp(
        transitionSpec = { tween(500) },
        label = "ImageHeight"
    ) {
        if (it == null) 0.dp else LocalConfiguration.current.screenHeightDp.dp * 0.5f
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
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("어디로 여행가고 싶나요?") },
            trailingIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val region = Region.fromName(query.trim())
                        if (region != null) {
                            placeList = TourService.getTouristSpotsByRegion(region.locationName)
                            selectedPlace = null
                        } else {
                            selectedPlace = TourService.getTouristSpotsByKeyword(query.trim()).firstOrNull()
                            placeList = emptyList()
                        }
                        isLoading = false
                    }
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "검색")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            selectedPlace != null -> {
                PlaceDetailBottomSheet(
                    place = selectedPlace!!,
                    onClose = { selectedPlace = null },
                    userState = userState,
                    imageHeight = imageHeight,
                    cardSize = cardSize,
                    imageAlpha = imageAlpha,
                    textAlpha = textAlpha

                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(placeList) { place ->
                        PlaceCard(
                            place = place,
                            onClick = {
                            selectedPlace = place
                            placeList = emptyList()
                        })
                    }
                }
            }
        }
    }
}
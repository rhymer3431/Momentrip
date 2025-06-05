package com.mp.momentrip.ui.screen.search

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.place.Region
import com.mp.momentrip.data.tourAPI.ContentType
import com.mp.momentrip.service.TourService
import com.mp.momentrip.ui.components.TallFeedPlaceCard
import com.mp.momentrip.ui.screen.feed.PlaceDetailBottomSheet
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch

private enum class SearchCategory(val label: String, val contentType: ContentType?) {
    ALL("전체", null),
    RESTAURANT("맛집", ContentType.RESTAURANT),
    TOUR("핫플", ContentType.TOURIST_SPOT),
    DORMITORY("숙소", ContentType.ACCOMMODATION),
    FESTIVAL("축제", ContentType.FESTIVAL_EVENT),
    CULTURE("문화시설", ContentType.CULTURAL_FACILITY),
    LEISURE("레포츠", ContentType.LEISURE_SPORTS),
    SHOPPING("쇼핑", ContentType.SHOPPING)
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SearchScreen(userState: UserViewModel, recommendViewModel: RecommendViewModel) {
    val coroutineScope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<List<Place>>(emptyList()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf(SearchCategory.ALL) }

    val recommendedPlaces by recommendViewModel.recommendPlacesByCategory.collectAsState()
    val initialPlaceList = when (val type = selectedCategory.contentType) {
        null -> recommendedPlaces.values.flatten()
        else -> recommendedPlaces[type] ?: emptyList()
    }.take(20)

    val placeList = if (query.isBlank()) initialPlaceList else searchResult

    val transition = updateTransition(targetState = selectedPlace, label = "DetailTransition")
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val imageHeight by transition.animateDp(
        transitionSpec = { tween(500) }, label = "ImageHeight"
    ) { if (it == null) 0.dp else screenHeight * 0.4f }

    val cardSize by transition.animateDp(
        transitionSpec = { tween(500) }, label = "CardSize"
    ) { if (it == null) 160.dp else screenHeight * 1f }

    val imageAlpha by transition.animateFloat(
        transitionSpec = { tween(500) }, label = "ImageAlpha"
    ) { if (it == null) 0f else 1f }

    val textAlpha by transition.animateFloat(
        transitionSpec = { tween(500) }, label = "TextAlpha"
    ) { if (it == null) 0f else 1f }

    BackHandler(enabled = selectedPlace != null) { selectedPlace = null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // 검색창
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                coroutineScope.launch {
                    if (query.isBlank()) return@launch
                    isLoading = true
                    val region = Region.fromName(query.trim())
                    searchResult = if (region != null) {
                        TourService.getTouristSpotsByRegion(region.locationName)
                    } else {
                        TourService.getTouristSpotsByKeyword(query.trim())
                    }
                    selectedPlace = null
                    isLoading = false
                }
            },
            placeholder = { Text("어디로 여행가고 싶나요?", color = Color.Gray) },
            trailingIcon = { Icon(Icons.Default.Search, contentDescription = "검색", tint = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 카테고리 선택
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchCategory.values().forEach { category ->
                val selected = selectedCategory == category
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .background(if (selected) Color(0xFF4F8EDA) else Color(0xFFE6EAF2))
                        .clickable {
                            selectedCategory = category
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category.label,
                        fontSize = 13.sp,
                        fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                        color = if (selected) Color.White else Color(0xFF4F5A63)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (selectedPlace != null) {
            PlaceDetailBottomSheet(
                place = selectedPlace!!,
                onClose = { selectedPlace = null },
                userState = userState,
                imageHeight = imageHeight,
                cardSize = cardSize,
                imageAlpha = imageAlpha,
                textAlpha = textAlpha
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(placeList) { place ->
                    TallFeedPlaceCard(
                        userState = userState,
                        place = place,
                        onClick = {
                            selectedPlace = place
                        }
                    )
                }
            }
        }
    }
}
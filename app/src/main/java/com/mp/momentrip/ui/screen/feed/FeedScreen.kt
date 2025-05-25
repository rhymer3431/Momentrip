// ✅ 카드 클릭 시 카드가 커지면서 상세 화면으로 자연스럽게 전환되는 애니메이션 적용됨
package com.mp.momentrip.ui.screen.feed

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.ui.components.LikeButton
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.ui.screen.LoadingScreen
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel

private enum class FeedCategory(val koLabel: String) {
    ALL("All"), RESTAURANT("맛집"), TOUR("핫플"), DORMITORY("숙소")
}

@Composable
fun FeedScreen(
    userState: UserViewModel,
    recommendViewModel: RecommendViewModel = viewModel()
) {
    LaunchedEffect(Unit) { recommendViewModel.initialize(userState) }

    val isLoading by recommendViewModel.isLoading.collectAsState()
    val restaurants by recommendViewModel.recommendRestaurant.collectAsState()
    val tourSpots by recommendViewModel.recommendTourSpot.collectAsState()
    val dormitories by recommendViewModel.recommendDormitory.collectAsState()

    var selected by rememberSaveable { mutableStateOf(FeedCategory.ALL) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    // 시스템 뒤로가기 처리
    BackHandler(enabled = selectedPlace != null) {
        selectedPlace = null
    }

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

    Box(Modifier.fillMaxSize()) {
        // Feed 리스트 또는 로딩
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    CategorySection(
                        selectedCategory = selected,
                        onCategorySelect = { selected = it }
                    )
                    Spacer(Modifier.height(24.dp))
                }
                when (selected) {
                    FeedCategory.ALL -> {
                        item { PlaceSection("추천 식당", restaurants, onPlaceClick = { selectedPlace = it }) }
                        item { PlaceSection("추천 핫플", tourSpots, onPlaceClick = { selectedPlace = it }) }
                        item { PlaceSection("추천 숙소", dormitories, onPlaceClick = { selectedPlace = it }) }
                    }
                    FeedCategory.RESTAURANT -> item {
                        PlaceSection("추천 식당", restaurants, onPlaceClick = { selectedPlace = it }, isGrid = true)
                    }
                    FeedCategory.TOUR -> item {
                        PlaceSection("추천 핫플", tourSpots, onPlaceClick = { selectedPlace = it }, isGrid = true)
                    }
                    FeedCategory.DORMITORY -> item {
                        PlaceSection("추천 숙소", dormitories, onPlaceClick = { selectedPlace = it }, isGrid = true)
                    }
                }

            }
        }

        // 상세 카드 전환
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
}


@Composable
fun PlaceSection(
    title: String,
    placeList: List<Place>?,
    onPlaceClick: (Place) -> Unit,
    isGrid: Boolean = false  // 카테고리에 따라 true/false 설정
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.arrow_sub),
                contentDescription = title,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (isGrid) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp), // 최대 높이 제한
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(placeList.orEmpty()) { place ->
                    PlaceCard(place = place, onClick = { onPlaceClick(place) })
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                placeList.orEmpty().take(5).forEach { place ->
                    PlaceCard(place = place, onClick = { onPlaceClick(place) })
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    selectedCategory: FeedCategory,
    onCategorySelect: (FeedCategory) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        FeedCategory.entries.forEach { category ->
            val icon = when (category) {
                FeedCategory.ALL -> GoogleMaterial.Icon.gmd_apps         // All
                FeedCategory.RESTAURANT -> GoogleMaterial.Icon.gmd_restaurant   // 맛집
                FeedCategory.TOUR -> GoogleMaterial.Icon.gmd_place        // 관광명소
                FeedCategory.DORMITORY -> GoogleMaterial.Icon.gmd_hotel        // 숙소
            }
            CategoryItem(
                icon = icon,
                title = category.koLabel,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelect(category) }
            )
        }
    }

}

@Composable
fun CategoryItem(
    icon: GoogleMaterial.Icon,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            icon,
            colorFilter = ColorFilter.tint(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF161823)
        )
    }
}

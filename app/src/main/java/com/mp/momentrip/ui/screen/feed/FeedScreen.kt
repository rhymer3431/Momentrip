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
                        item { PlaceSection("추천 식당", restaurants) { selectedPlace = it } }
                        item { PlaceSection("추천 핫플", tourSpots) { selectedPlace = it } }
                        item { PlaceSection("추천 숙소", dormitories) { selectedPlace = it } }
                    }
                    FeedCategory.RESTAURANT -> item {
                        PlaceSection("추천 식당", restaurants) { selectedPlace = it }
                    }
                    FeedCategory.TOUR -> item {
                        PlaceSection("추천 핫플", tourSpots) { selectedPlace = it }
                    }
                    FeedCategory.DORMITORY -> item {
                        PlaceSection("추천 숙소", dormitories) { selectedPlace = it }
                    }
                }
            }
        }

        // 상세 카드 전환
        selectedPlace?.let { place ->
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .size(cardSize)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 상단 이미지
                    ImageCard(
                        imageUrl = place.firstImage ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight)
                            .graphicsLayer { alpha = imageAlpha }
                            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    )
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        // 뒤로가기 버튼
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { selectedPlace = null }
                        )
                        Spacer(Modifier.height(16.dp))

                        // 제목 + LikeButton
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

                        // 아이콘 + 텍스트
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                                Image(
                                    asset = GoogleMaterial.Icon.gmd_schedule,
                                    contentDescription = "Schedule",
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF1D1B20))
                                )
                                Image(
                                    asset = GoogleMaterial.Icon.gmd_phone,
                                    contentDescription = "Phone",
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF1D1B20))
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(
                                    text = "운영시간: ${place.firstMenu ?: "-"}",
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = "전화번호: ${place.infoCenter ?: "-"}",
                                    fontSize = 20.sp
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // 설명 텍스트 (스크롤 가능)
                        Text(
                            text = place.overview ?: "",
                            fontSize = 20.sp,
                            lineHeight = 28.sp,
                            color = Color(0xFF828282),
                            modifier = Modifier.alpha(textAlpha)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PlaceSection(
    title: String,
    placeList: List<Place>?,
    onPlaceClick: (Place) -> Unit) {
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
                tint = Color.Unspecified // SVG 본연의 색상 유지
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            placeList.orEmpty().take(5).forEach { place ->
                PlaceCard(place = place, onClick = {onPlaceClick(place)})
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

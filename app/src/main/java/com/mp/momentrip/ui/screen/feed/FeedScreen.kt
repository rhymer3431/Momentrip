// ✅ 카드 클릭 시 카드가 커지면서 상세 화면으로 자연스럽게 전환되는 애니메이션 적용됨
package com.mp.momentrip.ui.screen.feed

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.components.FeedPlaceCard
import com.mp.momentrip.ui.components.LargePlaceCard
import com.mp.momentrip.ui.screen.LoadingScreen
import com.mp.momentrip.view.RecommendInitData
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel

private enum class FeedCategory(val koLabel: String) {
    ALL("All"), RESTAURANT("맛집"), TOUR("핫플"), DORMITORY("숙소")
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FeedScreen(
    userState: UserViewModel,
    recommendViewModel: RecommendViewModel
) {
    val user by userState.user.collectAsState()

    LaunchedEffect(Unit) {
        // 이 블록 안의 코드가 컴포넌트 최초 렌더링 시에만 실행됩니다.
        recommendViewModel.initialize(
            RecommendInitData(
                userPreference = user!!.userPreference,
                region = userState.region.value!!
            )
        )
    }

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
                        item {
                            Text(
                                text = "${user?.name}님 취향에 딱 맞는 곳",
                                modifier = Modifier
                                    .padding(start = 20.dp, bottom = 12.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B1E28), // MomenTrip 다크 네이비 or 기본 텍스트 컬러
                                letterSpacing = 0.5.sp
                            )
                        }
                        item {
                            // ✅ 대형 대표 카드 추가 (예: 첫 번째 추천 장소 사용)
                            if (tourSpots?.isNotEmpty() == true) {
                                LargePlaceCardCarouselSection(
                                    userState = userState,
                                    places = tourSpots!!,
                                    onPlaceClick = { selectedPlace = it }
                                )
                                Spacer(Modifier.height(24.dp))
                            }
                        }

                        item {
                            Text(
                                text = "인기 많은 장소",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        item {
                            // ✅ 추천 리스트 수평 스크롤로 표시
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                tourSpots?.let {
                                    items(it.take(5)) { place ->
                                        FeedPlaceCard(
                                            userState = userState,
                                            place = place,
                                            onClick = { selectedPlace = place }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    FeedCategory.RESTAURANT -> item {
                        PlaceSection(userState,"추천 식당", restaurants, onPlaceClick = { selectedPlace = it }, isGrid = true)
                    }
                    FeedCategory.TOUR -> item {
                        PlaceSection(userState,"추천 핫플", tourSpots, onPlaceClick = { selectedPlace = it }, isGrid = true)
                    }
                    FeedCategory.DORMITORY -> item {
                        PlaceSection(userState,"추천 숙소", dormitories, onPlaceClick = { selectedPlace = it }, isGrid = true)
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
    userState: UserViewModel,
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
                    FeedPlaceCard(
                        userState = userState,
                        place = place,
                        onClick = { onPlaceClick(place) }
                    )
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
                    FeedPlaceCard(
                        userState = userState,
                        place = place,
                        onClick = { onPlaceClick(place) }
                    )
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
fun LargePlaceCardCarouselSection(
    userState: UserViewModel,
    places: List<Place>,
    modifier: Modifier = Modifier,
    onPlaceClick: (Place) -> Unit
) {
    val actualPlaces = places.take(5)
    val pageCount = actualPlaces.size

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { Int.MAX_VALUE }
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardHeight = screenWidth * 1.15f

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
        ) { index ->
            val realIndex = index % pageCount
            LargePlaceCard(
                userState = userState,
                place = actualPlaces[realIndex],
                onClick = { onPlaceClick(actualPlaces[realIndex]) },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        PagerDotsIndicator(
            currentPage = pagerState.currentPage % pageCount,
            totalDots = pageCount,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PagerDotsIndicator(
    currentPage: Int,
    totalDots: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF4F8EDA),
    inactiveColor: Color = Color(0xFFCCCCCC),
    dotSize: Dp = 8.dp,
    spacing: Dp = 6.dp
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(if (index == currentPage) activeColor else inactiveColor)
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



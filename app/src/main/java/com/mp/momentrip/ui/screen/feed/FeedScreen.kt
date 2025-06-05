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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.tourAPI.ContentType
import com.mp.momentrip.ui.components.FeedPlaceCard
import com.mp.momentrip.ui.components.LargePlaceCard
import com.mp.momentrip.ui.screen.loading.LoadingScreen
import com.mp.momentrip.view.RecommendInitData
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel

private enum class FeedCategory(val koLabel: String) {
    ALL("All"),
    RESTAURANT("맛집"),
    TOUR("핫플"),                     // 관광지
    DORMITORY("숙소"),
    FESTIVAL("축제"),
    CULTURE("문화시설"),
    LEISURE("레포츠"),
    SHOPPING("쇼핑"),
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FeedScreen(
    userState: UserViewModel,
    recommendViewModel: RecommendViewModel,
    bannerClicked: ()->Unit
) {
    val user by userState.user.collectAsState()

    LaunchedEffect(Unit) {
        recommendViewModel.initialize(
            RecommendInitData(
                userVector = user!!.userVector!!,
                foodPreference = user!!.foodPreference,
                region = userState.region.value!!
            )
        )
    }

    val isLoading by recommendViewModel.isLoading.collectAsState()
    val placesByCategory by recommendViewModel.recommendPlacesByCategory.collectAsState()
    val similarPlaces by recommendViewModel.recommendFromSimilarUsers.collectAsState()

    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    BackHandler(enabled = selectedPlace != null) { selectedPlace = null }

    val transition = updateTransition(targetState = selectedPlace, label = "DetailTransition")
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val imageHeight by transition.animateDp(
        transitionSpec = { tween(500) },
        label = "ImageHeight"
    ) {
        if (it == null) 0.dp else screenHeight * 0.4f
    }

    val cardSize by transition.animateDp(
        transitionSpec = { tween(500) },
        label = "CardSize"
    ) {
        if (it == null) 160.dp else screenHeight
    }

    val imageAlpha by transition.animateFloat(
        transitionSpec = { tween(500) },
        label = "ImageAlpha"
    ) { if (it == null) 0f else 1f }

    val textAlpha by transition.animateFloat(
        transitionSpec = { tween(500) },
        label = "TextAlpha"
    ) { if (it == null) 0f else 1f }

    Box(Modifier.fillMaxSize()) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9FAFB))
                    .padding(horizontal = 20.dp, vertical = 24.dp) // 조금 더 넉넉하게
            ) {

                // 📋 추천 피드
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    val topSpots = placesByCategory[ContentType.TOURIST_SPOT]
                    item{
                        SearchBanner(onClick = {
                            bannerClicked()
                        })
                        Spacer(Modifier.height(24.dp))
                    }




                    // 🖼 대형 카드 캐러셀
                    if (!topSpots.isNullOrEmpty()) {
                        item {
                            // ✨ 상단 인사 문구
                            Text(
                                text = "✨ ${user?.name ?: ""}님 취향에 딱 맞는 여행지 추천",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B1E28),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            LargePlaceCardCarouselSection(
                                userState = userState,
                                places = topSpots,
                                modifier = Modifier.padding(bottom = 28.dp)
                            ) { selectedPlace = it }
                        }

                        // 🔥 인기 많은 장소 섹션
                        item {
                            Text(
                                text = "인기 많은 장소",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                            )
                        }

                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(topSpots.take(5)) { place ->
                                    FeedPlaceCard(userState, place) { selectedPlace = place }
                                }
                            }
                        }
                    }

                    // 👥 다른 사용자가 다녀간 장소
                    similarPlaces?.takeIf { it.isNotEmpty() }?.let { list ->
                        item {
                            Spacer(Modifier.height(28.dp))
                            Text(
                                text = "다른 사용자들이 다녀간 장소",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                            )
                        }

                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(list.take(10)) { place ->
                                    FeedPlaceCard(userState, place) { selectedPlace = place }
                                }
                            }
                        }
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
}

@Composable
fun SearchBanner(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFEAF2F8))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column {
            Text(
                text = "🔍 어디로 떠나고 싶으신가요?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1B1E28)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "장소를 검색하고 나만의 여행을 시작하세요",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}



@SuppressLint("ConfigurationScreenWidthHeight")
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




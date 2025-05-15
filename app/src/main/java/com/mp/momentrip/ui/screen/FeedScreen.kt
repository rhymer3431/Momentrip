// ✅ 카드 클릭 시 카드가 커지면서 상세 화면으로 자연스럽게 전환되는 애니메이션 적용됨
package com.mp.momentrip.ui.screen

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.ui.theme.MomenTripTheme
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel

private enum class FeedCategory(val koLabel: String) {
    ALL("All"), RESTAURANT("맛집"), TOUR("핫플"), DORMITORY("숙소")
}

@SuppressLint("StateFlowValueCalledInComposition")
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

    val transition = updateTransition(targetState = selectedPlace, label = "PlaceTransition")
    val cardSize by transition.animateDp(
        transitionSpec = { tween(500) }, label = "CardSize"
    ) { place -> if (place == null) 160.dp else 360.dp }
    val alpha by transition.animateFloat(
        transitionSpec = { tween(500) }, label = "Alpha"
    ) { place -> if (place == null) 0f else 1f }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    CategorySection(
                        selectedCategory = selected,
                        onCategorySelect = { selected = it }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
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

        selectedPlace?.let { place ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .size(cardSize)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFF9F9F9))
                        .clickable { selectedPlace = null }
                        .padding(16.dp)
                ) {

                    Text(
                        text = place.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = place.overview ?: "",
                        modifier = Modifier.alpha(alpha),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceSection(title: String, placeList: List<Place>?, onPlaceClick: (Place) -> Unit) {
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
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
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
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "카테고리",
                fontFamily = FontFamily(Font(R.font.omyu)),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FeedCategory.entries.forEach { category ->
                CategoryItem(
                    icon = GoogleMaterial.Icon.gmd_event,
                    title = category.koLabel,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelect(category) }
                )
            }
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

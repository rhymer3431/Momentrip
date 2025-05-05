package com.mp.momentrip.ui.screen
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.compose.Image

import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Region
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.ui.theme.MomenTripTheme
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.UserDestinations


import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FeedScreen(
    navController: NavController,
    userState: UserViewModel,
    recommendViewModel: RecommendViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        recommendViewModel.initialize(userState)
    }

    val isLoading by recommendViewModel.isLoading.collectAsState()
    val recommendRestaurant by recommendViewModel.recommendRestaurant.collectAsState()
    val recommendDormitory by recommendViewModel.recommendDormitory.collectAsState()
    val recommendTourSpot by recommendViewModel.recommendTourSpot.collectAsState()
    if(isLoading){
        LoadingScreen()
    }
    else{

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // First Section - Categories
                CategorySection(userState,navController)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {

                // Second Section - Recommended Places
                RecommendedPlacesSection(
                    userState,
                    navController,
                    title = "추천 식당",
                    placeList = recommendRestaurant
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                RecommendedPlacesSection(
                    userState,
                    navController,
                    title = "추천 숙소",
                    placeList = recommendDormitory
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                RecommendedPlacesSection(
                    userState,
                    navController,
                    title = "추천 여행지",
                    placeList = recommendTourSpot
                )
            }
        }

    }

}

@Composable
fun CategorySection(
    userState: UserViewModel,
    navController: NavController
) {
    Column {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Text(
                text = "Title",
                fontFamily = FontFamily(Font(R.font.omyu)),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "More",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Categories
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CategoryItem(
                icon = GoogleMaterial.Icon.gmd_event, // Replace with your actual drawable
                title = "나의 일정",
                onClick = {navController.navigate(UserDestinations.SCHEDULE_LIST_ROUTE)}
            )
            CategoryItem(
                icon = GoogleMaterial.Icon.gmd_luggage,
                title = "이전 여행",
                onClick = {}
            )
        }
    }
}


@Composable
fun CategoryItem(
    icon: GoogleMaterial.Icon, // 아이콘으로 받음
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clickable { onClick() }
    ) {
        Image(
            icon,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF161823)
        )
    }
}


@Composable
fun RecommendedPlacesSection(
    userState: UserViewModel,
    navController: NavController,
    title: String,
    placeList: List<Place>?
) {
    Column {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "More",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Places
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            placeList.orEmpty().take(5).forEach { place ->
                PlaceCard(
                    place = place,
                    onClick = {
                        userState.setPlace(place)
                        navController.navigate(MainDestinations.PLACE_DETAIL)}
                    )
            }
        }
    }
}

@Composable
fun PlaceCard(
    place: Place,
    onClick: () -> Unit // 클릭 이벤트 처리
) {
    Column(
        modifier = Modifier
            .width(148.dp)
            .clickable { onClick() }, // 클릭 시 호출될 함수

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .height(150.dp)

        ){
            ImageCard(place.firstImage2)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.5f)
            )
            Text(
                text = place.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = place.addr1.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview(){
    MomenTripTheme {
        FeedScreen(
            navController = rememberNavController(),
            userState = UserViewModel())
    }

}

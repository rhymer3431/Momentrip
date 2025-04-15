package com.mp.momentrip.ui.screen
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.service.RecommendService
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.UserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun FeedScreen(
    userState: UserViewModel,
    recommendViewModel: RecommendViewModel = viewModel()
) {

    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        // 이 블록 안의 코드가 컴포넌트 최초 렌더링 시에만 실행됩니다.
        recommendViewModel.setUser(userState)
        recommendViewModel.loadRecommendRestaurant()


    }

    val isLoading by recommendViewModel.isLoading.collectAsState()
    val recommendRestaurant by recommendViewModel.recommendRestaurant.collectAsState()
    if(isLoading){
        LoadingScreen()
    }
    else{

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // First Section - Categories
                    CategorySection()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Second Section - Recommended Places
                    RecommendedPlacesSection(
                        title = "추천 장소",
                        placeList = recommendRestaurant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                }

            }


        }
    }

}

@Composable
fun CategorySection() {
    Column {
        // Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Text(
                text = "Title",
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
                imageRes = R.drawable.yoga_s, // Replace with your actual drawable
                title = "여행일정"
            )
            CategoryItem(
                imageRes = R.drawable.yoga_s, // Replace with your actual drawable
                title = "이전 여행"
            )
            CategoryItem(
                imageRes = R.drawable.city, // Replace with your actual drawable
                title = "맛집"
            )
            CategoryItem(
                imageRes = R.drawable.city, // Replace with your actual drawable
                title = "숙소"
            )
        }
    }
}

@Composable
fun CategoryItem(imageRes: Int, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(76.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
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
                PlaceCard(place)
            }
        }
    }
}
@Composable
fun PlaceCard(
    place:Place
){
    Column(
        modifier = Modifier.width(148.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        place.firstImage2?.let {
            ImageCard(
                imageUrl = it
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = place.areaCode!!,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.5f)
            )
            Text(
                text = place.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Text(
                text = place.dist.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun FeedScreenPreview(){
    FeedScreen(UserViewModel())
}

package com.mp.momentrip.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.Category
import com.mp.momentrip.data.FoodCategory
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.ui.components.LikeButton
import com.mp.momentrip.ui.theme.MomenTripTheme
import com.mp.momentrip.view.UserViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PlaceDetailScreen(
    userState: UserViewModel
) {
    LaunchedEffect(Unit) {
        userState.loadPlaceDetail()
    }

    val isLoading by userState.isLoading.collectAsState()

    if(isLoading){
        LoadingScreen()
    }
    else{
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 상단 버튼 영역 (Back, Bookmark)
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LikeButton(userState = userState)

            }

            // 사진 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()  // 가로를 채움
                    .height(300.dp)  // 높이 300dp로 설정
            ) {
                userState.place.value?.firstImage?.let { image ->
                    ImageCard(image)  // 이미지는 ImageCard를 통해 표시
                }

            }

            // 제목 부분 (사진과 내용 사이에 위치)
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)  // 위 아래 여백 추가
                    .align(Alignment.CenterHorizontally)  // 가로 가운데 정렬
                    .width(271.dp)
                    .height(77.dp)
                    .shadow(4.dp, shape = RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userState.place.value?.title ?: "None",
                    fontSize = 25.sp,
                    lineHeight = 35.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )



            }

            // 내용 영역
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()  // 가로를 채움
                    .padding(horizontal = 29.dp)  // 좌우 여백 추가
            ) {
                item{
                    Text(
                        text = userState.place.value?.overview ?: "None",
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        color = Color(0xFF828282)
                    )
                }

            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun PlaceDetailScreenPreview(){
    MomenTripTheme {
        PlaceDetailScreen(UserViewModel())
    }
}
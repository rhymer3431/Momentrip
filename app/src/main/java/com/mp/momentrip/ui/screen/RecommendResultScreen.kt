package com.mp.momentrip.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mp.momentrip.ui.theme.OrangeNice
import com.mp.momentrip.ui.theme.TravelAppTheme
import com.mp.momentrip.util.UserDestinations
import com.mp.momentrip.view.UserViewModel
import java.time.LocalDate


@Composable
fun RecommendResult(
    navController: NavController,
    modifier: Modifier = Modifier,
    resultRegion: String,
    userState: UserViewModel
) {
    TravelAppTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "당신에게 추천하는 여행지는...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = resultRegion,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = OrangeNice
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Schedule 생성 버튼 추가
            Button(
                onClick = {
                    // viewModelScope를 사용하여 ViewModel에서 비동기 작업 처리
                    userState.generateSchedule(startAt = LocalDate.of(2025,3,3), endAt = LocalDate.of(2025,3,6), resultRegion)
                    navController.navigate(UserDestinations.SCHEDULE_ROUTE)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "여행 일정 생성")
            }
        }
    }
}

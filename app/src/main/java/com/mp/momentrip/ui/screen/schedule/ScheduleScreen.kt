package com.mp.momentrip.ui.screen.schedule
// ScheduleScreen.kt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mikepenz.iconics.compose.IconicsPainter
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.ui.MainDestinations

import com.mp.momentrip.view.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleListScreen(
    userState: UserViewModel,
    navController: NavController
) {
    // ① UserViewModel 에서 User? 를 수집
    val user by userState.user.collectAsState()
    // ② null 안전호출 + orEmpty 로 빈 리스트 처리
    val schedules = user?.schedules.orEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("나의 여행", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = IconicsPainter(
                                image = GoogleMaterial.Icon.gmd_arrow_back_ios
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFF0063F5)
                        )
                    }
                },
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // 일정 생성 화면으로 이동
                    navController.navigate(MainDestinations.SCHEDULE_CREATION)
                },
                containerColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "스케줄 추가", tint = Color.Black)
            }
        }
    ) { paddingValues ->
        if (schedules.isEmpty()) {
            // 아무 일정이 없을 때
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("생성된 스케줄이 없습니다.")
            }
        } else {
            // 일정 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                // 각 Schedule 아이템
                items(schedules) { schedule ->
                    if (schedule != null) {
                        ScheduleListItem(
                            schedule = schedule,
                            onClick = {
                                // TODO: 상세 화면으로 이동 (예: id 기반으로)
                                // navController.navigate("${MainDestinations.SCHEDULE_DETAIL}/${schedule.id}")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ScheduleListItem(
    schedule: Schedule,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Spacer(Modifier.width(12.dp))
            Icon(
                painter = IconicsPainter(
                    image = GoogleMaterial.Icon.gmd_location_on
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF0063F5)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schedule.region,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF343A40)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${schedule.startDate} ~ ${schedule.endDate}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = Color(0xFFADB5BD)
            )
            Spacer(Modifier.width(12.dp))
        }
        HorizontalDivider(modifier = Modifier.padding(start = 52.dp))
    }
}





// 더미 스케줄 목록
val dummySchedules = listOf(
    Schedule(
        user = "jinha@example.com",
        startDate = "20/05/22",
        endDate   = "20/05/27",
        duration  = 5L,
        days      = List(5) { Day() },
        region    = "제주도"
    ),
    Schedule(
        user = "jinha@example.com",
        startDate = "21/03/10",
        endDate   = "21/03/12",
        duration  = 2L,
        days      = List(2) { Day() },
        region    = "경주"
    ),
    Schedule(
        user = "jinha@example.com",
        startDate = "21/07/15",
        endDate   = "21/07/20",
        duration  = 5L,
        days      = List(5) { Day() },
        region    = "강릉"
    ),
    Schedule(
        user = "jinha@example.com",
        startDate = "21/09/01",
        endDate   = "21/09/05",
        duration  = 4L,
        days      = List(4) { Day() },
        region    = "진주"
    )
)
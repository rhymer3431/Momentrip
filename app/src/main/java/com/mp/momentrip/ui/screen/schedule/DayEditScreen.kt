package com.mp.momentrip.ui.screen.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.Activity
import com.mp.momentrip.data.schedule.Day
import com.mp.momentrip.ui.components.ActivityCard
import com.mp.momentrip.view.ScheduleViewModel
import java.time.LocalTime

@Composable
fun DayEditScreen(
    scheduleViewModel: ScheduleViewModel,
    onAddClick: () -> Unit,
    onDeleteClick: (Activity) -> Unit,
    modifier: Modifier = Modifier
) {
    val day by scheduleViewModel.currentDay.collectAsState()
    val activities = day?.timeTable.orEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 1) 타이틀
        Text(
            text = "Day ${day?.index?.plus(1) ?: "-"}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(activities) { activity ->
                Column {
                    ActivityCard(
                        activity = activity,
                        onDeleteClick = { onDeleteClick(activity) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = Color(0xFFE0F7FA),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "활동 추가",
                            tint = Color(0xFF00796B)
                        )
                    }
                }
            }
        }
    }
}




val sampleDay = Day(
    index = 1,
    timeTable = listOf(
        Activity(
            startTime = LocalTime.of(8, 0),
            endTime = LocalTime.of(9, 0),
            place = Place(
                title = "우도",
                addr1 = "제주 제주시 우도면",
                contentTypeId = 12,
                firstImage2 = "https://example.com/images/udo_thumb.jpg"
            )
        ),
        Activity(
            startTime = LocalTime.of(11, 0),
            endTime = LocalTime.of(12, 0),
            place = Place(
                title = "성산일출봉",
                addr1 = "제주 서귀포시 성산읍 성산리 1",
                contentTypeId = 12,
                firstImage2 = "https://example.com/images/seongsan_thumb.jpg"
            )
        ),
        Activity(
            startTime = LocalTime.of(13, 0),
            endTime = LocalTime.of(14, 0),
            place = Place(
                title = "전망좋은횟집&흑돼지",
                addr1 = "제주 서귀포시 성산읍 성산리 1",
                contentTypeId = 39,
                firstImage2 = "https://example.com/images/restaurant_thumb.jpg"
            )
        ),
        Activity(
            startTime = LocalTime.of(16, 0),
            endTime = LocalTime.of(17, 0),
            place = Place(
                title = "올레길",
                addr1 = "제주 제주시 우도면",
                contentTypeId = 12,
                firstImage2 = "https://example.com/images/olle_thumb.jpg"
            )
        )
    )
)



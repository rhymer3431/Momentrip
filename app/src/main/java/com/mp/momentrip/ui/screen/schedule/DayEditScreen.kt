package com.mp.momentrip.ui.screen.schedule

import AddButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.data.Activity
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.dummy_place
import com.mp.momentrip.ui.components.ActivityCard
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
import java.time.LocalTime

@Composable
fun DayEditScreen(
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel,
    onAddClick: () -> Unit,       // ★ 인자를 제거
    onDeleteClick: (Activity) -> Unit,
    modifier: Modifier = Modifier
) {


    val day = scheduleViewModel.currentDay.collectAsState()
    val activities = remember(day) { day.value?.timeTable.orEmpty() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 1) 타이틀
        Text(
            text = "Day ${day.value!!.index}",
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
            item{
                AddButton(
                    onClick = onAddClick
                )

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



package com.mp.momentrip.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.TimeSlot
import com.mp.momentrip.util.Recommender
import com.mp.momentrip.util.Scheduler
import com.mp.momentrip.view.ScheduleViewModel

@Composable
fun ScheduleScreen(navController: NavController, scheduleViewModel: ScheduleViewModel) {
    val schedule = scheduleViewModel.scheduleState.value

    if (schedule != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Schedule Duration: ${schedule.duration} days")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Region: ${schedule.region}")
            Spacer(modifier = Modifier.height(16.dp))
            DaySection(schedule.days[0]) // 예시: 첫 번째 날을 보여줌
        }
    } else {
        // schedule이 null일 경우 로딩 화면 또는 메시지
        Text(text = "Loading schedule...")
    }
}
@Composable
fun DaySection(day: Day) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Day Activities")
            Spacer(modifier = Modifier.height(8.dp))
            for (ts in day.timeTable){
                TimeSlotItem(ts)
            }


        }
    }
}
@Composable
fun TimeSlotItem(timeSlot: TimeSlot) {
    Column {
        Text(
            text = "Time: ${timeSlot.time}",

        )
        Text(
            text = "Place: ${timeSlot.place.name} (${timeSlot.place.category})",

        )
        Text(
            text = "Address: ${timeSlot.place.address}",

        )
        Text(
            text = "Phone: ${timeSlot.place.phone}",

        )
        Text(
            text = "Coordinates: (${timeSlot.place.x}, ${timeSlot.place.y})",

        )
    }
}



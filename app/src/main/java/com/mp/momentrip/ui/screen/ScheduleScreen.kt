package com.mp.momentrip.ui.screen
// ScheduleScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.Activity
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.ui.components.ScheduleCard
import com.mp.momentrip.ui.MainDestinations

import com.mp.momentrip.view.UserViewModel

@Composable
fun ScheduleListScreen(
    userState: UserViewModel,
    navController: NavController
) {
    val schedules by userState.schedules.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(MainDestinations.SCHEDULE_CREATION)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "스케쥴 추가")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "내 스케쥴",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (schedules.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("생성된 스케쥴이 없습니다.")
                }
            } else {
                LazyColumn {
                    items(schedules) { schedule ->
                        ScheduleCard(schedule = schedule)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun ScheduleDetails(schedule: Schedule) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {

            ScheduleCard(schedule)
        }
    }
}

@Composable
fun DayScheduleCard(day: Day) {
    Card (
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {


            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                day.timeTable.forEach { timeSlot ->
                    ActivityItem(timeSlot = timeSlot)
                }
            }
        }
    }
}

@Composable
fun ActivityItem(timeSlot: Activity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = timeSlot.startTime,

            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            PlaceCard(timeSlot.place)
        }
    }
}


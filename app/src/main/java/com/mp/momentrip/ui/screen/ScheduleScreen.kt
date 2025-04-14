package com.mp.momentrip
// ScheduleScreen.kt
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.Activity
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.ui.components.DayCard
import com.mp.momentrip.ui.components.PlaceCard
import com.mp.momentrip.ui.components.ScheduleCard

import com.mp.momentrip.util.UserDestinations
import com.mp.momentrip.view.UserViewModel


@Composable
fun ScheduleListScreen(
    navController: NavController,
    userState: UserViewModel
) {
    LaunchedEffect(Unit) {
        userState.loadSchedules()
    }

    val scheduleList by userState.schedules.collectAsState()
    val isLoading by userState.isLoading.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display loading, error, or schedule list
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            scheduleList.isNotEmpty() -> {
                ScheduleList(
                    userState = userState,
                    scheduleList = scheduleList,
                    navController = navController)
            }
        }
    }
}
@Composable
fun ScheduleList(
    userState: UserViewModel,
    scheduleList: List<Schedule>,
    navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

    }
}

@Composable
fun ScheduleScreen(
    navController: NavController,
    userState: UserViewModel
) {

    val schedule by userState.schedule.collectAsState()
    val isLoading by userState.isLoading.collectAsState()
    val error by userState.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))


        // Display loading, error, or schedule
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            schedule != null -> {
                ScheduleDetails(schedule = schedule!!)
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


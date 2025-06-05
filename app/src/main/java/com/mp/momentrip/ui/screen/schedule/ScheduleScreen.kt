package com.mp.momentrip.ui.screen.schedule
// ScheduleScreen.kt

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mikepenz.iconics.compose.IconicsPainter
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.data.schedule.Schedule
import com.mp.momentrip.ui.ScheduleDestinations
import com.mp.momentrip.ui.components.ScheduleListItem
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleListScreen(
    userState: UserViewModel,
    scheduleViewModel: ScheduleViewModel,
    navController: NavController,
    onClick: (Schedule) -> Unit
) {
    val user by userState.user.collectAsState()
    val schedules = user?.schedules.orEmpty()
    Box(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("나의 여행", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = IconicsPainter(GoogleMaterial.Icon.gmd_arrow_back_ios),
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
                    onClick = { navController.navigate(ScheduleDestinations.SCHEDULE_CREATION) },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) { Icon(Icons.Default.Add, null, tint = Color.Black) }
            }
        ) { padding ->
            if (schedules.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                    Text("생성된 스케줄이 없습니다.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(schedules) { sch ->
                        sch?.let {
                            ScheduleListItem(
                                schedule = it,
                                onClick = {onClick(it)}
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

    }
}

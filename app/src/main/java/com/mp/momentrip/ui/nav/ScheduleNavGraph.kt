package com.mp.momentrip.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.screen.schedule.ScheduleCreationScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleListScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleMapScreen
import com.mp.momentrip.view.UserViewModel


// ScheduleNavGraph.kt
fun NavGraphBuilder.scheduleNavGraph(
    navController: NavController,
    userViewModel: UserViewModel
) {
    composable(UserDestinations.SCHEDULE_ROUTE) {
        ScheduleMapScreen(userState = userViewModel)
    }
    composable(UserDestinations.SCHEDULE_LIST_ROUTE) {
        ScheduleListScreen(userViewModel,navController )
    }
    composable(MainDestinations.SCHEDULE_CREATION) {
        ScheduleCreationScreen(userViewModel) {
            navController.navigate(UserDestinations.SCHEDULE_ROUTE)
        }
    }
}
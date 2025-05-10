package com.mp.momentrip.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.screen.BottomNavBar
import com.mp.momentrip.view.UserViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    startDestination: String
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val bottomBarRoutes = listOf(
        MainDestinations.FEED_ROUTE,
        MainDestinations.PROFILE_ROUTE,
        UserDestinations.LIKED_ROUTE,
        UserDestinations.SCHEDULE_ROUTE
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,  // ✅ 동적 시작 지점 적용
            modifier = Modifier.padding(innerPadding)
        ) {
            authNavGraph(navController, userViewModel)
            mainNavGraph(navController, userViewModel)
            scheduleNavGraph(navController, userViewModel)
            recommendNavGraph(navController, userViewModel)
        }
    }
}

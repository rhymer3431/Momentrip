package com.mp.momentrip.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.components.CalendarSample3
import com.mp.momentrip.ui.screen.feed.FeedScreen
import com.mp.momentrip.ui.screen.profile.ProfileScreen
import com.mp.momentrip.ui.screen.schedule.CalendarScreen

import com.mp.momentrip.ui.screen.schedule.ScheduleCreationScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleListScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleMapScreen



import com.mp.momentrip.ui.screen.user.RecommendResult
import com.mp.momentrip.ui.screen.user.SignInScreen
import com.mp.momentrip.view.UserViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel
){
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(
            bottom = paddingValues.calculateBottomPadding()
        )) {
            BottomNavGraph(navController, userState)
        }
    }
}
@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(
            route = MainDestinations.FEED_ROUTE,
            icon = Icons.Default.Home,
            label = "피드"
        ),
        BottomNavItem(
            route = MainDestinations.SEARCH_ROUTE,
            icon = Icons.Default.Home,
            label = "검색"
        ),
        BottomNavItem(
            route = UserDestinations.LIKED_ROUTE,
            icon = Icons.Default.Star,
            label = "좋아요"
        ),
        BottomNavItem(
            route = MainDestinations.PROFILE_ROUTE,
            icon = Icons.Default.Face,
            label = "프로필"
        )
    )

    // Find the current selected index
    val selectedIndex = remember (items, currentRoute) {
        items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    }

    AnimatedNavigationBar(

        selectedIndex = selectedIndex,
        modifier = Modifier.fillMaxWidth(),
        barColor = colorScheme.primary,
        ballColor = colorScheme.onPrimary,
        cornerRadius = shapeCornerRadius(
            topLeft = 50.dp,
            topRight = 50.dp,
            bottomLeft = 0.dp,
            bottomRight = 0.dp
        ),
        ballAnimation = Straight(tween(300)),
        indentAnimation = Height(tween(300))
    ) {
        items.forEachIndexed { index, item ->
            IconButton(
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (selectedIndex == index) { // 눌렀을 때
                        colorScheme.secondary
                    } else { // 안 눌렀을 때
                        colorScheme.onPrimary
                    }
                )
            }
        }
    }
}

// Data class to represent navigation items
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavGraph(
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel
){

    NavHost(
        navController = navController,
        startDestination = MainDestinations.FEED_ROUTE
    ) {
        composable(MainDestinations.FEED_ROUTE) {
            FeedScreen(

                userState = userState,

            )
        }
        composable(MainDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                navController,
                userState
            )
        }
        composable(UserDestinations.SCHEDULE_ROUTE){
            ScheduleMapScreen(
                userState = userState,

                )

        }
        composable(UserDestinations.SCHEDULE_LIST_ROUTE){
            ScheduleListScreen(
                navController = navController,
                userState = userState
            )
        }
        composable(MainDestinations.SCHEDULE_CREATION){
            ScheduleCreationScreen(
                userViewModel = userState,
                onScheduleCreated = {navController.navigate(UserDestinations.SCHEDULE_ROUTE)}

            )
        }
        composable(UserDestinations.ANALYZE_RESULT + "/{place}"){
                backStackEntry -> val place = backStackEntry.arguments?.getString("place")
            if (place != null) {
                RecommendResult(
                    navController = navController,
                    modifier = Modifier,
                    resultRegion = place,
                    userState = userState
                )
            }
        }
        composable(MainDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                navController = navController,
                userState = userState
            )
        }

        composable(UserDestinations.LIKED_ROUTE) {
            LikedPlaceScreen(userState,{})
        }
        composable(MainDestinations.SEARCH_ROUTE){

        }

    }
}
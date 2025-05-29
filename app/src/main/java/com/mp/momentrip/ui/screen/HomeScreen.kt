package com.mp.momentrip.ui.screen


import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.ScheduleDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.schedule.ScheduleChecklistScreen
import com.mp.momentrip.ui.screen.feed.FeedScreen
import com.mp.momentrip.ui.screen.profile.ProfileScreen
import com.mp.momentrip.ui.screen.schedule.ActivitySelectScreen
import com.mp.momentrip.ui.screen.schedule.DayEditScreen


import com.mp.momentrip.ui.screen.schedule.ScheduleCreationScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleDetailScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleListScreen


import com.mp.momentrip.ui.screen.user.RecommendResult
import com.mp.momentrip.ui.screen.user.SignInScreen
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel,
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel
){
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(
            bottom = paddingValues.calculateBottomPadding()
        )) {
            BottomNavGraph(navController, userState,scheduleViewModel,recommendViewModel)
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
            icon = Icons.Default.Home, // 홈
            label = "피드"
        ),
        BottomNavItem(
            route = MainDestinations.SEARCH_ROUTE,
            icon = Icons.Default.Search, // 검색
            label = "검색"
        ),
        BottomNavItem(
            route = UserDestinations.LIKED_ROUTE,
            icon = Icons.Default.FavoriteBorder, // 좋아요
            label = "좋아요"
        ),
        BottomNavItem(
            route = MainDestinations.PROFILE_ROUTE,
            icon = Icons.Default.Person, // 프로필
            label = "프로필"
        )

    )

    // Find the current selected index
    val selectedIndex = remember (items, currentRoute) {
        items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    }

    AnimatedNavigationBar(
        selectedIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colorScheme.primary,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            )
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)), // 실제 모양 clip
        barColor = Color.Transparent, // 내부 색 제거
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
    userState: UserViewModel,
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel
){


    NavHost(
        navController = navController,
        startDestination = MainDestinations.FEED_ROUTE
    ) {
        composable(MainDestinations.FEED_ROUTE) {
            FeedScreen(
                userState = userState,
                recommendViewModel = recommendViewModel
            )
        }
        composable(MainDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                navController,
                userState
            )
        }
        composable(ScheduleDestinations.SCHEDULE_ROUTE){
            ScheduleDetailScreen(
                navController,
                scheduleViewModel,
                userState

            )

        }
        composable(ScheduleDestinations.SCHEDULE_LIST_ROUTE){
            ScheduleListScreen(
                navController = navController,
                scheduleViewModel = scheduleViewModel,
                userState = userState,
                onClick = {
                    scheduleViewModel.setSchedule(it)
                    recommendViewModel.loadRecommendPlaces(
                        region = it.region
                    )
                    navController.navigate(ScheduleDestinations.SCHEDULE_ROUTE)
                }
            )
        }
        composable(ScheduleDestinations.SCHEDULE_CREATION){
            ScheduleCreationScreen(
                userViewModel = userState,
                onScheduleCreated = {navController.navigate(ScheduleDestinations.SCHEDULE_ROUTE)}

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
            SearchScreen(
                userState = userState
            )
        }
        composable(ScheduleDestinations.DAY_EDIT_ROUTE){
            DayEditScreen(
                recommendViewModel = recommendViewModel,
                scheduleViewModel = scheduleViewModel,
                onDeleteClick = {},
                onAddClick = {navController.navigate(ScheduleDestinations.ACTIVITY_SELECT_ROUTE)}

            )
        }
        composable(ScheduleDestinations.CHECK_LIST_ROUTE){
            ScheduleChecklistScreen(
                scheduleViewModel = scheduleViewModel,
                {scheduleViewModel.updateChecklist(
                    updatedChecklist = it,
                    userVM = userState
                )}
            )
        }
        composable(ScheduleDestinations.ACTIVITY_SELECT_ROUTE){
            ActivitySelectScreen(
                scheduleViewModel = scheduleViewModel,
                recommendViewModel = recommendViewModel,
                onPlaceSelected = { },
                onCancel = { navController.popBackStack() }
            )
        }

    }
}
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
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.mp.momentrip.ui.CommunityDestinations
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.ScheduleDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.schedule.ScheduleChecklistScreen
import com.mp.momentrip.ui.screen.community.CommunityScreen
import com.mp.momentrip.ui.screen.community.PostCreateScreen
import com.mp.momentrip.ui.screen.feed.FeedScreen
import com.mp.momentrip.ui.screen.profile.ProfileScreen
import com.mp.momentrip.ui.screen.schedule.ActivitySelectScreen
import com.mp.momentrip.ui.screen.schedule.DayEditScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleCreationScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleDetailScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleListScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleOverviewScreen
import com.mp.momentrip.ui.screen.search.SearchScreen
import com.mp.momentrip.ui.screen.setting.ChangePasswordScreen
import com.mp.momentrip.ui.screen.setting.SettingsScreen
import com.mp.momentrip.ui.screen.user.SignInScreen
import com.mp.momentrip.view.CommunityViewModel
import com.mp.momentrip.view.PlaceViewModel
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    mainNavController: NavController,
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel,
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel,
    communityViewModel: CommunityViewModel,
    placeViewModel: PlaceViewModel = viewModel()
){
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(
            bottom = paddingValues.calculateBottomPadding()
        )) {
            BottomNavGraph(
                mainNavController,
                navController,
                userState,
                scheduleViewModel,
                recommendViewModel,
                communityViewModel,
                placeViewModel)
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(MainDestinations.FEED_ROUTE, Icons.Default.Home, "피드"),
        BottomNavItem(MainDestinations.SEARCH_ROUTE, Icons.Default.Search, "검색"),
        BottomNavItem(MainDestinations.COMMUNITY_ROUTE, Icons.Default.People, "커뮤니티"),
        BottomNavItem(UserDestinations.LIKED_ROUTE, Icons.Default.FavoriteBorder, "좋아요"),
        BottomNavItem(MainDestinations.PROFILE_ROUTE, Icons.Default.Person, "프로필")
    )

    val selectedIndex = remember(items, currentRoute) {
        items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    }

    AnimatedNavigationBar(
        selectedIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)),
        barColor = colorScheme.primary,
        ballColor = colorScheme.onPrimary,
        cornerRadius = shapeCornerRadius(
            topLeft = 50.dp, topRight = 50.dp, bottomLeft = 0.dp, bottomRight = 0.dp
        ),
        ballAnimation = Straight(tween(300)),
        indentAnimation = Height(tween(300))
    ) {
        items.forEachIndexed { index, item ->
            IconButton(onClick = { navController.navigateToRoot(item.route) }) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (selectedIndex == index) colorScheme.secondary else colorScheme.onPrimary
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
    mainNavController: NavController,
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel,
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel,
    communityViewModel: CommunityViewModel,
    placeViewModel: PlaceViewModel
) {
    val user by userState.user.collectAsState()
    NavHost(
        navController = navController,
        startDestination = MainDestinations.FEED_ROUTE
    ) {
        composable(MainDestinations.FEED_ROUTE) {
            FeedScreen(
                userState = userState,
                recommendViewModel = recommendViewModel,
                bannerClicked = {
                    navController.navigateClearingStack(MainDestinations.SEARCH_ROUTE)
                }
            )
        }
        composable(MainDestinations.PROFILE_ROUTE) {
            ProfileScreen(mainNavController,navController, userState)
        }
        composable(MainDestinations.COMMUNITY_ROUTE) {
            CommunityScreen(
                userViewModel = userState,
                viewModel = communityViewModel,
                onAddPostClick = {
                    navController.navigate(CommunityDestinations.POST_CREATE_ROUTE)
                },
                onPostClick = {}
            )
        }
        composable(UserDestinations.LIKED_ROUTE) {
            LikedPlaceScreen(
                placeViewModel = placeViewModel,
                userState = userState,
                onPlaceClick = { placeViewModel.setPlace(it) },
                onClose = { placeViewModel.clearPlace() }
            )
        }
        composable(MainDestinations.SEARCH_ROUTE) {
            SearchScreen(userState = userState, recommendViewModel = recommendViewModel)
        }
        composable(MainDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                navController = navController,
                userState = userState,
            )
        }
        composable(CommunityDestinations.POST_CREATE_ROUTE) {
            PostCreateScreen(
                userState = userState,
                placeViewModel = placeViewModel,
                onPlaceSelectClick = {
                    navController.navigate(UserDestinations.LIKED_ROUTE)
                },
                onPostSubmit = {
                    communityViewModel.uploadPost(it, user!!.toDto())
                    navController.navigateClearingStack(MainDestinations.COMMUNITY_ROUTE)
                },
                onCancel = { navController.popBackStack() }
            )
        }
        // ✳️ 이하 일정 관련은 그대로 유지 (세부 내비게이션은 popBackStack 위주)
        composable(ScheduleDestinations.SCHEDULE_ROUTE) {
            ScheduleDetailScreen(navController, scheduleViewModel, userState)
        }
        composable(ScheduleDestinations.SCHEDULE_LIST_ROUTE) {
            ScheduleListScreen(
                navController = navController,
                scheduleViewModel = scheduleViewModel,
                userState = userState,
                onClick = {
                    scheduleViewModel.setSchedule(it)
                    recommendViewModel.loadRecommendPlaces(region = it.region)
                    navController.navigate(ScheduleDestinations.SCHEDULE_ROUTE)
                }
            )
        }
        composable(ScheduleDestinations.SCHEDULE_CREATION) {
            ScheduleCreationScreen(
                userViewModel = userState,
                recommendViewModel = recommendViewModel,
                onScheduleCreated = {
                    scheduleViewModel.setSchedule(it)
                    navController.navigateClearingStack(ScheduleDestinations.SCHEDULE_ROUTE)
                }
            )
        }
        composable(ScheduleDestinations.DAY_EDIT_ROUTE) {
            DayEditScreen(
                scheduleViewModel = scheduleViewModel,
                onDeleteClick = { scheduleViewModel.removeActivityById(it.id, userState) },
                onAddClick = { navController.navigate(ScheduleDestinations.ACTIVITY_SELECT_ROUTE) }
            )
        }
        composable(ScheduleDestinations.CHECK_LIST_ROUTE) {
            ScheduleChecklistScreen(
                scheduleViewModel = scheduleViewModel,
                onChecklistChange = {
                    scheduleViewModel.updateChecklist(it, userVM = userState)
                }
            )
        }
        composable(ScheduleDestinations.ACTIVITY_SELECT_ROUTE) {
            ActivitySelectScreen(
                scheduleViewModel = scheduleViewModel,
                recommendViewModel = recommendViewModel,
                onPlaceTimeSelected = { place, start, end ->
                    scheduleViewModel.addActivity(place, start, end, userState)
                    navController.navigate(ScheduleDestinations.DAY_EDIT_ROUTE)
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(ScheduleDestinations.SCHEDULE_OVERVIEW_ROUTE) {
            ScheduleOverviewScreen(
                userState = userState,
                scheduleViewModel = scheduleViewModel,
                navController = navController
            )
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onPersonalityClick = {
                    navController.navigate(MainDestinations.PREFERENCE_ANALYZE)
                },
                onChangePasswordClick = {
                    navController.navigate(MainDestinations.CHANGE_PASSWORD_ROUTE)
                }
            )
        }
        composable(MainDestinations.CHANGE_PASSWORD_ROUTE) {
            ChangePasswordScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
fun NavController.navigateInit(
    route: String,
    scheduleViewModel: ScheduleViewModel
){
    this.navigate(route) {
        scheduleViewModel.initSchedule()
        launchSingleTop = true
        restoreState = false
    }
}
fun NavController.navigateClearingStack(
    route: String,
    inclusive: Boolean = true
) {
    this.navigate(route) {
        popUpTo(this@navigateClearingStack.graph.findStartDestination().id) {
            this.inclusive = inclusive
        }
        launchSingleTop = true
        restoreState = false
    }
}

fun NavController.navigateToRoot(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

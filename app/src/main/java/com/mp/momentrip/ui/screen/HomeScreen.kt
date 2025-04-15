package com.mp.momentrip.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mp.momentrip.ScheduleListScreen
import com.mp.momentrip.util.MainDestinations
import com.mp.momentrip.util.UserDestinations
import com.mp.momentrip.view.UserViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel
){
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) {
        BottomNavGraph(
            navController,
            userState)
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
            route = MainDestinations.PROFILE_ROUTE,
            icon = Icons.Default.Face,
            label = "프로필"
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavItem(
                    icon = item.icon,
                    isSelected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(76.dp, 44.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.Black else Color(0xFF1D1B20),
            modifier = Modifier.size(24.dp)
        )
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
            FeedScreen(userState)
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
        composable(UserDestinations.SCHEDULE_ROUTE){
            ScheduleMapScreen(
                userState = userState,

                )
        }
        composable(MainDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                navController = navController,
                modifier = Modifier,
                userState = userState
            )
        }

    }
}
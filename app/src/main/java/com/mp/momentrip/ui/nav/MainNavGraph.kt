package com.mp.momentrip.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.screen.feed.FeedScreen

import com.mp.momentrip.ui.screen.profile.ProfileScreen
import com.mp.momentrip.view.UserViewModel

// MainNavGraph.kt
fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    userViewModel: UserViewModel
) {
    composable(MainDestinations.FEED_ROUTE) {
        FeedScreen(userViewModel)
    }
    composable(MainDestinations.PROFILE_ROUTE) {
        ProfileScreen(navController, userViewModel)
    }
    composable(UserDestinations.LIKED_ROUTE) {

    }
}
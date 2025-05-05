package com.mp.momentrip.ui.nav

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.UserDestinations
import com.mp.momentrip.ui.screen.PlaceDetailScreen
import com.mp.momentrip.ui.screen.RecommendResult
import com.mp.momentrip.ui.screen.SignInScreen
import com.mp.momentrip.ui.screen.SignUpScreen
import com.mp.momentrip.view.UserViewModel

// RecommendNavGraph.kt
fun NavGraphBuilder.recommendNavGraph(
    navController: NavController,
    userViewModel: UserViewModel
) {
    composable(MainDestinations.PLACE_DETAIL_ROUTE) {
        PlaceDetailScreen(userViewModel)
    }
    composable(UserDestinations.ANALYZE_RESULT + "/{place}") { backStackEntry ->
        val place = backStackEntry.arguments?.getString("place") ?: return@composable
        RecommendResult(navController, Modifier, place, userViewModel)
    }
}

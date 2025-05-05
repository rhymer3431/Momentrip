package com.mp.momentrip.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.screen.SignInScreen
import com.mp.momentrip.ui.screen.SignUpScreen
import com.mp.momentrip.view.UserViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    userViewModel: UserViewModel
) {
    composable(MainDestinations.SIGN_IN_ROUTE) {
        SignInScreen(navController, userViewModel)
    }
    composable(MainDestinations.SIGN_UP_ROUTE) {
        SignUpScreen(navController, userViewModel)
    }
}

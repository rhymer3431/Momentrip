package com.mp.momentrip.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.service.RecommendService
import com.mp.momentrip.ui.screen.loading.LoadingScreen
import com.mp.momentrip.view.AuthViewModel
import com.mp.momentrip.view.CommunityViewModel
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel

@Composable
fun Momentrip(
    userState: UserViewModel = viewModel(),
    scheduleViewModel: ScheduleViewModel = viewModel(),
    recommendViewModel: RecommendViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    communityViewModel: CommunityViewModel = viewModel()
    ) {
    val navController = rememberNavController()
    val authState by authViewModel.authState



    LaunchedEffect(authState) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            AccountService.loadUser(user)?.let { loadedUser ->

                userState.setUser(loadedUser)

                if (userState.isValidUserVector() == true) {
                    userState.user.value!!.userVector.let { vector ->
                        userState.setRegion(RecommendService.getRegionByVector(vector!!.toList()))
                    }
                }

                authViewModel.setAuthenticated()
            }
        }
    }


    val startDestination = when {
        authState is AuthViewModel.AuthState.Loading -> null
        authState is AuthViewModel.AuthState.Unauthenticated -> MainDestinations.SIGN_IN_ROUTE
        authState is AuthViewModel.AuthState.Authenticated && userState.isValidUserVector() != true -> MainDestinations.PREFERENCE_ANALYZE
        else -> MainDestinations.HOME_ROUTE
    }

    if (startDestination == null) {
        LoadingScreen()

    } else {
        NavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            userState = userState,
            scheduleViewModel = scheduleViewModel,
            recommendViewModel = recommendViewModel,
            communityViewModel = communityViewModel,
            startDestination = startDestination
        )
    }
}



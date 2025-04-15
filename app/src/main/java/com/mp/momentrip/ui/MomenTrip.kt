package com.mp.momentrip.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.common.util.Utility
import com.mp.momentrip.service.AccountService
import com.mp.momentrip.service.RecommendService
import com.mp.momentrip.util.MainDestinations
import com.mp.momentrip.util.NavGraph
import com.mp.momentrip.view.AuthViewModel
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun MomenTrip(
    userState: UserViewModel,
    authViewModel: AuthViewModel = viewModel(),
    ) {
    val navController = rememberNavController()
    val authState by authViewModel.authState

    // ✅ Firebase 유저 가져와서 userState 업데이트
    LaunchedEffect(authState) {
        FirebaseAuth.getInstance().currentUser?.let { Log.d("test", it.uid) }
        FirebaseAuth.getInstance().currentUser?.let { user ->
            AccountService.loadUser(user)?.let { loadedUser ->

                userState.setUser(loadedUser)
                if(userState.isValidUserPreference()==true){
                    userState.setRegion(RecommendService.getRegionByPreference(userState.getUserPreference()))
                }

                authViewModel.setAuthenticated() // ✅ 유저 정보 로드 완료 후 상태 변경
            }
        }
    }

    val startDestination = when {
        authState is AuthViewModel.AuthState.Loading -> null
        authState is AuthViewModel.AuthState.Unauthenticated -> MainDestinations.SIGN_IN_ROUTE
        authState is AuthViewModel.AuthState.Authenticated && userState.isValidUserPreference() != true -> MainDestinations.PREFERENCE_ANALYZE
        else -> MainDestinations.HOME_ROUTE
    }



    if (startDestination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Log.d("test",startDestination.toString())

        NavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            userState = userState,
            startDestination = startDestination
        )
    }
}



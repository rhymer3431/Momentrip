package com.mp.momentrip.ui
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.mp.momentrip.ui.screen.HomeScreen



import com.mp.momentrip.ui.screen.profile.ProfileScreen
import com.mp.momentrip.ui.screen.user.QuestionScreen
import com.mp.momentrip.ui.screen.user.RecommendResult
import com.mp.momentrip.ui.screen.schedule.ScheduleListScreen
import com.mp.momentrip.ui.screen.schedule.ScheduleMapScreen
import com.mp.momentrip.ui.screen.user.SignInScreen
import com.mp.momentrip.ui.screen.user.SignUpScreen



import com.mp.momentrip.view.UserViewModel


object MainDestinations {
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
    const val PLACE_DETAIL_ROUTE = "place"
    const val SEARCH_ROUTE = "search"
    const val PLACE_DETAIL = "placeId"
    const val SIGN_IN_ROUTE = "signIn"
    const val SIGN_UP_ROUTE = "signUp"
    const val PREFERENCE_ANALYZE = "preferenceAnalyze"
    const val FEED_ROUTE = "feed"
    const val SCHEDULE_CREATION = "scheduleCreate"
}

object UserDestinations {
    const val ANALYZE_RESULT = "analyzeResult"
    const val SCHEDULE_ROUTE = "schedule"
    const val SCHEDULE_LIST_ROUTE = "scheduleList"
    const val LIKED_ROUTE = "liked"
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel,
    startDestination : String
)  {



    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.SIGN_IN_ROUTE) {
            SignInScreen(
                navController = navController,
                userState = userState
            )
        }

        composable(MainDestinations.SIGN_UP_ROUTE) {
            SignUpScreen(
                navController = navController,
                userState
            )
        }
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(
                userState = userState
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
        composable(MainDestinations.PREFERENCE_ANALYZE){
            QuestionScreen(
                modifier = Modifier,
                navController = navController,
                userState = userState
            )
        }
        composable(UserDestinations.SCHEDULE_LIST_ROUTE){
            ScheduleListScreen(
                navController = navController,
                userState = userState
            )
        }
        composable(MainDestinations.PREFERENCE_ANALYZE){
            QuestionScreen(
                modifier = Modifier,
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




    }

}

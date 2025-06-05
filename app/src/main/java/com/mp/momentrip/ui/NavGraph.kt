package com.mp.momentrip.ui



import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mp.momentrip.ui.screen.HomeScreen
import com.mp.momentrip.ui.screen.user.QuestionScreen
import com.mp.momentrip.ui.screen.user.RecommendResult
import com.mp.momentrip.ui.screen.user.SignInScreen
import com.mp.momentrip.ui.screen.user.SignUpScreen
import com.mp.momentrip.view.CommunityViewModel
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
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
    const val COMMUNITY_ROUTE = "community"

    // ✅ 추가된 라우트
    const val SETTINGS_ROUTE = "settings"
    const val CHANGE_PASSWORD_ROUTE = "changePassword"
}

object CommunityDestinations{
    const val POST_CREATE_ROUTE = "postCreate"
}
object ScheduleDestinations{
    const val SCHEDULE_ROUTE = "schedule"
    const val SCHEDULE_OVERVIEW_ROUTE = "scheduleOverview"
    const val SCHEDULE_LIST_ROUTE = "scheduleList"
    const val SCHEDULE_CREATION = "scheduleCreate"
    const val CHECK_LIST_ROUTE = "checklist"
    const val DAY_EDIT_ROUTE = "dayEdit"
    const val ACTIVITY_SELECT_ROUTE = "activitySelect"
    const val ACTIVITY_TIME_ROUTE = "activityTime"
}

object UserDestinations {
    const val ANALYZE_RESULT = "analyzeResult"
    const val LIKED_ROUTE = "liked"
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userState: UserViewModel,
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel,
    communityViewModel: CommunityViewModel,
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
                mainNavController = navController,
                userState = userState,
                scheduleViewModel = scheduleViewModel,
                recommendViewModel = recommendViewModel,
                communityViewModel = communityViewModel
            )
        }


        composable(MainDestinations.PREFERENCE_ANALYZE){
            QuestionScreen(
                modifier = Modifier,
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
                    onClick = {navController.navigate(MainDestinations.HOME_ROUTE)}
                )
            }
        }





    }

}

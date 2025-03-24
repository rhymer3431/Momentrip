package com.mp.momentrip

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mp.momentrip.ui.home.PreferenceScreen
import com.mp.momentrip.ui.home.ScheduleScreen
import com.mp.momentrip.view.ScheduleViewModel

@Composable
fun MyApp() {
    val modifier = Modifier
    val navController = rememberNavController()
    val scheduleViewModel: ScheduleViewModel = viewModel()
    NavHost(navController = navController, startDestination = "preference") {
        composable("preference") { PreferenceScreen(modifier,navController, scheduleViewModel = scheduleViewModel)  }
        composable("schedule") { ScheduleScreen(navController = navController, scheduleViewModel =scheduleViewModel) }
    }
}



@Composable
fun ScreenA(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is Screen A")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("screenB") }) {
            Text(text = "Go to Screen B")
        }
    }
}

@Composable
fun ScreenB(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is Screen B")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Go back to Screen A")
        }
    }
}
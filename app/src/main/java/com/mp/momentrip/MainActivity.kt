package com.mp.momentrip

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.mp.momentrip.ui.MomenTrip
import com.mp.momentrip.ui.theme.MomenTripTheme


import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            // ViewModel 인스턴스 생성
            val userViewModel: UserViewModel = UserViewModel()
            MomenTripTheme {
                MomenTrip(userViewModel)
            }

        }
    }
}




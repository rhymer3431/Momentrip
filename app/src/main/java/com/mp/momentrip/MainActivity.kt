package com.mp.momentrip


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mp.momentrip.ui.Momentrip
import com.mp.momentrip.ui.theme.MomenTripTheme
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            // ViewModel 인스턴스 생성
            MomenTripTheme {
                Momentrip()
            }

        }
    }
}




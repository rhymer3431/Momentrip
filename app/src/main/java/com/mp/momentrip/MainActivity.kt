package com.mp.momentrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.TimeSlot

import com.mp.momentrip.ui.home.PreferenceScreen
import com.mp.momentrip.ui.home.ScheduleScreen


import com.mp.momentrip.util.Scheduler


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }
}


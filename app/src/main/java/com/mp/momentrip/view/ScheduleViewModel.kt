package com.mp.momentrip.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mp.momentrip.data.Schedule

class ScheduleViewModel : ViewModel() {
    // Schedule 상태를 저장하는 mutableState
    private val _scheduleState = mutableStateOf<Schedule?>(null)
    val scheduleState: State<Schedule?> = _scheduleState

    // Schedule을 업데이트하는 함수
    fun setSchedule(schedule: Schedule) {
        _scheduleState.value = schedule
    }
}
// ScheduleViewModel.kt
package com.mp.momentrip.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mp.momentrip.data.Activity
import com.mp.momentrip.data.CheckItem
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ScheduleViewModel : ViewModel() {

    private val _schedule = MutableStateFlow<Schedule?>(null)
    val schedule: StateFlow<Schedule?> get() = _schedule

    private val _selectedDayIndex = MutableStateFlow(0)
    val selectedDayIndex: StateFlow<Int> get() = _selectedDayIndex

    private val _selectedActivityIndex = MutableStateFlow<Int?>(null)
    val selectedActivityIndex: StateFlow<Int?> get() = _selectedActivityIndex

    val currentDay: StateFlow<Day?> = _schedule
        .combine(_selectedDayIndex) { sched, idx ->
            sched?.days?.getOrNull(idx)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            initialValue = null
        )

    fun setSchedule(schedule: Schedule) {
        _schedule.value = schedule
        _selectedDayIndex.value = 0
        _selectedActivityIndex.value = null
    }

    fun selectDay(index: Int) {
        _selectedDayIndex.value = index
        _selectedActivityIndex.value = null
    }

    fun selectActivity(index: Int) {
        _selectedActivityIndex.value = index
    }

    fun getCurrentDay(): Day? = _schedule.value?.days?.getOrNull(_selectedDayIndex.value)

    fun getCurrentActivity(): Activity? =
        getCurrentDay()?.timeTable?.getOrNull(_selectedActivityIndex.value ?: -1)
}

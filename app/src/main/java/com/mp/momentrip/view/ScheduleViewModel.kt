// ScheduleViewModel.kt
package com.mp.momentrip.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.Activity
import com.mp.momentrip.data.schedule.CheckItem
import com.mp.momentrip.data.schedule.Day
import com.mp.momentrip.data.schedule.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalTime

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

    fun updateChecklist(
        updatedChecklist: List<CheckItem>,
        userVM: UserViewModel
    ) {
        val currentSchedule = _schedule.value
        val userSchedules = userVM.user.value?.schedules?.filterNotNull()

        if (currentSchedule != null && userSchedules != null) {
            val newSchedule = currentSchedule.copy(checklist = updatedChecklist)
            saveSchedule(
                newSchedule,
                userSchedules,
                userVM
            )
            _schedule.value = newSchedule
        }
    }

    fun addActivity(
        place: Place,
        start: LocalTime,
        end: LocalTime,
        userVM: UserViewModel
    ) {
        val current = _schedule.value ?: return
        val allSchedules = userVM.user.value?.schedules?.filterNotNull() ?: return

        if (end <= start) return

        val newActivity = Activity(
            startTime = start,
            endTime = end,
            place = place
        )

        val dayIdx = _selectedDayIndex.value
        val days = current.days.toMutableList()
        val today = days.getOrNull(dayIdx) ?: return

        val overlap = today.timeTable.any { exist ->
            val es = exist.startTime ?: return@any false
            val ee = exist.endTime ?: return@any false
            start.isBefore(ee) && end.isAfter(es)
        }
        if (overlap) return

        val updatedTable = (today.timeTable + newActivity)
            .sortedBy { it.startTime ?: LocalTime.MIN } // 정렬 추가

        days[dayIdx] = today.copy(timeTable = updatedTable)
        val updatedSchedule = current.copy(days = days)
        _schedule.value = updatedSchedule
        saveSchedule(updatedSchedule, allSchedules, userVM)
    }

    fun updateScheduleById(
        schedules: List<Schedule?>,
        updated: Schedule
    ): List<Schedule> {
        return schedules.mapNotNull {
            it?.let { if (it.id == updated.id) updated else it }
        }
    }
    fun initSchedule(){
        _schedule.value = null
    }
    fun deleteSchedule(userVM: UserViewModel) {
        val current = _schedule.value ?: return
        val currentId = current.id
        val existing = userVM.user.value?.schedules.orEmpty().filterNotNull()

        // 삭제된 리스트 생성
        val updated = existing.filterNot { it.id == currentId }

        // 업데이트 및 초기화
        userVM.updateSchedules(updated)
        userVM.refreshSchedules()
        _schedule.value = null
    }

    fun saveSchedule(
        updated: Schedule,
        allSchedules: List<Schedule>,
        userVM: UserViewModel
    ) {
        val replaced = updateScheduleById(allSchedules, updated)
        userVM.updateSchedules(replaced)
    }
    fun updateScheduleTitle(newTitle: String, userVM: UserViewModel) {
        val current = _schedule.value ?: return
        val updated = current.copy(title = newTitle)

        val schedules = userVM.user.value?.schedules.orEmpty()
        val newList = updateScheduleById(schedules, updated)

        // 1. 로컬 User 상태 업데이트
        userVM.updateSchedules(newList)

        // 2. 서버(Firebase)로부터 재동기화
        userVM.refreshSchedules()

        // 3. 현재 화면 상태 갱신
        _schedule.value = updated
    }
    fun removeActivityById(
        activityId: String,
        userVM: UserViewModel
    ) {
        val currentSchedule = _schedule.value ?: return
        val schedules = userVM.user.value?.schedules?.filterNotNull() ?: return
        val dayIndex = _selectedDayIndex.value

        val days = currentSchedule.days.toMutableList()
        val today = days.getOrNull(dayIndex) ?: return

        val updatedTimeTable = today.timeTable.filterNot { it.id == activityId }
        if (updatedTimeTable.size == today.timeTable.size) return // 삭제 대상 없음

        days[dayIndex] = today.copy(timeTable = updatedTimeTable)
        val updatedSchedule = currentSchedule.copy(days = days)

        _schedule.value = updatedSchedule
        saveSchedule(updatedSchedule, schedules, userVM)

        // 선택된 인덱스 초기화
        _selectedActivityIndex.value = null
    }




}

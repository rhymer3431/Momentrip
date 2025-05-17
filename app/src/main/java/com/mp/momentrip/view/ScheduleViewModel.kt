package com.mp.momentrip.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mp.momentrip.data.CheckItem
import com.mp.momentrip.data.Schedule
import java.time.LocalDate
import javax.inject.Inject

class ScheduleViewModel : ViewModel() {
    var checklistItems = mutableStateListOf<CheckItem>()
        private set

    fun loadSchedule(schedule: Schedule) {
        checklistItems.clear()
        checklistItems.addAll(schedule.checklistL)
    }

    fun toggleItem(index: Int) {
        if (index in checklistItems.indices) {
            val item = checklistItems[index]
            checklistItems[index] = item.copy(checked = !item.checked)
        }
    }

    fun addItem(name: String) {
        if (name.isNotBlank()) {
            checklistItems.add(CheckItem(name))
        }
    }

    fun removeItem(index: Int) {
        if (index in checklistItems.indices) {
            checklistItems.removeAt(index)
        }
    }

    fun getUpdatedChecklist(): List<CheckItem> = checklistItems.toList()
}

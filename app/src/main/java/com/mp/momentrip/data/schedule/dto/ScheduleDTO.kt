package com.mp.momentrip.data.schedule.dto

import com.mp.momentrip.data.schedule.CheckItem
import com.mp.momentrip.data.schedule.Schedule
import java.time.LocalDate

data class ScheduleDto(
    val id: String = "",
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val duration: Long = 0,
    val days: List<DayDto> = emptyList(),
    val region: String = "",
    val checklist: List<CheckItem> = emptyList()
){
    fun toModel(): Schedule = Schedule(
        id = id,
        title = title,
        startDate = startDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
        endDate = endDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
        duration = duration,
        region = region,
        checklist = checklist,
        days = days.map { it.toModel() }
    )


}

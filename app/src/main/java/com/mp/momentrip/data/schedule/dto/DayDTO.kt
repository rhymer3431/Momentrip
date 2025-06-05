package com.mp.momentrip.data.schedule.dto

import com.mp.momentrip.data.schedule.Day
import java.time.LocalDate


data class DayDto(
    val id: String = "",
    val index: Long = 0,
    val date: String = "",
    val timeTable: List<ActivityDto> = emptyList()
){
    fun toModel(): Day = Day(
        id = id,
        index = index,
        date = date.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
        timeTable = timeTable.map { it.toModel() }
    )



}



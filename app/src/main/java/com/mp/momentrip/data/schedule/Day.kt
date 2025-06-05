package com.mp.momentrip.data.schedule

import com.mp.momentrip.data.schedule.dto.DayDto
import java.time.LocalDate
import java.util.UUID

data class Day(
    val id: String = UUID.randomUUID().toString(),
    val index: Long = 0,
    val date: LocalDate? = null,
    val timeTable: List<Activity> = emptyList<Activity>()
){

    fun toDto(): DayDto = DayDto(
        id = id,
        index = index,
        date = date?.toString() ?: "",
        timeTable = timeTable.map { it.toDto() }
    )

}
package com.mp.momentrip.data.schedule

import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.dto.ActivityDto
import java.time.LocalTime
import java.util.UUID

data class Activity(
    val id: String = UUID.randomUUID().toString(),
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val place: Place = Place()
){
    fun toDto(): ActivityDto = ActivityDto(
        id = id,
        startTime = startTime?.toString() ?: "",
        endTime = endTime?.toString() ?: "",
        place = place
    )
}
package com.mp.momentrip.data.schedule.dto

import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.Activity
import java.time.LocalTime

data class ActivityDto(
    val id: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val place: Place = Place()
){
    fun toModel(): Activity = Activity(
        id = id,
        startTime = startTime.takeIf { it.isNotBlank() }?.let { LocalTime.parse(it) },
        endTime = endTime.takeIf { it.isNotBlank() }?.let { LocalTime.parse(it) },
        place = place
    )
}
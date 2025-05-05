package com.mp.momentrip.data

import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Schedule(
    val user: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val duration: Long = 0,
    val days: List<Day> = emptyList<Day>(),
    val region: String = "",

    ){
}

data class Day(
    val timeTable : List<Activity> = emptyList<Activity>()
){
    fun toLatLngList(): List<LatLng> {
        return timeTable.map { timeSlot ->
            LatLng.from(timeSlot.place.y, timeSlot.place.x)  // 위도(y), 경도(x)
        }
    }

}

data class Activity(
    val startTime: String = "",
    val endTime: String = "",
    val place: Place = Place()
)

fun getDuration(startDate: LocalDate, endDate: LocalDate): Long {
    return ChronoUnit.DAYS.between(startDate, endDate)
}


package com.mp.momentrip.data

import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Schedule(
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val duration: Long = 0,
    val days: List<Day> = emptyList<Day>(),
    val region: String = "",
    val checklist: List<CheckItem> = emptyList()
)

data class Day(
    val timeTable : List<Activity> = emptyList<Activity>()
)
data class Activity(
    val startTime: String = "",
    val endTime: String = "",
    val place: Place = Place()
)

data class CheckItem(
    val name: String = "",
    val checked: Boolean = false
)

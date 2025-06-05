package com.mp.momentrip.data.schedule

import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.dto.ScheduleDto
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Schedule(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val duration: Long = 0,
    val days: List<Day> = emptyList<Day>(),
    val region: String = "",
    val checklist: List<CheckItem> = emptyList()
){
    fun toDto(): ScheduleDto = ScheduleDto(
        id = id,
        title = title,
        startDate = startDate?.toString() ?: "",
        endDate = endDate?.toString() ?: "",
        duration = duration,
        region = region,
        checklist = checklist,
        days = days.map { it.toDto() }
    )
}

val dummy_schedule = Schedule(
    days = listOf(
        Day(
            date = LocalDate.of(2025, 5, 21),
            timeTable = listOf(
                Activity(
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(9, 0),
                    place = Place(
                        title = "우도",
                        addr1 = "제주 제주시 우도면",
                        contentTypeId = 12,
                        x = 126.9515,
                        y = 33.4961,
                        firstImage2 = "https://example.com/image/udo_thumb.jpg"
                    )
                ),
                Activity(
                    startTime = LocalTime.of(11, 0),
                    endTime = LocalTime.of(12, 0),
                    place = Place(
                        title = "성산일출봉",
                        addr1 = "제주 서귀포시 성산읍 성산리 1",
                        contentTypeId = 12,
                        x = 126.9410,
                        y = 33.4586,
                        firstImage2 = "https://example.com/image/seongsan_thumb.jpg"
                    )
                ),
                Activity(
                    startTime = LocalTime.of(13, 0),
                    endTime = LocalTime.of(14, 0),
                    place = Place(
                        title = "전망좋은횟집&흑돼지",
                        addr1 = "제주 서귀포시 성산읍",
                        contentTypeId = 39,
                        x = 126.9360,
                        y = 33.4595,
                        firstImage2 = "https://example.com/image/restaurant_thumb.jpg"
                    )
                )
            )
        )
    )
)
package com.mp.momentrip.data

import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class Schedule(
    val title: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val duration: Long = 0,
    val days: List<Day> = emptyList<Day>(),
    val region: String = "",
    val checklist: List<CheckItem> = emptyList()
)

data class Day(
    val index: Int = 0,
    val date: LocalDate? = null,
    val timeTable : List<Activity> = emptyList<Activity>()
)
data class Activity(
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val place: Place = Place()
)

data class ScheduleDto(
    val title: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val duration: Long = 0,
    val days: List<DayDto> = emptyList(),
    val region: String = "",
    val checklist: List<CheckItem> = emptyList()
)

data class DayDto(
    val index: Int = 0,
    val date: String = "",
    val timeTable: List<ActivityDto> = emptyList()
)

data class ActivityDto(
    val startTime: String = "",
    val endTime: String = "",
    val place: Place = Place()  // ⚠️ Place도 직렬화 가능한지 확인
)

fun Schedule.toDto(): ScheduleDto = ScheduleDto(
    title = title,
    startDate = startDate?.toString() ?: "",
    endDate = endDate?.toString() ?: "",
    duration = duration,
    region = region,
    checklist = checklist,
    days = days.map { it.toDto() }
)

fun ScheduleDto.toModel(): Schedule = Schedule(
    title = title,
    startDate = startDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
    endDate = endDate.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
    duration = duration,
    region = region,
    checklist = checklist,
    days = days.map { it.toModel() }
)

fun Day.toDto(): DayDto = DayDto(
    index = index,
    date = date?.toString() ?: "",
    timeTable = timeTable.map { it.toDto() }
)

fun DayDto.toModel(): Day = Day(
    index = index,
    date = date.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) },
    timeTable = timeTable.map { it.toModel() }
)

fun Activity.toDto(): ActivityDto = ActivityDto(
    startTime = startTime?.toString() ?: "",
    endTime = endTime?.toString() ?: "",
    place = place
)

fun ActivityDto.toModel(): Activity = Activity(
    startTime = startTime.takeIf { it.isNotBlank() }?.let { LocalTime.parse(it) },
    endTime = endTime.takeIf { it.isNotBlank() }?.let { LocalTime.parse(it) },
    place = place
)


data class CheckItem(
    val name: String = "",
    val checked: Boolean = false
)



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
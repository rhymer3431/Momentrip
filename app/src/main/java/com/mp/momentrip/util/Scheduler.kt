package com.mp.momentrip.util

import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.TimeSlot
import kotlinx.coroutines.*
import kotlin.math.*

object Scheduler {

    // 거리 계산 함수 (Haversine Formula)
    private suspend fun getSineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return withContext(Dispatchers.Default) {
            val R = 6371 // 지구 반지름 (km)
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            R * c
        }
    }

    // 일정 생성 함수
    suspend fun createTravelSchedule(
        days: Int,
        region: String,
    ): Schedule = withContext(Dispatchers.IO) {
        val restaurants = Recommender.getRestaurantList(region)
        val attractions = Recommender.getAttractionList(region)
        val mealTimes = listOf("08:00 아침", "12:00 점심", "18:00 저녁")
        val activityTimes = listOf("10:00", "14:00", "16:00", "20:00")
        val remainingRestaurants = restaurants.toMutableList()
        val remainingAttractions = attractions.toMutableList()
        val scheduleDays = mutableListOf<Day>()

        coroutineScope {
            repeat(days) {
                val timeSlots = mutableListOf<TimeSlot>()
                var lastLocation: Place? = null

                // 식당 방문 배정 (아침, 점심, 저녁)
                mealTimes.forEach { time ->
                    val referenceLocation = lastLocation ?: Place("", "", "", "",37.5, 127.0) // 기본 위치 설정
                    val restaurant = withContext(Dispatchers.Default) {
                        remainingRestaurants.minByOrNull { runBlocking { getSineDistance(referenceLocation.y.toDouble(), referenceLocation.x.toDouble(), it.y.toDouble(), it.x.toDouble()) } }
                    }
                    if (restaurant != null) {
                        timeSlots.add(TimeSlot(time, restaurant))
                        remainingRestaurants.remove(restaurant)
                        lastLocation = restaurant
                    }
                }

                // 놀거리 방문 배정 (식사 사이 시간)
                activityTimes.forEach { time ->
                    val referenceLocation = lastLocation ?: Place("", "", "", "",37.5, 127.0) // 기본 위치 설정
                    val attraction = withContext(Dispatchers.Default) {
                        remainingAttractions.minByOrNull { runBlocking { getSineDistance(referenceLocation.y.toDouble(), referenceLocation.x.toDouble(), it.y.toDouble(), it.x.toDouble()) } }
                    }
                    if (attraction != null) {
                        timeSlots.add(TimeSlot(time, attraction))
                        remainingAttractions.remove(attraction)
                        lastLocation = attraction
                    }
                }

                // 하루 일정 추가 (시간 순 정렬)
                scheduleDays.add(Day(timeSlots.sortedBy { it.time }))
            }
        }

        Schedule(duration = days, region = region, days = scheduleDays)
    }
}

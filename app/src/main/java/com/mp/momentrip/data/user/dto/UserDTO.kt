package com.mp.momentrip.data.user.dto

import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.dto.ScheduleDto
import com.mp.momentrip.data.user.FoodPreference
import com.mp.momentrip.data.user.User

data class UserDto(
    val id: String = "",
    val email: String = "none",
    val name: String = "none",
    val gender: Int = 0,
    val age: Int = 0,

    val userVector: List<Float>? = null,                    // 🔄 평탄화된 필드
    val foodPreference: FoodPreference = FoodPreference(),  // 🔄 UserPreference → foodPreference

    val liked: List<Place> = emptyList(),
    val schedules: List<ScheduleDto> = emptyList()
) {
    fun toModel(): User = User(
        id = id,
        email = email,
        name = name,
        gender = gender,
        age = age,

        userVector = userVector?.toMutableList(),            // 🔄 직접 전달
        foodPreference = foodPreference,                     // 🔄 직접 전달

        liked = liked,
        schedules = schedules.map { it.toModel() }
    )
}

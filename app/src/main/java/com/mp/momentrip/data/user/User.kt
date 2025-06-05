package com.mp.momentrip.data.user

import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.schedule.Schedule
import com.mp.momentrip.data.user.dto.UserDto

data class User(
    val id: String = "",
    val email: String = "none",
    val name: String = "none",
    val gender: Int = 0,
    val age: Int = 0,
    val userVector: MutableList<Float>? = null,
    val foodPreference: FoodPreference = FoodPreference(),
    val liked: List<Place?> = emptyList(),
    val schedules: List<Schedule?> = emptyList()

){
    fun toDto(): UserDto = UserDto(
        id = id,
        email = email,
        name = name,
        gender = gender,
        age = age,
        userVector = userVector,
        liked = liked.filterNotNull(),  // ✅ null 제거
        schedules = schedules.filterNotNull().map { it.toDto() }
    )
}


data class UserRegisterForm(
    val email: String,
    val password: String,
    val name: String,
    val gender: Int,
    val age: Int,
)


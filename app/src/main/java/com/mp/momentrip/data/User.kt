package com.mp.momentrip.data

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val email: String = "none",
    val name: String = "none",
    val gender: Int = 0,
    val age: Int = 0,
    val userPreference: UserPreference = UserPreference(),
    val liked: List<Place?> = emptyList(),
    val schedules: List<Schedule?> = emptyList()
)
data class UserDto(
    val id: String = "",
    val email: String = "none",
    val name: String = "none",
    val gender: Int = 0,
    val age: Int = 0,
    val userPreference: UserPreference = UserPreference(),  // ✅ 그대로 사용
    val liked: List<Place> = emptyList(),                    // ✅ 그대로 사용
    val schedules: List<ScheduleDto> = emptyList()           // ✅ 변환 대상
)
fun User.toDto(): UserDto = UserDto(
    id = id,
    email = email,
    name = name,
    gender = gender,
    age = age,
    userPreference = userPreference,
    liked = liked.filterNotNull(),  // ✅ null 제거
    schedules = schedules.filterNotNull().map { it.toDto() }
)
fun UserDto.toModel(): User = User(
    email = email,
    name = name,
    gender = gender,
    age = age,
    userPreference = userPreference,
    liked = liked,  // List<Place> → List<Place?>는 자동 변환
    schedules = schedules.map { it.toModel() }
)



data class UserRegisterForm(
    val email: String,
    val password: String,
    val name: String,
    val gender: Int,
    val age: Int,
)


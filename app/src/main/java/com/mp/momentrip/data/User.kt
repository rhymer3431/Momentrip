package com.mp.momentrip.data

import android.graphics.Picture

data class User(
    val email: String = "none",
    val name: String = "none",
    val gender: Int = 0,
    val age: Int = 0,
    val userPreference: UserPreference = UserPreference(),
    val liked: List<Place?> = emptyList(),
)
data class UserRegisterForm(
    val email: String,
    val password: String,
    val name: String,
    val gender: Int,
    val age: Int,
)


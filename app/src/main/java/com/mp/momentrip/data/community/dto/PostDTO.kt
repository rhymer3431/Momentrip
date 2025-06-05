package com.mp.momentrip.data.community.dto

import com.mp.momentrip.data.community.Post
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.user.dto.UserDto

data class PostDto(
    val id: String = "",
    val author: UserDto = UserDto(),
    val place: Place = Place(), // Place는 직렬화 가능한 구조라 따로 Dto로 안 빼도 무방
    val description: String = "",
    val tags: List<String> = emptyList(),
    val likeCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toModel(): Post = Post(
        id = id,
        author = author.toModel(),
        place = place,
        description = description,
        tags = tags,
        likeCount = likeCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

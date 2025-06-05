package com.mp.momentrip.data.community

import androidx.annotation.Keep
import com.mp.momentrip.data.community.dto.PostDto
import com.mp.momentrip.data.place.Place
import com.mp.momentrip.data.user.User
import java.util.UUID
import java.util.concurrent.TimeUnit

@Keep
data class Post(
    val id: String = UUID.randomUUID().toString(),                     // 게시글 ID

    val author: User = User(),               // 작성자 정보 (통합)

    val place: Place = Place(),              // 포함된 장소 정보 전체

    val description: String = "",            // 감성 중심 소개글
    val tags: List<String> = emptyList(),    // 해시태그 혹은 키워드

    val likeCount: Int = 0,                  // 좋아요 수
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
){
    fun toDto(): PostDto = PostDto(
        id = id,
        author = author.toDto(),
        place = place,
        description = description,
        tags = tags,
        likeCount = likeCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    fun formatTimeAgo(createdAtMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - createdAtMillis

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        if (seconds < 60) return "방금 전"

        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        if (minutes < 60) return "${minutes}분 전"

        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        if (hours < 24) return "${hours}시간 전"

        val days = TimeUnit.MILLISECONDS.toDays(diff)
        return "${days}일 전"
    }
}

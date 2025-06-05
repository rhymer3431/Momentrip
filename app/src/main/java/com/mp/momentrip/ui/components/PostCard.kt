package com.mp.momentrip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mp.momentrip.data.community.Post
import com.mp.momentrip.view.CommunityViewModel
import com.mp.momentrip.view.UserViewModel

@Composable
fun PostCard(
    userViewModel: UserViewModel,
    communityViewModel: CommunityViewModel,
    post: Post,
    modifier: Modifier = Modifier,
    onCardClick: (Post) -> Unit = {},
    onLikeClick: (Post) -> Unit = {},
    onDeletePost: (Post) -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val user by userViewModel.user.collectAsState()

    val isAuthor = user?.id == post.author.id

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFAFAFA))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onCardClick(post) }
        ) {
            AsyncImage(
                model = post.place.firstImage,
                contentDescription = post.place.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // 장소 제목 오버레이
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(
                        color = Color(0x99000000),
                        shape = RoundedCornerShape(topEnd = 12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = post.place.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 🗑 삭제 버튼 (작성자일 때만 표시)
            if (isAuthor) {
                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 작성자 정보 + 시간
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = post.author.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = post.formatTimeAgo(post.createdAt),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 설명
        if (post.description.isNotBlank()) {
            Text(
                text = post.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF444444),
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 태그
        if (post.tags.isNotEmpty()) {
            Row {
                post.tags.forEach { tag ->
                    Text(
                        text = "#$tag",
                        fontSize = 13.sp,
                        color = Color(0xFF777777),
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 좋아요
        Row(verticalAlignment = Alignment.CenterVertically) {
           user?.id?.let {
                PostLikeButton(
                    communityViewModel = communityViewModel,
                    postId = post.id,
                    userId = it,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (post.likeCount >= 1000)
                    "${post.likeCount / 1000}.${(post.likeCount % 1000) / 100}K likes"
                else
                    "${post.likeCount} likes",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF222222)
            )
        }
    }

    // ❗삭제 확인 다이얼로그
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("게시글 삭제") },
            text = { Text("이 게시글을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeletePost(post)
                        showDeleteConfirm = false
                    }
                ) { Text("삭제", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("취소")
                }
            }
        )
    }
}

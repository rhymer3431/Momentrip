package com.mp.momentrip.ui.components

import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mp.momentrip.view.UserViewModel


@Composable
fun LikeButton(
    userState: UserViewModel,
) {
    var isLiked by remember { mutableStateOf(userState.isLiked()) }
    val transition = updateTransition(targetState = isLiked, label = "likeTransition")

    IconToggleButton(
        checked = isLiked == true,
        onCheckedChange = {
            isLiked = it
            userState.like() // 상태 변경 시 사용자 좋아요 상태도 업데이트
        }
    ) {
        AnimatedVisibility(
            visible = isLiked == true,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Liked",

            )
        }

        isLiked?.let {
            AnimatedVisibility(
                visible = !it,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Not Liked",

                    )
            }
        }
    }
}

package com.mp.momentrip.ui.components


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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mp.momentrip.R
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.theme.Like
import com.mp.momentrip.view.UserViewModel

@Composable
fun LikeButton(
    userState: UserViewModel,
    place: Place?,
    modifier: Modifier = Modifier
) {
    val user by userState.user.collectAsState()
    val isLiked = user?.liked?.any { it?.contentId == place?.contentId } == true

    val iconColor by animateColorAsState(
        targetValue = if (isLiked) Color(0xFFEF1010) else Color(0xFFB09B9B),
        animationSpec = tween(durationMillis = 300)
    )

    IconToggleButton(
        checked = isLiked,
        onCheckedChange = { userState.like(place) },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.liked
            ),
            contentDescription = if (isLiked) "Liked" else "Not liked",
            tint = iconColor,
            modifier = Modifier.size(36.dp)
        )
    }
}


@Preview
@Composable
fun LikeButtonPreview(){
    LikeButton(UserViewModel(),Place())
}
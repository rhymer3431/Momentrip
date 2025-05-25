package com.mp.momentrip.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.CardElevation
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mp.momentrip.R
import com.mp.momentrip.data.Category
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Region
import com.mp.momentrip.data.User
import com.mp.momentrip.view.UserViewModel
@Composable
fun PlaceCard(
    place: Place,
    modifier: Modifier = Modifier,  // ✅ 외부에서 전달 가능하게
    onClick: () -> Unit
) {
    Box(
        modifier = modifier // ✅ 외부 modifier 사용
            .size(width = 161.dp, height = 214.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(width = 137.dp, height = 124.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                ImageCard(place.firstImage2)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier
                    .width(137.dp)
                    .height(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = place.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF1B1E28),
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF7D848D),
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "#" + Category.fromCode(place.cat1!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                    Text(
                        text = "#" + Category.fromCode(place.cat3!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                }
            }
        }
    }
}
@Composable
fun FeedPlaceCard(
    place: Place,
    userState: UserViewModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(width = 161.dp, height = 214.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(width = 137.dp, height = 124.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                ImageCard(place.firstImage2)

                LikeButton(
                    userState = userState,
                    place = place,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp)
                )

            }

            Spacer(modifier = Modifier.height(5.dp))

            Column(
                modifier = Modifier
                    .width(137.dp)
                    .height(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = place.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF1B1E28),
                    fontWeight = FontWeight.Medium
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF7D848D),
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "#" + Category.fromCode(place.cat1!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                    Text(
                        text = "#" + Category.fromCode(place.cat3!!)?.categoryName,
                        fontSize = 12.sp,
                        letterSpacing = 0.3.sp,
                        color = Color(0xFF4F8EDA)
                    )
                }
            }
        }
    }
}





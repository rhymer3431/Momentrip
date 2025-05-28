package com.mp.momentrip.ui.components

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Person

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import com.mp.momentrip.data.dummy_place
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
                ImageCard(place.firstImage)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .width(137.dp)
                    .height(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                    fontSize = 12.sp,
                    letterSpacing = 0.3.sp,
                    color = Color(0xFF7D848D),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = place.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF1B1E28),
                    fontWeight = FontWeight.Medium
                )

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
    userState: UserViewModel,
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
                ImageCard(place.firstImage)

                LikeButton(
                    userState = userState,
                    place = place,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(3.dp) // 여백 줘서 딱 붙지 않게
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .width(137.dp)
                    .height(68.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                    fontSize = 12.sp,
                    letterSpacing = 0.3.sp,
                    color = Color(0xFF7D848D),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )


                Text(
                    text = place.title,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF1B1E28),
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "#" + Category.fromCode(place.cat2!!)?.categoryName,
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
fun LargePlaceCard(
    userState: UserViewModel,
    place: Place,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x1FB4BCC9),
                spotColor = Color(0x1FB4BCC9)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        // 배경 이미지
        ImageCard(
            imageUrl = place.firstImage,
            modifier = Modifier.fillMaxSize()
        )

        // 오버레이 및 콘텐츠
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.4f to Color(0x91000000),
                        1f to Color(0xD6000000)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 상단 좋아요 버튼
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    LikeButton(
                        userState = userState,
                        place = place,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(36.dp)
                    )
                }

                // 하단 정보 영역
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "지역",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Region.fromCode(place.areaCode!!.toInt())?.locationName ?: "지역 미상",
                            fontSize = 14.sp,
                            color = Color.White,
                            letterSpacing = 0.3.sp
                        )
                    }

                    Text(
                        text = place.title,
                        fontSize = 26.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        place.cat2?.let {
                            Text(
                                text = "#" + Category.fromCode(it)?.categoryName,
                                fontSize = 14.sp,
                                color = Color(0xFFB8DAFF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        place.cat3?.let {
                            Text(
                                text = "#" + Category.fromCode(it)?.categoryName,
                                fontSize = 14.sp,
                                color = Color(0xFFB8DAFF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LargePlaceCardPreview(){
    LargePlaceCard(
        userState = UserViewModel(),
        place = dummy_place,
        onClick = {}
    )
}
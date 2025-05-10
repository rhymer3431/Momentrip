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

@Composable
fun PlaceCard(
    place: Place,
    modifier: Modifier = Modifier,

    ) {

    // Card UI
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {

            // 장소 정보 텍스트
            Text(
                text = place.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "📍 ${place.cat1}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "🏠 ${place.addr1}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "📞 ${place.tel}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PlacePreviewCard(
    place: Place,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(161.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(124.dp)
                .fillMaxWidth()
        ) {
            ImageCard(place.firstImage2)

            // 좋아요 버튼 (옵션)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color(0x331B1E28), shape = CircleShape)
                    .blur(15.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = Color.White,
                    modifier = Modifier
                        .size(14.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            // 지역명
            Text(
                text = Region.fromCode(place.areaCode!!.toInt())!!.locationName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF7D848D)
            )

            // 장소명
            Text(
                text = place.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1B1E28),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 주소
            Text(
                text = place.addr1 ?: "",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 해시태그 (예: #한식 #닭갈비 등)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOfNotNull(place.cat1, place.cat2).take(2).forEach { tag ->
                    Text(
                        text = "#$tag",
                        fontSize = 12.sp,
                        color = Color(0xFF4F8EDA),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun FoodCard(
    place : Place,
    onClick : () -> Unit
) {
    Box(
        modifier = Modifier
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
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0x331B1E28), shape = CircleShape)
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                ) {
                    // TODO: Replace with actual vector heart icon
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .border(1.1.dp, Color.White, shape = CircleShape)
                            .align(Alignment.Center)
                    )
                }
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
                        text = "#" + Category.fromCode(place.cat3!!)?.categoryName ,
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
fun LocationIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(15.dp)) {
        val strokeWidth = 1.1.dp.toPx()
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Outer path
        drawPath(
            path = Path().apply {
                addOval(Rect(center = Offset(centerX, centerY), radius = size.width / 2.3f))
                // TODO: Approximate the SVG shape more closely if needed
            },
            color = Color.Transparent,
            style = Stroke(width = strokeWidth)
        )

        // Inner circle
        drawCircle(
            color = Color.Transparent,
            radius = size.minDimension / 4,
            center = Offset(centerX, centerY),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Preview
@Composable
fun Preview(){
    FoodCard(Place(
        title = "test",
        areaCode = "1",
        cat1 = "A05",
        cat2 = "A05",
        cat3 = "A05",
        firstImage = "https://tourvis.com/activity/product/PRD3005753471",
        firstImage2 = "https://tourvis.com/activity/product/PRD3005753471",

    ), {})
}
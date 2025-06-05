package com.mp.momentrip.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mp.momentrip.data.place.dummy_place
import com.mp.momentrip.data.schedule.Activity
import com.mp.momentrip.data.tourAPI.ContentType
import java.time.LocalTime

@Composable
fun ActivityCard(
    activity: Activity,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    val place = activity.place
    val imageUrl = place.firstImage ?: ""

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 시간 (좌측)
        Text(
            text = activity.startTime.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black,
            modifier = Modifier.width(48.dp)
        )

        // 이미지 및 정보 묶음
        Row(
            modifier = Modifier
                .weight(1f)
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이미지
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 95.dp, height = 70.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 텍스트 묶음
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Row {
                    Text(
                        text = place.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1E28)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = ContentType.fromId(place.contentTypeId)?.label ?: "기타",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF7D848D)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = listOfNotNull(place.addr1, place.addr2).joinToString(" "),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF7D848D),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // 삭제 아이콘 (우측)
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "삭제",
                tint = Color(0xFF79747E)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ActivityCardPreview(){
    ActivityCard(
        activity = dummy_activity,
        onDeleteClick = {}
    )
}

val dummy_activity = Activity(
    startTime = LocalTime.of(12,0),
    endTime = LocalTime.of(13,0),
    place = dummy_place

)
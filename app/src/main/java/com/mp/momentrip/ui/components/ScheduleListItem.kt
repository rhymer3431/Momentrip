package com.mp.momentrip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.iconics.compose.IconicsPainter
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mp.momentrip.data.schedule.Schedule
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun ScheduleListItem(
    schedule: Schedule,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {

                // 📅 날짜 표시 (예: 6월 1일 ~ 6월 3일 (3일))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = IconicsPainter(GoogleMaterial.Icon.gmd_event_note),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF6C757D)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "${schedule.startDate?.format(DateTimeFormatter.ofPattern("M월 d일"))} ~ " +
                                "${schedule.endDate?.format(DateTimeFormatter.ofPattern("M월 d일"))} " +
                                "(${schedule.duration}일)",
                        fontSize = 12.sp,
                        color = Color(0xFF6C757D)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // 🏷 제목
                Text(
                    text = schedule.title.ifBlank { "제목 없는 여행" },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B1E28)
                )

                Spacer(Modifier.height(6.dp))

                // 📍 지역
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = IconicsPainter(GoogleMaterial.Icon.gmd_place),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFADB5BD)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = schedule.region,
                        fontSize = 13.sp,
                        color = Color(0xFF6C757D)
                    )
                }
            }

            Icon(
                painter = IconicsPainter(GoogleMaterial.Icon.gmd_chevron_right),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFCED4DA)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleListItemPreview() {
    val sampleSchedule = Schedule(
        title = "서울 감성 여행",
        region = "서울",
        startDate = LocalDate.of(2025, 6, 10),
        endDate = LocalDate.of(2025, 6, 12),
        duration = 3,
        days = emptyList() // 미리보기에서는 생략 가능
    )

    ScheduleListItem(schedule = sampleSchedule, onClick = {})
}

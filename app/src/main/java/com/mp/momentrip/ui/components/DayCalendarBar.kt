package com.mp.momentrip.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



data class DayItem(
    val index: String,   // 날짜 (예: "21", "22", ...)
    val isSelected: Boolean = false
)



@Composable
fun DayCalendarBar(
    modifier: Modifier = Modifier,
    days: List<DayItem>,
    onDaySelected: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(days) { index, day ->
            DayColumn(
                day = day,
                onClick = { onDaySelected(index) }
            )
        }
    }
}

@Composable
private fun DayColumn(
    day: DayItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(44.dp)
            .height(57.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
            .background(if (day.isSelected) Color(0xFF24BAEC) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Day",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (day.isSelected) Color(0xFFEBEAEA) else Color(0xFFBCC1CD)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = day.index,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (day.isSelected) Color(0xFFEBEAEA) else Color(0xFF212525)
        )
    }
}

@Preview
@Composable
fun CalendarBarPreview() {
    val days = listOf(

        DayItem("21"),
        DayItem("22"),
        DayItem("23"),
        DayItem("24", isSelected = true),
        DayItem("25"),
        DayItem("26"),
        DayItem("27"),
        DayItem("27"),
        DayItem("27"),
        DayItem("27"),
        DayItem("27"),

        )

    DayCalendarBar(days = days, onDaySelected = {

    })
}


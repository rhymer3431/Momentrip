package com.mp.momentrip.ui.screen.schedule

import android.util.Range
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.util.toRange
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen() {
    var start by remember { mutableStateOf<LocalDate?>(null) }
    var end by remember { mutableStateOf<LocalDate?>(null) }

    var showPicker by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("선택된 일정:")
        Text("출발일: ${start ?: "--"}")
        Text("도착일: ${end ?: "--"}")

        Spacer(Modifier.height(16.dp))

        Button(onClick = { showPicker = true }) {
            Text("일정 선택")
        }

        if (showPicker) {
            // 오버레이로 표시하거나, 전체 전환해도 됨
            DateRangePickerScreen(
                onCancel = { showPicker = false },
                onConfirm = { s, e ->
                    start = s
                    end = e
                    showPicker = false
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerScreen(
    onCancel: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit,
) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    val calendarState = rememberCalendarState(
        startMonth = YearMonth.now().minusMonths(3),
        endMonth = YearMonth.now().plusMonths(3),
        firstVisibleMonth = YearMonth.of(2025, 8),
        firstDayOfWeek = DayOfWeek.SUNDAY
    )

    val headerFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.KOREAN)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("일정 선택") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { startDate = null; endDate = null }) {
                    Text("취소")
                }
                TextButton(
                    enabled = startDate != null && endDate != null,
                    onClick = { onConfirm(startDate!!, endDate!!) }
                ) {
                    Text("선택")
                }
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            Text("출발 - 도착 날짜", style = MaterialTheme.typography.labelMedium)
            Text(
                text = "${startDate?.format(headerFormatter) ?: "--"} – ${endDate?.format(headerFormatter) ?: "--"}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider()

            VerticalCalendar(
                state = calendarState,
                monthHeader = {
                    Text(
                        text = "${it.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.KOREAN)} ${it.yearMonth.year}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                },
                dayContent = { day ->
                    val date = day.date
                    val isStart = date == startDate
                    val isEnd = date == endDate
                    val inRange = startDate != null && endDate != null && date > startDate && date < endDate

                    val baseCircleSize = 36.dp
                    val animatedCircleSize by animateDpAsState(
                        targetValue = if (isStart || isEnd) 44.dp else baseCircleSize,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val animatedCircleColor by animateColorAsState(
                        targetValue = if (isStart || isEnd) Color(0xFF4FC3F7) else Color.Transparent,
                        animationSpec = tween(durationMillis = 300)
                    )

                    // ✅ 범위 배경용 애니메이션
                    val animatedRangeAlpha by animateFloatAsState(
                        targetValue = if (inRange || (isStart && startDate != endDate) || (isEnd && startDate != endDate)) 1f else 0.6f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val animatedRangeColor by animateColorAsState(
                        targetValue = if (inRange || isStart || isEnd) Color(0xFFE1F5FE) else Color.Transparent,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val animatedRangeScale by animateFloatAsState(
                        targetValue = if (inRange) 1f else 0.95f, // 살짝 작게 시작 후 확장
                        animationSpec = tween(durationMillis = 300)
                    )

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                when {
                                    startDate == null -> startDate = date
                                    endDate == null && date >= startDate -> endDate = date
                                    else -> {
                                        startDate = date
                                        endDate = null
                                    }
                                }
                            }
                    ) {
                        // ✅ 중앙 범위 배경
                        if (inRange) {
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .graphicsLayer {
                                        scaleX = animatedRangeScale
                                        scaleY = animatedRangeScale
                                        alpha = animatedRangeAlpha
                                    }
                                    .background(animatedRangeColor)
                            )
                        }

                        // ✅ 시작일 오른쪽 반쪽 배경
                        if (isStart && startDate != endDate) {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterEnd)
                                    .graphicsLayer {
                                        scaleX = animatedRangeScale
                                        scaleY = animatedRangeScale
                                        alpha = animatedRangeAlpha
                                    }
                                    .background(animatedRangeColor)
                            )
                        }

                        // ✅ 종료일 왼쪽 반쪽 배경
                        if (isEnd && startDate != endDate) {
                            Box(
                                Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterStart)
                                    .graphicsLayer {
                                        scaleX = animatedRangeScale
                                        scaleY = animatedRangeScale
                                        alpha = animatedRangeAlpha
                                    }
                                    .background(animatedRangeColor)
                            )
                        }

                        // ✅ 날짜 텍스트 (선택된 경우 확대됨)
                        Box(
                            modifier = Modifier
                                .size(animatedCircleSize)
                                .align(Alignment.Center)
                                .clip(CircleShape)
                                .background(animatedCircleColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = if (isStart || isEnd) Color.White else Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DateRangePickerScreenPreview() {

    val timeBoundary = LocalDate.now().let { now -> now.minusYears(2)..now }
    val selectedRange = remember {
        val default = LocalDate.now().minusYears(2).let { time -> time.plusDays(5)..time.plusDays(8) }
        mutableStateOf(default.toRange())
    }
    CalendarDialog(
        state = rememberUseCaseState(visible = true, true, onCloseRequest = {}),
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            boundary = timeBoundary,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Period(
            selectedRange = selectedRange.value
        ) { startDate, endDate ->
            selectedRange.value = Range(startDate, endDate)
        },
    )
}

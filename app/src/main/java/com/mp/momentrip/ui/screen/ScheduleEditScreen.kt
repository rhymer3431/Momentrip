package com.mp.momentrip.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDatePickerScreen() {
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)")
    val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "여행 기간 선택",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 시작일 선택
            DateSelectionButton(
                label = "여행 시작일",
                selectedDate = startDate,
                dateFormatter = dateFormatter,
                icon = Icons.Default.ArrowDropDown,
                onClick = { showStartDatePicker = true }
            )

            // 종료일 선택
            DateSelectionButton(
                label = "여행 종료일",
                selectedDate = endDate,
                dateFormatter = dateFormatter,
                icon = Icons.Default.ArrowDropDown,
                onClick = { showEndDatePicker = true }
            )

            // 선택된 기간 표시
            SelectedPeriodInfo(
                startDate = startDate,
                endDate = endDate,
                totalDays = totalDays
            )

            // 시작일 DatePicker
            if (showStartDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showStartDatePicker = false
                                // 종료일이 시작일보다 앞서있다면 종료일도 조정
                                if (endDate.isBefore(startDate)) {
                                    endDate = startDate.plusDays(1)
                                }
                            }
                        ) {
                            Text("확인")
                        }
                    }
                ) {
                    DatePicker(
                        state = rememberDatePickerState(
                            initialSelectedDateMillis = startDate.toEpochMillis(),
                            yearRange = LocalDate.now().year..(LocalDate.now().year + 1)
                        )
                    )
                }
            }

            // 종료일 DatePicker
            if (showEndDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = { showEndDatePicker = false }
                        ) {
                            Text("확인")
                        }
                    }
                ) {
                    DatePicker(
                        state = rememberDatePickerState(
                            initialSelectedDateMillis = endDate.toEpochMillis(),
                            yearRange = startDate.year..(startDate.year + 1),
                            selectableDates = object : SelectableDates {
                                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                    return utcTimeMillis.toLocalDate().isAfter(startDate.minusDays(1))
                                }
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun DateSelectionButton(
    label: String,
    selectedDate: LocalDate,
    dateFormatter: DateTimeFormatter,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = label, style = MaterialTheme.typography.labelMedium)
                Text(
                    text = selectedDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}

@Composable
fun SelectedPeriodInfo(startDate: LocalDate, endDate: LocalDate, totalDays: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "선택한 여행 기간",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "${startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)} ~ ${endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "총 ${totalDays}박 ${totalDays + 1}일",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

// LocalDate를 epoch millis로 변환
fun LocalDate.toEpochMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

// Long을 LocalDate로 변환
fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Preview(showBackground = true)
@Composable
fun TripDatePickerPreview() {
    TripDatePickerScreen()
}
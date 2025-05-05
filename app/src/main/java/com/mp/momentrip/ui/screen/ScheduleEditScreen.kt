package com.mp.momentrip.ui.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mp.momentrip.data.Activity
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.User
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import org.burnoutcrew.reorderable.*

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

@Composable
fun ScheduleCreationScreen(
    userViewModel: UserViewModel,
    onScheduleCreated: () -> Unit
) {
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()

    var region by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isCreating by remember { mutableStateOf(false) }

    val areaCodes = listOf(
        "서울특별시", "인천광역시", "대전광역시", "대구광역시", "광주광역시",
        "부산광역시", "울산광역시", "세종특별자치시", "경기도", "강원도",
        "충청북도", "충청남도", "경상북도", "경상남도", "전라북도",
        "전라남도", "제주특별자치도"
    )

    var expanded by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val startDatePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val endDatePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "새 스케줄 만들기",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 지역 선택
        Box {
            OutlinedTextField(
                value = region,
                onValueChange = {},
                label = { Text("지역 선택") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                readOnly = true,
                enabled = false  // <- 추가!
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                areaCodes.forEach { area ->
                    DropdownMenuItem(
                        text = { Text(area) },
                        onClick = {
                            region = area
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 시작일 선택
        OutlinedTextField(
            value = startDate,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("시작일 선택") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { startDatePicker.show() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 종료일 선택
        OutlinedTextField(
            value = endDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("종료일 선택") },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { endDatePicker.show() }

        )

        Spacer(modifier = Modifier.height(24.dp))

        error?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (region.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                    isCreating = true
                    userViewModel.createSchedule(
                        region = region,
                        startDate = startDate,
                        endDate = endDate,
                        onSuccess = {
                            isCreating = false
                            onScheduleCreated()
                        },
                        onError = { msg ->
                            isCreating = false
                            error = msg
                        }
                    )
                } else {
                    error = "모든 정보를 입력해주세요."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("스케줄 생성")
        }
    }
}

// 날짜 차이 계산
fun calculateDuration(startDate: String, endDate: String): Long {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)
        ChronoUnit.DAYS.between(start, end) + 1
    } catch (e: Exception) {
        0
    }
}

@Composable
fun ScheduleEditScreen(
    userViewModel: UserViewModel,
    onScheduleUpdated: () -> Unit
) {
   
}


@Composable
fun ActivityCard(
    activity: Activity,
    isDragging: Boolean,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(if (isDragging) 8.dp else 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = activity.place.title.ifEmpty { "장소 이름 없음" },
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${activity.startTime} ~ ${activity.endTime}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "삭제")
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun TripDatePickerPreview() {
    ScheduleCreationScreen(UserViewModel(),{})
}

@Preview(showBackground = true)
@Composable
fun ScheduleEditScreenPreview() {
    ScheduleEditScreen(UserViewModel(),{})
}
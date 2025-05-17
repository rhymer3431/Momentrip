package com.mp.momentrip.ui.screen.schedule

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mp.momentrip.view.UserViewModel
import java.util.Calendar

@Composable
fun ScheduleCreationScreen(
    userViewModel: UserViewModel,
    onScheduleCreated: () -> Unit
) {
    val context = LocalContext.current

    var region by remember { mutableStateOf("") }


    var showPicker by remember { mutableStateOf(false) }
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





@Preview(showBackground = true)
@Composable
fun TripDatePickerPreview() {
    ScheduleCreationScreen(UserViewModel(),{})
}


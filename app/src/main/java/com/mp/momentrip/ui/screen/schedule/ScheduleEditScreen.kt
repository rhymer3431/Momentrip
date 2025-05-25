package com.mp.momentrip.ui.screen.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mp.momentrip.view.UserViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleCreationScreen(
    userViewModel: UserViewModel,
    onScheduleCreated: () -> Unit
) {

    /* ---------- 상태 ---------- */
    val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE
    var region     by remember { mutableStateOf("") }
    var startDate  by remember { mutableStateOf<LocalDate?>(null) }
    var endDate    by remember { mutableStateOf<LocalDate?>(null) }
    var showPicker by remember { mutableStateOf(false) }
    var expanded   by remember { mutableStateOf(false) }
    var error      by remember { mutableStateOf<String?>(null) }
    var isCreating by remember { mutableStateOf(false) }

    /* ---------- UI ---------- */
    val areaNames = listOf(
        "서울",     // 서울특별시
        "인천",     // 인천광역시
        "대전",     // 대전광역시
        "대구",     // 대구광역시
        "광주",     // 광주광역시
        "부산",     // 부산광역시
        "울산",     // 울산광역시
        "세종",     // 세종특별자치시
        "경기",     // 경기도
        "강원",     // 강원도
        "충북",     // 충청북도
        "충남",     // 충청남도
        "전북",     // 전라북도
        "전남",     // 전라남도
        "경북",     // 경상북도
        "경남",     // 경상남도
        "제주"      // 제주특별자치도
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "새 스케줄 만들기",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        /* ───── 지역 선택 ───── */
        Box {
            OutlinedTextField(
                value = region,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("지역 선택") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                areaNames.forEach { area ->
                    DropdownMenuItem(
                        text = { Text(area) },
                        onClick = { region = area; expanded = false }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ───── 날짜 범위 선택 ───── */
        OutlinedTextField(
            value = if (startDate != null && endDate != null)
                "${startDate!!.format(dateFmt)} → ${endDate!!.format(dateFmt)}"
            else "",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text("여행 기간 선택") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPicker = true }
        )

        Spacer(Modifier.height(24.dp))

        /* ───── 오류 메시지 ───── */
        error?.let {
            Text(it, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
        }

        /* ───── 생성 버튼 ───── */
        Button(
            onClick = {
                if (region.isNotEmpty() && startDate != null && endDate != null) {
                    isCreating = true
                    userViewModel.createSchedule(
                        region    = region,
                        startDate = startDate!!,
                        endDate   = endDate!!,
                        onSuccess = { isCreating = false; onScheduleCreated() },
                        onError   = { msg -> isCreating = false; error = msg }
                    )
                } else {
                    error = "지역과 날짜를 모두 선택하세요."
                }
            },
            enabled = !isCreating,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("스케줄 생성")
        }
    }

    /* ---------- DateRangePicker를 Dialog로 ---------- */
    if (showPicker) {
        Dialog(
            onDismissRequest = { showPicker = false },
            properties = DialogProperties(usePlatformDefaultWidth = false) // 전체 화면
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxSize()     // 풀-스크린
            ) {
                DateRangePickerScreen(
                    onCancel = { showPicker = false },
                    onConfirm = { s, e ->
                        startDate = s
                        endDate   = e
                        showPicker = false
                    }
                )
            }
        }
    }
}

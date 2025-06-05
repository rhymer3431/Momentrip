package com.mp.momentrip.ui.screen.schedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mp.momentrip.R
import com.mp.momentrip.data.place.Region
import com.mp.momentrip.ui.ScheduleDestinations
import com.mp.momentrip.util.formatDateRange
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel

@Composable
fun ScheduleDetailScreen(
    navController: NavController,
    scheduleViewModel: ScheduleViewModel,
    userState: UserViewModel
) {
    val schedule by scheduleViewModel.schedule.collectAsState()

    var isEditingTitle by remember { mutableStateOf(false) }
    var editableTitle by remember(schedule?.title) { mutableStateOf(schedule?.title ?: "") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val primary = MaterialTheme.colorScheme.primary
    val lightGray = Color(0xFFB0B0B0)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 상단 지역 이미지 + 그라데이션 처리
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = Region.getRegionImage(schedule?.region ?: "") ?: R.drawable.q4a1
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White,
                                    ),
                                    startY = size.height * 0.4f,
                                    endY = size.height
                                )
                            )
                        }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "여행 정보",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // 본문 콘텐츠 영역
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 제목
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isEditingTitle) {
                        TextField(
                            value = editableTitle,
                            onValueChange = { editableTitle = it },
                            modifier = Modifier.weight(1f),
                            textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = primary,
                                unfocusedIndicatorColor = lightGray,
                                cursorColor = primary
                            )
                        )
                        IconButton(onClick = {
                            scheduleViewModel.updateScheduleTitle(editableTitle, userState)
                            isEditingTitle = false
                        }) {
                            Icon(Icons.Default.Check, null, tint = primary)
                        }
                        IconButton(onClick = {
                            isEditingTitle = false
                            editableTitle = schedule?.title ?: ""
                        }) {
                            Icon(Icons.Default.Close, null, tint = lightGray)
                        }
                    } else {
                        Text(
                            text = editableTitle.ifBlank { "제목 없음" },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { isEditingTitle = true }) {
                            Icon(Icons.Default.Edit, null, tint = lightGray)
                        }
                    }
                }

                // 지역 및 기간 정보
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDFDFD), RoundedCornerShape(18.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    InfoRow("지역", schedule?.region ?: "정보 없음")
                    InfoRow("기간", formatDateRange(schedule?.startDate, schedule?.endDate))
                }

                // 일정 보기 / 체크리스트 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navController.navigate(ScheduleDestinations.SCHEDULE_OVERVIEW_ROUTE) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.White)
                    ) {
                        Text("일정 보기", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                    Button(
                        onClick = { navController.navigate(ScheduleDestinations.CHECK_LIST_ROUTE) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC), contentColor = Color.Black)
                    ) {
                        Text("체크리스트", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // 삭제 버튼 (우하단)
        IconButton(
            onClick = { showDeleteConfirm = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(56.dp)
                .background(Color(0xFF9A9A9A), shape = RoundedCornerShape(36.dp))
        ) {
            Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.White, modifier = Modifier.size(24.dp))
        }

        // 삭제 확인 다이얼로그
        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("일정 삭제", fontWeight = FontWeight.Bold) },
                text = { Text("정말로 이 일정을 삭제하시겠습니까?\n삭제된 일정은 복구할 수 없습니다.") },
                confirmButton = {
                    TextButton(onClick = {
                        scheduleViewModel.deleteSchedule(userState)
                        showDeleteConfirm = false
                        navController.popBackStack()
                    }) {
                        Text("삭제", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleDetailScreenPreview() {
    ScheduleDetailScreen(
        navController = NavController(LocalContext.current),
        scheduleViewModel = ScheduleViewModel(),
        userState = UserViewModel()
    )
}

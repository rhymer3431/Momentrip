package com.mp.momentrip.ui.screen.schedule
import com.mp.momentrip.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.mp.momentrip.data.Place
import com.mp.momentrip.data.UserPreference
import com.mp.momentrip.data.dummy_place
import com.mp.momentrip.ui.components.ImageCard
import com.mp.momentrip.view.RecommendInitData
import com.mp.momentrip.view.RecommendViewModel
import com.mp.momentrip.view.ScheduleViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalTime


@Composable
fun ActivitySelectScreen(
    scheduleViewModel: ScheduleViewModel,
    recommendViewModel: RecommendViewModel,
    onPlaceTimeSelected: (Place, LocalTime, LocalTime) -> Unit,
    onCancel: () -> Unit,
) {
    val categories = listOf("관광명소", "음식점", "숙소")
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }   // ← 수정
    val isLoading by recommendViewModel.isLoading.collectAsState()
    val tourList  by recommendViewModel.recommendTourSpot.collectAsState()
    val foodList  by recommendViewModel.recommendRestaurant.collectAsState()
    val dormList  by recommendViewModel.recommendDormitory.collectAsState()

    // ---------- ① 장소가 아직 선택되지 않은 경우 ----------
    if (selectedPlace == null) {

        val places = when (selectedCategory) {
            "관광명소" -> tourList
            "음식점"  -> foodList
            "숙소"   -> dormList
            else      -> emptyList()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            /* 카테고리 버튼 */
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                categories.forEach { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (selectedCategory == category) Color(0xFF4F8EDA)
                                else Color.LightGray
                        )
                    ) { Text(category) }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                /* 장소 리스트 */
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(places.orEmpty()) { place ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPlace = place },   // ← 장소 선택
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(place.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold)
                                Text(place.addr1 ?: "",
                                    fontSize = 13.sp,
                                    color = Color.Gray)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) { Text("취소") }
        }

        // ---------- ② 장소를 선택한 뒤: 시간 범위 선택 ----------
    } else {

        var startTime by remember { mutableStateOf(LocalTime.of(8, 0)) }
        var endTime   by remember { mutableStateOf(LocalTime.of(8, 30)) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* 장소 이미지 */
            ImageCard(
                imageUrl = selectedPlace!!.firstImage,
                modifier = Modifier.height(300.dp)
                )
            Spacer(Modifier.height(12.dp))

            Text(selectedPlace!!.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black)

            Spacer(Modifier.height(32.dp))

            /* 시작-시간 */
            Row(verticalAlignment = Alignment.CenterVertically) {
                WheelTimePicker(
                    startTime = startTime,
                    size = DpSize(120.dp, 50.dp),
                    rowCount = 1,
                    timeFormat = TimeFormat.HOUR_24,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        color   = Color.White,
                        border  = BorderStroke(1.dp, Color(0xFFBDBDBD))
                    ),
                    onSnappedTime = { startTime = it }
                )
                Spacer(Modifier.width(12.dp))
                Text("부터", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(24.dp))

            /* 종료-시간 */
            Row(verticalAlignment = Alignment.CenterVertically) {
                WheelTimePicker(
                    startTime = endTime,
                    size = DpSize(120.dp, 50.dp),
                    rowCount = 1,
                    timeFormat = TimeFormat.HOUR_24,
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                    selectorProperties = WheelPickerDefaults.selectorProperties(
                        enabled = true,
                        color   = Color.White,
                        border  = BorderStroke(1.dp, Color(0xFFBDBDBD))
                    ),
                    onSnappedTime = { endTime = it }
                )
                Spacer(Modifier.width(12.dp))
                Text("까지", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(36.dp))

            /* 완료 */
            Button(
                onClick = {
                    onPlaceTimeSelected(selectedPlace!!, startTime, endTime)
                    // 다음 선택을 위해 상태 초기화
                    selectedPlace = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                shape = RoundedCornerShape(12.dp)
            ) { Text("완료", color = Color.White, fontSize = 16.sp) }

            Spacer(Modifier.height(12.dp))

            /* 뒤로가기 */
            TextButton(onClick = { selectedPlace = null }) {
                Text("← 장소 다시 선택")
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewTimePickerPartOnly() {
    val selectedPlace = Place(
        title = "경복궁",
        addr1 = "서울 종로구",
        firstImage = ""  // 실제 이미지 URL이 있으면 Coil 사용 가능
    )

    var startTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(10, 0)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 영역 (예: 네트워크 이미지가 없으면 대체 처리)
        ImageCard(
            imageUrl = selectedPlace.firstImage,
            modifier = Modifier)

        Spacer(Modifier.height(12.dp))

        Text(
            selectedPlace.title,
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Spacer(Modifier.height(32.dp))

        // 시작 시간
        Row(verticalAlignment = Alignment.CenterVertically) {
            WheelTimePicker(
                startTime = startTime,
                size = DpSize(120.dp, 50.dp),
                rowCount = 1,
                timeFormat = TimeFormat.HOUR_24,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = true,
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFBDBDBD))
                ),
                onSnappedTime = { startTime = it }
            )
            Spacer(Modifier.width(12.dp))
            Text("부터", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))

        // 종료 시간
        Row(verticalAlignment = Alignment.CenterVertically) {
            WheelTimePicker(
                startTime = endTime,
                size = DpSize(120.dp, 50.dp),
                rowCount = 1,
                timeFormat = TimeFormat.HOUR_24,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    enabled = true,
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFBDBDBD))
                ),
                onSnappedTime = { endTime = it }
            )
            Spacer(Modifier.width(12.dp))
            Text("까지", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = { /* no-op */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("완료", color = Color.White, fontSize = 16.sp)
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = { /* no-op */ }) {
            Text("← 장소 다시 선택")
        }
    }
}

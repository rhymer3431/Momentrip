package com.mp.momentrip.ui.screen.schedule

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetScaffold

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.animation.Interpolation
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.route.OnRouteLineAnimatorStopCallback
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.kakao.vectormap.route.animation.ProgressAnimation
import com.kakao.vectormap.route.animation.ProgressDirection
import com.kakao.vectormap.route.animation.ProgressType
import com.mp.momentrip.data.Day
import com.mp.momentrip.data.Schedule
import com.mp.momentrip.data.Activity
import com.mp.momentrip.data.Place
import com.mp.momentrip.ui.MainDestinations
import com.mp.momentrip.ui.components.DayCalendarBar
import com.mp.momentrip.ui.components.DayItem
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScheduleOverviewScreen(
    modifier: Modifier = Modifier,
    scheduleViewModel: ScheduleViewModel = viewModel(),
    userState: UserViewModel,
    baseZoom: Float = 13f,
    markerText: String = "위치",
    navController: NavController
) {
    // --- 상태 읽어오기 ---
    val schedule by scheduleViewModel.schedule.collectAsState()
    val selectedDayIndex by scheduleViewModel.selectedDayIndex.collectAsState()
    val selectedActivityIndex by scheduleViewModel.selectedActivityIndex.collectAsState()
    val currentDay = schedule?.days?.getOrNull(selectedDayIndex) ?: Day()

    // --- Map 준비 ---
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var currentPosition by remember { mutableStateOf(LatLng.from(0.0, 0.0)) }

    // --- Sheet 높이 계산 & 애니메이션 준비 ---
    val density = LocalDensity.current
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val sheetHeightPx = screenHeightPx * 0.4f
    val peekHeightPx = with(density) { 110.dp.toPx() }
    val maxOffset = sheetHeightPx - peekHeightPx
    val offsetY = remember { Animatable(maxOffset) }
    val scope = rememberCoroutineScope()

    // --- 첫 장소로 카메라 이동 ---
    LaunchedEffect(currentDay) {
        currentDay.timeTable.firstOrNull()?.let { first ->
            val pos = LatLng.from(first.place.y, first.place.x)
            currentPosition = pos
            kakaoMap?.moveCamera(
                CameraUpdateFactory.newCenterPosition(pos),
                CameraAnimation.from(300)
            )
            kakaoMap?.moveCamera(
                CameraUpdateFactory.zoomTo(baseZoom.toInt()),
                CameraAnimation.from(300)
            )
        }
    }

    // --- Drag 제스처에 따라 sheet offset & zoom 조정 ---
    val dragModifier = Modifier.pointerInput(Unit) {
        detectVerticalDragGestures(
            onVerticalDrag = { _, dy ->
                scope.launch {
                    val newY = (offsetY.value + dy).coerceIn(0f, maxOffset)
                    offsetY.snapTo(newY)
                    val progress = (newY / maxOffset).coerceIn(0f, 1f)
                    val zoomInt = (baseZoom + progress * 2f).roundToInt()
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.zoomTo(zoomInt),
                        CameraAnimation.from(0)
                    )
                }
            },
            onDragEnd = {
                scope.launch {
                    val target = if (offsetY.value > maxOffset / 2) maxOffset else 0f
                    offsetY.animateTo(target, tween(300))
                    val progress = (target / maxOffset).coerceIn(0f, 1f)
                    val zoomInt = (baseZoom + progress * 2f).roundToInt()
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.zoomTo(zoomInt),
                        CameraAnimation.from(300)
                    )
                }
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        // 1) Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* 뒤로가기 */ }) {

                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
            }
            IconButton(onClick = {
                navController.navigate(MainDestinations.DAY_EDIT_ROUTE)
            }) {
                Icon(Icons.Default.Edit, contentDescription = "수정하기")
            }
        }

        // 2) DayCalendarBar
        DayCalendarBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            days = schedule?.days
                ?.let { convertDaysToDayItems(it, selectedDayIndex) }
                ?: emptyList(),
            onDaySelected = { scheduleViewModel.selectDay(it) }
        )

        // 3) Map + BottomSheet
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)  // 남은 영역 전부 사용
        ) {
            // A) Map View
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .border(1.dp, Color.Transparent, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                factory = { mapView },
                update = { view ->
                    view.start(
                        object : MapLifeCycleCallback() {
                            override fun onMapDestroy() {}
                            override fun onMapError(exception: Exception?) { exception?.printStackTrace() }
                        },
                        object : KakaoMapReadyCallback() {
                            override fun onMapReady(map: KakaoMap) {
                                kakaoMap = map
                                currentDay.timeTable.firstOrNull()?.let { first ->
                                    val pos = LatLng.from(first.place.y, first.place.x)
                                    currentPosition = pos
                                    map.moveCamera(CameraUpdateFactory.newCenterPosition(pos), CameraAnimation.from(300))
                                    updateMapPosition(map, pos, zoomLevel, markerText)
                                    addMarkers(map, schedule, markerText)
                                }
                            }
                            override fun getPosition(): LatLng = currentPosition
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)                   // ← 이 줄 추가
                    .offset { IntOffset(0, offsetY.value.roundToInt()) }
                    .fillMaxWidth()
                    .height(with(density) { sheetHeightPx.toDp() })
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .then(dragModifier)
            ) {
                // 일정 리스트
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    itemsIndexed(currentDay.timeTable) { idx, activity ->
                        ScheduleItemCard(
                            index = idx,
                            activity = activity,
                            isSelected = idx == selectedActivityIndex,
                            onClick = { /* … */ }
                        )
                    }
                }
            }
        }
    }
}

/* -----------------------------------------------------------------------------
 * 일정 카드 (시트 내부)
 * -------------------------------------------------------------------------- */
@Composable
private fun ScheduleItemCard(
    index: Int,
    activity: Activity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 순번 원형
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7D5CF6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 시간 + 장소 정보
            Column {
                activity.startTime?.let {
                    Text(
                        text = it.format(DateTimeFormatter.ofPattern("HH:mm")),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = activity.place.firstImage2,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = activity.place.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        activity.place.addr1?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
private fun updateMapPosition(
    kakaoMap: KakaoMap,
    position: LatLng,
    zoomLevel: Int,
    markerText: String
) {
    val cameraPosition = CameraPosition.from(
        position.latitude,
        position.longitude,
        zoomLevel,
        0.0, 0.0, 0.0
    )
    kakaoMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    kakaoMap.labelManager?.let { manager ->
        manager.clearAll()
        val labelStyles = manager.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from().setTextStyles(20, 0)
            )
        )
        manager.layer?.addLabel(
            LabelOptions.from(position)
                .setStyles(labelStyles)
                .setTexts(LabelTextBuilder().setTexts(markerText))
        )
    }
}

// 경로 그리기 함수 (일부 수정)
private fun drawRouteWithRouteLine(kakaoMap: KakaoMap, positions: List<LatLng>) {
    if (positions.size < 2) {
        // 경로가 2개 미만이면 기존 경로 제거
        kakaoMap.routeLineManager?.layer?.removeAll()
        return
    }

    kakaoMap.routeLineManager?.let { manager ->
        manager.layer?.removeAll()

        // 경로 스타일 설정 (두껍고 진한 파란색)
        val lineStyle = RouteLineStyle.from(
            16f,
            0xFF4285F4.toInt(), // 파란색
            4f,
            0xFFFFFFFF.toInt()
        )

        val stylesSet = RouteLineStylesSet.from(
            "selectedRouteStyle",
            RouteLineStyles.from(lineStyle)
        )

        val segment = RouteLineSegment.from(positions)
            .setStyles(stylesSet.getStyles(0))

        val routeLine = manager.layer?.addRouteLine(
            RouteLineOptions.from(segment)
        )

        // 애니메이션 설정
        val animation = ProgressAnimation.from("routeAnimation", 5000).apply {
            setInterpolation(Interpolation.CubicInOut)
            setProgressType(ProgressType.ToShow)
            setProgressDirection(ProgressDirection.StartFirst)
            setHideAtStop(false)
            setResetToInitialState(false)
        }

        manager.addAnimator(animation).apply {
            routeLine?.let { addRouteLines(it) }
            start(OnRouteLineAnimatorStopCallback {})
        }
    }
}
// 마커 추가 함수
private fun addMarkers(kakaoMap: KakaoMap, schedule: Schedule?, markerText: String) {
    kakaoMap.labelManager?.clearAll()
    schedule?.days?.flatMap { it.timeTable }?.forEachIndexed { index, timeSlot ->
        val position = LatLng.from(timeSlot.place.y, timeSlot.place.x)

        // 마커 스타일 설정
        val labelStyles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from()
                    .setTextStyles(20, 0xFF000000.toInt()) // 검은색 텍스트
            )
        )

        // 마커 추가
        labelStyles?.let { styles ->
            kakaoMap.labelManager?.layer?.addLabel(
                LabelOptions.from(position)
                    .setStyles(styles)
                    .setTexts(LabelTextBuilder().setTexts("${index + 1}. $markerText"))
            )
        }
    }
}


fun convertDaysToDayItems(days: List<Day>, selectedIndex: Int): List<DayItem> {
    return days.mapIndexed { index, day ->
        DayItem(
            date = day.date?.dayOfMonth.toString(), // ex: "21"
            isSelected = index == selectedIndex
        )
    }
}



val dummy_schedule = Schedule(
    days = listOf(
        Day(
            date = LocalDate.of(2025, 5, 21),
            timeTable = listOf(
                Activity(
                    startTime = LocalTime.of(8, 0),
                    endTime = LocalTime.of(9, 0),
                    place = Place(
                        title = "우도",
                        addr1 = "제주 제주시 우도면",
                        contentTypeId = 12,
                        x = 126.9515,
                        y = 33.4961,
                        firstImage2 = "https://example.com/image/udo_thumb.jpg"
                    )
                ),
                Activity(
                    startTime = LocalTime.of(11, 0),
                    endTime = LocalTime.of(12, 0),
                    place = Place(
                        title = "성산일출봉",
                        addr1 = "제주 서귀포시 성산읍 성산리 1",
                        contentTypeId = 12,
                        x = 126.9410,
                        y = 33.4586,
                        firstImage2 = "https://example.com/image/seongsan_thumb.jpg"
                    )
                ),
                Activity(
                    startTime = LocalTime.of(13, 0),
                    endTime = LocalTime.of(14, 0),
                    place = Place(
                        title = "전망좋은횟집&흑돼지",
                        addr1 = "제주 서귀포시 성산읍",
                        contentTypeId = 39,
                        x = 126.9360,
                        y = 33.4595,
                        firstImage2 = "https://example.com/image/restaurant_thumb.jpg"
                    )
                )
            )
        )
    )
)
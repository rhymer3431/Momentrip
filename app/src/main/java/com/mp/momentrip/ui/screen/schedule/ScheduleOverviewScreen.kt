package com.mp.momentrip.ui.screen.schedule

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kakao.vectormap.*
import com.kakao.vectormap.animation.Interpolation
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.*
import com.kakao.vectormap.route.*
import com.kakao.vectormap.route.animation.ProgressAnimation
import com.kakao.vectormap.route.animation.ProgressDirection
import com.kakao.vectormap.route.animation.ProgressType
import com.mp.momentrip.data.*
import com.mp.momentrip.ui.ScheduleDestinations
import com.mp.momentrip.ui.components.DayCalendarBar
import com.mp.momentrip.ui.components.DayItem
import com.mp.momentrip.view.ScheduleViewModel
import com.mp.momentrip.view.UserViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
    /* ───────── 상태 ───────── */
    val schedule by scheduleViewModel.schedule.collectAsState()
    val selectedDayIndex by scheduleViewModel.selectedDayIndex.collectAsState()
    val selectedActivityIndex by scheduleViewModel.selectedActivityIndex.collectAsState()
    val currentDay = schedule?.days?.getOrNull(selectedDayIndex) ?: Day()

    /* ───────── Map ───────── */
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var currentPosition by remember { mutableStateOf<LatLng?>(null) }

    /* ───────── 시트 높이 (고정) ───────── */
    val density = LocalDensity.current
    val sheetHeightDp = with(density) {
        (LocalConfiguration.current.screenHeightDp * 0.4f).dp   // 화면 40 %
    }

    /* ───────── 첫 장소로 카메라 이동 ───────── */
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

    /* ───────── UI ───────── */
    Column(modifier = modifier.fillMaxSize()) {

        /* 1) Top Bar */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
            }
            IconButton(onClick = {
                navController.navigate(ScheduleDestinations.DAY_EDIT_ROUTE)
            }) {
                Icon(Icons.Default.Edit, contentDescription = "수정하기")
            }
        }

        /* 2) DayCalendarBar */
        DayCalendarBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            days = schedule?.days
                ?.let { convertDaysToDayItems(it, selectedDayIndex) }
                ?: emptyList(),
            onDaySelected = { scheduleViewModel.selectDay(it) }
        )

        /* 3) Map + BottomSheet */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)              // 남은 영역
        ) {
            /* A) 지도 */
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .height(sheetHeightDp)
                    .border(
                        1.dp, Color.Transparent,
                        RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    ),
                factory = { mapView },
                update = { view ->
                    view.start(
                        object : MapLifeCycleCallback() {
                            override fun onMapDestroy() {}
                            override fun onMapError(exception: Exception?) {
                                exception?.printStackTrace()
                            }
                        },
                        object : KakaoMapReadyCallback() {
                            override fun onMapReady(map: KakaoMap) {
                                kakaoMap = map

                                /* 첫 장소 카메라 설정 */
                                currentDay.timeTable.firstOrNull()?.let { first ->
                                    val pos = LatLng.from(first.place.y, first.place.x)
                                    currentPosition = pos
                                    updateMapPosition(map, pos, baseZoom.toInt(), markerText)
                                }

                                /* 마커 추가 */
                                schedule?.let { addMarkers(map, it, markerText) }
                            }

                            override fun getPosition(): LatLng =
                                currentPosition ?: LatLng.from(0.0, 0.0)
                        }
                    )
                }
            )

            /* B) BottomSheet (고정, 드래그 X) */
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(sheetHeightDp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    itemsIndexed(currentDay.timeTable) { idx, activity ->
                        ScheduleItemCard(
                            index = idx,
                            activity = activity,
                            isSelected = idx == selectedActivityIndex,
                            onClick = {
                                scheduleViewModel.selectActivity(idx)

                                val newPosition = LatLng.from(activity.place.y, activity.place.x)
                                currentPosition = newPosition

                                kakaoMap?.let { map ->
                                    map.moveCamera(
                                        CameraUpdateFactory.newCenterPosition(newPosition),
                                        CameraAnimation.from(300)
                                    )

                                    // 현재 → 다음 장소까지 경로 표시
                                    val positions = mutableListOf(newPosition)
                                    if (idx < currentDay.timeTable.size - 1) {
                                        val next = currentDay.timeTable[idx + 1]
                                        positions.add(LatLng.from(next.place.y, next.place.x))
                                    }
                                    drawRouteWithRouteLine(map, positions)
                                }
                            }


                        )
                    }
                }
            }
        }
    }
}

/* ───────── 일정 카드 ───────── */
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
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /* 순번 원형 */
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

            /* 시간 + 장소 정보 */
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
                        model = activity.place.firstImage,
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

/* ───────── 지도 유틸 ───────── */
private fun updateMapPosition(
    kakaoMap: KakaoMap,
    position: LatLng,
    zoomLevel: Int,
    markerText: String
) {
    val cameraPosition = CameraPosition.from(
        position.latitude, position.longitude,
        zoomLevel, 0.0, 0.0, 0.0
    )
    kakaoMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    kakaoMap.labelManager?.let { manager ->
        manager.clearAll()
        val styles = manager.addLabelStyles(
            LabelStyles.from(LabelStyle.from().setTextStyles(20, 0))
        )
        manager.layer?.addLabel(
            LabelOptions.from(position)
                .setStyles(styles)
                .setTexts(LabelTextBuilder().setTexts(markerText))
        )
    }
}

/* 경로 그리기 */
private fun drawRouteWithRouteLine(kakaoMap: KakaoMap, positions: List<LatLng>) {
    if (positions.size < 2) {
        kakaoMap.routeLineManager?.layer?.removeAll()
        return
    }

    kakaoMap.routeLineManager?.let { manager ->
        manager.layer?.removeAll()

        val style = RouteLineStyle.from(
            16f, 0xFF4285F4.toInt(), 4f, 0xFFFFFFFF.toInt()
        )
        val stylesSet = RouteLineStylesSet.from(
            "routeStyle", RouteLineStyles.from(style)
        )
        val segment = RouteLineSegment.from(positions)
            .setStyles(stylesSet.getStyles(0))

        val route = manager.layer?.addRouteLine(RouteLineOptions.from(segment))

        val anim = ProgressAnimation.from("anim", 5000).apply {
            setInterpolation(Interpolation.CubicInOut)
            setProgressType(ProgressType.ToShow)
            setProgressDirection(ProgressDirection.StartFirst)
            setHideAtStop(false)
            setResetToInitialState(false)
        }
        manager.addAnimator(anim).apply {
            route?.let { addRouteLines(it) }
            start(OnRouteLineAnimatorStopCallback {})
        }
    }
}

/* 마커 추가 */
private fun addMarkers(kakaoMap: KakaoMap, schedule: Schedule?, markerText: String) {
    kakaoMap.labelManager?.clearAll()
    schedule?.days
        ?.flatMap { it.timeTable }
        ?.forEachIndexed { idx, slot ->
            val pos = LatLng.from(slot.place.y, slot.place.x)
            val styles = kakaoMap.labelManager?.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from().setTextStyles(20, 0xFF000000.toInt())
                )
            )
            styles?.let {
                kakaoMap.labelManager?.layer?.addLabel(
                    LabelOptions.from(pos)
                        .setStyles(it)
                        .setTexts(LabelTextBuilder().setTexts("${idx + 1}. $markerText"))
                )
            }
        }
}

/* Day → DayItem 변환 */
fun convertDaysToDayItems(days: List<Day>, selectedIndex: Int): List<DayItem> =
    days.mapIndexed { idx, d ->
        DayItem(index = (d.index + 1).toString(), isSelected = idx == selectedIndex)
    }

/* ───────── 더미 데이터 (예시) ───────── */
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
                        x = 126.9515, y = 33.4961,
                        firstImage2 = "https://example.com/udo_thumb.jpg"
                    )
                ),
                Activity(
                    startTime = LocalTime.of(11, 0),
                    endTime = LocalTime.of(12, 0),
                    place = Place(
                        title = "성산일출봉",
                        addr1 = "제주 서귀포시 성산읍",
                        contentTypeId = 12,
                        x = 126.9410, y = 33.4586,
                        firstImage2 = "https://example.com/seongsan_thumb.jpg"
                    )
                ),
                Activity(
                    startTime = LocalTime.of(13, 0),
                    endTime = LocalTime.of(14, 0),
                    place = Place(
                        title = "전망좋은횟집&흑돼지",
                        addr1 = "제주 서귀포시 성산읍",
                        contentTypeId = 39,
                        x = 126.9360, y = 33.4595,
                        firstImage2 = "https://example.com/restaurant_thumb.jpg"
                    )
                )
            )
        )
    )
)

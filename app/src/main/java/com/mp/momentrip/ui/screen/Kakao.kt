package com.mp.momentrip.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.kakao.vectormap.route.RouteLineAnimator
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
import com.mp.momentrip.view.UserViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue

@SuppressLint("UnrememberedMutableState")
@Composable
fun ScheduleMapScreen(
    modifier: Modifier = Modifier,
    userState: UserViewModel,
    zoomLevel: Int = 1,
    markerText: String = "위치"
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val schedule = userState.getSchedule()

    // 초기 currentDay를 schedule의 첫 번째 day로 설정 (null 체크 추가)
    var currentDay by remember {
        mutableStateOf(schedule?.days?.firstOrNull() ?: Day())
    }

    var currentPosition by remember { mutableStateOf(LatLng.from(0.0, 0.0)) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }


    var selectedActivityIndex by remember { mutableStateOf<Int?>(null) }

    // 스케줄이 변경될 때 처리
    LaunchedEffect(schedule) {
        schedule?.let { sched ->
            // 첫 번째 위치로 카메라 이동
            sched.days.firstOrNull()?.let { day ->
                day.timeTable.firstOrNull()?.let { firstActivity ->
                    currentPosition = LatLng.from(firstActivity.place.y, firstActivity.place.x)
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.newCenterPosition(currentPosition),
                        CameraAnimation.from(300)
                    )
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 상단: Day 선택 및 표시
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("일정 선택", style = MaterialTheme.typography.bodyLarge)

                // Day 변경 버튼
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {
                            // 이전 Day로 변경
                            val currentIndex = schedule?.days?.indexOf(currentDay) ?: 0
                            if (currentIndex > 0) {
                                currentDay = schedule?.days?.get(currentIndex-1) ?: currentDay
                                // 첫 번째 Activity의 장소로 카메라 이동
                                currentDay.timeTable.firstOrNull()?.let { firstActivity ->
                                    val newPosition = LatLng.from(firstActivity.place.y, firstActivity.place.x)
                                    currentPosition = newPosition
                                    kakaoMap?.moveCamera(
                                        CameraUpdateFactory.newCenterPosition(newPosition),
                                        CameraAnimation.from(300)
                                    )
                                    kakaoMap?.let {
                                        drawRouteWithRouteLine(it, currentDay.timeTable.map {
                                            LatLng.from(it.place.y, it.place.x)
                                        })
                                    }
                                }
                            }
                        }
                    ) {
                        Text("◀") // 왼쪽 버튼
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Day ${schedule?.days?.indexOf(currentDay)?.plus(1) ?: 1}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            // 다음 Day로 변경
                            val currentIndex = schedule?.days?.indexOf(currentDay) ?: 0
                            if (currentIndex < (schedule?.days?.size ?: 1) - 1) {
                                currentDay = schedule?.days?.get(currentIndex + 1) ?: currentDay
                                // 첫 번째 Activity의 장소로 카메라 이동
                                currentDay.timeTable.firstOrNull()?.let { firstActivity ->

                                    val newPosition = LatLng.from(firstActivity.place.y, firstActivity.place.x)
                                    currentPosition = newPosition
                                    kakaoMap?.moveCamera(
                                        CameraUpdateFactory.newCenterPosition(newPosition),
                                        CameraAnimation.from(300)
                                    )
                                    kakaoMap?.let {
                                        drawRouteWithRouteLine(it, currentDay.timeTable.map {
                                            LatLng.from(it.place.y, it.place.x)
                                        })
                                    }
                                }
                            }
                        }
                    ) {
                        Text("▶") // 오른쪽 버튼
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(3f)
                .padding(16.dp)  // 패딩 추가로 둥근 효과 강조
        ) {
            // Clip 모디파이어로 둥근 테두리 적용
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
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
                                override fun onMapReady(kakaoMapInstance: KakaoMap) {
                                    kakaoMap = kakaoMapInstance
                                    currentDay.timeTable.firstOrNull()?.let { firstActivity ->
                                        val newPosition = LatLng.from(firstActivity.place.y, firstActivity.place.x)
                                        currentPosition = newPosition
                                        kakaoMap?.moveCamera(
                                            CameraUpdateFactory.newCenterPosition(newPosition),
                                            CameraAnimation.from(300)
                                        )
                                        updateMapPosition(kakaoMapInstance, newPosition, zoomLevel, markerText)
                                        addMarkers(kakaoMapInstance, schedule, markerText)
                                    }
                                }

                                override fun getPosition(): LatLng = currentPosition
                            }
                        )
                    }
                )
            }
        }
        LazyColumn(
            modifier = Modifier.weight(3f).padding(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(currentDay.timeTable) { timeSlot ->
                val currentIndex = currentDay.timeTable.indexOf(timeSlot)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // 선택된 Activity 인덱스 업데이트
                            selectedActivityIndex = currentIndex
                            val newPosition = LatLng.from(timeSlot.place.y, timeSlot.place.x)
                            currentPosition = newPosition
                            kakaoMap?.moveCamera(
                                CameraUpdateFactory.newCenterPosition(newPosition),
                                CameraAnimation.from(300)
                            )

                            // 선택된 Activity과 다음 Activity까지의 경로 그리기
                            kakaoMap?.let { map ->
                                val positions = mutableListOf<LatLng>()

                                // 현재 선택된 Activity 추가
                                positions.add(LatLng.from(timeSlot.place.y, timeSlot.place.x))

                                // 다음 Activity이 있으면 추가
                                if (currentIndex < currentDay.timeTable.size - 1) {
                                    val nextActivity = currentDay.timeTable[currentIndex + 1]
                                    positions.add(LatLng.from(nextActivity.place.y, nextActivity.place.x))
                                }

                                drawRouteWithRouteLine(map, positions)
                            }
                        },

                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(timeSlot.place.title, style = MaterialTheme.typography.bodySmall)
                        timeSlot.place.addr1?.let { Text(it, style = MaterialTheme.typography.bodySmall) }


                    }
                }
            }
        }
    }
}


// Activity 카드 컴포저블 (분리된 컴포넌트)
@Composable
private fun ActivityCard(
    timeSlot: Activity,
    currentIndex: Int,
    totalCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 위치 표시 (예: 2/5)
            Text(
                text = "${currentIndex + 1}/$totalCount",

            )

            Spacer(modifier = Modifier.height(8.dp))

            // 장소 정보
            Text(
                timeSlot.place.title,
                style = MaterialTheme.typography.bodyMedium
            )
            timeSlot.place.addr1?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            // 탐색 버튼 (보조 수단)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onPrevious,
                    enabled = currentIndex > 0
                ) {
                    Text("← 이전")
                }

                TextButton(
                    onClick = onNext,
                    enabled = currentIndex < totalCount - 1
                ) {
                    Text("다음 →")
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
}// 경로 그리기 함수 (일부 수정)
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




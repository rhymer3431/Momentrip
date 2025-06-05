package com.mp.momentrip.util.map

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.animation.Interpolation
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
import com.mp.momentrip.data.schedule.Schedule


fun updateMapPosition(
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
fun drawRouteWithRouteLine(kakaoMap: KakaoMap, positions: List<LatLng>) {
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
fun addMarkers(kakaoMap: KakaoMap, schedule: Schedule?, markerText: String) {
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

